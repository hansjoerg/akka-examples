package com.example

import akka.stream._
import akka.stream.scaladsl._
import akka.actor.ActorSystem
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success }
import akka.util.ByteString
import scala.concurrent._
import Tcp.IncomingConnection
import Tcp.ServerBinding


class Consumer(system: ActorSystem) {
  def run(){

    implicit val sys = system
    implicit val materializer = ActorMaterializer()
    val localhost = "127.0.0.1"

    Thread.sleep(3000)
    val testInput = ('a' to 'z').map(ByteString(_))

    val result = Source(testInput).via(Tcp().outgoingConnection(localhost, 9999)).
      runFold(ByteString.empty) { (acc, in) ⇒ acc ++ in }

    result.onComplete {
      case Success(result) =>
        println(s"Result: " + result.utf8String)
        println("Shutting down client")
        system.shutdown()
      case Failure(e) =>
        println("Failure: " + e.getMessage)
        system.shutdown()
    }


  }
}
