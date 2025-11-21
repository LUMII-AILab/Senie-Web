package lv.ailab.senie.rest.controllers

import lv.ailab.senie.db.repositories.BookRepository
import lv.ailab.senie.db.repositories.ContentRepository
import lv.ailab.senie.rest.CommonFailures.bookNotFound
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.view.RedirectView
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@Controller class GotoController(
    private val bookRepo: BookRepository,
    private val contentRepo: ContentRepository,
) {
    @GetMapping("/goto")
    fun goto(
        @RequestParam("source") source: String?,
        @RequestParam("address") address: String?,
        @RequestParam("page") inputPage: String?,
    ): RedirectView {
        val (book, page, line) = when {
            address != null -> {
                val line = contentRepo.findByAddress(address)
                    ?: error("Sistēmā nav rindiņas ar adresi [$address].")
                val book = bookRepo.findByFullSource(line.source)!!
                Triple(book, line.page, line)
            }
            source != null -> {
                val book = bookRepo.findByFullSource(source) ?: throw bookNotFound(source)
                Triple(book, inputPage, null)
            }
            else -> throw Exception("`/goto` adresei nepieciešams `address` vai `source` parametrs.")
        }

        val target = UriComponentsBuilder
            .fromPath(book.urlPath)
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .let { if (line != null) it.fragment(line.address) else it }
            .build()
            .toUriString()
        return RedirectView(target)
    }
}