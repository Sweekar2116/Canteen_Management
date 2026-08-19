package com.canteen.service;

import com.canteen.dto.CartItemRequest;
import com.canteen.dto.CartResponse;
import com.canteen.entity.*;
import com.canteen.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private CartService cartService;

    private User sampleUser;
    private Cart sampleCart;
    private MenuItem sampleItem;

    @BeforeEach
    void setUp() {
        sampleUser = new User("John", "john@example.com", "9999999999", "pass");
        sampleUser.setId(1L);

        sampleCart = new Cart(sampleUser);
        sampleCart.setId(1L);

        Category cat = new Category("Breakfast", "Desc", null);
        sampleItem = new MenuItem("Idli", "Steamed cake", new BigDecimal("40.00"), cat, true);
        sampleItem.setId(2L);
        sampleItem.setAvailable(true);
    }

    @Test
    void testGetCart_CalculatesCorrectTaxAndSubtotal() {
        CartItem cartItem = new CartItem(sampleCart, sampleItem, 2);
        cartItem.setId(10L);
        sampleCart.getItems().add(cartItem);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(sampleCart));

        CartResponse response = cartService.getCart(1L);

        assertNotNull(response);
        assertEquals(2, response.getTotalItems());
        assertEquals(new BigDecimal("80.00"), response.getSubtotal()); // 2 * 40
        assertEquals(new BigDecimal("4.00"), response.getTax()); // 5% of 80 = 4.00
        assertEquals(new BigDecimal("84.00"), response.getTotal()); // 80 + 4
    }

    @Test
    void testAddItemToCart() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(sampleCart));
        when(menuItemRepository.findById(2L)).thenReturn(Optional.of(sampleItem));
        when(inventoryRepository.findByMenuItemId(2L)).thenReturn(Optional.empty());
        when(cartItemRepository.findByCartIdAndMenuItemId(1L, 2L)).thenReturn(Optional.empty());
        when(cartRepository.findById(1L)).thenReturn(Optional.of(sampleCart));

        CartItemRequest request = new CartItemRequest(2L, 2);
        CartResponse response = cartService.addItemToCart(1L, request);

        assertNotNull(response);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }
}
