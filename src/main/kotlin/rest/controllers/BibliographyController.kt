package lv.ailab.senie.rest.controllers

import lv.ailab.senie.db.repositories.AuthorRepository
import lv.ailab.senie.db.repositories.BookRepository
import lv.ailab.senie.db.repositories.GenreRepository
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
    private val genreRepo: GenreRepository,
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
        val displayYear =
            if (currentBook.year1 != currentBook.year2) "${currentBook.year1}–${currentBook.year2}"
            else currentBook.year1
        val collectionDisplayYear = currentBook.collectionCode?.let {
            collection?.year1?.let {
                if (collection.year1 != collection.year2)
                    "${collection.year1}–${collection.year2}"
                else collection.year1
            }
        }
        val mainGenre = genreRepo.findMainGenre(currentBook.collectionCode ?: source)
        val subGenres = genreRepo.findSubgenres(currentBook.collectionCode ?: source)
            .sortedBy { it.name }.stream()
            .map { genre -> genre.name }
            .reduce{a, b -> a + ", " + b}.getOrNull()
        val genres =
            if (subGenres != null) "${mainGenre.name} – $subGenres"
            else mainGenre.name
        val biblioImagePath = imageClient.findUrlFor(currentBook.collectionCode ?: source)
        model.addAttribute("collection", collection)
        model.addAttribute("currentBook", currentBook)
        model.addAttribute("topAuthor", itemTopAuthor.name)
        model.addAttribute("otherAuthors", otherItemAuthors)
        model.addAttribute("collectionAuthor", collectionAuthor?.name)
        model.addAttribute("displayYear", displayYear)
        model.addAttribute("collectionDisplayYear", collectionDisplayYear)
        model.addAttribute("genres", genres)
        model.addAttribute("biblioImage", biblioImagePath)

        return "biblio";
    }
}