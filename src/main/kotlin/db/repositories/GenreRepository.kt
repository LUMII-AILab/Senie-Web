package lv.ailab.senie.db.repositories

import lv.ailab.senie.db.entities.Genre
import org.springframework.data.jpa.repository.NativeQuery
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface GenreRepository : CrudRepository<Genre, Int> {

    @NativeQuery(MAIN_GENRE_SQL)
    fun findMainGenre(@Param("code") source: String): Genre

    @NativeQuery(SUBGENRES_SQL)
    fun findSubgenres(@Param("code") source: String): List<Genre>

    companion object {
        const val MAIN_GENRE_SQL = """
            SELECT g.id, g.name, g.subgenre 
            FROM genres g
            JOIN books_genres bg ON g.id = bg.genre_id 
            WHERE bg.source = :code and g.subgenre = 0
        """
        const val SUBGENRES_SQL = """
            SELECT DISTINCT g.id, g.name, g.subgenre
            FROM genres g
            JOIN books_genres bg ON g.id = bg.genre_id 
            WHERE bg.source = :code and g.subgenre = 1
        """
    }
}