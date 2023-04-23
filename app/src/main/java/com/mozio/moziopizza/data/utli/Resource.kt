package com.mozio.moziopizza.data.utli

sealed class Resource<out T> {
    data class Success<out T>(val data: T,val isNew: Boolean? = null) : Resource<T>()
    data class Error(val message: String?) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}