package com.example.framework.base

data class BaseResponse<T>(
    val status: Int,
    val info: String,
    val content: T
)