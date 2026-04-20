package com.example.data

import android.content.Context
import com.example.data.local.UserDataStore
import com.example.domain.model.SocialLoginType
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.manipulation.Ordering

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    val context = mockk<Context>(relaxed = true)
    val userDataStore = mockk<UserDataStore>()
    val repository = mockk<UserRepository>()

    @Test
    fun getCurrentUser_returnsValueFromDataStore(): Unit = runTest {
        // given
        val expected = User(
            uid = "1",
            email = "test@test.com",
            displayName = "hw",
            photoUrl = null,
            loginType = SocialLoginType.GOOGLE
        )

        coEvery { userDataStore.getLoginInfo() } returns expected

        val repository = repository

        // when
        val result = repository.getCurrentUser()

        // then
        assertEquals(expected, result)
    }
}