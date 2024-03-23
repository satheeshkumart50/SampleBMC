package com.bmc.security.dao;

import com.bmc.security.model.ApplicationUser;

public interface ApplicationUserDao {
    public ApplicationUser loadUserByUsername(String s);
}
