package lv.ailab.senie.rest.controllers

import lv.ailab.senie.db.repositories.AuthorRepository
import lv.ailab.senie.db.repositories.BookRepository
import lv.ailab.senie.rest.BiblioImageClient
import lv.ailab.senie.rest.CommonFailures.bookNotFound
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import kotlin.jvm.optionals.getOrNull

@Controller
class BibliographyController(
    private val bookRepo: BookRepository,
    private val authorRepo: AuthorRepository,
    private val imageClient: BiblioImageClient,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/biblio/{code}")
    fun bibliography(
        @PathVariable("code") source: String,
        model: Model,
    ): String {

        val currentBook = bookRepo.findByFullSource(source) ?: throw bookNotFound(source)
        val collection = currentBook.collectionCode?.let(bookRepo::findCollection)
        val itemTopAuthor = authorRepo.findMainAuthor(source)
        val otherItemAuthors = authorRepo.findAdditionalAuthors(source)
            .sortedBy { it.name }.stream()
            .map { author -> author.name }
            .reduce{a, b -> a + ", " + b}.getOrNull()
        val collectionAuthor = currentBook.collectionCode?.let ( authorRepo::findMainAuthor )
        val biblioImagePath =
            if (currentBook.collectionCode == null) imageClient.findUrlFor(currentBook.fullSource)
            else imageClient.findUrlFor(currentBook.collectionCode)
        model.addAttribute("collection", collection)
        model.addAttribute("currentBook", currentBook)
        model.addAttribute("topAuthor", itemTopAuthor.name)
        model.addAttribute("otherAuthors", otherItemAuthors)
        model.addAttribute("collectionAuthor", collectionAuthor?.name)
        model.addAttribute("biblioImage", biblioImagePath)

        return "biblio";
    }
}