package org.hyperskill.app.step_quiz.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

class StepQuizHiddenDailyNotificationsNoticeHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    isAgreed: Boolean
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.DAILY_NOTIFICATIONS_NOTICE,
    target = if (isAgreed) HyperskillAnalyticTarget.OK else HyperskillAnalyticTarget.LATER
)