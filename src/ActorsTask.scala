import akka.actor.ActorSystem
import akka.actor.Props

object ActorsTask extends App {
	val system = ActorSystem("demo")
	val master = system.actorOf(Props(classOf[MasterNode], 101), name="master")
	master ! Start()
}