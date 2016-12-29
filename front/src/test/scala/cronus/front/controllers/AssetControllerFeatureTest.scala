package cronus.front.controllers

import com.google.inject.Stage
import com.twitter.finagle.http.Status
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import com.google.common.net.MediaType
import cronus.front.FrontServer
import org.webjars.WebJarAssetLocator

import scala.io.Source


class AssetControllerFeatureTest extends FeatureTest
{

  override val server = new EmbeddedHttpServer(
    twitterServer = new FrontServer,
    stage = Stage.PRODUCTION,
    verbose = true,
    maxStartupTimeSeconds = 60)

  "AssetController" should  {


    "return -ok- for an existing WebJar resource" in {
      val locator: WebJarAssetLocator = new WebJarAssetLocator()
      val fullPath = locator.getFullPath("bootstrap", "js/npm.js")
      val is = locator.getClass.getClassLoader.getResourceAsStream(fullPath)
      val response = server.httpGet(path="/assets/bootstrap/js/npm.js", andExpect = Status.Ok,
        withBody = Source.fromInputStream(is).mkString)
      response.contentType should equal(Some(MediaType.JAVASCRIPT_UTF_8.toString))

      val fullPath2 = locator.getFullPath("backbonejs", "backbone-min.js")
      val is2 = locator.getClass.getClassLoader.getResourceAsStream(fullPath2)
      val response2 = server.httpGet(path="/assets/backbonejs/backbone-min.js", andExpect = Status.Ok,
        withBody = Source.fromInputStream(is2).mkString)
      response2.contentType should equal(Some(MediaType.JAVASCRIPT_UTF_8.toString))
    }

    "return -not found- for an path containing only a webjar name" in {
      server.httpGet(path="/assets/bootstrap/", andExpect = Status.NotFound)
      server.httpGet(path="/assets/bootstrap", andExpect = Status.NotFound)
    }

    "return -not found- for an path containing /assets" in {
      server.httpGet(path="/assets/", andExpect = Status.NotFound)
      server.httpGet(path="/assets", andExpect = Status.NotFound)
    }

    "return -not found- for an non existing WebJar resource" in {
      server.httpGet(path="/assets/bootstrap/css/.fail", andExpect = Status.NotFound)
    }
  }
}
