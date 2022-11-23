package org.hyperskill.app.sentry.domain.model.breadcrumb

/**
 * Represents the category of the event.
 * This data is similar to a logger name, and helps understand the area in which an event took place, such as *auth*.
 *
 * @property stringValue The raw string value.
 */
enum class HyperskillSentryBreadcrumbCategory(val stringValue: String) {
    CATEGORY_AUTH_SOCIAL("auth_social"),
    CATEGORY_AUTH_CREDENTIALS("auth_social_credentials")
}