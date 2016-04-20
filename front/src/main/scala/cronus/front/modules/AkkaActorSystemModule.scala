package cronus.front.modules

import akka.actor.ActorSystem
import com.google.inject.{Provides, Singleton}
import com.twitter.inject.{Injector, Logging, TwitterModule}

object AkkaActorSystemModule extends TwitterModule with Logging{

  val system = Some(ActorSystem())

  override def singletonStartup(injector: Injector): Unit = {
    if (system.isEmpty) {
      info(s"AkkaActorSystemModule failed")
    } else {
      info(s"AkkaActorSystemModule ${system.get.name} started")
    }
  }

  override def singletonShutdown(injector: Injector): Unit = {
    info("AkkaActorSystemModule stopping")
    for (sys <- system) {
      info(s"AkkaActorSystemModule ${system.get.name} terminate")
      sys.terminate()
    }
  }

  @Singleton
  @Provides
  def providesActorSystem: ActorSystem = {
    debug("AkkaActorSystemModule Injected")
    system.get
  }

}
