package cronus.front.modules

import com.google.inject.{Provides, Singleton}
import com.twitter.inject.TwitterModule
import org.webjars.WebJarAssetLocator

object WebJarAssetModule extends TwitterModule {

  @Singleton
  @Provides
  def providesWebJarAssetLocator: WebJarAssetLocator = {
    new WebJarAssetLocator()
  }
}
