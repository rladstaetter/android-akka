package macroid.akkafragments

import akka.actor.{Actor, ActorSystem}
import com.typesafe.config.ConfigFactory
import android.app.Activity
import akka.event.Logging._
import android.util.Log

trait AkkaActivity {
  self: Activity ⇒
  val actorSystemName: String
  lazy val actorSystem = ActorSystem(
    actorSystemName,
    ConfigFactory.load(getApplication.getClassLoader),
    getApplication.getClassLoader
  )
}

