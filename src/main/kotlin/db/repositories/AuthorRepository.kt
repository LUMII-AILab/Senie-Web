package lv.ailab.senie.db.repositories

import lv.ailab.senie.db.entities.Author
import org.springframework.data.jpa.repository.NativeQuery
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface AuthorRepository : CrudRepository<Author, Int> {

    @NativeQuery(AUTHORS_SQL)
    fun findAuthors(@Param("code") source: String): List<Author>

    // This will return the same author twice, if they are both top author
    // (written on the cover) and additional author (mentioned in the middle of
    // the text again), but at this point I'm not sure what is the preferred
    // end look, so this stays until further user feedback.
    companion object {
        const val AUTHORS_SQL = """
            SELECT DISTINCT a.id, a.name, ba.top_author 
            FROM authors a
            JOIN books_authors ba ON a.id = ba.author_id 
            WHERE ba.source = :code 
            ORDER BY ba.top_author DESC, a.name ASC
        """
    }
}