package monix.iwantparallel

import cats.Applicative
import cats.implicits._
import monix.eval.Task
import monix.execution.Scheduler

object MonixParallelApplicative {

  def main(args: Array[String]): Unit = {
    implicit val scheduler = Scheduler.fixedPool("monix-demo", 5)

    def makeATask(i: Int) = {
      Task {
        println(s"${Thread.currentThread().getName} is running job $i and will sleep for 5 seconds")
        Thread.sleep(5000)
        i * 10
      }
    }

    val allTasks: List[Task[Int]] = (1 to 10).map(makeATask).toList

    import monix.eval.Task.nondeterminism
    import monix.cats._
    implicitly[Applicative[Task]]

    // Using the cats `sequence` method
    val singleTask: Task[List[Int]] = allTasks.sequence

    singleTask.runAsync.onComplete { result =>
      println(s"All tasks completed with result $result")
    }

    Thread.sleep(20000)
  }
}
