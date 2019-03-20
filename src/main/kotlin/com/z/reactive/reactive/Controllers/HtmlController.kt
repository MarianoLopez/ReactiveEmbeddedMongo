package com.z.reactive.reactive.Controllers

import com.z.reactive.reactive.DAO.RegisteredTemperatureDAO
import com.z.reactive.reactive.Models.RegisteredTemperature
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Flux
import java.time.Duration

@Controller
@RequestMapping("/")
class HtmlController(private val temperatureDAO: RegisteredTemperatureDAO) {

    @GetMapping("/") fun thyMyLeaf(model: Model, @RequestParam device:String?,@RequestParam buffer:Int?): String {
        val stream:Flux<RegisteredTemperature> = temperatureDAO.findByDevice(device ?: "arduino")
        val dataStreamBufferSizeElements = buffer ?: 2
        model.addAttribute("temperatures",ReactiveDataDriverContextVariable(stream,dataStreamBufferSizeElements))
        return "index"
    }

    @GetMapping("/delay") fun thyMyLeafWithDelay(model: Model, @RequestParam device:String?, @RequestParam delay:Long?,@RequestParam buffer:Int?): String {
        val stream = temperatureDAO
                .findByDevice(device ?: "arduino")
                .delayElements(Duration.ofSeconds(delay ?: 2))
        val dataStreamBufferSizeElements = buffer ?: 2
        model.addAttribute("temperatures",ReactiveDataDriverContextVariable(stream,dataStreamBufferSizeElements))
        return "index"
    }

    @GetMapping("/noTail") fun thyMyLeafNoTail(model: Model, @RequestParam device:String?, @RequestParam buffer:Int?,@RequestParam delay:Long?): String {
        val stream = temperatureDAO
                .findAll()
                .filter { it.device == device ?: "arduino" }
                .delayElements(Duration.ofSeconds(delay ?: 0))
        val dataStreamBufferSizeElements = buffer ?: 2
        model.addAttribute("temperatures",ReactiveDataDriverContextVariable(stream,dataStreamBufferSizeElements))
        return "index"
    }



    @GetMapping("/sse") fun index(model: Model,@RequestParam device:String?) = "sse".also { model.addAttribute("device",device?:"arduino") }
}