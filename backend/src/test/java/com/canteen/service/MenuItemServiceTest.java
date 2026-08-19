package com.canteen.service;

import com.canteen.dto.MenuItemResponse;
import com.canteen.entity.Category;
import com.canteen.entity.MenuItem;
import com.canteen.repository.CategoryRepository;
import com.canteen.repository.InventoryRepository;
import com.canteen.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private MenuItemService menuItemService;

    private MenuItem sampleItem;
    private Category sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = new Category("Breakfast", "Breakfast items", null);
        sampleCategory.setId(1L);

        sampleItem = new MenuItem("Masala Dosa", "Crispy crepe", new BigDecimal("80.00"), sampleCategory, true);
        sampleItem.setId(1L);
        sampleItem.setAvailable(true);
    }

    @Test
    void testGetAllAvailable() {
        when(menuItemRepository.findByAvailableTrue()).thenReturn(List.of(sampleItem));
        when(inventoryRepository.findByMenuItemId(1L)).thenReturn(Optional.empty());

        List<MenuItemResponse> result = menuItemService.getAllAvailable();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Masala Dosa", result.get(0).getName());
        assertEquals(new BigDecimal("80.00"), result.get(0).getPrice());
        verify(menuItemRepository, times(1)).findByAvailableTrue();
    }

    @Test
    void testGetMenuItemById() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(sampleItem));
        when(inventoryRepository.findByMenuItemId(1L)).thenReturn(Optional.empty());

        MenuItemResponse result = menuItemService.getMenuItemById(1L);

        assertNotNull(result);
        assertEquals("Masala Dosa", result.getName());
        assertTrue(result.isVegetarian());
        assertTrue(result.isAvailable());
    }

    @Test
    void testToggleAvailability() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(sampleItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MenuItemResponse result = menuItemService.toggleAvailability(1L, 1L);

        assertFalse(result.isAvailable());
        verify(auditLogService, times(1)).log(eq(1L), eq("TOGGLE_ITEM_AVAILABILITY"), eq("MenuItem"), eq(1L), anyString());
    }
}
