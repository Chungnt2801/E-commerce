package chungnt.own.vn.e_commerce.auth_service.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
}
