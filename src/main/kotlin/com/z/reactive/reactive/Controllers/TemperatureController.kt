package com.z.reactive.reactive.Controllers

import com.z.reactive.reactive.DAO.RegisteredTemperatureDAO
import com.z.reactive.reactive.Models.RegisteredTemperature
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("temperature")
class TemperatureController(val registeredTemperatureDAO: RegisteredTemperatureDAO) {

    @GetMapping fun getAll() = registeredTemperatureDAO.findAll()

    @GetMapping("/{device}",produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    fun getByDevice(@PathVariable("device")device:String) = registeredTemperatureDAO.findByDevice(device)

    @PostMapping fun save(@RequestBody registeredTemperature: RegisteredTemperature) = registeredTemperatureDAO.save(registeredTemperature)
}