package practice.crud.kotlin.global.exception.exception

import practice.crud.kotlin.global.exception.ErrorCode

class AccessTokenExpiredException(
    errorCode: ErrorCode
): BasicException(
    errorCode
)