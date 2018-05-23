package com.z.reactive.reactive

import com.mongodb.ReadPreference
import com.z.reactive.reactive.DAO.RegisteredTemperatureDAO
import com.z.reactive.reactive.Models.RegisteredTemperature
import com.z.reactive.reactive.Utils.random
import org.bson.Document
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.core.CollectionOptions
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.createCollection
import reactor.core.publisher.Flux
import reactor.core.publisher.toMono

@SpringBootApplication
class ReactiveApplication(val reactiveMongoTemplate: ReactiveMongoTemplate, val registeredTemperatureDAO: RegisteredTemperatureDAO):ApplicationRunner {
    //on boot
    override fun run(args: ApplicationArguments?) {
        this.reloadCappedCollection()
    }

    private fun reloadCappedCollection(){
        reactiveMongoTemplate.dropCollection(RegisteredTemperature::class.java)
                .then(
                        reactiveMongoTemplate.createCollection(RegisteredTemperature::class.java, CollectionOptions.empty().capped().size(9128)) //equivalente a db.runCommand({ convertToCapped: 'registeredTemperature', size: 9128 })
                                .thenMany(
                                        registeredTemperatureDAO.saveAll(Flux.just(
                                                RegisteredTemperature(id = null,device = "arduino",temperature = (0.00..100.00).random()),
                                                RegisteredTemperature(id = null,device = "arduino",temperature = (0.00..100.00).random()),
                                                RegisteredTemperature(id = null,device = "raspberry",temperature = (0.00..100.00).random()),
                                                RegisteredTemperature(id = null,device = "raspberry",temperature = (0.00..100.00).random())
                                        )))
                                .toMono()
                ).subscribe()
    }
}

fun main(args: Array<String>) {
    runApplication<ReactiveApplication>(*args)
}
