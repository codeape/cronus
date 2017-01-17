package cronus

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, ExceptionMappingFilter}
import com.twitter.finatra.http.routing.HttpRouter
import cronus.auth.{AuthFilter, UserLoginController}
import cronus.modules.{AkkaActorSystemModule, CronusConfigFlagsModule, WebJarAssetModule}
import cronus.controllers._
import cronus.exception.JsonMappingExceptionMapper

object FrontServerMain extends FrontServer

class FrontServer extends HttpServer {

  override val modules = Seq(
    new CronusConfigFlagsModule,
    WebJarAssetModule,
    new AkkaActorSystemModule
  )

  override def configureHttp(router: HttpRouter) {
    router
      .filter[CommonFilters]
      .filter[ExceptionMappingFilter[Request]]
      .add[AssetController]
      .add[UserLoginController]
      .add[TimeController]
      .add[AuthFilter, TimeSheetController]
      .exceptionMapper[JsonMappingExceptionMapper]
  }
}