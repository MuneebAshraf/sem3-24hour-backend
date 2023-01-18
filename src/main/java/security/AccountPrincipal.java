package security;

import entities.User;

import java.security.Principal;

public class AccountPrincipal implements Principal {

    private String username;
    private Permission permission;

    /* Create a AccountPrincipal, given the Entity class User*/
    public AccountPrincipal(User user, Permission permission) {
        this.username = user.getUsername();
        this.permission = permission;
    }

    public AccountPrincipal(String username, Permission permission) {
        super();
        this.username = username;
        this.permission = permission;

    }

    @Override
    public String getName() {
        return username;
    }

    public boolean hasPermission(Permission permission) {
        boolean isAllowed = permission == this.permission;
        if (permission == Permission.ADMIN && this.permission == Permission.ADMIN) isAllowed = true;

        return isAllowed;
    }
}
