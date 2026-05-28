package dk.ek.gruppe2.chooseyourfate.integration;

import dk.ek.gruppe2.chooseyourfate.dto.InventoryResponseDTO;
import dk.ek.gruppe2.chooseyourfate.model.mysql.CharacterAvatar;
import dk.ek.gruppe2.chooseyourfate.model.mysql.Inventory;
import dk.ek.gruppe2.chooseyourfate.model.mysql.Item;
import dk.ek.gruppe2.chooseyourfate.repository.mysql.InventoryHasItemRepository;
import dk.ek.gruppe2.chooseyourfate.repository.mysql.InventoryRepository;
import dk.ek.gruppe2.chooseyourfate.service.InventoryService;
import dk.ek.gruppe2.chooseyourfate.service.ItemService;
import dk.ek.gruppe2.chooseyourfate.TestContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public class InventoryServiceIT {

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", TestContainerConfig.MYSQL::getJdbcUrl);
        registry.add("spring.datasource.password", TestContainerConfig.MYSQL::getPassword);
        registry.add("spring.datasource.username", TestContainerConfig.MYSQL::getUsername);
    }

    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    InventoryHasItemRepository inventoryHasItemRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    InventoryService inventoryService;

    @BeforeEach
    void setup() {
        CharacterAvatar characterAvatar = new CharacterAvatar();
        CharacterAvatar characterAvatar2 = new CharacterAvatar();
        characterAvatar.setId(1);
        characterAvatar2.setId(2);
        characterAvatar.setName("Test Character Avatar");
        characterAvatar2.setName("Test Character Avatar 2");
        inventoryRepository.deleteAll();
        inventoryHasItemRepository.deleteAll();
        inventoryRepository.save(new Inventory(1, characterAvatar));
        inventoryRepository.save(new Inventory(2, characterAvatar2));
        Item testItem1 = new Item();
        testItem1.setName("Test Item 1");
        testItem1.setId(1);
    }

    @Test
    void getInventoryByCharacterId_ShouldReturnDTO_WhenInventoryExists() {
        Integer query = 1;
        InventoryResponseDTO responseDTO = inventoryService.getInventoryByCharacterId(query);
        assert responseDTO != null;
    }

}
