package cronus.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.response.Mustache
import com.twitter.inject.Logging
import cronus.auth.AuthData
import cronus.filter.AuthDataContext._

@Mustache("timesheet")
case class TimeSheet(token: String)

class TimeSheetController extends Controller with Logging {

  get("/timesheet") { request: Request =>
    val authData: AuthData = request.authData.getOrElse(AuthData(""))
    TimeSheet(authData.token)
  }

}
