package com.sjoqvist.wigell_mc_rental.config;

import com.sjoqvist.wigell_mc_rental.entity.*;
import com.sjoqvist.wigell_mc_rental.repository.*;
import com.sjoqvist.wigell_mc_rental.security.AppUser;
import com.sjoqvist.wigell_mc_rental.security.Role;
import com.sjoqvist.wigell_mc_rental.service.BookingPriceCalculator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
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
            BookingRepo bookingRepo,
            PasswordEncoder passwordEncoder,
            BookingPriceCalculator bookingPriceCalculator) {
        return args -> {
            if (appUserRepo.count() > 0) {
                return;
            }

            // ==================================================================
            // Addresses
            // ==================================================================

            Address address1 = new Address("Kanalgatan 1", "Karlstad", "66450");
            Address address2 = new Address("Trägatan 2", "Stockholm", "77888");
            Address address3 = new Address("Golvgatan 23", "Göteborg", "44332");

            var addresses = addressRepo.saveAll(List.of(address1, address2, address3));

            // ==================================================================
            // Customers
            // ==================================================================

            Customer customerAdmin = new Customer("Tomas", List.of(addresses.getFirst()));
            Customer customerUser1 = new Customer("Peter", List.of(addresses.get(1)));
            Customer customerUser2 = new Customer("Sara", List.of(addresses.get(1)));
            Customer customerUser3 = new Customer("Bosse", List.of(addresses.get(2)));
            Customer customerUser4 = new Customer("Jörgen", List.of(addresses.get(2)));

            var customers =
                    customerRepo.saveAll(
                            List.of(
                                    customerAdmin,
                                    customerUser1,
                                    customerUser2,
                                    customerUser3,
                                    customerUser4));

            // ==================================================================
            // Users
            // ==================================================================

            AppUser admin1 =
                    new AppUser(
                            "admin",
                            passwordEncoder.encode("admin"),
                            Set.of(Role.ADMIN),
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

            // ==================================================================
            // Bikes
            // ==================================================================

            Bike bike1 = new Bike("A", "A", 20d, BikeStatus.UNAVAILABLE);
            Bike bike2 = new Bike("B", "A", 25d, BikeStatus.AVAILABLE);
            Bike bike3 = new Bike("C", "B", 30d, BikeStatus.AVAILABLE);
            Bike bike4 = new Bike("D", "C", 40d, BikeStatus.AVAILABLE);
            Bike bike5 = new Bike("E", "C", 50d, BikeStatus.AVAILABLE);

            var bikes = bikeRepo.saveAll(List.of(bike1, bike2, bike3, bike4, bike5));

            // ==================================================================
            // Bookings
            // ==================================================================

            var bookingBike1 = bikes.getFirst();
            var dates1 = new LocalDate[] {LocalDate.now(), LocalDate.now()};
            var booking1 =
                    new Booking(
                            bookingBike1,
                            customers.get(1),
                            dates1[0],
                            dates1[1],
                            bookingPriceCalculator.calculateTotalPrice(
                                    dates1[0], dates1[1], bookingBike1),
                            BookingStatus.ACTIVE);

            var bookingBike2 = bikes.get(1);
            var dates2 =
                    new LocalDate[] {LocalDate.now().minusDays(10), LocalDate.now().minusDays(5)};
            var booking2 =
                    new Booking(
                            bookingBike2,
                            customers.get(2),
                            dates2[0],
                            dates2[1],
                            bookingPriceCalculator.calculateTotalPrice(
                                    dates2[0], dates2[1], bookingBike2),
                            BookingStatus.COMPLETED);

            var bookingBike3 = bikes.get(2);
            var dates3 =
                    new LocalDate[] {LocalDate.now().plusDays(10), LocalDate.now().plusDays(15)};
            var booking3 =
                    new Booking(
                            bookingBike3,
                            customers.get(2),
                            dates3[0],
                            dates3[1],
                            bookingPriceCalculator.calculateTotalPrice(
                                    dates3[0], dates3[1], bookingBike3),
                            BookingStatus.RESERVED);

            bookingRepo.saveAll(List.of(booking1, booking2, booking3));
        };
    }
}
