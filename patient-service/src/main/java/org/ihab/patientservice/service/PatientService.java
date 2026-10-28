package org.ihab.patientservice.service;

import lombok.RequiredArgsConstructor;
import org.ihab.patientservice.dto.PatientRequestDTO;
import org.ihab.patientservice.dto.PatientResponseDTO;
import org.ihab.patientservice.exception.EmailAlreadyExistException;
import org.ihab.patientservice.exception.PatientNotFoundException;
import org.ihab.patientservice.grpc.BillingServiceGrpcClient;
import org.ihab.patientservice.mapper.PatientMapper;
import org.ihab.patientservice.model.Patient;
import org.ihab.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient  billingServiceGrpcClient;


    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {

        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistException("A patient with this email already exist " + patientRequestDTO.getEmail());
        }

        Patient newPatient = patientRepository.save(PatientMapper.toPatient(patientRequestDTO));

        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(), newPatient.getEmail());

        return PatientMapper.toDTO(newPatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        isEmailExist(patientRequestDTO.getEmail(), id);

        Patient patient = getPatient(id);

        patient = patientRepository.save(updatePatient(patientRequestDTO, patient));
        return PatientMapper.toDTO(patient);


    }

    private Patient getPatient(UUID id) {
       return patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient with id " + id + " not found")
        );
    }

    private Patient updatePatient(PatientRequestDTO newPatient, Patient oldPatient) {
        oldPatient.setName(newPatient.getName());
        oldPatient.setEmail(newPatient.getEmail());
        oldPatient.setAddress(newPatient.getAddress());
        oldPatient.setDateOfBirth(LocalDate.parse(newPatient.getDateOfBirth()));

        return oldPatient;
    }

    private void isEmailExist(String email, UUID id) {
        if (patientRepository.existsByEmailAndIdNot(email, id)) {
            throw new EmailAlreadyExistException("A patient with this email already exist " + email);
        }

    }

}
