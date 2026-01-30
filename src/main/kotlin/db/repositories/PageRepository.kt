package lv.ailab.senie.db.repositories

import lv.ailab.senie.db.entities.Page
import org.springframework.data.repository.CrudRepository

interface PageRepository : CrudRepository<Page, Int> {
    fun findAllByBookFullSource(source: String): List<Page>
}