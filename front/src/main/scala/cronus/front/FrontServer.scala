package cronus.front

import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.routing.HttpRouter
import cronus.front.controllers.{AssetController, UserLoginController}
import cronus.front.modules.{AkkaActorSystemModule, WebJarAssetModule}

object FrontServerMain extends FrontServer

class FrontServer extends HttpServer {

  override val modules = Seq(WebJarAssetModule, AkkaActorSystemModule)

  override def configureHttp(router: HttpRouter) {
    router
      .filter[CommonFilters]
      .add[AssetController]
      .add[UserLoginController]
  }
}