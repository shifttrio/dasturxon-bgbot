package org.example.tezkor.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.tezkor.enums.Role;

@Getter
@Setter
public class RegisterRequest {
    private String fullname;
    private String phone;
    private String password;
    private Role role; // OWNER, ADMIN, USER
}
