package dk.ek.gruppe2.chooseyourfate.service;

import dk.ek.gruppe2.chooseyourfate.dto.RaceDetailsResponseDTO;
import dk.ek.gruppe2.chooseyourfate.enums.DataSourceType;
import dk.ek.gruppe2.chooseyourfate.interfaces.RaceDetailsDataAccess;
import dk.ek.gruppe2.chooseyourfate.service.mongodb.MongoRaceDetailsService;
import dk.ek.gruppe2.chooseyourfate.service.mysql.SqlRaceDetailsService;
import dk.ek.gruppe2.chooseyourfate.service.neo4j.Neo4jRaceDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceDetailsService {

    private final SqlRaceDetailsService sqlRaceDetailsService;
    private final Neo4jRaceDetailsService neo4jRaceDetailsService;
    private final MongoRaceDetailsService mongoRaceDetailsService;

    public RaceDetailsService(
            SqlRaceDetailsService sqlRaceDetailsService,
            Neo4jRaceDetailsService neo4jRaceDetailsService,
            MongoRaceDetailsService mongoRaceDetailsService
    ) {
        this.sqlRaceDetailsService = sqlRaceDetailsService;
        this.neo4jRaceDetailsService = neo4jRaceDetailsService;
        this.mongoRaceDetailsService = mongoRaceDetailsService;
    }

    public List<RaceDetailsResponseDTO> getAllRaceDetails(DataSourceType sourceHeader) {
        return resolveDataAccess(sourceHeader).getAllRaceDetails();
    }

    public RaceDetailsResponseDTO getRaceDetailsById(DataSourceType sourceHeader, Integer id) {
        return resolveDataAccess(sourceHeader).getRaceDetailsById(id);
    }

    public RaceDetailsResponseDTO createRaceDetails(DataSourceType sourceHeader) {
        return resolveDataAccess(sourceHeader).createRaceDetails();
    }

    public void deleteRaceDetails(DataSourceType sourceHeader, Integer id) {
        resolveDataAccess(sourceHeader).deleteRaceDetails(id);
    }

    private RaceDetailsDataAccess resolveDataAccess(DataSourceType sourceHeader) {
        return switch (sourceHeader) {
            case SQL -> sqlRaceDetailsService;
            case NEO4J -> neo4jRaceDetailsService;
            case MONGODB -> mongoRaceDetailsService;
        };
    }
}
