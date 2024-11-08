//package com.opensource.cloudnest.bootstrap;
//
//import com.opensource.cloudnest.entity.Role;
//import com.opensource.cloudnest.entity.RoleEnum;
//import com.opensource.cloudnest.repository.RoleRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//import java.util.Map;
//import java.util.Optional;
//
//@Component
//public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
//    private final RoleRepository roleRepository;
//
//
//    @Autowired
//    public RoleSeeder(RoleRepository roleRepository) {
//        this.roleRepository = roleRepository;
//    }
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//        this.loadRoles();
//    }
//
//    private void loadRoles() {
//        RoleEnum[] roleNames = new RoleEnum[] { RoleEnum.ROLE_USER, RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_SUPER_ADMIN };
//        Map<RoleEnum, String> roleDescriptionMap = Map.of(
//                RoleEnum.ROLE_USER, "Default user role",
//                RoleEnum.ROLE_ADMIN, "Administrator role",
//                RoleEnum.ROLE_SUPER_ADMIN, "Super Administrator role"
//        );
//
//        Arrays.stream(roleNames).forEach((roleName) -> {
//            Optional<Role> optionalRole = roleRepository.findByName(roleName);
//
//            optionalRole.ifPresentOrElse(System.out::println, () -> {
//                Role roleToCreate = new Role();
//
//                roleToCreate.setName(roleName);
//                roleToCreate.setDescription(roleDescriptionMap.get(roleName));
//
//                roleRepository.save(roleToCreate);
//            });
//        });
//    }
//}