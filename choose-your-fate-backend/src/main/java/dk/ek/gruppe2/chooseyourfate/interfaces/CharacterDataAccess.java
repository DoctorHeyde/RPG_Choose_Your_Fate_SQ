package dk.ek.gruppe2.chooseyourfate.interfaces;

import dk.ek.gruppe2.chooseyourfate.dto.CharacterResponseDTO;
import dk.ek.gruppe2.chooseyourfate.dto.CreateCharacterRequestDTO;

import java.util.List;

public interface CharacterDataAccess {

    List<CharacterResponseDTO> getAllCharacters();

    CharacterResponseDTO getCharacterById(Integer id);

    CharacterResponseDTO createCharacter(CreateCharacterRequestDTO request);

    void deleteCharacter(Integer id);
}
