package lv.ailab.senie.rest.controllers

import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorHandler {
    @ExceptionHandler(Exception::class)
    fun error(ex: Exception, model: Model): String {
        model.addAttribute("message", ex.message)
        return "error"
    }
}