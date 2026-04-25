package dk.ek.gruppe2.chooseyourfate.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public class Neo4jMigrationResponseDTO {

    private final String targetDatabase;
    private final boolean clearedExistingData;
    private final OffsetDateTime migratedAt;
    private final Map<String, Integer> migratedCounts;
    private final Map<String, Integer> integrityViolations;

    public Neo4jMigrationResponseDTO(
            String targetDatabase,
            boolean clearedExistingData,
            OffsetDateTime migratedAt,
            Map<String, Integer> migratedCounts,
            Map<String, Integer> integrityViolations
    ) {
        this.targetDatabase = targetDatabase;
        this.clearedExistingData = clearedExistingData;
        this.migratedAt = migratedAt;
        this.migratedCounts = migratedCounts;
        this.integrityViolations = integrityViolations;
    }

    public String getTargetDatabase() {
        return targetDatabase;
    }

    public boolean isClearedExistingData() {
        return clearedExistingData;
    }

    public OffsetDateTime getMigratedAt() {
        return migratedAt;
    }

    public Map<String, Integer> getMigratedCounts() {
        return migratedCounts;
    }

    public Map<String, Integer> getIntegrityViolations() {
        return integrityViolations;
    }
}
