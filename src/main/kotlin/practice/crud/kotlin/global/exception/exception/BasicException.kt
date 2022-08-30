package practice.crud.kotlin.global.exception.exception

import practice.crud.kotlin.global.exception.ErrorCode

open class BasicException(
    val errorCode: ErrorCode,
): RuntimeException(errorCode.msg)