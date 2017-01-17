package cronus.services

import java.nio.file.{Files, Path, Paths}
import javax.inject.{Inject, Singleton}

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.twitter.bijection.Conversion.asMethod
import com.twitter.bijection.twitter_util.UtilBijections._
import com.twitter.inject.Logging
import com.twitter.util.{Future => TwitterFuture}
import cronus.modules.ConfigFlags
import org.webjars.WebJarAssetLocator

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.io.Source


@Singleton
class WebJarAssetService @Inject()(
                                    actorSystem: ActorSystem,
                                    webJarAssetLocator: WebJarAssetLocator,
                                    configFlags: ConfigFlags
                                  )
  extends Logging
{

  import WebJarAssetCache._

  implicit val timeout = Timeout(5.seconds)

  val cache: ActorRef = WebJarAssetCache.make(actorSystem, webJarAssetLocator, configFlags)

  def getFile(webjar: String, partialPath: String): TwitterFuture[WebJarAssetCache.ContentReply] = {
    val scalaFuture = ask(cache, ContentRequest(webjar, partialPath)).mapTo[WebJarAssetCache.ContentReply]
    scalaFuture.as[TwitterFuture[WebJarAssetCache.ContentReply]]
  }
}

private case class PageKey(webJar: String, path: String)

class WebJarAssetCache(assetLocator: WebJarAssetLocator, configFlags: ConfigFlags)
  extends Actor with ActorLogging
{

  import WebJarAssetCache._

  private val pages: scala.collection.mutable.Map[PageKey, ContentReply] =
    scala.collection.mutable.Map.empty[PageKey, ContentReply]

  private def createContentReply(bytes: Array[Byte], path: Path): ContentReply = {
    ContentReply(Some(bytes), Files.probeContentType(path))
  }

  private def readFromWebJar(webjar: String, partialPath: String): Option[Array[Byte]] = {
    try {
      assetLocator.getClass.getClassLoader.getResourceAsStream(assetLocator.getFullPath(webjar, partialPath)) match {
        case null => None
        case is => Some(Source.fromInputStream(is).map(_.toByte).toArray)
      }
    } catch {
      case e: IllegalArgumentException => None
    }
  }

  @tailrec
  private def readFromFileSystem(paths: List[String], path: String): Option[ContentReply] = {
    paths match {
      case Nil => None
      case assetsPath :: tail =>
        val systemPath = Paths.get(assetsPath, path)
        Files.exists(systemPath) match {
          case true => Some(
            createContentReply(Source.fromFile(systemPath.toString).map(_.toByte).toArray,systemPath))
          case _ => readFromFileSystem(tail, path)
        }
    }
  }

  private def readFromWebJarAndCache(webjar: String, partialPath: String): ContentReply = {
    pages.get(PageKey(webjar, partialPath)) match {
      case Some(page) => page
      case None => readFromWebJar(webjar, partialPath) match {
        case Some(bytes) =>
          val cacheIt = createContentReply(bytes, Paths.get(partialPath))
          pages.put(PageKey(webjar, partialPath), cacheIt)
          cacheIt
        case None => ContentReply(None, "")
      }
    }
  }

  private def resolveFile(webjar: String, partialPath: String): ContentReply = {
    val systemPaths = if (webjar == "root") {
      configFlags.cronusAssetPaths.getOrElse(List.empty[String])
    } else {
      List.empty[String]
    }
    readFromFileSystem(systemPaths, partialPath) match {
      case Some(content) => content
      case None => readFromWebJarAndCache(webjar, partialPath)
    }
  }


  def receive = {
    case ContentRequest(webJar, path) =>
      log.debug(s"Cache received request for webjar: $webJar path: $path")
      val response = pages.get(PageKey(webJar, path)) match {
        case Some(page) => page
        case None => resolveFile(webJar, path)
      }
      sender() ! response
    case default =>
      log.error(s"Unknown message to WebJarAssetCache :${default.getClass.getName}")
  }
}

object WebJarAssetCache extends Logging{
  case class ContentRequest(webJar: String, path: String)
  case class ContentReply(content: Option[Array[Byte]], mimeType: String)

  def make(sys: ActorSystem, assetLocator: WebJarAssetLocator, configFlags: ConfigFlags): ActorRef = {
    logger.debug(s"Using debug config $configFlags")
    sys.actorOf(Props(classOf[WebJarAssetCache], assetLocator, configFlags))
  }
}

