package lv.ailab.senie.db.entities

import jakarta.persistence.*

@Entity
@Table(name = "books_authors")
data class BookAuthor(
    @Id val id: Int,
    val source: String,
    /* This is a property describing author's relation to a book, not author itself. */
    @Column("cover_author") // TODO: Rename column to match
    val isCoverAuthor: Boolean,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId")
    val author: Author,
)
