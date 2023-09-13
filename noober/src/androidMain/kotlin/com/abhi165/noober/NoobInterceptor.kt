package com.abhi165.noober

import com.abhi165.noober.model.APIInfoModel
import okhttp3.Interceptor
import okhttp3.Response

class NoobInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (NoobRepository.OLD_URL.isNotBlank() && NoobRepository.NEW_URL.isNotBlank()) {
            request = request
                .newBuilder()
                .url(
                    request.url.toString()
                        .replace(NoobRepository.OLD_URL, NoobRepository.NEW_URL)
                )
                .build()
        }
        val requestModel = request.toModel(chain.readTimeoutMillis().toLong())

        val response = chain.proceed(request)
        val responseModel = response.toModel(requestModel.date)
        val apiInfoModel =
            APIInfoModel(request = requestModel, response = responseModel, error = "")
        NoobRepository.recordAPI(apiInfoModel)
        NoobHelper.notificationManager.showNotification(requestModel.baseURL)
        return response
    }
}