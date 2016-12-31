package cronus.controllers

import com.twitter.finatra.http.Controller
import com.twitter.finagle.http.Request
import com.twitter.finatra.response.Mustache
import com.twitter.inject.Logging
import cronus.auth.AuthData
import cronus.auth.AuthDataContext._

@Mustache("time")
case class TimeData()

class TimeController extends Controller with Logging {

  get("/time/:*") { request: Request =>
    TimeData()
  }

}
