package cronus.front

import com.google.inject.Stage
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

class FrontServerStartupTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(
    twitterServer = new FrontServer,
    stage = Stage.PRODUCTION,
    verbose = false)

  "Server" should {
    "startup" in {
      server.assertHealthy()
    }
  }

}
