package demo

import java.util.concurrent.atomic.AtomicInteger

import com.github.dapeng.hello.scala._

object ScalaCombineTest extends App {
  val debug = false
  // scala sync->java sync
  val ssjsSuccess = new AtomicInteger(0)
  val ssjsFailed = new AtomicInteger(0)
  val ssjsClient = new JavaSyncHelloServiceClient

  // scala sync->java async
  val ssjaSuccess = new AtomicInteger(0)
  val ssjaFailed = new AtomicInteger(0)
  val ssjaClient = new JavaSyncHelloServiceAsyncClient

  // scala sync->scala sync
  val ssssSuccess = new AtomicInteger(0)
  val ssssFailed = new AtomicInteger(0)
  val ssssClient = new ScalaSyncHelloServiceClient

  // scala sync->scala async
  val sssaSuccess = new AtomicInteger(0)
  val sssaFailed = new AtomicInteger(0)
  val sssaClient = new ScalaAsyncHelloServiceAsyncClient

  // scala async->java sync
  val sajsSuccess = new AtomicInteger(0)
  val sajsFailed = new AtomicInteger(0)
  val sajsClient = new JavaSyncHelloServiceAsyncClient

  // scala async->java async
  val sajaSuccess = new AtomicInteger(0)
  val sajaFailed = new AtomicInteger(0)
  val sajaClient = new JavaAsyncHelloServiceAsyncClient

  // scala async->scala sync
  val sassSuccess = new AtomicInteger(0)
  val sassFailed = new AtomicInteger(0)
  val sassClient = new ScalaSyncHelloServiceAsyncClient

  // scala async->scala async
  val sasaSuccess = new AtomicInteger(0)
  val sasaFailed = new AtomicInteger(0)
  val sasaClient = new ScalaAsyncHelloServiceAsyncClient

}
