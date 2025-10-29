package dev.logickoder.newshub.app.data.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime

object LocalDateTimeSerializer : KSerializer<LocalDateTime?> {
    override val descriptor = PrimitiveSerialDescriptor(
        javaClass.simpleName,
        PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): LocalDateTime? {
        return when (val data = decoder.decodeString()) {
            "0001-01-01T00:00:00" -> null
            else -> LocalDateTime.parse(data.removeSuffix("Z"))
        }
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime?) {
        if (value == null) {
            return
        }
        encoder.encodeString(value.toString())
    }
}