package com.trivaris.kenmei.model.types

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive

@Serializable(with = ScoreSerializer::class)
data class Score(val value: Double?)

object ScoreSerializer : KSerializer<Score> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Score", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Score {
        val input = decoder as? JsonDecoder
            ?: error("This serializer can only be used with JSON")

        val jsonPrimitive = input.decodeJsonElement() as? JsonPrimitive
            ?: return Score(null)

        val value = jsonPrimitive.content.toDoubleOrNull()
        return Score(value)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Score) {
        if (value.value == null)
            encoder.encodeNull()
        else
            encoder.encodeDouble(value.value)
    }
}
