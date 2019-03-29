package monix.intention

import monix.eval.Task

import scala.concurrent.duration._

object SeparateDefinitionFromExecution {
  def main(args: Array[String]): Unit = {
    val task = Task {
      println(s"headtohead.task A is running ${Thread.currentThread().getName}")
      5
    }.map { result =>
      println(s"calculated result ${Thread.currentThread().getName}")
      result * 2
    }


    task.delayExecution(2 seconds).runToFuture(monix.execution.Scheduler.Implicits.global)

    println(s"hello ${Thread.currentThread().getName}")

    Thread.sleep(10000)

  }
}
