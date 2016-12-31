package cronus.services

import java.nio.file.{Files, Paths}
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

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


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

class WebJarAssetCache(assetLocator: WebJarAssetLocator, configFlags: ConfigFlags)
  extends Actor with ActorLogging
{

  import WebJarAssetCache._

  var pages: scala.collection.mutable.Map[(String, String), ContentReply] =
    scala.collection.mutable.Map.empty[(String, String), ContentReply]

  private def fileToArray(webjar: String, partialPath: String): Option[Array[Byte]] = {
    try {
      val path = assetLocator.getFullPath(webjar, partialPath)
      val is = assetLocator.getClass.getClassLoader.getResourceAsStream(path)
      if (null == is) {
        None
      } else {
        Some(Iterator.continually(is.read()).takeWhile(-1!=_).map(_.toByte).toArray)
      }
    } catch  {
      case e: IllegalArgumentException => None
    }
  }

  def receive = {
    case ContentRequest(webJar, path) =>
      log.debug(s"Cache received request for webjar: $webJar path: $path")
      val page = pages.get((webJar, path))
      val response = if(webJar == "cronusw" && configFlags.cronusAssetPath.isDefined) {
        val fsystemPath = Paths.get(configFlags.cronusAssetPath.get, path).toString
        val mime: String = Files.probeContentType(Paths.get(fsystemPath))
        val bytes =  scala.io.Source.fromFile(fsystemPath).map(_.toByte).toArray
        ContentReply(Some(bytes), if (mime == null) "text/plain" else mime )
      } else if (page.isDefined) {
        page.get
      } else {
        val bytes = fileToArray(webJar, path)
        if (bytes.isDefined) {
          val mime: String = Files.probeContentType(Paths.get(path))
          val cacheIt = ContentReply(bytes, if (mime == null) "text/plain" else mime )
          pages.put((webJar, path), cacheIt)
          cacheIt
        } else {
          ContentReply(None, "")
        }
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

