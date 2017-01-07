package cronus.auth

import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

import com.twitter.util.Future
import sun.misc.{BASE64Decoder, BASE64Encoder}

import scala.annotation.tailrec

private[auth] class CryptUtil(private val key: String) {

  private val keyAlgorithm = "AES"
  private val algorithm = "AES"
  private val iterations = 2

  // We need a 128 bit key for AES
  private val key128bit = {
    val byteKey = key.getBytes("UTF-8")
    val sha = MessageDigest.getInstance("SHA-1")
    val digest = sha.digest(byteKey)
    val keySlice = digest.slice(0, 16)
    new SecretKeySpec(keySlice, keyAlgorithm)
  }

  def encrypt(salt: String, data: String): Future[String] = Future[String] {
    @tailrec
    def encryptIteration(cipher: Cipher, srcSalt: String, src: String, count: Int): String = count match {
      case 0 => src
      case c =>
        val saltedData = s"$srcSalt$src"
        val encryptedData = cipher.doFinal(saltedData.getBytes("UTF-8"))
        encryptIteration(cipher, srcSalt, new BASE64Encoder().encode(encryptedData), count - 1)
    }
    val cipher = Cipher.getInstance(algorithm)
    cipher.init(Cipher.ENCRYPT_MODE, key128bit)
    encryptIteration(cipher, salt, data, iterations)
  }

  def decrypt(salt: String, encryptedData: String): Future[String] = Future[String] {
    @tailrec
    def decryptIteration(cipher: Cipher, srcSalt: String, src: String, count: Int): String = count match {
      case 0 => src
      case c =>
        val encryptedData = new BASE64Decoder().decodeBuffer(src)
        val decryptedData = cipher.doFinal(encryptedData)
        val desaltedData = new String(decryptedData).drop(salt.length)
        decryptIteration(cipher, srcSalt, desaltedData, count - 1)
    }
    val cipher = Cipher.getInstance(algorithm)
    cipher.init(Cipher.DECRYPT_MODE, key128bit)
    decryptIteration(cipher, salt, encryptedData, iterations)
  }
}
