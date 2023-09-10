package com.abhi165.noober.util

import com.abhi165.noober.model.RequestModel
import com.abhi165.noober.model.ResponseModel
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.free
import kotlinx.cinterop.nativeHeap
import platform.Foundation.HTTPBodyStream
import platform.Foundation.HTTPMethod
import platform.Foundation.NSHTTPURLResponse
import platform.Foundation.NSInputStream
import platform.Foundation.NSJSONReadingFragmentsAllowed
import platform.Foundation.NSJSONSerialization
import platform.Foundation.NSJSONWritingPrettyPrinted
import platform.Foundation.NSMutableData
import platform.Foundation.NSString
import platform.Foundation.NSURLRequest
import platform.Foundation.NSURLResponse
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.allHTTPHeaderFields
import platform.Foundation.appendBytes
import platform.Foundation.create
import platform.darwin.UInt8Var
import kotlin.to


internal fun NSURLRequest.toModel(withDate: String = DateUtil.now()): RequestModel {
    val url = this.URL?.absoluteString ?: ""
    val headers =  this.allHTTPHeaderFields()?.filter { it.key != null && it.value != null }
        ?.map { it.key.toString() to it.value.toString() }
        ?.toMap() ?: mapOf()
    val body = this.HTTPBodyStream?.toHttpBody() ?: ""
    val method = this.HTTPMethod ?: ""

    return RequestModel(
        baseURL = url,
        curl = toCurlCommand(body),
        header = headers,
        body = body,
        date = withDate,
        method = method,
        timeout = this.timeoutInterval.toFloat(),
        cachePolicy = this.cachePolicy.toString()
    )
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal fun NSMutableData.toModel(response: NSURLResponse?, requestDate: String): ResponseModel {
    val httpResponse = response as? NSHTTPURLResponse
    val headers = httpResponse?.allHeaderFields?.filter { it.key != null && it.value != null }
        ?.map { it.key.toString() to it.value.toString() }
        ?.toMap() ?: mapOf()
    val status = httpResponse?.statusCode ?: 0
    val date = DateUtil.now()
    var responseBody = ""
    try {
        val jsonData = NSJSONSerialization.JSONObjectWithData(this, options = NSJSONReadingFragmentsAllowed, error = null)
        jsonData?.let {
            val jsonString = NSJSONSerialization.dataWithJSONObject(jsonData, options = NSJSONWritingPrettyPrinted, error = null)
            jsonString?.let {
                responseBody = NSString.create(it, NSUTF8StringEncoding).toString()
            }
        }
    }
    finally { }
return ResponseModel(
    header = headers,
    body = responseBody,
    date = date,
    statusCode = status.toInt(),
    timeInterval = DateUtil.calculateDiff(from = date, to = requestDate),
)
}

fun NSURLRequest.toCurlCommand(body: String): String {
    val curlCommand = StringBuilder("curl -i")

    this.HTTPMethod()?.let { method ->
        curlCommand.append(" -X $method")
    }

    this.allHTTPHeaderFields()?.let { headers ->
        for ((key, value) in headers) {
            curlCommand.append(" -H '$key: $value'")
        }
    }

    curlCommand.append(" -d '$body'")


    this.URL()?.let { url ->
        curlCommand.append(" '${url.absoluteString}'")
    }

    return curlCommand.toString()
}


@OptIn(ExperimentalForeignApi::class)
fun NSInputStream.toHttpBody(): String {
    this.let { inputStream ->
        inputStream.open()
        val bufferSize: Int = 16

        val buffer = nativeHeap.allocArray<UInt8Var>(bufferSize)

        val data = NSMutableData()

        while (inputStream.hasBytesAvailable) {
            val readBytes = inputStream.read(buffer, maxLength = bufferSize.toULong())
            if (readBytes <= 0) {
                break
            }
            data.appendBytes(buffer, length = readBytes.toULong())
        }

        nativeHeap.free(buffer)
        inputStream.close()
        return  NSString.create(data = data, encoding = NSUTF8StringEncoding).toString()
    }
}
