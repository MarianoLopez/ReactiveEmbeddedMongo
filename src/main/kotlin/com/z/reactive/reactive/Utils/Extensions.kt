package com.z.reactive.reactive.Utils

import java.util.concurrent.ThreadLocalRandom

fun ClosedRange<Double>.random() = ThreadLocalRandom.current().nextDouble(endInclusive-start) +  start