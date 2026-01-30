package lv.ailab.senie.db.entities

import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY

@Entity
@Table(name = "books_authors")
data class BookAuthor(
    @EmbeddedId val id: CompositeId,

    @MapsId("source")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "source", referencedColumnName = "fullSource")
    val book: Book,

    @MapsId("authorId")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_id")
    val author: Author,

    /* This is a property describing author's relation to a book, not author itself. */
    @Column("cover_author") // TODO: Rename column to match
    val isCoverAuthor: Boolean,
) {
    class CompositeId {
        var source: String? = null
        var authorId: Int? = null
    }
}