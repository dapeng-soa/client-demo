package demo

import com.github.dapeng.hello.scala.{ScalaAsyncHelloServiceAsyncClient, ScalaSyncHelloServiceClient}
import com.github.dapeng.hello.scala.domain.Hello

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


object ScalaClientTest extends App {
  val begin = System.currentTimeMillis()
  val times = 10000
  (0 to times).foreach(id => {
    syncTest(id)

//        asyncTest(id)
  }
  )
  println(s"${times} costs:${System.currentTimeMillis() - begin}")


  def syncTest(id: Int): Unit = {

    val client: ScalaSyncHelloServiceClient = new ScalaSyncHelloServiceClient
    val response = client.sayHello(Hello("今天便利店", id))
    println(response)
  }

  def asyncTest(id: Int): Unit = {
    val client: ScalaAsyncHelloServiceAsyncClient = new ScalaAsyncHelloServiceAsyncClient
    val response: Future[String] = client.sayHello(Hello("今天便利店", id), 200)
    response.onComplete({
      case Success(result) => println(result)
      case Failure(e) => println(e.getMessage)
    })(ExecutionContext.global)

  }
}
