package com.example.demotest.database;

import com.example.demotest.model.Product;
import com.example.demotest.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                Product productA = new Product("MAC", 2023,25000.0,"");
//                Product productB = new Product("IPAD", 2023,28000.0,"");
//                System.out.println("insert data"+productRepository.save(productA));
//                System.out.println("insert data"+productRepository.save(productB));

            }
        };
    }
}
