package com.me.clouddrive.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestConfig {
    private static final String MINIO_TEST_USER = "user";
    private static final String MINIO_TEST_PASSWORD = "test_pass";

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> pgContainer() {
        return new PostgreSQLContainer<>("postgres:alpine");
    }

    @Bean
    public GenericContainer<?> minioContainer(DynamicPropertyRegistry registry) {
        try (var minio = new GenericContainer<>("quay.io/minio/minio")
                .withCommand("server /data")
                .withEnv("MINIO_ROOT_USER", MINIO_TEST_USER)
                .withEnv("MINIO_ROOT_PASSWORD", MINIO_TEST_PASSWORD)
                .withExposedPorts(9000)) {
            registry.add("minio.user.bucket", () -> "user-files");
            registry.add("minio.client.accessKey", () -> MINIO_TEST_USER);
            registry.add("minio.client.secretKey", () -> MINIO_TEST_PASSWORD);
            registry.add("minio.client.endpoint", () -> "http://127.0.0.1:" + minio.getMappedPort(9000));
            return minio;
        }
    }
}
