package cronus.front.services


import javax.inject.{Inject, Singleton}

import org.webjars.WebJarAssetLocator

@Singleton
class WebJarAssetService @Inject()(webJarAssetLocator: WebJarAssetLocator) {

  def getFilePath(webjar: String, partialPath: String): Option[String] = {
    try {
      Some(webJarAssetLocator.getFullPath(webjar, partialPath))
    } catch  {
      case e: IllegalArgumentException => None
    }
  }

  def fileToString(path: String): Option[Array[Byte]] = {
    try {
      val is = webJarAssetLocator.getClass.getClassLoader.getResourceAsStream(path)
      if (null == is) {
        None
      } else {
        Some(Iterator.continually(is.read()).takeWhile(-1!=_).map(_.toByte).toArray)
      }
    } catch  {
      case e: IllegalArgumentException => None
    }
  }

}
