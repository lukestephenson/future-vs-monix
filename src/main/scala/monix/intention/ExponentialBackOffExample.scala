package monix.intention

import java.util.concurrent.TimeoutException

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Future
import scala.concurrent.duration._

object ExponentialBackOffExample {
  def main(args: Array[String]): Unit = {
    var attempt = 0
    val startTime = System.currentTimeMillis()
    val task = Task.deferFuture(Future {
      val elapsed = System.currentTimeMillis()
      attempt = attempt + 1
      if (attempt < 10) {
        println(s"${Thread.currentThread().getName} - ${elapsed - startTime}ms - Attempt $attempt failed")
        throw new TimeoutException("Timeout")
      } else {
        "Hello"
      }
    })

    val taskWithRetryBackOff = runWithBackOff(task)

    taskWithRetryBackOff.runToFuture.onComplete { result =>
      println(s"${Thread.currentThread().getName} - ${System.currentTimeMillis() - startTime}ms - Completed with $result")
    }

    Thread.sleep(30000)
  }

  def runWithBackOff[T](task: Task[T], attemptsRemaining: Int = 10, retryDelayMs: Int = 10): Task[T] = {
    if (attemptsRemaining > 1)
      task.onErrorRecoverWith {
        case _: TimeoutException =>
          runWithBackOff(task, attemptsRemaining - 1, retryDelayMs * 2).delayExecution(retryDelayMs.milliseconds)
      }
    else task
  }
}
