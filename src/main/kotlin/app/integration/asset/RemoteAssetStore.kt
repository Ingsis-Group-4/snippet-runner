package app.integration.asset

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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
            val response = this.rest.getForEntity(bucketURL, String::class.java)
            if (response.statusCode.isError) {
                throw Exception(
                    "Could not fetch snippet $snippetKey from asset service. Error: ${response.body}",
                )
            }

            return response.body!!
        }
    }
