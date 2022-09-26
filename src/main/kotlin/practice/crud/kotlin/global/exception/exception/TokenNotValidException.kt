package practice.crud.kotlin.global.exception.exception

import practice.crud.kotlin.global.exception.ErrorCode

class TokenNotValidException(
    errorCode: ErrorCode,
): BasicException(
    errorCode = errorCode
)