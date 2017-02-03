package headtohead.future

import java.util.concurrent.Executors

import headtohead.future.Helper._

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

  def getHost(): Future[String] = Future.successful(java.net.InetAddress.getLocalHost.getHostName)

  def getVersion(): Future[String] = Future.successful(version)
}

case class DiagnosticResult(host: String, version: String)

object DiagnosticService {
  def diagnostics(): Future[DiagnosticResult] = {
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
  def diagnostics(): Future[String] = {
    DiagnosticService.diagnostics().map { diagnostic =>
      log("diagnostic result") {
        // OK ~> jsonString(diagnostic)
        diagnostic.toString
      }
    }
  }
}

object FutureExamples {
  def main(args: Array[String]): Unit = {
    log("main method") {
      val future = DiagnosticController.diagnostics()

      future.onComplete { diagnosticResult =>
        log("main method onComplete") {
          println(s"program finished with $diagnosticResult")
        }
      }

      Thread.sleep(2000)
    }
  }
}
