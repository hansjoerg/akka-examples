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
