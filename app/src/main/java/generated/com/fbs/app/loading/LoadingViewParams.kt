// automatically generated by the FlatBuffers compiler, do not modify

package com.fbs.app.loading

import java.nio.*
import kotlin.math.sign
import com.google.flatbuffers.*

@Suppress("unused")
class LoadingViewParams : Table() {

    fun __init(_i: Int, _bb: ByteBuffer)  {
        __reset(_i, _bb)
    }
    fun __assign(_i: Int, _bb: ByteBuffer) : LoadingViewParams {
        __init(_i, _bb)
        return this
    }
    val content : String?
        get() {
            val o = __offset(4)
            return if (o != 0) __string(o + bb_pos) else null
        }
    val contentAsByteBuffer : ByteBuffer get() = __vector_as_bytebuffer(4, 1)
    fun contentInByteBuffer(_bb: ByteBuffer) : ByteBuffer = __vector_in_bytebuffer(_bb, 4, 1)
    val show : Byte
        get() {
            val o = __offset(6)
            return if(o != 0) bb.get(o + bb_pos) else 0
        }
    companion object {
        fun validateVersion() = Constants.FLATBUFFERS_2_0_8()
        fun getRootAsLoadingViewParams(_bb: ByteBuffer): LoadingViewParams = getRootAsLoadingViewParams(_bb, LoadingViewParams())
        fun getRootAsLoadingViewParams(_bb: ByteBuffer, obj: LoadingViewParams): LoadingViewParams {
            _bb.order(ByteOrder.LITTLE_ENDIAN)
            return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb))
        }
        fun createLoadingViewParams(builder: FlatBufferBuilder, contentOffset: Int, show: Byte) : Int {
            builder.startTable(2)
            addContent(builder, contentOffset)
            addShow(builder, show)
            return endLoadingViewParams(builder)
        }
        fun startLoadingViewParams(builder: FlatBufferBuilder) = builder.startTable(2)
        fun addContent(builder: FlatBufferBuilder, content: Int) = builder.addOffset(0, content, 0)
        fun addShow(builder: FlatBufferBuilder, show: Byte) = builder.addByte(1, show, 0)
        fun endLoadingViewParams(builder: FlatBufferBuilder) : Int {
            val o = builder.endTable()
            return o
        }
        fun finishLoadingViewParamsBuffer(builder: FlatBufferBuilder, offset: Int) = builder.finish(offset)
        fun finishSizePrefixedLoadingViewParamsBuffer(builder: FlatBufferBuilder, offset: Int) = builder.finishSizePrefixed(offset)
    }
}
