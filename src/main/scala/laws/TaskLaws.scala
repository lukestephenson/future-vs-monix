package laws

import monix.eval.Task

import monix.execution.Scheduler.Implicits.global

object TaskLaws {
  def main(args: Array[String]): Unit = {
    println("Example 1")
    example1().runToFuture
    Thread.sleep(1000)
    println("Example 2")
    example2().runToFuture
    Thread.sleep(1000)
  }

  def example1() = {
    val f1 = Task.raiseError(new RuntimeException("dodgy example 1"))

    val f2 = Task(println("second future is running dodgy example 1"))

    f1.flatMap(_ => f2)
  }

  def example2() = {
    val f1 = Task.raiseError(new RuntimeException("dodgy example 2"))

    f1.flatMap(_ => Task(println("second future is running dodgy example 2")))
  }
}
