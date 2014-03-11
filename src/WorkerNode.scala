import akka.actor.Actor

case class TellMeYourSecret()
case class ThisIsMySecret(secret: Int)
case class ThisIsSum(sum: Int)
case class SumReceived()

class WorkerNode(val secret: Int) extends Actor {
	var sum: Int = 0
  
	def receive = {
	  case TellMeYourSecret() =>
	    sender ! ThisIsMySecret(secret)
	  case ThisIsSum(_sum: Int) =>
	    sum = _sum
	    sender ! SumReceived()
	}
}