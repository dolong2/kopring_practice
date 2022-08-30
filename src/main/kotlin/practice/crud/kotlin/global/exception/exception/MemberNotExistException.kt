package practice.crud.kotlin.global.exception.exception

import practice.crud.kotlin.global.exception.ErrorCode

class MemberNotExistException(
    errorCode: ErrorCode
): BasicException(
    errorCode
)