package cronus.auth

import com.google.inject.Stage
import com.twitter.finagle.http.Status
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.finatra.json.FinatraObjectMapper
import com.twitter.inject.server.FeatureTest
import cronus.FrontServer

class UserLoginControllerTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(
    twitterServer = new FrontServer,
    stage = Stage.PRODUCTION,
    verbose = true,
    maxStartupTimeSeconds = 60)

  val url = "/user/authenticate"

  val objectMapper = FinatraObjectMapper.create(injector.underlying)

  "UserLoginController" should {

    "Get a token when sending the right access credentials" in {
      val shuttle = AuthShuttle("test", "test", "")
      val sendJson = objectMapper.writePrettyString(shuttle)
      val response = server.httpPostJson[AuthShuttle](path = url, postBody = sendJson, andExpect = Status.Ok)
      assert(response.uid == response.uid)
      assert(response.password == "")
      assert(response.token == "TESTTOKEN")
      //objectMapper.writePrettyString()
    }

    "Get Unauthorized when sending wrong user" in {
      val shuttle = AuthShuttle("wrong", "test", "")
      val sendJson = objectMapper.writePrettyString(shuttle)
      server.httpPost(path = url, postBody = sendJson, andExpect = Status.Unauthorized)
    }

    "Get Unauthorized when sending wrong password" in {
      val shuttle = AuthShuttle("test", "wrong",  "")
      val sendJson = objectMapper.writePrettyString(shuttle)
      server.httpPost(path = url, postBody = sendJson, andExpect = Status.Unauthorized)
    }

    "Get BadRequest when sending non json" in {
      server.httpPost(path = url, postBody = "wrong", andExpect = Status.BadRequest)
    }

    "Get BadRequest when sending empty string" in {
      server.httpPost(path = url, postBody = "", andExpect = Status.BadRequest)
    }

  }

}
