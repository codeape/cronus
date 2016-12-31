package cronus.modules

import com.google.inject.{Provides, Singleton}
import com.twitter.inject.TwitterModule

case class ConfigFlags(cronusAssetPath: Option[String])

class CronusConfigFlagsModule extends TwitterModule {

  private val cronusAssetPath = flag("cronus.asset.path", "", "The path to the sours directory of cronus asset web jar.")

  @Provides
  @Singleton
  def providesConfigFlags() = {
    val path: Option[String] = cronusAssetPath.get
    ConfigFlags(path)
  }

}
