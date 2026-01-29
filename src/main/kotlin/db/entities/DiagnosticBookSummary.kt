package lv.ailab.senie.db.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.PostLoad
import jakarta.persistence.Transient

@Entity
data class DiagnosticBookSummary(
    @Id val source: String,
    val totalCount: Int,
    val missingFacsimileCount: Int,
    val foundFacsimileCount: Int,
    val needsLookupCount: Int,
) {

    @Transient
    lateinit var foundDisplay: String

    @Transient
    lateinit var missingDisplay: String

    @PostLoad
    fun init() {
        val prefix = if (needsLookupCount > 0) "≥" else ""
        foundDisplay = "${prefix}${missingFacsimileCount}"
        missingDisplay = "${prefix}${foundFacsimileCount}"
    }
}