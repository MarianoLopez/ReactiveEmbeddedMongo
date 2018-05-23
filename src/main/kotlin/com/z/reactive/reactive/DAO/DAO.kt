package com.z.reactive.reactive.DAO

import com.z.reactive.reactive.Models.RegisteredTemperature
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import org.springframework.data.mongodb.repository.Tailable



interface RegisteredTemperatureDAO:ReactiveMongoRepository<RegisteredTemperature,String>{

    @Tailable fun findByDevice(device: String): Flux<RegisteredTemperature>
}