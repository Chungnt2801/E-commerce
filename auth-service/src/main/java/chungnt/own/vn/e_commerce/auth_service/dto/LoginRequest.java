package chungnt.own.vn.e_commerce.auth_service.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
