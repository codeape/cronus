package cronus.front.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.response.Mustache
import com.twitter.inject.Logging

@Mustache("login")
case class UserLogin()

class UserLoginController extends Controller with Logging {

  get("/login") { request: Request =>
    UserLogin()
  }

  post("/user/authenticate"){ request: Request =>
    logger.debug(s"Auth: ${request.getContentString()}")
  }

}
