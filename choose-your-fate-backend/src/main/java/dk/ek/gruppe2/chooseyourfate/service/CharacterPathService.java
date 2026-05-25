package dk.ek.gruppe2.chooseyourfate.service;

import dk.ek.gruppe2.chooseyourfate.dto.CharacterPathResponseDTO;
import dk.ek.gruppe2.chooseyourfate.dto.UpdateCharacterPathRequestDTO;
import dk.ek.gruppe2.chooseyourfate.enums.DataSourceType;
import dk.ek.gruppe2.chooseyourfate.interfaces.CharacterPathDataAccess;
import dk.ek.gruppe2.chooseyourfate.service.mongodb.MongoCharacterPathService;
import dk.ek.gruppe2.chooseyourfate.service.mysql.SqlCharacterPathService;
import dk.ek.gruppe2.chooseyourfate.service.neo4j.Neo4jCharacterPathService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacterPathService {

    private final SqlCharacterPathService sqlCharacterPathService;
    private final Neo4jCharacterPathService neo4jCharacterPathService;
    private final MongoCharacterPathService mongoCharacterPathService;

    public CharacterPathService(
            SqlCharacterPathService sqlCharacterPathService,
            Neo4jCharacterPathService neo4jCharacterPathService,
            MongoCharacterPathService mongoCharacterPathService
    ) {
        this.sqlCharacterPathService = sqlCharacterPathService;
        this.neo4jCharacterPathService = neo4jCharacterPathService;
        this.mongoCharacterPathService = mongoCharacterPathService;
    }

    public List<CharacterPathResponseDTO> getAllCharacterPaths(DataSourceType sourceHeader) {
        return resolveDataAccess(sourceHeader).getAllCharacterPaths();
    }

    public CharacterPathResponseDTO getCharacterPathByCharacterId(DataSourceType sourceHeader, Integer characterId) {
        return resolveDataAccess(sourceHeader).getCharacterPathByCharacterId(characterId);
    }

    public CharacterPathResponseDTO updateCharacterPath(
            DataSourceType sourceHeader,
            Integer characterId,
            UpdateCharacterPathRequestDTO request
    ) {
        return resolveDataAccess(sourceHeader).updateCharacterPath(characterId, request);
    }

    private CharacterPathDataAccess resolveDataAccess(DataSourceType sourceHeader) {
        return switch (sourceHeader) {
            case SQL -> sqlCharacterPathService;
            case NEO4J -> neo4jCharacterPathService;
            case MONGODB -> mongoCharacterPathService;
        };
    }
}