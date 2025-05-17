package chungnt.own.vn.e_commerce.auth_service.entity;

public enum Role {
    USER,
    ADMIN,
    MODERATOR;

    public String getAuthority() {
        return "ROLE_" + name();
    }
}
