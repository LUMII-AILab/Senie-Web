package lv.ailab.senie.db.entities

import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY

@Entity
@Table(name = "books_authors")
data class BookAuthor(
    @Id val id: Int,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "source", referencedColumnName = "fullSource")
    val book: Book,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "authorId")
    val author: Author,

    /* This is a property describing author's relation to a book, not author itself. */
    @Column("cover_author") // TODO: Rename column to match
    val isCoverAuthor: Boolean,
)
