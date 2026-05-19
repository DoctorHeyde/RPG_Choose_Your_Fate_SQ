package dk.ek.gruppe2.chooseyourfate.controller;

import dk.ek.gruppe2.chooseyourfate.dto.CharacterPathResponseDTO;
import dk.ek.gruppe2.chooseyourfate.dto.UpdateCharacterPathRequestDTO;
import dk.ek.gruppe2.chooseyourfate.model.mysql.CharacterPathChoiceId;
import dk.ek.gruppe2.chooseyourfate.service.CharacterPathService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/character-paths")
public class CharacterPathController {
    private final CharacterPathService characterPathService;

    public CharacterPathController(CharacterPathService characterPathService) {
        this.characterPathService = characterPathService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CharacterPathResponseDTO> getAllCharacterPaths() {
        return characterPathService.getAllCharacterPaths();
    }

    @GetMapping("/{characterId}")
    @PreAuthorize("hasRole('ADMIN') or @characterAuthorizationService.canAccessCharacter(#characterId, authentication)")
    public CharacterPathResponseDTO getCharacterPathByCharacterId(
            @PathVariable Integer characterId
    ) {
        return characterPathService.getCharacterPathByCharacterId(characterId);
    }

    @PutMapping("/{characterId}")
    @PreAuthorize("hasRole('ADMIN') or @characterAuthorizationService.canAccessCharacter(#characterId, authentication)")
    public CharacterPathResponseDTO updateCharacterPath(
            @PathVariable Integer characterId,
            @RequestBody UpdateCharacterPathRequestDTO request
    ) {
        return characterPathService.updateCharacterPath(characterId, request);
    }

    @PutMapping("/{characterId}/chosen/{choiceId}")
    @PreAuthorize("hasRole('ADMIN') or @characterAuthorizationService.canAccessCharacter(#characterId, authentication)")
    public CharacterPathChoiceId updateCharacterPathChoices(
            @PathVariable Integer characterId,
            @PathVariable Integer choiceId
    ) {
        return characterPathService.updateCharacterPathChoice(characterId, choiceId);
    }
}