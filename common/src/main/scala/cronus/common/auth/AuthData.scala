package cronus.common.auth

import com.fasterxml.jackson.annotation.JsonProperty

case class AuthData(@JsonProperty("token") token: String)
