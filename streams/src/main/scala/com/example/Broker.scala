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


/*
netcat -l 9999
echo "sss" | netcat localhost 8888
*/




class Broker(system: ActorSystem) {
  def run(){

    implicit val sys = system  
    implicit val materializer = ActorMaterializer()


    val host = "127.0.0.1"

    val producer: Source[IncomingConnection, Future[ServerBinding]] = Tcp().bind(host, 8888)
    val consumer = Tcp().outgoingConnection(host, 9999)


    val echo = Flow[ByteString]
      .via(Framing.delimiter(
        ByteString("\n"),
        maximumFrameLength = 256, 
        allowTruncation = true))
      .map(_.utf8String)
      .map(_ + " sent \n")
      .map(ByteString(_))
      .via(consumer)


    producer runForeach { connection =>
      println(s"New producer from: ${connection.remoteAddress}") 
      connection.handleWith(echo)
    }


  }
}
