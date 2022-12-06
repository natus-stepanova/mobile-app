package org.hyperskill.app.topics_repetitions.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Repetition(
    @SerialName("id")
    val id: Long,
    @SerialName("topic_id")
    val topicId: Long,
    @SerialName("next_repeat_at")
    val nextRepeatAt: Instant,
    @SerialName("steps")
    val steps: List<Long>,
)