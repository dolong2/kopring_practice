package practice.crud.kotlin.global.exception.exception

import practice.crud.kotlin.global.exception.ErrorCode

class PasswordNotCorrectException(
    errorCode: ErrorCode
):BasicException(
    errorCode
)