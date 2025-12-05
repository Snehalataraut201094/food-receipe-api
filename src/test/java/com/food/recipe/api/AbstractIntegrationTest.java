package com.food.recipe.api;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public abstract class AbstractIntegrationTest {

    private static final PostgreSQLContainer<?> POSTGRES;
    static   {
        POSTGRES = new PostgreSQLContainer<>("postgres:17")
                .withDatabaseName("recipe_db")
                .withUsername("postgres")
                .withPassword("root")
                .withReuse(true);
        POSTGRES.start();

        Runtime.getRuntime().addShutdownHook(new Thread(POSTGRES::stop));
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
}
