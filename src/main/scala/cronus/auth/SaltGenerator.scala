package cronus.auth

import java.security.SecureRandom

import com.twitter.util.Future

case class Salt(bytes: Array[Byte]) {
  def str(): String = new String(bytes.map(_.toChar))
}

private[auth] class SaltGenerator {

  val random = new SecureRandom()

  def getNext(): Future[Salt] = Future[Salt]{
    val salt = new Array[Byte](16)
    random.nextBytes(salt)
    Salt(salt)
  }

}
