package org.example.tezkor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tezkor.dto.ContactRequestDto;
import org.example.tezkor.dto.ContactResponseDto;
import org.example.tezkor.enums.Contact;
import org.example.tezkor.repository.ContactRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    private final ContactRepository contactRepository;

    @Transactional(readOnly = true)
    public ContactResponseDto getContactByPhoneNumber(String phoneNumber) {
        Contact contact = contactRepository.findByPhoneNumber(phoneNumber).orElse(null);

        if (contact == null) {
            return ContactResponseDto.builder()
                    .isNewContact(true)
                    .phoneNumber(phoneNumber)
                    .build();
        }

        return mapToResponseDto(contact, false);
    }

    @Transactional
    public ContactResponseDto saveOrUpdateContact(ContactRequestDto requestDto) {
        Contact contact = contactRepository.findByPhoneNumber(requestDto.getPhoneNumber())
                .orElse(null);

        boolean isNew = (contact == null);

        if (isNew) {
            contact = Contact.builder()
                    .phoneNumber(requestDto.getPhoneNumber())
                    .firstName(requestDto.getFirstName())
                    .lastName(requestDto.getLastName())
                    .chatId(requestDto.getChatId())
                    .isActive(true)
                    .lastLogin(LocalDateTime.now())
                    .build();
            log.info("Creating new contact with phone number: {}", requestDto.getPhoneNumber());
        } else {
            if (requestDto.getFirstName() != null) contact.setFirstName(requestDto.getFirstName());
            if (requestDto.getLastName() != null) contact.setLastName(requestDto.getLastName());
            if (requestDto.getChatId() != null) contact.setChatId(requestDto.getChatId());
            contact.setLastLogin(LocalDateTime.now());
            log.info("Updating existing contact with phone number: {}", requestDto.getPhoneNumber());
        }

        Contact savedContact = contactRepository.save(contact);
        return mapToResponseDto(savedContact, isNew);
    }

    @Transactional
    public ContactResponseDto updatePhoneNumber(String oldPhoneNumber, String newPhoneNumber) {
        Contact contact = contactRepository.findByPhoneNumber(oldPhoneNumber)
                .orElseThrow(() -> new RuntimeException("Contact not found with phone number: " + oldPhoneNumber));

        if (contactRepository.existsByPhoneNumber(newPhoneNumber)) {
            throw new RuntimeException("Phone number already exists: " + newPhoneNumber);
        }

        contact.setPhoneNumber(newPhoneNumber);
        contact.setLastLogin(LocalDateTime.now());
        Contact updatedContact = contactRepository.save(contact);
        log.info("Phone number updated from {} to {}", oldPhoneNumber, newPhoneNumber);
        return mapToResponseDto(updatedContact, false);
    }

    @Transactional
    public void updateLastLogin(String phoneNumber) {
        Contact contact = contactRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Contact not found with phone number: " + phoneNumber));
        contact.setLastLogin(LocalDateTime.now());
        contactRepository.save(contact);
        log.info("Last login updated for phone number: {}", phoneNumber);
    }

    @Transactional(readOnly = true)
    public boolean contactExists(String phoneNumber) {

        return contactRepository.existsByPhoneNumber(phoneNumber);
    }

    private ContactResponseDto mapToResponseDto(Contact contact, boolean isNew) {
        return ContactResponseDto.builder()
                .id(contact.getId())
                .phoneNumber(contact.getPhoneNumber())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .chatId(contact.getChatId())
                .isActive(contact.getIsActive())
                .lastLogin(contact.getLastLogin())
                .createdAt(contact.getCreatedAt())
                .updatedAt(contact.getUpdatedAt())
                .isNewContact(isNew)
                .build();
    }
}
