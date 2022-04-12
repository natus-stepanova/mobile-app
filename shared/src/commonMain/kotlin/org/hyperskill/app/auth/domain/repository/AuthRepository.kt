package org.hyperskill.app.auth.domain.repository

interface AuthRepository {
    suspend fun isAuthorized(): Result<Boolean>
    suspend fun authWithCode(authCode: String): Result<Unit>
}