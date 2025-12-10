package lv.ailab.senie.rest.controllers

import lv.ailab.senie.db.repositories.BookRepository
import lv.ailab.senie.rest.CommonFailures.bookNotFound
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class BibliographyController(
    private val bookRepo: BookRepository,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/biblio/{code}")
    fun bibliography(
        @PathVariable("code") source: String,
        model: Model,
    ): String {

        val currentBook = bookRepo.findByFullSource(source) ?: throw bookNotFound(source)
        val collection = currentBook.collectionCode?.let(bookRepo::findCollection)
        model.addAttribute("pageTitle", collection?.displayTitle ?: currentBook.displayTitle)
        model.addAttribute("collection", collection)
        model.addAttribute("currentBook", currentBook)

        return "biblio";
    }
}