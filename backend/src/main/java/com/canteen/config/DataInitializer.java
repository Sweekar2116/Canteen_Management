package com.canteen.config;

import com.canteen.entity.*;
import com.canteen.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final InventoryRepository inventoryRepository;
    private final CouponRepository couponRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
        UserRepository userRepository,
        RoleRepository roleRepository,
        CategoryRepository categoryRepository,
        MenuItemRepository menuItemRepository,
        InventoryRepository inventoryRepository,
        CouponRepository couponRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.menuItemRepository = menuItemRepository;
        this.inventoryRepository = inventoryRepository;
        this.couponRepository = couponRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        logger.info("Initializing / updating default security roles and users...");

        // Ensure Roles exist
        Role adminRole = roleRepository.findByName(Role.RoleName.ADMIN)
            .orElseGet(() -> roleRepository.save(new Role(Role.RoleName.ADMIN)));

        Role staffRole = roleRepository.findByName(Role.RoleName.STAFF)
            .orElseGet(() -> roleRepository.save(new Role(Role.RoleName.STAFF)));

        Role customerRole = roleRepository.findByName(Role.RoleName.CUSTOMER)
            .orElseGet(() -> roleRepository.save(new Role(Role.RoleName.CUSTOMER)));

        // Create or Reset Admin Account: admin@canteen.com / admin123
        User admin = userRepository.findByEmail("admin@canteen.com").orElseGet(() -> {
            User u = new User();
            u.setEmail("admin@canteen.com");
            return u;
        });
        admin.setName("Admin User");
        admin.setPhone("9999999999");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEnabled(true);
        admin.setRoles(Set.of(adminRole, customerRole));
        userRepository.save(admin);

        // Create or Reset Staff Account: staff@canteen.com / staff123
        User staff = userRepository.findByEmail("staff@canteen.com").orElseGet(() -> {
            User u = new User();
            u.setEmail("staff@canteen.com");
            return u;
        });
        staff.setName("Chef Staff");
        staff.setPhone("9888888888");
        staff.setPassword(passwordEncoder.encode("staff123"));
        staff.setEnabled(true);
        staff.setRoles(Set.of(staffRole));
        userRepository.save(staff);

        // Create or Reset Customer Account: rahul@example.com / customer123
        User customer = userRepository.findByEmail("rahul@example.com").orElseGet(() -> {
            User u = new User();
            u.setEmail("rahul@example.com");
            return u;
        });
        customer.setName("Rahul Sharma");
        customer.setPhone("9876543210");
        customer.setPassword(passwordEncoder.encode("customer123"));
        customer.setEnabled(true);
        customer.setRoles(Set.of(customerRole));
        userRepository.save(customer);

        logger.info("Default user credentials initialized successfully.");
    }
}
