package dk.ek.gruppe2.chooseyourfate.controller;

import dk.ek.gruppe2.chooseyourfate.dto.Neo4jMigrationResponseDTO;
import dk.ek.gruppe2.chooseyourfate.service.migration.neo4j.Neo4jMigrationService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/migrations/neo4j")
public class Neo4jMigrationController {

    private final Neo4jMigrationService neo4jMigrationService;

    public Neo4jMigrationController(Neo4jMigrationService neo4jMigrationService) {
        this.neo4jMigrationService = neo4jMigrationService;
    }

    @PostMapping
    public Neo4jMigrationResponseDTO migrateToNeo4j(
            @RequestParam(defaultValue = "true") boolean clearExisting
    ) {
        return neo4jMigrationService.migrateFromMySql(clearExisting);
    }

    @GetMapping("/integrity")
    public Map<String, Integer> getIntegrityViolations() {
        return neo4jMigrationService.runIntegrityChecks();
    }
}
