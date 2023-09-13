package com.abhi165.noober

import com.abhi165.noober.model.RequestModel
import com.abhi165.noober.model.ResponseModel
import com.abhi165.noober.util.DateUtil
import com.abhi165.noober.util.generateCurlCommand
import com.abhi165.noober.util.prettyPrint
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.util.concurrent.TimeUnit

internal fun Request.toModel(timeout: Long): RequestModel {
    var requestBody = ""
    body?.let { body ->
        val buffer = Buffer().also {
            body.writeTo(it)
        }
        requestBody = buffer.readUtf8()
    }
    val header = headers.associate { it.first to it.second }

    return RequestModel(
        baseURL = url.toString(),
        curl = generateCurlCommand(
            url = url.toString(),
            method = method,
            headers = header,
            body = requestBody
        ),
        header = header,
        body = prettyPrint(requestBody),
        date = DateUtil.now(),
        method = method,
        timeout = TimeUnit.MILLISECONDS.toSeconds(timeout).toFloat(),
        cachePolicy = cacheControl.toString()
    )
}

internal fun Response.toModel(requestDate: String): ResponseModel {
    val responseBodyString = peekBody(Long.MAX_VALUE).string()

    return ResponseModel(
        header = headers.associate { it.first to it.second },
        body = prettyPrint(responseBodyString),
        date = DateUtil.now(),
        statusCode = this.code,
        timeInterval = DateUtil.calculateDiff(DateUtil.now(), requestDate)
    )
}

