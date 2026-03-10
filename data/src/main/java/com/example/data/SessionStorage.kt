package com.example.data

import com.example.domain.model.AuthCredential
import com.example.domain.model.AuthUser

interface SessionStorage {
    suspend fun saveUser(user: AuthUser)
    suspend fun saveCredential(credential: AuthCredential)
    suspend fun getUser(): AuthUser?
    suspend fun clear()
}