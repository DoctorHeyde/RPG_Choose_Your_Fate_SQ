package dk.ek.gruppe2.chooseyourfate.service;

import dk.ek.gruppe2.chooseyourfate.dto.ItemRequestDTO;
import dk.ek.gruppe2.chooseyourfate.dto.ItemResponseDTO;
import dk.ek.gruppe2.chooseyourfate.enums.DataSourceType;
import dk.ek.gruppe2.chooseyourfate.interfaces.ItemDataAccess;
import dk.ek.gruppe2.chooseyourfate.service.mysql.SqlItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.List;

@Service
public class ItemService {

    private final SqlItemService sqlItemService;

    public ItemService(SqlItemService sqlItemService) {
        this.sqlItemService = sqlItemService;
    }

    private ItemDataAccess resolveDataAccess(DataSourceType source) {
        return switch (source) {
            case SQL -> sqlItemService;
            //case NEO4J -> neo4jItemService;
            //case MONGODB -> mongoItemservice;
            default -> throw new IllegalArgumentException("Unexpected value: " + source);
        };
    }

    public List<ItemResponseDTO> getAllItems(DataSourceType source) {
        return resolveDataAccess(source).getAllItems();
    }

    public ItemResponseDTO findById(DataSourceType source, Integer id) {
        return resolveDataAccess(source).findById(id);
    }

    public ResponseEntity<ItemResponseDTO> createItem(DataSourceType source, ItemRequestDTO requestDTO) {
        return resolveDataAccess(source).createItem(requestDTO);
    }

    public ItemResponseDTO updateItem(DataSourceType source, Integer id, ItemRequestDTO requestDTO) {
        return resolveDataAccess(source).updateItem(id, requestDTO);
    }

    public void deleteItem(DataSourceType source, Integer id) {
        resolveDataAccess(source).deleteItem(id);
    }
}
