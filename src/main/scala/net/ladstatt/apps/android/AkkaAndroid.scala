package net.ladstatt.apps.android

import _root_.android.app.Activity
import akka.actor.{Actor, ActorRef, Props}
import akka.event.LoggingReceive
import akka.util.Timeout
import android.os.Bundle
import akka.pattern.ask
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import macroid.akkafragments.AkkaActivity

import scala.concurrent.duration._
import scala.util.{Failure, Success}

trait AndroidTools {

  def TAG: String

  def logInfo(message: String): Unit = {
    Log.i(TAG, message)
    ()
  }

  def logError(message: String): Unit = {
    Log.e(TAG, message)
    ()
  }

  def mkOnClickListener(f: => Unit): OnClickListener = {
    new OnClickListener {
      override def onClick(v: View): Unit = f
    }
  }

}

class MyActor extends Actor with AndroidTools {

  val TAG = "AkkaAndroid"

  override def receive: Receive = LoggingReceive {
    case _ => {
      logInfo("got message!")
      sender ! "ok"
    }

  }
}


class AkkaAndroid extends Activity with AkkaActivity with AndroidTools {

  def execOnUi(f: => Unit): Unit = {
    runOnUiThread(new Runnable {
      override def run(): Unit = f
    })
  }

  val TAG = "AkkaAndroid"

  val actorSystemName = TAG

  var myActor: ActorRef = _

  var startButton: Button = _

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    implicit val timeout = Timeout(5.second)
    implicit val ec = actorSystem.dispatcher

    setContentView(R.layout.akka)
    startButton = findViewById(R.id.btn_start).asInstanceOf[Button]

    assert(startButton != null)

    myActor = actorSystem.actorOf(Props(classOf[MyActor]))

    startButton.setOnClickListener(mkOnClickListener({
      logInfo("clicked")
      myActor ? "a message" onComplete {
        case Success(s) => execOnUi {
          startButton.setEnabled(false)
        }
        case Failure(e) => {
          e.printStackTrace()
          ???
        }
      }
    }))

    logInfo("onCreate called.")
  }

  override def onBackPressed(): Unit = {
    super.onBackPressed()
  }

  override def onStop: Unit = {
    super.onStop()
    logInfo("Stopped application.")
  }

  override def onPause {
    super.onPause
    logInfo("onPause called")
  }

  override def onResume {
    super.onResume
    logInfo("onResume called")
  }

  override def onDestroy {
    super.onDestroy
    //mqttServer.stopServer()
    actorSystem.shutdown()
    logInfo("onDestroy called")
  }

}
