package cronus.front.controllers

import com.google.inject.Stage
import com.twitter.finagle.http.Status
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import cronus.front.FrontServer
import org.webjars.WebJarAssetLocator

import scala.io.Source


class AssetControllerFeatureTest extends FeatureTest
{

  override val server = new EmbeddedHttpServer(
    twitterServer = new FrontServer,
    stage = Stage.PRODUCTION,
    verbose = false)

  "AssetController" should  {
    "return -ok- for an existing WebJar resource" in {
      val locator: WebJarAssetLocator = new WebJarAssetLocator()
      val fullPath = locator.getFullPath("bootstrap", "js/npm.js")
      val is = locator.getClass.getClassLoader.getResourceAsStream(fullPath)
      server.httpGet(path="/assets/bootstrap/js/npm.js", andExpect = Status.Ok,
        withBody = Source.fromInputStream(is).mkString)
    }

    "return -not found- for an non existing WebJar resource" in {
      server.httpGet(path="/assets/bootstrap/css/bootstrap.css.fail", andExpect = Status.NotFound)
    }

    "return -not found- for an path containing only a webjar name" in {
      server.httpGet(path="/assets/bootstrap/", andExpect = Status.NotFound)
      server.httpGet(path="/assets/bootstrap", andExpect = Status.NotFound)
    }

    "return -not found- for an path containing /assets" in {
      server.httpGet(path="/assets/", andExpect = Status.NotFound)
      server.httpGet(path="/assets", andExpect = Status.NotFound)
    }
  }
}
