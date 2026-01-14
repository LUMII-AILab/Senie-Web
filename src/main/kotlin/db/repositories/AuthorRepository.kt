package lv.ailab.senie.db.repositories

import lv.ailab.senie.db.entities.Author
import org.springframework.data.jpa.repository.NativeQuery
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface AuthorRepository : CrudRepository<Author, Int> {

    @NativeQuery(TOP_AUTHOR_SQL)
    fun findMainAuthor(@Param("code") source: String): Author

    @NativeQuery(ADDITIONAL_AUTHORS_SQL)
    fun findAdditionalAuthors(@Param("code") source: String): List<Author>

    companion object {
        const val TOP_AUTHOR_SQL = """
            SELECT DISTINCT a.id, a.name 
            FROM authors a
            JOIN books_authors ba ON a.id = ba.author_id 
            WHERE ba.source = :code and ba.top_author = 1
        """
        // TODO: maybe we need here some kind of filtration to avoid repeating primary authors?
        const val ADDITIONAL_AUTHORS_SQL = """
            SELECT DISTINCT a.id, a.name 
            FROM authors a
            JOIN books_authors ba ON a.id = ba.author_id 
            WHERE ba.source = :code and ba.top_author != 1
        """
    }
}