import msgpack4z._
import msgpack4z.CodecInstances.all._

object Sample extends App {
  case class Foo(a: Boolean, b: String, c: List[Int])
  val factory = new PackerUnpackerFactory {
    def packer = MsgOutBuffer.create()
    def unpacker(bytes: Array[Byte]) = MsgInBuffer(bytes)
  }

  val mapCodec = CaseMapCodec.string(factory)
  val instance = mapCodec.codec(Foo.apply _, Foo.unapply _)("a", "b", "c")

  val sample = Foo(true, "abcde", List(10, 20, 30))
  val bytes = instance.toBytes(sample, MsgOutBuffer.create())
  val unpack1 = MsgpackCodec[MsgpackUnion].unpackAndClose(MsgInBuffer(bytes))
  println(unpack1)

  val unpack2 = instance.unpackAndClose(MsgInBuffer(bytes))
  println(unpack2)
}
