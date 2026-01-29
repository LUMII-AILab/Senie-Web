package lv.ailab.senie.db.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "pages")
data class Page(
    @Id val id: Int,
    val source: String,
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