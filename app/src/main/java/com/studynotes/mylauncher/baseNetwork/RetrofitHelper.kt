package com.studynotes.mylauncher.baseNetwork

object RetrofitHelper {

    @JvmStatic
    fun <S> createRetrofitService(serviceClass: Class<S>): S {
        return ApiClient.buildRetrofit().create(serviceClass)
    }
}