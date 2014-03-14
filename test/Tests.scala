import org.scalatest.FunSuite
import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.actor.Props

class Tests extends FunSuite {

  implicit val system = ActorSystem("demo")
  
  test("Binary Tree property test 1") {    
    val actorRef = TestActorRef(new WorkerNode(3, 3, 5))
    val actor = actorRef.underlyingActor
    assert(actor.hasRight == false)
    assert(actor.hasLeft == false)
  }
  
  test("Binary Tree property test 2") {    
    val actorRef = TestActorRef(new WorkerNode(3, 3, 50))
    val actor = actorRef.underlyingActor
    assert(actor.hasRight == true)
    assert(actor.hasLeft == true)
  }
  
  test("Final result distribution test 1") {    
    val actorRef = TestActorRef(new WorkerNode(0, 0, 20))
    val actor = actorRef.underlyingActor
    
    Thread.sleep(3000);
    assert(actor.sum == 190)
  }
  
  test("Final result distribution test 2") {    
    val actorRef = TestActorRef(new WorkerNode(0, 0, 101))
    val actor = actorRef.underlyingActor
    
    Thread.sleep(3000);
    assert(actor.sum == 5050)
  }
}