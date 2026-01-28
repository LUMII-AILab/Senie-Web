package lv.ailab.senie.db.repositories

import lv.ailab.senie.db.entities.ContentLine
import lv.ailab.senie.db.repositories.GenreRepository.Companion.GENRES_SQL
import org.springframework.data.jpa.repository.NativeQuery
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface ContentLineRepository : CrudRepository<ContentLine, Int> {
    @NativeQuery(BY_ADDRESS_SQL)
    fun findByAddress(@Param("address") address: String): ContentLine?

    @NativeQuery(BY_SOURCE_AND_PAGESO_SQL)
    fun findAllBySourceAndPageSortOrderIn(
        @Param("source")source: String,
        @Param("pso")pageOrders: Iterable<Int>): List<ContentLine>

    companion object {
        const val BY_ADDRESS_SQL = """
            SELECT c.id, c.source, c.address, c.line_sort_order, c.data_html, c.data_plain,
                   p.name AS page, p.sort_order AS page_sort_order
            FROM content_lines c
            JOIN pages p ON c.page_id = p.id 
            WHERE c.address = :address 
        """

        const val BY_SOURCE_AND_PAGESO_SQL = """
            SELECT c.id, c.address, c.line_sort_order, c. data_html, c.data_plain,
                   p.name AS page, p.sort_order AS page_sort_order, p.source 
            FROM content_lines c
            JOIN pages p ON c.page_id = p.id 
            WHERE p.source = :source AND p.sort_order IN ( :pso )
        """

    }
}