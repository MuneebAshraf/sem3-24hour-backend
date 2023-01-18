package security;

public enum Permission {
    WALKER(Types.WALKER),
    OWNER(Types.OWNER),
    ADMIN(Types.ADMIN);

    public class Types{
        public static final String WALKER = "WALKER";
        public static final String OWNER = "OWNER";
        public static final String ADMIN = "ADMIN";
    }

    private final String value;

    Permission(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
