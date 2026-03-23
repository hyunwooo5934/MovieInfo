package com.example.domain.model

sealed class AppError (
    override val message: String,
    override val cause: Throwable?
) : Exception(message,cause) {

    // 인증 관련
    sealed class AuthError(message: String, cause: Throwable? = null)
        : AppError(message, cause) {
        data object UserCancelled : AuthError("사용자가 로그인을 취소했습니다")
        data object TokenExpired  : AuthError("토큰이 만료되었습니다")
        data object InvalidToken  : AuthError("유효하지 않은 토큰입니다")
        data class  Unknown(val detail: String) : AuthError("알 수 없는 인증 오류: $detail")
    }

    // 네트워크 관련
    sealed class NetworkError(message: String, cause: Throwable? = null)
        : AppError(message, cause) {
        data object NoConnection : NetworkError("인터넷 연결을 확인해 주세요")
        data object Timeout      : NetworkError("요청 시간이 초과되었습니다")
        data class  ServerError(val code: Int) : NetworkError("서버 오류 ($code)")
        data class  Unknown(val detail: String) : NetworkError("네트워크 오류: $detail")
    }

    // 서버 검증 (추후 서버 추가 시 사용)
    sealed class ServerError(message: String, cause: Throwable? = null)
        : AppError(message, cause) {

    }


}