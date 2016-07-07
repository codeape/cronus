package cronus.front.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.inject.Logging
import com.twitter.finatra.response.Mustache
import cronus.common.filter.AuthData
import cronus.common.filter.AuthDataContext._

@Mustache("timesheet")
case class TimeSheet(token: String)

class TimeSheetController extends Controller with Logging {

  get("/timesheet") { request: Request =>
    val authData: AuthData = request.authData.getOrElse(AuthData(""))
    TimeSheet(authData.token)
  }

}
