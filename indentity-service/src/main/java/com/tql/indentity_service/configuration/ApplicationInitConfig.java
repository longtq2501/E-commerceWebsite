package com.tql.indentity_service.configuration;


import com.tql.indentity_service.entity.User;
import com.tql.indentity_service.entity.Role;
import com.tql.indentity_service.enums.ErrorCode;
import com.tql.indentity_service.exception.AppException;
import com.tql.indentity_service.repository.RoleRepository;
import com.tql.indentity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
//    @ConditionalOnProperty(prefix = "spring",
//            value = "datasource.driverClassname",
//            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {

        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                HashSet<Role> roles = new HashSet<>();
                roles.add(roleRepository.findById("ADMIN").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .build();
                user.setRoles(roles);
                userRepository.save(user);
                log.info("Admin account has password is: admin, please check it out!");
            }
        };

    }
}











