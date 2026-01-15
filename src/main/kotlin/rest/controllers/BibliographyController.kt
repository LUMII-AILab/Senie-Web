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
        val itemAuthors = authorRepo.findAuthors(source).partition { author -> author.topAuthor }
        val itemTopAuthors = itemAuthors.first.joinToString { author -> author.name }
        val otherItemAuthors = itemAuthors.second.joinToString { author -> author.name }
        val collectionTopAuthors = currentBook.collectionCode?.let{
            authorRepo.findAuthors(currentBook.collectionCode)
                .filter { author -> author.topAuthor }
                .joinToString { author -> author.name }
        }
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
        val genres = genreRepo.findGenres(currentBook.collectionCode ?: source)
        val mainGenre = genres.firstOrNull()?.name // Built-in assumption that there is only one `subgenre = FALSE` item for each book.
        val subGenres = genres.subList(1, genres.size).joinToString { genre -> genre.name }
        val displayGenres =
            if (genres.size > 1) "$mainGenre – $subGenres"
            else mainGenre
        val biblioImagePath = imageClient.findUrlFor(currentBook.collectionCode ?: source)
        model.addAttribute("collection", collection)
        model.addAttribute("currentBook", currentBook)
        model.addAttribute("topAuthors", itemTopAuthors)
        model.addAttribute("otherAuthors", otherItemAuthors)
        model.addAttribute("collectionTopAuthors", collectionTopAuthors)
        model.addAttribute("displayYear", displayYear)
        model.addAttribute("collectionDisplayYear", collectionDisplayYear)
        model.addAttribute("genres", displayGenres)
        model.addAttribute("biblioImage", biblioImagePath)

        return "biblio";
    }
}