package com.example.data.auth.google

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.domain.model.AuthCredential
import com.example.domain.model.AuthError
import com.example.domain.model.AuthResult
import com.example.domain.model.AuthUser
import com.example.domain.model.SocialProvider
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Named

class GoogleAuthClient @Inject constructor(
    @ApplicationContext private val context: Context,
    @Named("google_web_client_id") private val webClientId: String
) {
    private val credentialManager = CredentialManager.create(context)

    suspend fun signIn(): AuthResult {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential
            if (
                credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)

                AuthResult.Success(
                    user = AuthUser(
                        provider = SocialProvider.GOOGLE,
                        userId = googleCredential.id,
                        email = null, // 필요 시 서버/프로필 확장
                        name = googleCredential.displayName,
                        profileImageUrl = googleCredential.profilePictureUri?.toString()
                    ),
                    credential = AuthCredential(
                        provider = SocialProvider.GOOGLE,
                        idToken = googleCredential.idToken
                    )
                )
            } else {
                AuthResult.Failure(AuthError.InvalidCredential)
            }
        } catch (e: GoogleIdTokenParsingException) {
            AuthResult.Failure(AuthError.InvalidCredential)
        } catch (e: Exception) {
            AuthResult.Failure(AuthError.Unknown(e.message))
        }
    }

    suspend fun signOut() {
        runCatching {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        }
    }
}