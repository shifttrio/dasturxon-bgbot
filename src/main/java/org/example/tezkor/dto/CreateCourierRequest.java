package org.example.tezkor.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.tezkor.enums.TransportType;

@Getter
@Setter
public class CreateCourierRequest {

    private String fullname;  // ✅ Bu bor
    private String phone;
    private String password;
    private TransportType transportType;
}