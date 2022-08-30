package practice.crud.kotlin.global.exception.exception

import practice.crud.kotlin.global.exception.ErrorCode

class MemberAlreadyExistException(
    errorCode: ErrorCode
):BasicException(
    errorCode
)