package dk.ek.gruppe2.chooseyourfate.controller;

import dk.ek.gruppe2.chooseyourfate.dto.ItemRequestDTO;
import dk.ek.gruppe2.chooseyourfate.dto.ItemResponseDTO;
import dk.ek.gruppe2.chooseyourfate.enums.DataSourceType;
import dk.ek.gruppe2.chooseyourfate.service.ItemService;
import dk.ek.gruppe2.chooseyourfate.service.mysql.SqlItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/choose-your-fate/items")
public class ItemController {

    private static final String DATA_SOURCE_HEADER = "X-Data-Source";

    ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or @characterAuthorizationService.canAccessCharacter(#characterId, authentication)")
    public List<ItemResponseDTO> getAllItems(
            @RequestHeader(value = DATA_SOURCE_HEADER, required = true) DataSourceType dataSource
    ) {
        return itemService.getAllItems(dataSource);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @characterAuthorizationService.canAccessCharacter(#characterId, authentication)")
    public ItemResponseDTO getItemById(
            @RequestHeader(value = DATA_SOURCE_HEADER, required = true) DataSourceType dataSource,
            @PathVariable("id") Integer id) {
        return itemService.findById(dataSource, id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @characterAuthorizationService.canAccessCharacter(#characterId, authentication)")
    public ResponseEntity<ItemResponseDTO> createItem(
            @RequestHeader(value = DATA_SOURCE_HEADER, required = true) DataSourceType dataSource,
            @RequestBody ItemRequestDTO requestDTO) {
        return itemService.createItem(dataSource, requestDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @characterAuthorizationService.canAccessCharacter(#characterId, authentication)")
    public ItemResponseDTO updateItem(
            @RequestHeader(value = DATA_SOURCE_HEADER, required = true) DataSourceType dataSource,
            @PathVariable("id") Integer id,
            @RequestBody ItemRequestDTO requestDTO) {
        return itemService.updateItem(dataSource, id, requestDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @characterAuthorizationService.canAccessCharacter(#characterId, authentication)")
    public void deleteItem(
            @RequestHeader(value = DATA_SOURCE_HEADER, required = true) DataSourceType dataSource,
            @PathVariable("id") Integer id) {
        itemService.deleteItem(dataSource, id);
    }
}

