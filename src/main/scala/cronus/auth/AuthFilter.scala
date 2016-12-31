package cronus.auth

import javax.inject.Singleton

import com.google.inject.Inject
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finatra.http.response.ResponseBuilder
import com.twitter.finatra.json.FinatraObjectMapper
import com.twitter.inject.Logging
import com.twitter.util.Future

@Singleton
class AuthFilter @Inject()(
                            finatraObjectMapper: FinatraObjectMapper,
                            responseBuilder: ResponseBuilder
                          )
  extends SimpleFilter[Request, Response] with Logging
{
  override def apply(request: Request, service: Service[Request, Response]): Future[Response] = {
    logger.debug(s"AuthFilter: Content type: ${request.contentType}")
    logger.debug(s"AuthFilter: Content string: ${request.contentString}")
    try {
      val authData = finatraObjectMapper.parse[AuthData](request.contentString)
      AuthDataContext.setTokenData(request, Some(authData))
      logger.debug(s"AuthData: ${authData.token}")
      service(request)
    } catch {
      case e: Exception =>
        logger.debug(e)
        Future.value(responseBuilder
          .status(401))

    }
  }
}

object AuthDataContext extends Logging {
  private val TokenField = Request.Schema.newField[Option[AuthData]]()

  implicit class TokenDataContextSyntax(val request: Request) extends AnyVal {
    def authData: Option[AuthData] = request.ctx(TokenField)
  }

  private[cronus] def setTokenData(request: Request, authData: Option[AuthData]): Unit = {
    request.ctx.update(TokenField, authData)
  }
}