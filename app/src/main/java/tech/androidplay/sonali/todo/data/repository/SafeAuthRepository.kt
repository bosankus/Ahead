package tech.androidplay.sonali.todo.data.repository

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 12/Nov/2020
 * Email: ankush@androidplay.in
 */
class SafeAuthRepository {

    fun createAccountSafely(
        username: String,
        password: String
    ): Boolean {
        return if (password.isEmpty() || username.isEmpty()) {
            false
        } else if (password.count { it.isLetterOrDigit() } < 4) {
            false
        } else (password.isNotEmpty() &&
                username.isNotEmpty() &&
                password.count { it.isLetterOrDigit() } >= 4)
    }
}