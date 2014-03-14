import akka.actor.Actor
import akka.actor.Props

/*
 * In this demo example 'secret' and 'index' are equal. Let's keep them both
 * for more flexibility and possible future changes.
 */
class WorkerNode(val secret: Int, val index: Int, val numWorkers: Int) extends Actor {
  // This is used for both partial sum (when children will send us their values) and
  // final sum (when parent will send us result)
  var sum: Int = secret

  // As we know in binary tree if node has an index 'i', its children are found at indices
  // '2i + 1' and '2i + 2'. We can test if these indices exceed total number of workers to
  // decide whether we should create more nodes or not
  val leftIndex = 2 * index + 1
  val rightIndex = 2 * index + 2
  val hasLeft = leftIndex < numWorkers
  val hasRight = rightIndex < numWorkers

  // Since we are going to send/receive Int values only, we need to keep track of state somehow.
  // The simplest way is to have counter of expected messages, 
  // which will tell us, what to do, when new message arrive.
  // 3 - we are waiting for 2 partial sums from children
  // 2 - we are waiting for 1 partial sum from child
  // 1 - we are waiting for final result from parent
  // Please note, that some Actors may start their life from state 2 for example (if has only one child) 
  var expectedMessages = {
    var i = 1
    if (hasLeft) i += 1
    if (hasRight) i += 1
    i
  }

  val left = if (hasLeft)
    context.actorOf(Props(classOf[WorkerNode], leftIndex, leftIndex, numWorkers), name = s"worker-${leftIndex}")
  else null
  val right = if (hasRight)
    context.actorOf(Props(classOf[WorkerNode], rightIndex, rightIndex, numWorkers), name = s"worker-${rightIndex}")
  else null

  if (!hasLeft && !hasRight) {
    // We are leaf. Nobody is going to send us partial sum.
    context.actorSelection("../") ! sum
  }

  def receive = {
    case value: Int =>
      if (expectedMessages == 3) {
        sum += value
      } else if (expectedMessages == 2) {
        sum += value
        // Send partial sum to parent
        context.actorSelection("../") ! sum
        if (index == 0) {
          // We are root. Should start distribution of final result.
          if (hasLeft) {
            left ! sum
          }
          if (hasRight) {
            right ! sum
          }
        }
      } else if (expectedMessages == 1) {
        // Final result
        sum = value
        // Continue distribution
        if (hasLeft) {
          left ! sum
        }
        if (hasRight) {
          right ! sum
        }
        // Let's print messages for leaf nodes to be sure that everything was delivered correctly.
        if (!hasLeft && !hasRight) {
          println("I am leaf number " + index + " and I got result: " + sum);
        }
      }
      expectedMessages -= 1
  }
}