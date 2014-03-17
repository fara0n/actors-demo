import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.testkit.TestKit
import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll
import akka.testkit.ImplicitSender
import akka.testkit.CallingThreadDispatcher
import akka.testkit.TestProbe

class Tests2(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("demo"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A master actor" must {

    "get final result" in {
      val master = system.actorOf(Props(classOf[MasterNode], 101))
      val probe = new TestProbe(system)
      master ! probe.ref
      probe.expectMsg(5050)
    }

  }
}