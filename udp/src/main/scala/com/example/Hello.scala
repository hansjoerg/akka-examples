package com.example

import java.net.InetSocketAddress;
import java.net.InetAddress;

import akka.io.{IO, Udp}
import akka.actor.{Stash, Props, ActorRef, Actor}
import java.net.InetSocketAddress
import akka.util.ByteString
import akka.actor.ActorSystem


class UdpConnection extends Actor  {
  import context.system
  //InetAddress address = InetAddress.getByName(GameOptions.host);
  //val bindAddr = new InetSocketAddress("localhost");

  IO(Udp) ! Udp.Bind(self, new InetSocketAddress("localhost", 9001))

  def receive = {
    case Udp.Bound(local) =>
      context.become(ready(sender()))
  }
 
  def ready(socket: ActorRef): Receive = {
    case Udp.Received(data, remote) =>
      //val processed = // parse data etc., e.g. using PipelineStage
      //socket ! Udp.Send(data, remote) // example server echoes back
      //nextActor ! processed
      println(data.utf8String)
    case Udp.Unbind  => socket ! Udp.Unbind
    case Udp.Unbound => context.stop(self)
  }
}


object Hello {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("hbr")
    val myActor = system.actorOf(Props[UdpConnection], "udpserver")
    print("Press ENTER to shutdown")
    val ln = readLine()
    system.shutdown()
    system.awaitTermination()
  }  
}
