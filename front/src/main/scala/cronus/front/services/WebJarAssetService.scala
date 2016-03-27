package cronus.front.services


import javax.inject.{Inject, Singleton}

import org.webjars.WebJarAssetLocator

import scala.io.Source

@Singleton
class WebJarAssetService @Inject()(webJarAssetLocator: WebJarAssetLocator) {

  def getFilePath(webjar: String, partialPath: String): Option[String] = {
    try {
      Some(webJarAssetLocator.getFullPath(webjar, partialPath))
    } catch  {
      case e: IllegalArgumentException => None
    }
  }

  def fileToString(path: String): Option[String] = {
    try {
      val is = webJarAssetLocator.getClass.getClassLoader.getResourceAsStream(path)
      if (null == is) {
        None
      } else {
        Some(Source.fromInputStream(is).mkString)
      }
    } catch  {
      case e: IllegalArgumentException => None
    }
  }

}
