package com.example.domain.usecase

import com.example.domain.model.AppError
import com.example.domain.model.SocialLoginType
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SocialLoginUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: SocialLoginUseCase

    @Before
    fun setUp() {
        userRepository = mockk()
        useCase = SocialLoginUseCase(userRepository)
    }

    @Test
    fun `GOOGLE 로그인 성공 시 Result_success와 User를 반환한다`() = runTest {
        // given
        val token = "google_token_abc"
        val fakeUser = User(
            uid = "uid_google",
            email = "google@test.com",
            displayName = "구글유저",
            photoUrl = "https://photo.url",
            loginType = SocialLoginType.GOOGLE
        )
        coEvery { userRepository.signIn(token, SocialLoginType.GOOGLE) } returns Result.success(fakeUser)

        // when
        val result = useCase(token, SocialLoginType.GOOGLE)

        // then
        assertTrue(result.isSuccess)
        assertEquals(fakeUser, result.getOrNull())
        coVerify(exactly = 1) { userRepository.signIn(token, SocialLoginType.GOOGLE) }
    }

    @Test
    fun `NAVER 로그인 성공 시 Result_success와 User를 반환한다`() = runTest {
        // given
        val token = "naver_token_xyz"
        val fakeUser = User(
            uid = "uid_naver",
            email = "naver@test.com",
            displayName = "네이버유저",
            photoUrl = null,
            loginType = SocialLoginType.NAVER
        )
        coEvery { userRepository.signIn(token, SocialLoginType.NAVER) } returns Result.success(fakeUser)

        // when
        val result = useCase(token, SocialLoginType.NAVER)

        // then
        assertTrue(result.isSuccess)
        assertEquals(fakeUser, result.getOrNull())
        coVerify(exactly = 1) { userRepository.signIn(token, SocialLoginType.NAVER) }
    }

    @Test
    fun `KAKAO 로그인 성공 시 Result_success와 User를 반환한다`() = runTest {
        // given
        val token = "kakao_token_def"
        val fakeUser = User(
            uid = "uid_kakao",
            email = "kakao@test.com",
            displayName = "카카오유저",
            photoUrl = null,
            loginType = SocialLoginType.KAKAO
        )
        coEvery { userRepository.signIn(token, SocialLoginType.KAKAO) } returns Result.success(fakeUser)

        // when
        val result = useCase(token, SocialLoginType.KAKAO)

        // then
        assertTrue(result.isSuccess)
        assertEquals(fakeUser, result.getOrNull())
        coVerify(exactly = 1) { userRepository.signIn(token, SocialLoginType.KAKAO) }
    }

    @Test
    fun `로그인 실패 시 Result_failure를 반환한다`() = runTest {
        // given
        val token = "invalid_token"
        val error = AppError.AuthError.InvalidToken
        coEvery { userRepository.signIn(token, SocialLoginType.GOOGLE) } returns Result.failure(error)

        // when
        val result = useCase(token, SocialLoginType.GOOGLE)

        // then
        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
        coVerify(exactly = 1) { userRepository.signIn(token, SocialLoginType.GOOGLE) }
    }
}
