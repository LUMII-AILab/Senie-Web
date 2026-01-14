package lv.ailab.senie.rest

import lv.ailab.senie.utils.urlEncode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class BiblioImageClient(
    restClientBuilder: RestClient.Builder,

    @Value($$"${senie.data.base-url}")
    dataUrl: String,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    val baseUrl = "${dataUrl}/biblio"

    val restClient by lazy {
        restClientBuilder
            // 404 are expected, so do nothing (don't throw exceptions)
            .defaultStatusHandler(HttpStatus.NOT_FOUND::equals) { _, _ -> }
            .build()
    }

    /**
     * Finds the bibliography image URL for a [source], trying every known
     * variant. Currently, it is assumed that parts of collections won't have
     * their own individual bibliography images.
     *
     * @return Image URL for the facsimile, or `null` if none found.
     */
    fun findUrlFor(source: String): String? {
        // Page images may have any of these file extensions
        val extensions = listOf("jpg", "png")

        logger.debug("Looking for facsimile image for ${source}...")
        return extensions.firstNotNullOfOrNull { extension ->
            val fileName = "$source.$extension"
            val fileUrl = "$baseUrl/${fileName.urlEncode()}"
            val isFound = restClient
                .head().uri(fileUrl)
                .retrieve()
                .toBodilessEntity().statusCode.is2xxSuccessful
            fileUrl.takeIf { isFound }
                .also {
                    if (isFound) logger.debug("200 : $fileUrl")
                    else logger.debug("404 : $fileUrl")
                }
        }
    }
}
