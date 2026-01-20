package lv.ailab.senie.db.repositories

import lv.ailab.senie.db.entities.Genre
import org.springframework.data.jpa.repository.NativeQuery
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface GenreRepository : CrudRepository<Genre, Int> {


    @NativeQuery(GENRES_SQL)
    fun findGenres(@Param("code") source: String): List<Genre>

    companion object {
        const val GENRES_SQL = """
            SELECT DISTINCT g.id, g.name, g.subgenre
            FROM genres g
            JOIN books_genres bg ON g.id = bg.genre_id 
            WHERE bg.source = :code 
            ORDER BY g.subgenre, g.name
        """
    }
}