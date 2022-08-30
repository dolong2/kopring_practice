package practice.crud.kotlin.global.exception.exception

import practice.crud.kotlin.global.exception.ErrorCode

class NotWriterException(
    errorCode: ErrorCode,
): BasicException(
    errorCode
)