package dk.ek.gruppe2.chooseyourfate.service.neo4j;

import dk.ek.gruppe2.chooseyourfate.dto.LoadoutResponseDTO;
import dk.ek.gruppe2.chooseyourfate.interfaces.LoadoutDataAccess;
import dk.ek.gruppe2.chooseyourfate.repository.neo4j.Neo4jLoadoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Neo4jLoadoutService implements LoadoutDataAccess {

    private final Neo4jLoadoutRepository loadoutRepository;

    public Neo4jLoadoutService(Neo4jLoadoutRepository loadoutRepository) {
        this.loadoutRepository = loadoutRepository;
    }

    @Override
    public LoadoutResponseDTO getLoadoutByCharacterId(Integer characterId) {
        return loadoutRepository.getLoadoutByCharacterId(characterId);
    }

    @Override
    @Transactional
    public LoadoutResponseDTO equipItem(Integer characterId, Integer itemId) {
        return loadoutRepository.equipItem(characterId, itemId);
    }

    @Override
    @Transactional
    public LoadoutResponseDTO unequipItem(Integer characterId, Integer itemId) {
        return loadoutRepository.unequipItem(characterId, itemId);
    }
}

