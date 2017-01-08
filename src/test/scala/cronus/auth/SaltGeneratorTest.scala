package cronus.auth

import com.twitter.inject.Logging
import org.scalatest.WordSpec

import scala.annotation.tailrec

class SaltGeneratorTest extends WordSpec with Logging  {

  val salt = new SaltGenerator

  "SaltGenerator" should {

    "Not generate two salts that are the same" in {
      @tailrec
      def testSalt(i: Int): Unit = {
        if (i == 0) return
        val salt1 = new String(salt.getNext().map(_.toChar))
        val salt2 = new String(salt.getNext().map(_.toChar))
        assert(salt1 != salt2)
        testSalt(i-1)
      }
      testSalt(100)
    }

  }

}
