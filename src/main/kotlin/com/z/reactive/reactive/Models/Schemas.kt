package com.z.reactive.reactive.Models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class RegisteredTemperature(@Id val id:String?, val device:String,val temperature:Double, val date:LocalDateTime = LocalDateTime.now())