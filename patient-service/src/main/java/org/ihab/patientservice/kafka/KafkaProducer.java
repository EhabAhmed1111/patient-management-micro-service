package org.ihab.patientservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ihab.patientservice.model.Patient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.event.PatientEvent;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    @Value("${app.kafka.topic}")
    private String topic;

    public void sendEvent(Patient patient) {
        PatientEvent event = PatientEvent.newBuilder()
                .setEmail(patient.getEmail())
                .setName(patient.getName())
                .setPatientId(patient.getId().toString())
                .setEventType("PATIENT_CREATED")
                .build();

        try {
            kafkaTemplate.send(
                    topic,
                    event.toByteArray()
            );
        }catch (Exception e) {
            log.error("Error sending PatientCreated event: {}", event);
        }
    }
}
