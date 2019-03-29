package headtohead.task

import java.util.concurrent.Executors

import headtohead.future.Helper._
import monix.eval.Task
import monix.execution.Scheduler

import scala.concurrent.{ExecutionContext, Future}

object Helper {
  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(10))

  def log[T](job: String)(f: => T): T = {
    println(s"$job on ${Thread.currentThread().getName}")
    f
  }
}

object Diagnostic {
  val version = "1.0"

  def getHost(): Task[String] = Task.now(java.net.InetAddress.getLocalHost.getHostName)

  def getVersion(): Task[String] = Task.now(version)
}

case class DiagnosticResult(host: String, version: String)

object DiagnosticService {
  def diagnostics(): Task[DiagnosticResult] = {
    Diagnostic.getHost().flatMap { host =>
      log("host result") {
        Diagnostic.getVersion().map { version =>
          log("version result") {
            DiagnosticResult(host, version)
          }
        }
      }
    }
  }
}

object DiagnosticController {
  def diagnostics(): Task[String] = {
    DiagnosticService.diagnostics().map { diagnostic =>
      log("diagnostic result") {
        // OK ~> jsonString(diagnostic)
        diagnostic.toString
      }
    }
  }
}

object TaskExamples {
  def main(args: Array[String]): Unit = {
    log("main method") {
      val scheduler = Scheduler(ec)
      val task = DiagnosticController.diagnostics()

      val task2 = task.map { diagnosticResult =>
        log("main method doOnFinish") {
          println(s"program finished with $diagnosticResult")
        }
      }

      task2.runToFuture(scheduler)
     }
  }
}
