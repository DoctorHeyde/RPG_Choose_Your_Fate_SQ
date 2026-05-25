package dk.ek.gruppe2.chooseyourfate.service;

import dk.ek.gruppe2.chooseyourfate.dto.EquipmentResponseDTO;
import dk.ek.gruppe2.chooseyourfate.enums.DataSourceType;
import dk.ek.gruppe2.chooseyourfate.interfaces.EquipmentDataAccess;
import dk.ek.gruppe2.chooseyourfate.service.mongodb.MongoEquipmentService;
import dk.ek.gruppe2.chooseyourfate.service.mysql.SqlEquipmentService;
import dk.ek.gruppe2.chooseyourfate.service.neo4j.Neo4jEquipmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    private final SqlEquipmentService sqlEquipmentService;
    private final Neo4jEquipmentService neo4jEquipmentService;
    private final MongoEquipmentService mongoEquipmentService;

    public EquipmentService(
            SqlEquipmentService sqlEquipmentService,
            Neo4jEquipmentService neo4jEquipmentService,
            MongoEquipmentService mongoEquipmentService
    ) {
        this.sqlEquipmentService = sqlEquipmentService;
        this.neo4jEquipmentService = neo4jEquipmentService;
        this.mongoEquipmentService = mongoEquipmentService;
    }

    public List<EquipmentResponseDTO> getAllEquipment(DataSourceType sourceHeader) {
        return resolveDataAccess(sourceHeader).getAllEquipment();
    }

    public EquipmentResponseDTO getEquipmentByCharacterId(DataSourceType sourceHeader, Integer characterId) {
        return resolveDataAccess(sourceHeader).getEquipmentByCharacterId(characterId);
    }

    private EquipmentDataAccess resolveDataAccess(DataSourceType sourceHeader) {
        return switch (sourceHeader) {
            case SQL -> sqlEquipmentService;
            case NEO4J -> neo4jEquipmentService;
            case MONGODB -> mongoEquipmentService;
        };
    }
}
