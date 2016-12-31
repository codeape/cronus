package cronus.controllers

import com.google.inject.Inject
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.inject.Logging
import cronus.domain.RequireJSHelper

class MainJsController @Inject()(
                                  requireJSHelper: RequireJSHelper
                                )
  extends Controller with Logging
{
  val mainJsSource = getMainFile()

  private def getMainFile(): String = {
    val inclueded = requireJSHelper.getRequireJSModules(
      "/assets/",
      Set(
        "jquery",
        "underscorejs",
        "backbonejs",
        "mustachejs"
      )
    )
    .map(setup => s"requirejs.config(${setup.setup});")
    .mkString("\n")

    s"""
       |$inclueded
       |
       |requirejs.config({baseUrl: '/'});
       |
       |require( ['assets/cronusw/js/cronus-time'], function ( cronus ) {});
     """.stripMargin
  }

  get("/main.js") { request: Request =>
    response.ok.contentType("application/javascript").body(mainJsSource)
  }

}

