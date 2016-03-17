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
import scala.concurrent._
import akka.pattern.ask
import scala.concurrent._


import scala.concurrent.duration._

*/



object Hello {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("QuickStart")
    implicit val materializer = ActorMaterializer()
    //val broker = new Broker(system).run
    //val consumer = new Consumer(system).run
    //val spike = new SleepyEcho(system).run

    //val source = Source(1 to 10).map(_.toString)
    //Source.single("just one")
    val source = Source.repeat("and again")
    val fun = Flow[String].map(s => s + " ;-)")
    val sink = Sink.foreach[String] {
      println
    }
    source.via(fun).runWith(sink)
  }
}


/*


  implicit val system = ActorSystem("hbr")
  val (address, port) = ("127.0.0.1", 6000)
  server(system, address, port)
  client(system, address, port)



  val in = readLine()
  system.shutdown()
  system.awaitTermination()
  println("Done!")
  }
  def server(system: ActorSystem, address: String, port: Int): Unit = {
    implicit val sys = system
    import system.dispatcher
    implicit val materializer = ActorMaterializer()

    val handler = Sink.foreach[Tcp.IncomingConnection] { conn =>
      println("Client connected from: " + conn.remoteAddress)
      conn handleWith Flow[ByteString]
    }

    val connections = Tcp().bind(address, port)
    val binding = connections.to(handler).run()

    binding.onComplete {
      case Success(b) =>
        println("Server started, listening on: " + b.localAddress)
      case Failure(e) =>
        println(s"Server could not bind to $address:$port: ${e.getMessage}")
        system.shutdown()
    }

  }

  def client(system: ActorSystem, address: String, port: Int): Unit = {
    implicit val sys = system
    import system.dispatcher
    implicit val materializer = ActorMaterializer()

    val testInput = ('a' to 'z').map(ByteString(_))

    val result = Source(testInput).via(Tcp().outgoingConnection(address, port)).
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
  

*/


/*

val source: Source[Int, NotUsed] = Source(1 to 100)
	source.runForeach(i => println(i))(materializer)

*/


/*
package com.example


import scala.concurrent._

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ Flow, Sink, Source, Tcp }
import akka.util.ByteString
import scala.concurrent.duration._
import scala.util.{ Failure, Success }


object Hello {
  def main(args: Array[String]): Unit = {


	implicit val system: ActorSystem = ActorSystem("hbr")
    import system.dispatcher
    implicit val materializer = ActorMaterializer()

	val source = Source(1 to 10)
	val sink = Sink.fold[Int, Int](0)(_ + _)
	 
	// materialize the flow, getting the Sinks materialized value
	val sum: Future[Int] = source.runWith(sink)

	sum.onComplete {
		case Success(result) => {
			println(s"A miracle! got $result ")
			system.shutdown()
		}      	
    	case Failure(ex) => {
			println(s"They broke their promises! Again! Because of a ${ex.getMessage}")
			system.shutdown()
    	}       	
	}
	println("Done!")
  }
}

*/