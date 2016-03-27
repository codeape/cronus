package cronus.front.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.response.Mustache

@Mustache("login")
case class UserLogin()

class UserLoginController extends Controller {

  get("/login") { request: Request =>
    UserLogin()
  }

}
