package com.z.reactive.reactive.Models

import io.swagger.annotations.ApiModelProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class RegisteredTemperature(
        @ApiModelProperty(required = false)
        @Id val id:String?,
        val device:String,
        val temperature:Double,
        @ApiModelProperty(required = false)
        val date:LocalDateTime = LocalDateTime.now())