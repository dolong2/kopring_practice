package practice.crud.kotlin.global.exception.exception

import practice.crud.kotlin.global.exception.ErrorCode

class NotValidTokenExpiredException(
    errorCode: ErrorCode
):BasicException(
    errorCode
)