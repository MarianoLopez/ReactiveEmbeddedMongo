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
@CrossOrigin(allowedHeaders = ["*"],methods = [RequestMethod.GET,RequestMethod.POST],origins = ["*"])
class TemperatureController(val registeredTemperatureDAO: RegisteredTemperatureDAO, val reactiveMongoTemplate: ReactiveMongoTemplate) {
    @GetMapping fun getAll(): Flux<RegisteredTemperature> = registeredTemperatureDAO.findAll()

    @GetMapping("/stream/{device}",produces = [MediaType.TEXT_EVENT_STREAM_VALUE])//TEXT_EVENT_STREAM_VALUE = SSE default
    fun getStreamByDevice(@PathVariable("device")device:String): Flux<RegisteredTemperature> = registeredTemperatureDAO.findByDevice(device)

    @GetMapping("/jsonStream/{device}",produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])//SSE gets http 406
    fun getJsonStreamByDevice(@PathVariable("device")device:String): Flux<RegisteredTemperature> = registeredTemperatureDAO.findByDevice(device)

    @GetMapping("/arduinoJsonStream", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    fun testTailQuery(): Flux<RegisteredTemperature> = reactiveMongoTemplate.tail(Query.query(Criteria.where("device").`is`("arduino")),RegisteredTemperature::class.java)

    @PostMapping fun save(@RequestBody registeredTemperature: RegisteredTemperature) = registeredTemperatureDAO.save(registeredTemperature)
}