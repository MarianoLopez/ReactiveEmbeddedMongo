package com.z.reactive.reactive.Controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller("/")
class HtmlController {
    @GetMapping fun index(model: Model,@RequestParam device:String?) = "index".also { model.addAttribute("device",device?:"arduino") }
}