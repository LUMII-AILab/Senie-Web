package lv.ailab.senie.db.entities

import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY

@Entity
@Table(name = "content_lines")
data class ContentLine(
    @Id val id: Int,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "page_id")
    val page: Page,

    val address: String?,
    val lineSortOrder: Int,
    val dataHtml: String,
    val dataPlain: String,
)
