package lv.ailab.senie.db.repositories

import lv.ailab.senie.db.entities.DiagnosticBookSummary
import org.springframework.data.jpa.repository.NativeQuery
import org.springframework.data.repository.CrudRepository

interface DiagnosticRepository : CrudRepository<DiagnosticBookSummary, String> {
    @NativeQuery(BOOK_SUMMARIES)
    fun findAllBookSummaries(): List<DiagnosticBookSummary>

    companion object {
        const val BOOK_SUMMARIES = """
  SELECT `source`
       , COUNT(`id`) AS `total_count`
       , COUNT(`facsimile_filename` IS NULL AND `facsimile_checked_on`) AS `missing_facsimile_count`
       , COUNT(`facsimile_filename`) AS `found_facsimile_count`
       , COUNT(`facsimile_checked_on` IS NULL) AS `needs_lookup_count`
    FROM `pages`
GROUP BY `source`
ORDER BY `missing_facsimile_count` DESC, `source` ASC
        """
    }
}