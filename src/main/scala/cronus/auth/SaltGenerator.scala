package cronus.auth

import java.security.SecureRandom

private[auth] class SaltGenerator {

  val random = new SecureRandom()

  def getNext(): Array[Byte] = {
    val salt = new Array[Byte](16)
    random.nextBytes(salt)
    salt
  }

}
