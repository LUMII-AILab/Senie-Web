package lv.ailab.senie.db.entities

import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import java.time.Instant

@Entity
@Table(name = "pages")
data class Page(
    @Id val id: Int,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "source", referencedColumnName = "fullSourceCode")
    val book: Book,

    val name: String?,
    val sortOrder: Int,
    val facsimileFilename: String?,
    val facsimileCheckedOn: Instant?,
) {

    val linkName: String?
        get() = if (sortOrder == 0 && name == null) "_" else name

    val displayName: String?
        get() = if (linkName == "_") "Titullapa" else name
}