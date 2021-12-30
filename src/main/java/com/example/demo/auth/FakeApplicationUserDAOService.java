package com.example.demo.auth;

import com.example.demo.security.ApplicationUserRoles;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("fake")
public class FakeApplicationUserDAOService implements  ApplicationUserDAO{
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FakeApplicationUserDAOService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        //in real database, we can fetch the data here along with user role and permission
        return getApplicationUsers().stream()
                .filter(user -> username.equals(user.getUsername())).findFirst();
    }

    private List<ApplicationUser> getApplicationUsers(){

        List<ApplicationUser> users = Lists.newArrayList(
                new ApplicationUser(ApplicationUserRoles.STUDENT.getGrantedAuth(), passwordEncoder.encode("password"), "Anna",
                        true,true,true,true),
                new ApplicationUser(ApplicationUserRoles.ADMIN.getGrantedAuth(), passwordEncoder.encode("password"), "Linda",
                        true,true,true,true),
                new ApplicationUser(ApplicationUserRoles.ADMINTRAINEE.getGrantedAuth(), passwordEncoder.encode("password"), "Tom",
                        true,true,true,true)
        );
        return  users;
    }
}
