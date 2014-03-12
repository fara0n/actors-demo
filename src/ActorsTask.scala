import akka.actor.ActorSystem
import akka.actor.Props

object ActorsTask extends App {
    val defaultClusterSize = 101
    val clusterSize = if (args.length > 0) args(0).toInt else defaultClusterSize
	val system = ActorSystem("demo")
	val master = system.actorOf(Props(classOf[MasterNode], clusterSize), name="master")
}