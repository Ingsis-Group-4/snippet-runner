package app.integration.asset

import app.logs.CorrelationIdFilter.Companion.CORRELATION_ID_KEY
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class RemoteAssetStore
    @Autowired
    constructor(
        private val rest: RestTemplate,
        @Value("\${asset.service.url}")
        private val bucketUrl: String,
    ) {
        fun getSnippet(snippetKey: String): String {
            val bucketURL = "$bucketUrl/$snippetKey"
            val headers = HttpHeaders()
            headers.set("X-Correlation-Id", MDC.get(CORRELATION_ID_KEY))
            val entity = HttpEntity<String>(headers)
            val response = this.rest.exchange(bucketURL, HttpMethod.GET, entity, String::class.java)
            if (response.statusCode.isError) {
                throw Exception(
                    "Could not fetch snippet $snippetKey from asset service. Error: ${response.body}",
                )
            }

            return response.body!!
        }
    }
