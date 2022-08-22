package antifraud.model;

public enum UserRole {

    ADMINISTRATOR("ROLE_ADMINISTRATOR"),
    MERCHANT("ROLE_MERCHANT"),
    SUPPORT("ROLE_SUPPORT");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return this.role;
    }
}