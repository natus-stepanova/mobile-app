package org.hyperskill.app.step.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Step(
    @SerialName("id")
    val id: Long,
    @SerialName("stepik_id")
    val stepikId: Long,
    @SerialName("lesson_stepik_id")
    val lessonStepikId: Long,
    @SerialName("position")
    val position: Int,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: String,
    @SerialName("block")
    val block: Block,
    @SerialName("topic")
    val topic: Long,
    @SerialName("topic_theory")
    val topicTheory: Long,
    @SerialName("can_abandon")
    val canAbandon: Boolean,
    @SerialName("can_skip")
    val canSkip: Boolean,
    @SerialName("content_created_at")
    val contentCreatedAt: Instant,
    @SerialName("content_updated_at")
    val contentUpdatedAt: Instant
)