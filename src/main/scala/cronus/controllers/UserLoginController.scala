package cronus.controllers

import com.google.inject.Inject
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.json.FinatraObjectMapper
import com.twitter.inject.Logging
import cronus.auth.{AuthShuttle, TokenCodec}

class UserLoginController @Inject()(finatraObjectMapper: FinatraObjectMapper) extends Controller with Logging {

  post("/user/authenticate"){ request: Request =>
    //logger.debug(s"Auth: ${request.getContentString()}")
    val userData: AuthShuttle = finatraObjectMapper.parse[AuthShuttle](request.content)
    logger.debug(s"AuthShuttle: $userData")
    if (userData.uid == "test" && userData.password == "test") { // TODO: Move to a user and password check
      val token: Option[String] = TokenCodec.userToToken(userData.uid)
      if (token.isDefined) {
        val authedUser = AuthShuttle(userData.uid, "", token.get)
        val retJson = finatraObjectMapper.writePrettyString(authedUser)
        logger.debug(s"Ret Auth: $retJson")
        response.ok.contentTypeJson().body(retJson)
      } else {
        logger.debug(s"User ${userData.uid} unauthorized on token.")
        response.unauthorized
      }
    } else {
      logger.debug(s"User ${userData.uid} unauthorized on user auth check.")
      response.unauthorized
    }
  }

}
