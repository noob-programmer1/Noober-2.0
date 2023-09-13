package com.abhi165.noober.util

import com.abhi165.noober.NoobRepository
import com.abhi165.noober.model.APIInfoModel
import com.abhi165.noober.model.RequestModel
import platform.Foundation.NSCachedURLResponse
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSMutableData
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSURL
import platform.Foundation.NSURLCacheStoragePolicy
import platform.Foundation.NSURLProtocol
import platform.Foundation.NSURLProtocolClientProtocol
import platform.Foundation.NSURLProtocolMeta
import platform.Foundation.NSURLRequest
import platform.Foundation.NSURLResponse
import platform.Foundation.NSURLSession
import platform.Foundation.NSURLSessionConfiguration
import platform.Foundation.NSURLSessionDataDelegateProtocol
import platform.Foundation.NSURLSessionDataTask
import platform.Foundation.NSURLSessionResponseAllow
import platform.Foundation.NSURLSessionResponseDisposition
import platform.Foundation.NSURLSessionTask
import platform.Foundation.appendData

class NoobProtocol : NSURLProtocol, NSURLSessionDataDelegateProtocol {

    @OverrideInit
    constructor (
        request: NSURLRequest,
        cachedResponse: NSCachedURLResponse?,
        client: NSURLProtocolClientProtocol?
    ) : super(request, cachedResponse, client)

    private var urlResponse: NSURLResponse? = null
    private var responseData = NSMutableData()
    private var requestDate = ""

    private val session = NSURLSession.sessionWithConfiguration(
        configuration = NSURLSessionConfiguration.defaultSessionConfiguration,
        this,
        null
    )

    companion object : NSURLProtocolMeta() {

        override fun canInitWithRequest(request: NSURLRequest): Boolean {
            val requestURL = request.URL?.absoluteString ?: ""
            return ((requestURL.startsWith("http") || requestURL.startsWith("https")) &&
                    NSURLProtocol.propertyForKey(Constants.PROTOCOL_KEY, request) == null)
        }

        override fun canonicalRequestForRequest(request: NSURLRequest): NSURLRequest {
            return request
        }
    }

    override fun startLoading() {
        val request = request().mutableCopy() as NSMutableURLRequest
        val oldURL = request.URL?.absoluteString()
        val newURLString = oldURL?.replace(NoobRepository.OLD_URL, NoobRepository.NEW_URL)
        newURLString?.let {
            request.setURL(NSURL(string = newURLString))
        }
        NSURLProtocol.setProperty(true, Constants.PROTOCOL_KEY, request)
        requestDate = DateUtil.now()
        session.dataTaskWithRequest(request).resume()
    }

    override fun stopLoading() {
        session.getTasksWithCompletionHandler { datatask, _, _ ->
            datatask?.forEach {
                (it as NSURLSessionDataTask).cancel()
            }
        }
    }

    override fun URLSession(
        session: NSURLSession,
        dataTask: NSURLSessionDataTask,
        didReceiveData: NSData
    ) {
        responseData.appendData(didReceiveData)
        client?.URLProtocol(this, didLoadData = didReceiveData)
    }

    override fun URLSession(
        session: NSURLSession,
        task: NSURLSessionTask,
        didCompleteWithError: NSError?
    ) {
        val request = task.originalRequest?.toModel(withDate = requestDate) ?: RequestModel()
        val response = responseData.toModel(response = urlResponse, requestDate = requestDate)
        val apiModel = APIInfoModel(
            request = request,
            response = response,
            error = didCompleteWithError?.localizedDescription
        )
        NoobRepository.recordAPI(apiModel)

        didCompleteWithError?.let {
            client?.URLProtocol(this, it)
        } ?: kotlin.run {
            client?.URLProtocolDidFinishLoading(this)
        }
    }

    override fun URLSession(
        session: NSURLSession,
        dataTask: NSURLSessionDataTask,
        didReceiveResponse: NSURLResponse,
        completionHandler: (NSURLSessionResponseDisposition) -> Unit
    ) {
        responseData = NSMutableData()
        urlResponse = didReceiveResponse
        client?.URLProtocol(
            this,
            didReceiveResponse,
            NSURLCacheStoragePolicy.NSURLCacheStorageNotAllowed
        )
        completionHandler(NSURLSessionResponseAllow)
    }
}
