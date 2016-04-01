package cronus.front.controllers

import java.nio.file.{Files, Paths}
import javax.inject.Inject

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.utils.FuturePools
import cronus.front.services.WebJarAssetService

class AssetController @Inject()(webJarAssetService: WebJarAssetService)
  extends Controller {

  private val futurePool = FuturePools.unboundedPool("CallbackConverter")

  get("/assets/:webjar/") { request: Request =>
    response.notFound
  }

  get("/assets/:webjar/:*") { request: Request =>
    futurePool {
      val webjar: String = request.params("webjar")
      val tail: String = request.params("*")
      if (tail.isEmpty) {
        response.notFound
      } else {
        val path = webJarAssetService.getFilePath(webjar, tail)
        if (path.isEmpty) {
          response.notFound
        }
        else {
          val mimeType: String = Files.probeContentType(Paths.get(path.get))
          val bytesOpt: Option[Array[Byte]] = webJarAssetService.fileToString(path.get)
          bytesOpt match {
            case Some(bytes) => response.ok.body(bytes).contentType(if (mimeType == null) "text/plain" else mimeType)
            case None => response.internalServerError
          }
        }
      }
    }

  }
}
