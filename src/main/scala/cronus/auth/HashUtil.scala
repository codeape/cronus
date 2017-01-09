package cronus.auth

import javax.crypto.{SecretKey, SecretKeyFactory}
import javax.crypto.spec.PBEKeySpec

import com.twitter.util.Future

case class Hash(bytes: Array[Byte]) {
  def str(): String = new String(bytes.map(_.toChar))
}

private[auth] class HashUtil {

  val iterations: Int = 2048
  val keylength: Int = 512

  def createPasswordHash(password: String, salt: Array[Byte]): Future[Hash] = Future[Hash]{
    val skf: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
    val spec: PBEKeySpec = new PBEKeySpec(password.toArray, salt, iterations, keylength)
    val key: SecretKey = skf.generateSecret(spec)
    Hash(key.getEncoded)
  }

}
