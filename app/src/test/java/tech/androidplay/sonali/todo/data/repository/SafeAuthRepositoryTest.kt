package tech.androidplay.sonali.todo.data.repository

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 12/Nov/2020
 * Email: ankush@androidplay.in
 */
class SafeAuthRepositoryTest {

    private lateinit var safeAuthRepository: SafeAuthRepository

    @Before
    fun setUp() {
        safeAuthRepository = SafeAuthRepository()
    }

    @Test
    fun passwordOrUsernameIsEmpty_returnFalse() {
        val result = safeAuthRepository.createAccountSafely(
            "",
            "Roni"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun passwordIsLessThanFourCharacters_returnFalse() {
        val result = safeAuthRepository.createAccountSafely(
            "ankush@androidplay.in",
            "123"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun validInput_returnTrue() {
        val result = safeAuthRepository.createAccountSafely(
            "ankushbose5@gmail.com",
            "Roni.123"
        )
        assertThat(result).isTrue()
    }
}