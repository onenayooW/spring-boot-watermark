package com.watermark

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WatermarkApplication

fun main(args: Array<String>) {
	runApplication<WatermarkApplication>(*args)
}
