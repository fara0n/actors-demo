import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef

class MasterNode(val numWorkers: Int) extends Actor {
  val root = context.actorOf(Props(classOf[WorkerNode], 0, 0, numWorkers), name = "worker-0")

  var testActor: ActorRef = _

  def receive = {
    case (actor: ActorRef) =>
      testActor = actor
    case value: Int =>
      println("Result sum was obtained by master node: " + value)
      if (testActor != null) testActor ! value
  }
}