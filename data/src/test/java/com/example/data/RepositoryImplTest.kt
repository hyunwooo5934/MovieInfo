package com.example.data

import android.content.Context
import com.example.data.datasource.GoogleAuthDataSource
import com.example.data.datasource.KakaoAuthDataSource
import com.example.data.local.UserDataStore
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.model.SocialLoginType
import com.example.snslogin.data.datasource.NaverAuthDataSource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import net.bytebuddy.matcher.ElementMatchers.any
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


}