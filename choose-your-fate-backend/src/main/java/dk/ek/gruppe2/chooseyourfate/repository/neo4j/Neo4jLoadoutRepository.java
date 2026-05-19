package dk.ek.gruppe2.chooseyourfate.repository.neo4j;

import dk.ek.gruppe2.chooseyourfate.dto.LoadoutResponseDTO;

public interface Neo4jLoadoutRepository {

    LoadoutResponseDTO getLoadoutByCharacterId(Integer characterId);

    LoadoutResponseDTO equipItem(Integer characterId, Integer itemId);

    LoadoutResponseDTO unequipItem(Integer characterId, Integer itemId);
}
