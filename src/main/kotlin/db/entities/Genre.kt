package lv.ailab.senie.db.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "genres")
data class Genre (
    @Id val id: Int,
    val name: String,
    val subgenre: Boolean,
    @ManyToMany(mappedBy = "genres")
    val books: List<Book>
)
