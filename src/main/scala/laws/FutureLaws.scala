package laws

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

object FutureLaws {
  def main(args: Array[String]): Unit = {
    println("Example 1")
    example1()
    Thread.sleep(1000)
    println("Example 2")
    example2()
    Thread.sleep(1000)
  }

  def example1() = {
    val f1 = Future.failed(new RuntimeException("dodgy example 1"))

    val f2 = Future(println("second future is running dodgy example 1"))

    f1.flatMap(_ => f2)
  }

  def example2() = {
    val f1 = Future.failed(new RuntimeException("dodgy example 2"))

    f1.flatMap(_ => Future(println("second future is running dodgy example 2")))
  }
}
