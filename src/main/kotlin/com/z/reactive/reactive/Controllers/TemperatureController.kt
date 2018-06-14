package com.z.reactive.reactive.Controllers

import com.z.reactive.reactive.DAO.RegisteredTemperatureDAO
import com.z.reactive.reactive.Models.RegisteredTemperature
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux


@RestController
@RequestMapping("temperature")
class TemperatureController(val registeredTemperatureDAO: RegisteredTemperatureDAO, val reactiveMongoTemplate: ReactiveMongoTemplate) {
    @GetMapping fun getAll() = registeredTemperatureDAO.findAll()

    @GetMapping("/stream/{device}",produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    fun getByDevice(@PathVariable("device")device:String) = registeredTemperatureDAO.findByDevice(device)

    @GetMapping("/arduinoStream", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    fun test(): Flux<RegisteredTemperature> = reactiveMongoTemplate.tail(Query.query(Criteria.where("device").`is`("arduino")),RegisteredTemperature::class.java)

    @PostMapping fun save(@RequestBody registeredTemperature: RegisteredTemperature) = registeredTemperatureDAO.save(registeredTemperature)
}