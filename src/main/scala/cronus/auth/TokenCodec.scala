package cronus.auth

case class UserWithToken(uid: String, timeStamp: String, token: String){}

case class AuthShuttle(uid: String="", password: String="", token: String = "")

private[auth] object TokenCodec {

  def tokenToUserWithToken(token: String): Option[UserWithToken] = {
    token match {
      case "TESTTOKEN" => Some(UserWithToken("test", "12:12:12", "TESTTOKEN"))
      case _ => None
    }
  }

  def userToToken(user: String): Option[String] = {
    user match {
      case "test" => Some("TESTTOKEN")
      case _ => None
    }
  }

}
