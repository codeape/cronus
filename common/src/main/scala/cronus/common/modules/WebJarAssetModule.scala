package cronus.common.modules

import com.google.inject.{Provides, Singleton}
import com.twitter.inject.{Logging, TwitterModule}
import org.webjars.WebJarAssetLocator

object WebJarAssetModule extends TwitterModule with Logging {

  @Singleton
  @Provides
  def providesWebJarAssetLocator: WebJarAssetLocator = {
    debug("WebJarAssetLocator Injected")
    new WebJarAssetLocator()
  }

}
