/**
 * Copyright 2013-2015, AlwaysResolve Project (alwaysresolve.org), MOYD.CO LTD
 * This file incorporates work covered by the following copyright and permission notice:
 *
 * Copyright 2012 silenteh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scalaframes

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.channel.DefaultAddressedEnvelope
import io.netty.channel.socket.DatagramPacket

import payload.Header
import payload.Message
import org.slf4j.LoggerFactory
import java.util.List
import java.net.InetSocketAddress

class UDPDnsMessageDecoder extends MessageToMessageDecoder[DatagramPacket] {

  val logger = LoggerFactory.getLogger("app")

  //@Override
  override def decode(ctx: ChannelHandlerContext, buf: DatagramPacket, out: List[Object]): Unit = {
    logger.debug("Reading message delivered by UDP")
    // 12 it is the minimum lenght in bytes of the header
    if (buf.content.readableBytes() < 12) null
    else {
      // The length field was not received yet - return null.
      // This method will be invoked again when more packets are
      // received and appended to the buffer.

      // The length field is in the buffer.

      // Mark the current buffer position before reading the length field
      // because the whole frame might not be in the buffer yet.
      // We will reset the buffer position to the marked position if
      // there's not enough bytes in the buffer.
      //buf.markReaderIndex();

      // Read the length field.
      //val length = buf.readUnsigned
      //println(buf.readableBytes())

      // Il sender viene passato come terzo parametro al prossimo handler, dove verrà spacchettato
      out.add(new DefaultAddressedEnvelope[payload.Message, InetSocketAddress](payload.Message(buf.content), buf.sender, buf.sender.asInstanceOf[InetSocketAddress]))
    }

  }

  /*def fromBytesToString(buf: ChannelBuffer, length: Int) = {
    val marray = new Array[Byte](length)
    buf.readBytes(marray)
    new String(marray, "UTF-8")
  }*/

  /*def toBitArracy(byte: Int, size: Short): Array[Short] =
    Array.tabulate(size) { i => ((byte >> (size - i - 1)) % 2).toShort }*/

  // there is probably a better way via bit operations to calculate this.
  // not a tail recursion
  //def toInt(bits: Array[Short]): Int =
  //  if (bits.isEmpty) 0 else (bits.head * (scala.math.pow(2, bits.tail.length))).toInt + toInt(bits.tail)

}
