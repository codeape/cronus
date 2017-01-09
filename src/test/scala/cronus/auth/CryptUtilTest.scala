package cronus.auth

import java.nio.charset.StandardCharsets
import javax.crypto.{BadPaddingException, IllegalBlockSizeException}

import com.twitter.inject.Logging
import com.twitter.util.Await
import org.scalatest.WordSpec

import scala.annotation.tailrec

class CryptUtilTest extends WordSpec with Logging {

  val cryptoUtil = new CryptUtil("TestKeyLongerThan128Bits!")
  val defaultSalt = Await.result((new SaltGenerator).getNext()).bytes
  val data = "A fine string"

  "CryptoUtil" should {

    "Encrypt and decrypt a string correctly" in {
      val decryptedDataFuture = cryptoUtil.encrypt(defaultSalt, data).flatMap{ encryptedData =>
        logger.debug(s"Encrypted data: $encryptedData")
        assert(encryptedData != data, "Enrypted data is the same as the source data")
        cryptoUtil.decrypt(defaultSalt, encryptedData)
      }
      val decryptedData = Await.result(decryptedDataFuture)
      assert(decryptedData == data, "The encrypted data was not the same as the source")
    }

    "Encrypt and decrypt a empty string and salt correctly" in {
      val mysalt = "".getBytes(StandardCharsets.UTF_8)
      val decryptedDataFuture = cryptoUtil.encrypt(mysalt, "").flatMap{ encryptedData =>
        assert(encryptedData != data, "Enrypted data is the same as the source data")
        cryptoUtil.decrypt(mysalt, encryptedData)
      }
      val decryptedData = Await.result(decryptedDataFuture)
      assert(decryptedData == "", "The encrypted data was not the same as the source")
    }

    "Encrypt and decrypt many string correctly" in {
      @tailrec
      def testEncryptDecrypt(data: String, count: Int): Unit ={
        if (count == 0) return
        val decryptedDataFuture = cryptoUtil.encrypt(defaultSalt, data).flatMap{ encryptedData =>
          assert(encryptedData != data, "Enrypted data is the same as the source data")
          cryptoUtil.decrypt(defaultSalt, encryptedData)
        }
        val decryptedData = Await.result(decryptedDataFuture)
        assert(decryptedData == data, "The encrypted data was not the same as the source")
        testEncryptDecrypt(data + "a", count-1)
      }
      testEncryptDecrypt("", 1000)
    }

    "Fail decrypting with the wrong key" in {
      val encryptedDataFuture = cryptoUtil.encrypt(defaultSalt, data)
      val encryptedData = Await.result(encryptedDataFuture)
      try {
        assert(encryptedData != data, "Enrypted data is the same as the source data")
        val cryptoUtilFail = new CryptUtil("WrongTestKey")
        val decryptDataFuture = cryptoUtilFail.decrypt(defaultSalt, encryptedData)
        Await.result(decryptDataFuture)
        fail("We did not get BadPaddingException")
      } catch {
        case e: BadPaddingException => logger.debug("Ok, got BadPaddingException")
        case e: Throwable => fail(s"We did not get BadPaddingException, got $e instead")
      }
    }

    "Fail to Encrypt and decrypt a string correctly with wrong salt" in {
      val encryptedDataFuture = cryptoUtil.encrypt(defaultSalt, data)
      val encryptedData = Await.result(encryptedDataFuture)
      try {
        val decryptedDataFuture = cryptoUtil.decrypt("wrong".getBytes(), encryptedData)
        val decryptedData = Await.result(decryptedDataFuture)
        fail(s"We did not get IllegalBlockSizeException, returned: $decryptedData")
      } catch {
        case e: IllegalBlockSizeException => logger.debug("Ok, got IllegalBlockSizeException")
        case e: Throwable => fail(s"We did not get IllegalBlockSizeException, got $e instead")
      }

    }

  }

}
