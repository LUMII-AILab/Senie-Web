package lv.ailab.senie.db.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "content_lines")
data class ContentLine(
    @Id val id: Int,
    val source: String,
    val address: String?,
    val page: String?,
    val pageSortOrder: Int,
    val lineSortOrder: Int,
    val dataHtml: String,
    val dataPlain: String,
)
