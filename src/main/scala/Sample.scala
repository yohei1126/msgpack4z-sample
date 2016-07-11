import msgpack4z._
import msgpack4z.CodecInstances.all._

import scalaz.\/-

object Sample extends App {
  case class Foo(a: Boolean, b: String, c: List[Int])
  val factory = new PackerUnpackerFactory {
    def packer = MsgOutBuffer.create()
    def unpacker(bytes: Array[Byte]) = MsgInBuffer(bytes)
  }

  val mapCodec = CaseMapCodec.string(factory)
  val instance = mapCodec.codec(Foo.apply _, Foo.unapply _)("a", "b", "c")

  val sample = Foo(true, "abcde", List(10, 20, 30))
  println ("original value = " + sample)

  // pack
  val bytes = instance.toBytes(sample, MsgOutBuffer.create())

  // unpack
  val unpack1 = instance.unpackAndClose(MsgInBuffer(bytes))
  unpack1 match {
    case \/-(Foo(a,b,c)) => println ("unpacked value: a = " + a + ", b = " + b + ", c = " + c)
    case _ => println ("unpack failed")
  }
}
