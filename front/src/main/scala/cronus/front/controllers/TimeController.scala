package cronus.front.controllers

import com.twitter.finatra.http.Controller
import com.twitter.finagle.http.Request
import com.twitter.finatra.response.Mustache
import com.twitter.inject.Logging
import cronus.common.filter.AuthData
import cronus.common.filter.AuthDataContext._

@Mustache("time")
case class TimeData(token: String)

class TimeController extends Controller with Logging {

  get("/time/:*") { request: Request =>
    request.ctx
    val authData: AuthData = request.authData.getOrElse(AuthData(""))
    logger.debug(s"TOKEN: ${authData.token}")
    TimeData(authData.token)
  }

}
