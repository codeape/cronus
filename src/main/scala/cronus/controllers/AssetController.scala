package cronus.controllers

import javax.inject.Inject

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.utils.FuturePools
import cronus.services.WebJarAssetCache.ContentReply
import cronus.services.WebJarAssetService


class AssetController @Inject()(webJarAssetService: WebJarAssetService)
  extends Controller {

  private val futurePool = FuturePools.unboundedPool("CallbackConverter")

  get("/assets/:webjar/") { request: Request =>
    response.notFound
  }

  get("/assets/:webjar/:*") { request: Request =>
    webJarAssetService.getFile(request.params("webjar"), request.params("*"))
      .map {
        case ContentReply(Some(bytes), mime) => response.ok.body(bytes).contentType(mime)
        case ContentReply(None, mime) => response.notFound
      }
  }
}
