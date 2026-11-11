package org.ihab.analyticsservice.kafka

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import patient.event.PatientEvent

@Service
@EnableKafka
class KafkaConsumer {

    private val logger = LoggerFactory.getLogger(KafkaConsumer::class.java)


    @KafkaListener(topics = ["patients"], groupId = "analytics-service")
    fun consumeEvent(event: ByteArray) {
        try {
            val patientEvent = PatientEvent.parseFrom(event)
            logger.info("Received event: ${patientEvent.name} ${patientEvent.patientId} ${patientEvent.email} ${patientEvent.eventType}")
        } catch (ex: Exception) {
            logger.error("Error deserializing ${ex.message}")
        }
    }
}