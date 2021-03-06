// automatically generated by the FlatBuffers compiler, do not modify

package com.fbs.app.main.viewmodel

import java.nio.*
import kotlin.math.sign
import com.google.flatbuffers.*

@Suppress("unused")
class UiData : Table() {

    fun __init(_i: Int, _bb: ByteBuffer)  {
        __reset(_i, _bb)
    }
    fun __assign(_i: Int, _bb: ByteBuffer) : UiData {
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
    companion object {
        fun validateVersion() = Constants.FLATBUFFERS_2_0_0()
        fun getRootAsUiData(_bb: ByteBuffer): UiData = getRootAsUiData(_bb, UiData())
        fun getRootAsUiData(_bb: ByteBuffer, obj: UiData): UiData {
            _bb.order(ByteOrder.LITTLE_ENDIAN)
            return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb))
        }
        fun createUiData(builder: FlatBufferBuilder, contentOffset: Int) : Int {
            builder.startTable(1)
            addContent(builder, contentOffset)
            return endUiData(builder)
        }
        fun startUiData(builder: FlatBufferBuilder) = builder.startTable(1)
        fun addContent(builder: FlatBufferBuilder, content: Int) = builder.addOffset(0, content, 0)
        fun endUiData(builder: FlatBufferBuilder) : Int {
            val o = builder.endTable()
            return o
        }
        fun finishUiDataBuffer(builder: FlatBufferBuilder, offset: Int) = builder.finish(offset)
        fun finishSizePrefixedUiDataBuffer(builder: FlatBufferBuilder, offset: Int) = builder.finishSizePrefixed(offset)
    }
}
