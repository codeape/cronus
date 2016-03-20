package cronus.front.controllers

import java.util.Collection
import java.util.regex.Pattern

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import org.webjars.WebJarAssetLocator


import scala.collection.JavaConversions._

class AssetController extends Controller {

  get("/atest") {request: Request =>
    val locator: WebJarAssetLocator = new WebJarAssetLocator()
    val str: String = locator.getFullPath("bootstrap", "bootstrap.min.css")

    response.ok.fileOrIndex(str, "index.html")
  }

  get("/wj") {request: Request =>
    val locator: WebJarAssetLocator = new WebJarAssetLocator()
    val assets = locator.listAssets()
    val str: String = assets.mkString("\n")
    response.ok.body(str)
  }

  get("/assets:*") { request: Request =>
    response.ok.body("ok")
  }
}
