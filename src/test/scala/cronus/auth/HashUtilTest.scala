package cronus.auth

import com.twitter.inject.Logging
import com.twitter.util.Await
import org.scalatest.WordSpec


class HashUtilTest extends WordSpec with Logging {

  val defaultSalt = Await.result((new SaltGenerator).getNext())
  val hash = new HashUtil

  "HashUtil" should {

    "Should return the same hash when using the same hash and password" in {
      val hash1 = Await.result(hash.createPasswordHash("test", defaultSalt.bytes))
      val hash2 = Await.result(hash.createPasswordHash("test", defaultSalt.bytes))
      assert(hash1.str() == hash2.str())
    }

    "Should return different hash when using the same hash butt different password" in {
      val hash1 = Await.result(hash.createPasswordHash("test1", defaultSalt.bytes))
      val hash2 = Await.result(hash.createPasswordHash("test2", defaultSalt.bytes))
      assert(hash1.str() != hash2.str())
    }

    "Should return different hash when using different hash butt same password" in {
      val hash1 = Await.result(hash.createPasswordHash("test", defaultSalt.bytes))
      val otherSalt = Await.result((new SaltGenerator).getNext())
      val hash2 = Await.result(hash.createPasswordHash("test", otherSalt.bytes))
      assert(hash1.str() != hash2.str())
    }

  }

}
