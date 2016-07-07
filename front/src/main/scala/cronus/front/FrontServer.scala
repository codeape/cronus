package cronus.front

import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.routing.HttpRouter
import cronus.common.controllers.AssetController
import cronus.common.filter.{AuthFilter, LaxedAuthFilter}
import cronus.common.modules.{AkkaActorSystemModule, WebJarAssetModule}
import cronus.front.controllers.{TimeController, TimeSheetController, UserLoginController}

object FrontServerMain extends FrontServer

class FrontServer extends HttpServer {

  override val modules = Seq(WebJarAssetModule, new AkkaActorSystemModule)

  override def configureHttp(router: HttpRouter) {
    router
      .filter[CommonFilters]
      .add[AssetController]
      .add[UserLoginController]
      .add[LaxedAuthFilter, TimeController] // Filter needed to handle empty token.
      .add[AuthFilter, TimeSheetController]
  }
}