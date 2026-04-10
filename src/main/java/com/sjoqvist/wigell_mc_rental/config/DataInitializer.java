package com.sjoqvist.wigell_mc_rental.config;

import com.sjoqvist.wigell_mc_rental.entity.*;
import com.sjoqvist.wigell_mc_rental.repository.AddressRepo;
import com.sjoqvist.wigell_mc_rental.repository.AppUserRepo;
import com.sjoqvist.wigell_mc_rental.repository.BikeRepo;
import com.sjoqvist.wigell_mc_rental.repository.CustomerRepo;
import com.sjoqvist.wigell_mc_rental.security.Role;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initData(
            AppUserRepo appUserRepo,
            CustomerRepo customerRepo,
            AddressRepo addressRepo,
            BikeRepo bikeRepo,
            PasswordEncoder passwordEncoder) {
        return args -> {
            if (appUserRepo.count() > 0) {
                return;
            }

            Address address1 = new Address("Kanalgatan 1", "Karlstad", "66450");
            Address address2 = new Address("Trägatan 2", "Stockholm", "77888");
            Address address3 = new Address("Golvgatan 23", "Göteborg", "44332");

            var addresses = addressRepo.saveAll(List.of(address1, address2, address3));

            Customer customerAdmin = new Customer("Tomas", addresses.getFirst());
            Customer customerUser1 = new Customer("Peter", addresses.get(1));
            Customer customerUser2 = new Customer("Sara", addresses.get(1));
            Customer customerUser3 = new Customer("Bosse", addresses.get(2));
            Customer customerUser4 = new Customer("Jörgen", addresses.get(2));

            var customers =
                    customerRepo.saveAll(
                            List.of(
                                    customerAdmin,
                                    customerUser1,
                                    customerUser2,
                                    customerUser3,
                                    customerUser4));

            AppUser admin1 =
                    new AppUser(
                            "admin",
                            passwordEncoder.encode("admin"),
                            Set.of(Role.ADMIN, Role.USER),
                            customers.getFirst());
            AppUser user1 =
                    new AppUser(
                            "peter",
                            passwordEncoder.encode("peter"),
                            Set.of(Role.USER),
                            customers.get(1));
            AppUser user2 =
                    new AppUser(
                            "sara",
                            passwordEncoder.encode("sara"),
                            Set.of(Role.USER),
                            customers.get(2));
            AppUser user3 =
                    new AppUser(
                            "bosse",
                            passwordEncoder.encode("bosse"),
                            Set.of(Role.USER),
                            customers.get(3));
            AppUser user4 =
                    new AppUser(
                            "jörgen",
                            passwordEncoder.encode("jörgen"),
                            Set.of(Role.USER),
                            customers.get(4));

            appUserRepo.saveAll(List.of(admin1, user1, user2, user3, user4));

            var bike1 = new Bike("A", "A", 25d, BikeStatus.AVAILABLE);
            var bike2 = new Bike("B", "A", 20d, BikeStatus.AVAILABLE);
            var bike3 = new Bike("C", "B", 30d, BikeStatus.AVAILABLE);

            bikeRepo.saveAll(List.of(bike1, bike2, bike3));
        };
    }
}
