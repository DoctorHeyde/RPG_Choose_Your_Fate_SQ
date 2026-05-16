package dk.ek.gruppe2.chooseyourfate.service;

import dk.ek.gruppe2.chooseyourfate.datasource.DataSourceResolver;
import dk.ek.gruppe2.chooseyourfate.dto.CharacterResponseDTO;
import dk.ek.gruppe2.chooseyourfate.dto.CreateCharacterRequestDTO;
import dk.ek.gruppe2.chooseyourfate.enums.DataSourceType;
import dk.ek.gruppe2.chooseyourfate.interfaces.CharacterDataAccess;
import dk.ek.gruppe2.chooseyourfate.service.mysql.SqlCharacterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacterService {

    private final DataSourceResolver dataSourceResolver;
    private final SqlCharacterService sqlCharacterService;

    public CharacterService(
            DataSourceResolver dataSourceResolver,
            SqlCharacterService sqlCharacterService
    ) {
        this.dataSourceResolver = dataSourceResolver;
        this.sqlCharacterService = sqlCharacterService;
    }

    public List<CharacterResponseDTO> getAllCharacters(String sourceHeader) {
        return resolveDataAccess(sourceHeader).getAllCharacters();
    }

    public CharacterResponseDTO getCharacterById(String sourceHeader, Integer id) {
        return resolveDataAccess(sourceHeader).getCharacterById(id);
    }

    public CharacterResponseDTO createCharacter(String sourceHeader, CreateCharacterRequestDTO request) {
        return resolveDataAccess(sourceHeader).createCharacter(request);
    }

    public void deleteCharacter(String sourceHeader, Integer id) {
        resolveDataAccess(sourceHeader).deleteCharacter(id);
    }

    private CharacterDataAccess resolveDataAccess(String sourceHeader) {
        DataSourceType dataSourceType = dataSourceResolver.resolve(sourceHeader);
        return switch (dataSourceType) {
            case SQL -> sqlCharacterService;
        };
    }
}
