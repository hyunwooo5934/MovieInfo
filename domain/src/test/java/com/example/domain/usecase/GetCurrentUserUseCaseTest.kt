package com.example.domain.usecase

import com.example.domain.model.SocialLoginType
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetCurrentUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: GetCurrentUserUseCase

    @Before
    fun setUp() {
        userRepository = mockk()
        useCase = GetCurrentUserUseCase(userRepository)
    }

    @Test
    fun `로그인된 유저가 있으면 User를 반환한다`() = runTest {
        // given
        val fakeUser = User(
            uid = "uid_123",
            email = "test@test.com",
            displayName = "테스트유저",
            photoUrl = null,
            loginType = SocialLoginType.GOOGLE
        )
        coEvery { userRepository.getCurrentUser() } returns fakeUser

        // when
        val result = useCase()

        // then
        assertEquals(fakeUser, result)
        coVerify(exactly = 1) { userRepository.getCurrentUser() }
    }

    @Test
    fun `로그인된 유저가 없으면 null을 반환한다`() = runTest {
        // given
        coEvery { userRepository.getCurrentUser() } returns null

        // when
        val result = useCase()

        // then
        assertNull(result)
        coVerify(exactly = 1) { userRepository.getCurrentUser() }
    }
}
