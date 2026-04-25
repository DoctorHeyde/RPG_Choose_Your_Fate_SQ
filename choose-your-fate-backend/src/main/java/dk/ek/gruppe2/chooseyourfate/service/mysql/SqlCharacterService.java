package dk.ek.gruppe2.chooseyourfate.service.mysql;

import dk.ek.gruppe2.chooseyourfate.dto.CharacterResponseDTO;
import dk.ek.gruppe2.chooseyourfate.dto.CreateCharacterRequestDTO;
import dk.ek.gruppe2.chooseyourfate.interfaces.CharacterDataAccess;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SqlCharacterService implements CharacterDataAccess {

    private static final String MESSAGE =
            "SQL character functionality is planned to use stored procedures and is not implemented yet";

    @Override
    public List<CharacterResponseDTO> getAllCharacters() {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    public CharacterResponseDTO getCharacterById(Integer id) {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    public CharacterResponseDTO createCharacter(CreateCharacterRequestDTO request) {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    public void deleteCharacter(Integer id) {
        throw new UnsupportedOperationException(MESSAGE);
    }
}
