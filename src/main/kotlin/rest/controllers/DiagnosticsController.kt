package lv.ailab.senie.rest.controllers

import lv.ailab.senie.db.repositories.DiagnosticRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("diagnostics")
class DiagnosticsController(
    private val repo: DiagnosticRepository,
) {
    @GetMapping
    fun index(model: Model): String {
        val summaries = repo.findAllBookSummaries()
        model.addAttribute("summaries", summaries)
        return "diagnostics"
    }
}