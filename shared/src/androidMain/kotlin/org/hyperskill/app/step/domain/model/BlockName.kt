package org.hyperskill.app.step.domain.model

actual val BlockName.supportedBlocksNames: Set<String>
    get() = setOf(
        CHOICE,
        CODE,
        MATCHING,
        SORTING,
        TABLE,
        STRING,
        MATH,
        NUMBER
    )