package cronus.front

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, ExceptionMappingFilter}
import com.twitter.finatra.http.routing.HttpRouter
import cronus.common.controllers.AssetController
import cronus.common.filter.AuthFilter
import cronus.common.modules.{AkkaActorSystemModule, CronusConfigFlagsModule, WebJarAssetModule}
import cronus.front.controllers.{MainJsController, TimeController, TimeSheetController, UserLoginController}
import cronus.front.exception.JsonMappingExceptionMapper

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
      .add[MainJsController]
      .add[TimeController]
      .add[AuthFilter, TimeSheetController]
      .exceptionMapper[JsonMappingExceptionMapper]
  }
}