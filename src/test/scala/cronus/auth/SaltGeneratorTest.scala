package cronus.auth

import com.twitter.inject.Logging
import com.twitter.util.Await
import org.scalatest.WordSpec

import scala.annotation.tailrec

class SaltGeneratorTest extends WordSpec with Logging  {

  val salt = new SaltGenerator

  "SaltGenerator" should {

    "Not generate two salts that are the same" in {
      @tailrec
      def testSalt(i: Int): Unit = {
        if (i == 0) return
        val salt1 = Await.result(salt.getNext()).str()
        val salt2 = Await.result(salt.getNext()).str()
        assert(salt1 != salt2)
        testSalt(i-1)
      }
      testSalt(100)
    }

  }

}
