import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef

case class Start()

class MasterNode(val count: Int) extends Actor {
	var sum: Int = 0
	var currentCount: Int = 0
	var workers: Seq[ActorRef] = Nil
  
	def receive = {
	  case Start() =>
	    workers = createWorkers(count)
	    workers.foreach(worker => worker ! TellMeYourSecret())
	  case ThisIsMySecret(secret: Int) =>
	    sum += secret
	    currentCount += 1
	    if (currentCount == count) {
	      // All the nodes have sent us a secret. We can start sum distribution.
	      currentCount = 0
	      workers.foreach(worker => worker ! ThisIsSum(sum))
	    }
	  case SumReceived() =>
	    currentCount += 1
	    if (currentCount == count) {
	      // Secret sum was successfully delivered to all the nodes.
	      // We can start doing something useful here. In this demo example
	      // we just print sum and shutdown.
	      println(sum)
	      context.system.shutdown()
	    }
	}
	
	private def createWorkers(count: Int) = {
	  	// In this demo example we pass index to each Worker node as constructor parameter.
	  	// This value will be used as secret. In real world scenario secret values will probably
	  	// appear somehow differently on Worker nodes.
		for (i <- 0 until count) 
			yield context.actorOf(Props(classOf[WorkerNode], i), name = s"worker-${i}")
	}
}