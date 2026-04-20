package com.example.movieinfo.feature.splash.viewmodel

import com.example.domain.model.SocialLoginType
import com.example.domain.model.User
import com.example.domain.usecase.GetCurrentUserUseCase
import com.example.movieinfo.feature.splash.model.SplashEffect
import com.example.movieinfo.feature.splash.model.SplashState
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getCurrentUserUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `로그인된 유저가 있으면 Success 상태와 NavigateMain Effect가 발행된다`() = runTest {
        // given
        val fakeUser = User(
            uid = "uid_123",
            email = "test@test.com",
            displayName = "테스트유저",
            photoUrl = null,
            loginType = SocialLoginType.GOOGLE
        )
        coEvery { getCurrentUserUseCase() } returns fakeUser

        // when
        viewModel = SplashViewModel(getCurrentUserUseCase)
        advanceUntilIdle()

        // then
        val state = viewModel.state.value
        assertTrue(state is SplashState.Success)
        assertEquals(fakeUser, (state as SplashState.Success).user)

        val effect = viewModel.effect.first()
        assertEquals(SplashEffect.NavigateMain, effect)
    }

    @Test
    fun `로그인된 유저가 없으면 Idle 상태와 NavigateLogin Effect가 발행된다`() = runTest {
        // given
        coEvery { getCurrentUserUseCase() } returns null

        // when
        viewModel = SplashViewModel(getCurrentUserUseCase)
        advanceUntilIdle()

        // then
        assertEquals(SplashState.Idle, viewModel.state.value)

        val effect = viewModel.effect.first()
        assertEquals(SplashEffect.NavigateLogin, effect)
    }
}
