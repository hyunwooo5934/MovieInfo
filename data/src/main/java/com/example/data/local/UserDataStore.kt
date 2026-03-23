package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.model.SocialLoginType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


// Context 확장 프로퍼티로 DataStore 인스턴스 생성 (앱 전체에서 하나만 존재)
private val Context.userDataStore: DataStore<Preferences>
        by preferencesDataStore(name = "user_prefs")

@Singleton
class UserDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // ── 키 정의 ─────────────────────────────────────────────────────
    private object Keys {
        val LOGIN_TYPE = stringPreferencesKey("login_type")
    }

    // ── 읽기 ────────────────────────────────────────────────────────

    /** 저장된 loginType Flow로 관찰 */
    val loginTypeFlow: Flow<SocialLoginType?> = context.userDataStore.data
        .catch { e ->
            // IOException은 빈 preferences 방출 (DataStore 손상 시 대비)
            if (e is IOException) emit(emptyPreferences())
            else throw e
        }
        .map { prefs ->
            prefs[Keys.LOGIN_TYPE]?.let { typeName ->
                runCatching { SocialLoginType.valueOf(typeName) }.getOrNull()
            }
        }

    /** 저장된 loginType 단건 조회 (suspend) */
    suspend fun getLoginType(): SocialLoginType? = loginTypeFlow.first()

    // ── 쓰기 ────────────────────────────────────────────────────────

    /** 로그인 성공 시 loginType 저장 */
    suspend fun saveLoginType(loginType: SocialLoginType) {
        context.userDataStore.edit { prefs ->
            prefs[Keys.LOGIN_TYPE] = loginType.name
        }
    }

    /** 로그아웃 시 전체 초기화 */
    suspend fun clear() {
        context.userDataStore.edit { prefs ->
            prefs.clear()
        }
    }
}