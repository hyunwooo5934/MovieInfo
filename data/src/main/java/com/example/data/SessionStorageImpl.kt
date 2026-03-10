package com.example.data

import com.example.domain.model.AuthCredential
import com.example.domain.model.AuthUser
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionStorageImpl @Inject constructor() : SessionStorage {
    private val mutex = Mutex()

    private var savedUser: AuthUser? = null
    private var savedCredential: AuthCredential? = null

    override suspend fun saveUser(user: AuthUser) {
        mutex.withLock { savedUser = user }
    }

    override suspend fun saveCredential(credential: AuthCredential) {
        mutex.withLock { savedCredential = credential }
    }

    override suspend fun getUser(): AuthUser? {
        return mutex.withLock { savedUser }
    }

    override suspend fun clear() {
        mutex.withLock {
            savedUser = null
            savedCredential = null
        }
    }
}