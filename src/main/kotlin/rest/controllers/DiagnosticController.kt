package lv.ailab.senie.rest.controllers

import lv.ailab.senie.db.repositories.BookRepository
import lv.ailab.senie.db.repositories.ContentRepository
import lv.ailab.senie.db.repositories.PageRepository
import lv.ailab.senie.rest.FacsimileClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.view.RedirectView

@Controller
@RequestMapping("/diagnostics")
class DiagnosticController(
    private val bookRepo: BookRepository,
    private val contentRepo: ContentRepository,
    private val facsimileClient: FacsimileClient,
    private val pageRepo: PageRepository,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun index(): RedirectView {
        // If/when other types of diagnostics get added, this should be turned into a page with links.
        // For now we only have one, so just redirect.
        return RedirectView("/diagnostics/facsimiles")
    }

    @GetMapping("/facsimiles")
    fun facsimiles(): String {
        // Do this one book at a time to not load the whole content table in memory at once
        bookRepo.findAll().mapNotNull { book ->
            val allPages = pageRepo.findAllPagesInBook(book.fullSource).filter { it.name != null }
            allPages
                .filter { page -> facsimileClient.findUrlFor(book, page) == null }
                .takeUnless { it.isEmpty() }
                ?.let { missing ->
                    book.fullSource to
                        MissingFacsimileResult(
                            missingAllFiles = missing.size == allPages.size,
                            missingFilesFor = missing.map { it.name!! },
                        )
                }
                .also { result ->
                    if (result != null)
                        logger.debug("Book [${book.fullSource}] has missing pages: ${result.second}.")
                    else
                        logger.debug("Book [${book.fullSource}] has no missing pages.")
                }
        }

        return "diagnostics/facsimilies"
    }

    data class MissingFacsimileResult(
        val missingAllFiles: Boolean,
        val missingFilesFor: List<String>,
    )
}