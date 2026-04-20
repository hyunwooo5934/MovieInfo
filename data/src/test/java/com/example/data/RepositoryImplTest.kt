package com.example.data

import android.content.Context
import com.example.data.datasource.GoogleAuthDataSource
import com.example.data.datasource.KakaoAuthDataSource
import com.example.data.local.UserDataStore
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.model.SocialLoginType
import com.example.domain.model.User
import com.example.snslogin.data.datasource.NaverAuthDataSource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryImplTest {
    private lateinit var googleDataSource: GoogleAuthDataSource
    private lateinit var naverAuthDataSource: NaverAuthDataSource
    private lateinit var kakaoAuthDataSource: KakaoAuthDataSource
    private lateinit var userDataStore: UserDataStore
    private lateinit var context: Context

    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setUp() {
        googleDataSource = mockk()
        naverAuthDataSource = mockk()
        kakaoAuthDataSource = mockk()
        userDataStore = mockk()
        context = mockk(relaxed = true)

        repository = UserRepositoryImpl(
            googleDataSource = googleDataSource,
            naverAuthDataSource = naverAuthDataSource,
            kakaoAuthDataSource = kakaoAuthDataSource,
            userDataStore = userDataStore,
            context = context
        )
    }


    @Test
    fun `signIn GOOGLE succeeds and saves login data`() = runTest {
        // given
        val token = "abcdefghijklmnopqrstuv"

        val fakeUser = User(
            uid = token.take(20),   // "abcdefghijklmnopqrst"
            email = "",
            displayName = "",
            photoUrl = null,
            loginType = SocialLoginType.GOOGLE
        )

        coEvery { googleDataSource.fetchGoogleProfile(token) } returns fakeUser

        coEvery { userDataStore.saveLoginType(any()) } just Runs
        coEvery { userDataStore.saveLoginInfo(any()) } just Runs

        // when
        val result = repository.signIn(
            token = token,
            loginType = SocialLoginType.GOOGLE
        )

        // then
        assertTrue(result.isSuccess)

        val user = result.getOrNull()
        assertNotNull(user)
        assertEquals("abcdefghijklmnopqrst", user?.uid) // take(20)
        assertEquals("", user?.email)
        assertEquals("", user?.displayName)
        assertEquals(null, user?.photoUrl)
        assertEquals(SocialLoginType.GOOGLE, user?.loginType)

        coVerify(exactly = 1) { userDataStore.saveLoginType(SocialLoginType.GOOGLE) }
        coVerify(exactly = 1) {
            userDataStore.saveLoginInfo(
                match {
                    it.uid == "abcdefghijklmnopqrst" &&
                            it.loginType == SocialLoginType.GOOGLE
                }
            )
        }
        confirmVerified(userDataStore)
    }

    @Test
    fun `signIn NAVER 성공 시 유저 정보를 저장한다`() = runTest {
        // given
        val token = "naver_token_xyz"
        val fakeUser = User(
            uid = "uid_naver",
            email = "naver@test.com",
            displayName = "네이버유저",
            photoUrl = null,
            loginType = SocialLoginType.NAVER
        )
        coEvery { naverAuthDataSource.fetchNaverProfile() } returns fakeUser
        coEvery { userDataStore.saveLoginType(any()) } just Runs
        coEvery { userDataStore.saveLoginInfo(any()) } just Runs

        // when
        val result = repository.signIn(token = token, loginType = SocialLoginType.NAVER)

        // then
        assertTrue(result.isSuccess)
        assertEquals(fakeUser, result.getOrNull())
        coVerify(exactly = 1) { userDataStore.saveLoginType(SocialLoginType.NAVER) }
        coVerify(exactly = 1) { userDataStore.saveLoginInfo(fakeUser) }
    }

    @Test
    fun `signIn KAKAO 성공 시 유저 정보를 저장한다`() = runTest {
        // given
        val token = "kakao_token_def"
        val fakeUser = User(
            uid = "uid_kakao",
            email = "kakao@test.com",
            displayName = "카카오유저",
            photoUrl = null,
            loginType = SocialLoginType.KAKAO
        )
        coEvery { kakaoAuthDataSource.fetchKakaoProfile() } returns fakeUser
        coEvery { userDataStore.saveLoginType(any()) } just Runs
        coEvery { userDataStore.saveLoginInfo(any()) } just Runs

        // when
        val result = repository.signIn(token = token, loginType = SocialLoginType.KAKAO)

        // then
        assertTrue(result.isSuccess)
        assertEquals(fakeUser, result.getOrNull())
        coVerify(exactly = 1) { userDataStore.saveLoginType(SocialLoginType.KAKAO) }
        coVerify(exactly = 1) { userDataStore.saveLoginInfo(fakeUser) }
    }

    @Test
    fun `signIn 예외 발생 시 Result_failure를 반환한다`() = runTest {
        // given
        val token = "bad_token"
        coEvery { googleDataSource.fetchGoogleProfile(token) } throws RuntimeException("네트워크 오류")

        // when
        val result = repository.signIn(token = token, loginType = SocialLoginType.GOOGLE)

        // then
        assertTrue(result.isFailure)
    }

    @Test
    fun `getCurrentUser는 DataStore에서 유저 정보를 반환한다`() = runTest {
        // given
        val fakeUser = User(
            uid = "uid_stored",
            email = "stored@test.com",
            displayName = "저장된유저",
            photoUrl = null,
            loginType = SocialLoginType.GOOGLE
        )
        coEvery { userDataStore.getLoginInfo() } returns fakeUser

        // when
        val result = repository.getCurrentUser()

        // then
        assertEquals(fakeUser, result)
        coVerify(exactly = 1) { userDataStore.getLoginInfo() }
    }

    @Test
    fun `getCurrentUser DataStore에 저장된 유저가 없으면 null을 반환한다`() = runTest {
        // given
        coEvery { userDataStore.getLoginInfo() } returns null

        // when
        val result = repository.getCurrentUser()

        // then
        assertNull(result)
    }

    @Test
    fun `signOut 호출 시 DataStore가 초기화된다`() = runTest {
        // given - 먼저 signIn으로 currentUser를 메모리에 캐시
        val token = "google_token"
        val fakeUser = User(
            uid = "uid_google",
            email = "google@test.com",
            displayName = "구글유저",
            photoUrl = null,
            loginType = SocialLoginType.GOOGLE
        )
        coEvery { googleDataSource.fetchGoogleProfile(token) } returns fakeUser
        coEvery { userDataStore.saveLoginType(any()) } just Runs
        coEvery { userDataStore.saveLoginInfo(any()) } just Runs
        repository.signIn(token = token, loginType = SocialLoginType.GOOGLE)

        coEvery { googleDataSource.logout() } just Runs
        coEvery { userDataStore.clear() } just Runs

        // when
        repository.signOut()

        // then
        coVerify(exactly = 1) { googleDataSource.logout() }
        coVerify(exactly = 1) { userDataStore.clear() }
    }

    @Test
    fun `signOut 로그인 이력 없을 때도 DataStore만 초기화된다`() = runTest {
        // given - signIn 없이 바로 signOut
        coEvery { userDataStore.clear() } just Runs

        // when
        repository.signOut()

        // then - currentUser가 null이므로 DataSource logout은 호출되지 않음
        coVerify(exactly = 0) { googleDataSource.logout() }
        coVerify(exactly = 1) { userDataStore.clear() }
    }
}