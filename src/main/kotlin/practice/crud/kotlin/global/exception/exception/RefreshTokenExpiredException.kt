package practice.crud.kotlin.global.exception.exception

import practice.crud.kotlin.global.exception.ErrorCode

class RefreshTokenExpiredException(
    errorCode: ErrorCode
):BasicException(
    errorCode
)