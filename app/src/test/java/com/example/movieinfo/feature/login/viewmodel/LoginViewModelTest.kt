package com.example.movieinfo.feature.login.viewmodel

import android.app.Activity
import com.example.data.datasource.GoogleAuthDataSource
import com.example.data.datasource.KakaoAuthDataSource
import com.example.domain.model.AppError
import com.example.domain.model.SocialLoginType
import com.example.domain.model.User
import com.example.domain.usecase.SocialLoginUseCase
import com.example.movieinfo.feature.login.model.LoginEffect
import com.example.movieinfo.feature.login.model.LoginIntent
import com.example.movieinfo.feature.login.model.LoginState
import com.example.snslogin.data.datasource.NaverAuthDataSource
import io.mockk.*
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var socialLoginUseCase: SocialLoginUseCase
    private lateinit var googleAuthDataSource: GoogleAuthDataSource
    private lateinit var naverAuthDataSource: NaverAuthDataSource
    private lateinit var kakaoAuthDataSource: KakaoAuthDataSource
    private lateinit var activity: Activity
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        socialLoginUseCase = mockk()
        googleAuthDataSource = mockk()
        naverAuthDataSource = mockk()
        kakaoAuthDataSource = mockk()
        activity = mockk(relaxed = true)

        viewModel = LoginViewModel(
            socialLoginUseCase = socialLoginUseCase,
            googleAuthDataSource = googleAuthDataSource,
            naverAuthDataSource = naverAuthDataSource,
            kakaoAuthDataSource = kakaoAuthDataSource
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── GOOGLE 로그인 ──────────────────────────────────────────────────

    @Test
    fun `구글 로그인 성공 시 Success 상태와 NavigateToMain Effect가 발행된다`() = runTest {
        // given
        val token = "google_token_abc"
        val fakeUser = User(
            uid = "uid_google",
            email = "google@test.com",
            displayName = "구글유저",
            photoUrl = null,
            loginType = SocialLoginType.GOOGLE
        )
        coEvery { googleAuthDataSource.login(activity) } returns Result.success(token)
        coEvery { socialLoginUseCase(token, SocialLoginType.GOOGLE) } returns Result.success(fakeUser)

        // effect는 advanceUntilIdle 이전에 수집을 시작해야 emit을 놓치지 않는다
        val collectedEffects = mutableListOf<LoginEffect>()
        val collectJob = launch { viewModel.effect.collect { collectedEffects.add(it) } }

        // when
        viewModel.onIntent(LoginIntent.SocialLogin(SocialLoginType.GOOGLE, activity))
        advanceUntilIdle()

        // then
        val state = viewModel.state.value
        assertTrue(state is LoginState.Success)
        assertEquals(fakeUser, (state as LoginState.Success).user)
        assertEquals(LoginEffect.NavigateToMain, collectedEffects.first())

        collectJob.cancel()
    }

//    @Test
//    fun `구글 로그인 시 Loading 상태가 먼저 설정된다`() = runTest {
//        // given
//        // DataSource가 완료되지 않도록 CompletableDeferred로 블로킹 → Loading 중간 상태 포착
//        val loginDeferred = CompletableDeferred<Result<String>>()
//        coEvery { loginDeferred.await() }.also {
//            coEvery { googleAuthDataSource.login(activity) } coAnswers { loginDeferred.await() }
//        }
//
//        // when
//        viewModel.onIntent(LoginIntent.SocialLogin(SocialLoginType.GOOGLE, activity))
//        // 코루틴이 loginDeferred.await()에서 suspend된 시점까지 진행
//        testDispatcher.scheduler.runCurrent()
//
//        // then - DataSource가 블로킹 중이므로 아직 Loading 상태여야 한다
//        val state = viewModel.state.value
//        assertTrue(state is LoginState.Loading)
//        assertEquals(SocialLoginType.GOOGLE, (state as LoginState.Loading).type)
//
//        // 테스트 종료를 위해 deferred 완료 처리
//        loginDeferred.cancel()
//    }

    // ── NAVER 로그인 ──────────────────────────────────────────────────

    @Test
    fun `네이버 로그인 성공 시 Success 상태와 NavigateToMain Effect가 발행된다`() = runTest {
        // given
        val token = "naver_token_xyz"
        val fakeUser = User(
            uid = "uid_naver",
            email = "naver@test.com",
            displayName = "네이버유저",
            photoUrl = null,
            loginType = SocialLoginType.NAVER
        )
        coEvery { naverAuthDataSource.login(activity) } returns Result.success(token)
        coEvery { socialLoginUseCase(token, SocialLoginType.NAVER) } returns Result.success(fakeUser)

        val collectedEffects = mutableListOf<LoginEffect>()
        val collectJob = launch { viewModel.effect.collect { collectedEffects.add(it) } }

        // when
        viewModel.onIntent(LoginIntent.SocialLogin(SocialLoginType.NAVER, activity))
        advanceUntilIdle()

        // then
        val state = viewModel.state.value
        assertTrue(state is LoginState.Success)
        assertEquals(fakeUser, (state as LoginState.Success).user)
        assertEquals(LoginEffect.NavigateToMain, collectedEffects.first())

        collectJob.cancel()
    }

    // ── KAKAO 로그인 ──────────────────────────────────────────────────

    @Test
    fun `카카오 로그인 성공 시 Success 상태와 NavigateToMain Effect가 발행된다`() = runTest {
        // given
        val token = "kakao_token_def"
        val fakeUser = User(
            uid = "uid_kakao",
            email = "kakao@test.com",
            displayName = "카카오유저",
            photoUrl = null,
            loginType = SocialLoginType.KAKAO
        )
        coEvery { kakaoAuthDataSource.login(activity) } returns Result.success(token)
        coEvery { socialLoginUseCase(token, SocialLoginType.KAKAO) } returns Result.success(fakeUser)

        val collectedEffects = mutableListOf<LoginEffect>()
        val collectJob = launch { viewModel.effect.collect { collectedEffects.add(it) } }

        // when
        viewModel.onIntent(LoginIntent.SocialLogin(SocialLoginType.KAKAO, activity))
        advanceUntilIdle()

        // then
        val state = viewModel.state.value
        assertTrue(state is LoginState.Success)
        assertEquals(fakeUser, (state as LoginState.Success).user)
        assertEquals(LoginEffect.NavigateToMain, collectedEffects.first())

        collectJob.cancel()
    }

    // ── 실패 케이스 ───────────────────────────────────────────────────

    @Test
    fun `사용자가 로그인을 취소하면 Idle 상태로 돌아간다`() = runTest {
        // given
        coEvery { googleAuthDataSource.login(activity) } returns
            Result.failure(AppError.AuthError.UserCancelled)

        // when
        viewModel.onIntent(LoginIntent.SocialLogin(SocialLoginType.GOOGLE, activity))
        advanceUntilIdle()

        // then
        assertEquals(LoginState.Idle, viewModel.state.value)
    }

    @Test
    fun `네트워크 오류 발생 시 Failure 상태와 ShowError Effect가 발행된다`() = runTest {
        // given
        val error = AppError.NetworkError.NoConnection
        coEvery { googleAuthDataSource.login(activity) } returns Result.failure(error)

        val collectedEffects = mutableListOf<LoginEffect>()
        val collectJob = launch { viewModel.effect.collect { collectedEffects.add(it) } }

        // when
        viewModel.onIntent(LoginIntent.SocialLogin(SocialLoginType.GOOGLE, activity))
        advanceUntilIdle()

        // then
        val state = viewModel.state.value
        assertTrue(state is LoginState.Failure)
        assertEquals(error, (state as LoginState.Failure).error)
        assertTrue(collectedEffects.first() is LoginEffect.ShowError)
        assertEquals(error.message, (collectedEffects.first() as LoginEffect.ShowError).message)

        collectJob.cancel()
    }

    @Test
    fun `토큰 획득 후 UseCase 오류 시 Failure 상태가 된다`() = runTest {
        // given
        val token = "google_token"
        val error = AppError.AuthError.TokenExpired
        coEvery { googleAuthDataSource.login(activity) } returns Result.success(token)
        coEvery { socialLoginUseCase(token, SocialLoginType.GOOGLE) } returns Result.failure(error)

        // when
        viewModel.onIntent(LoginIntent.SocialLogin(SocialLoginType.GOOGLE, activity))
        advanceUntilIdle()

        // then
        val state = viewModel.state.value
        assertTrue(state is LoginState.Failure)
        assertEquals(error, (state as LoginState.Failure).error)
    }
}
