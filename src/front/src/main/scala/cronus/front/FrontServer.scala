package cronus.front

import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.routing.HttpRouter
import cronus.front.controllers.{AssetController, UserLoginController}

object FrontServerMain extends FrontServer

class FrontServer extends HttpServer {
  override def configureHttp(router: HttpRouter) {
    router.add[AssetController].
      add[UserLoginController]
  }
}