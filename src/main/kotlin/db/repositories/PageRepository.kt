package lv.ailab.senie.db.repositories

import lv.ailab.senie.db.entities.Page
import org.springframework.data.jpa.repository.NativeQuery
import org.springframework.data.repository.CrudRepository

interface PageRepository : CrudRepository<Page, Int> {
    @NativeQuery(BOOK_PAGES_SQL)
    fun findAllPagesInBook(source: String): List<Page>

    companion object {
        const val BOOK_PAGES_SQL = """
            SELECT id, name, sort_order AS `order`
              FROM `pages`
             WHERE `source` = :source
        """
    }
}