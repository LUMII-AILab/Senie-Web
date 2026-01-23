package lv.ailab.senie.db.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class BookAuthor (
    @Id val id: Int,
    val name: String,
    /* This is a property describing author's relation to a book, not author itself. */
    val coverAuthor: Boolean,
)
