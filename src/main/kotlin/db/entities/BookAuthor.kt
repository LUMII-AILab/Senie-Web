package lv.ailab.senie.db.entities

import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY

@Entity
@Table(name = "books_authors")
data class BookAuthor(
    @EmbeddedId val id: CompositeId,

    @MapsId("source")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "source", referencedColumnName = Book.FULL_SOURCE_CODE)
    val book: Book,

    @MapsId("authorId")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_id")
    val author: Author,

    @Column("is_cover_author")
    val isCoverAuthor: Boolean,
    @Column("is_fragment_author")
    val isFragmentAuthor: Boolean,
) {
    class CompositeId {
        var source: String? = null
        var authorId: Int? = null
    }
}