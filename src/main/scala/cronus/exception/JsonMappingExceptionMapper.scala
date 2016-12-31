package cronus.exception

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.exceptions.ExceptionMapper
import com.twitter.finatra.http.response.ResponseBuilder
import com.twitter.finatra.json.internal.caseclass.jackson.JacksonUtils
import com.twitter.inject.Logging
import javax.inject.{Inject, Singleton}

import com.fasterxml.jackson.databind.JsonMappingException

@Singleton
class JsonMappingExceptionMapper @Inject()(response: ResponseBuilder)
  extends ExceptionMapper[JsonMappingException]
    with Logging {

  override def toResponse(request: Request, e: JsonMappingException): Response = {
    warn(e)
    response
      .badRequest
      .jsonError(JacksonUtils.errorMessage(e))
  }
}
