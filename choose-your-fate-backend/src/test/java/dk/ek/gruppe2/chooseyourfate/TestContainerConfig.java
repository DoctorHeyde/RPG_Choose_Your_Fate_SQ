package dk.ek.gruppe2.chooseyourfate;

import org.testcontainers.mysql.MySQLContainer;

public class TestContainerConfig {

    public static final MySQLContainer MYSQL;

    static {
        MYSQL = new MySQLContainer("mysql:9.5")
                .withDatabaseName("testdb")
                .withUsername("root")
                .withPassword("root")
                .withReuse(true);
        MYSQL.start();
    }
}
