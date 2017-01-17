package cronus.modules

import com.google.inject.{Provides, Singleton}

import com.twitter.inject.TwitterModule

case class ConfigFlags(cronusAssetPaths: Option[List[String]])

class CronusConfigFlagsModule extends TwitterModule {

  private val cronusAssetPaths = flag(
    "cronus.asset.path",
    Seq.empty[String],
    "The paths to the source directories of cronus asset web jar."
  )

  @Provides
  @Singleton
  def providesConfigFlags() = {
    val paths: Option[Seq[String]] = cronusAssetPaths.get
    ConfigFlags(paths.map(s => s.toList))
  }

}
