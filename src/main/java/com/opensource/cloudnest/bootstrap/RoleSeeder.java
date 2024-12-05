//package com.opensource.cloudnest.bootstrap;
//
//import com.opensource.cloudnest.entity.Role;
//import com.opensource.cloudnest.repository.RoleRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RoleSeeder implements CommandLineRunner {
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        seedRoles();
//    }
//
//    private void seedRoles() {
//        // Define your roles
//        String[] roles = {"ROLE_ADMIN", "ROLE_USER", "ROLE_SUPER_ADMIN"};
//
//        for (String roleName : roles) {
//            // Check if the role already exists
//            if (roleRepository.findByName(roleName) == null) {
//                // Create and save the new role
//                Role role = new Role();
//                role.setName(roleName);
//                roleRepository.save(role);
//            }
//        }
//    }
//}