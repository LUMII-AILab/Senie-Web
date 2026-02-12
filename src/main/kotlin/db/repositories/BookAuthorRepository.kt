package lv.ailab.senie.db.repositories

import lv.ailab.senie.db.entities.BookAuthor
import org.springframework.data.repository.CrudRepository

interface BookAuthorRepository : CrudRepository<BookAuthor, Int> {

    fun findAllByBookFullSourceCodeOrderByAuthorName(source: String): List<BookAuthor>

}