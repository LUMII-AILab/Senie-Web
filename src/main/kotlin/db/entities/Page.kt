package lv.ailab.senie.db.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "pages")
data class Page(
    @Id val id: Int,
    val name: String?,
    val order: Int,
) {

    val linkName: String?
        get() = if (order == 0 && name == null) "_" else name

    val displayName: String?
        get() = if (linkName == "_") "Titullapa" else name
}