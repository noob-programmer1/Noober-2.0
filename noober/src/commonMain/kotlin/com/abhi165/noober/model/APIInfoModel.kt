package com.abhi165.noober.model

import androidx.compose.ui.graphics.Color
import com.abhi165.noober.model.state.APIInfoState
import com.abhi165.noober.model.state.APISummaryState
import com.abhi165.noober.model.state.HeaderBodyState
import com.abhi165.noober.util.DateUtil

internal data class APIInfoModel(
    val request: RequestModel,
    val response: ResponseModel,
    val error: String?
) {


    val statusCodeColor: Color
        get() =  if (response.statusCode in 200..299) Color(0,100,0) else Color(139,0,0)
}

internal fun APIInfoModel.convertToState(): APIInfoState {
    val summary = APISummaryState(
        cURL = request.curl,
        statusCode = response.statusCode.toString(),
        statusCodeColor = if (response.statusCode in 200..299) Color(0,100,0) else Color(139,0,0),
        timeout = request.timeout.toString(),
        timeInterval = response.timeInterval,
        responseTime = DateUtil.formatTime(response.date),
        requestTime = DateUtil.formatTime(request.date),
        url = request.baseURL,
        method = request.method,
        error = error,
        cache = request.cachePolicy
    )

    val responseState = HeaderBodyState(
        headers = response.header,
        body = response.body
    )

    val requestState = HeaderBodyState(
        headers = request.header,
        body = request.body
    )
    return APIInfoState(
        summary = summary,
        requestState = requestState,
        responseState = responseState
    )
}
