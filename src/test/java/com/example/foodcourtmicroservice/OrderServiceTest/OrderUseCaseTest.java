package com.example.foodcourtmicroservice.OrderServiceTest;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderEntity;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderStatusEntity;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderStatusRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.OrderPaginationEmployeeResponseDto;
import com.example.foodcourtmicroservice.domain.api.IAuthenticationUserInfoServicePort;
import com.example.foodcourtmicroservice.domain.exceptions.ClientHasOrderException;
import com.example.foodcourtmicroservice.domain.model.Order.Order;
import com.example.foodcourtmicroservice.domain.model.Order.PlateOrder;
import com.example.foodcourtmicroservice.domain.spi.IOrderPersistencePort;
import com.example.foodcourtmicroservice.domain.spi.IPlatePersistencePort;
import com.example.foodcourtmicroservice.domain.spi.IRestaurantPersistencePort;
import com.example.foodcourtmicroservice.domain.usecase.OrderUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@TestPropertySource(locations = "classpath:application-dev.yml")
@SpringBootTest
public class OrderUseCaseTest {
    @Mock
    private IOrderPersistencePort orderPersistencePort;
    @Mock
    private IPlatePersistencePort platePersistencePort;
    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;
    private IAuthenticationUserInfoServicePort authenticationUserInfoServicePort;

    private OrderUseCase orderUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationUserInfoServicePort = Mockito.mock(IAuthenticationUserInfoServicePort.class);
        orderUseCase = new OrderUseCase(orderPersistencePort, platePersistencePort, restaurantPersistencePort, authenticationUserInfoServicePort);
    }


    @Test
    @DisplayName("Test: createOrder - Successful")
    public void createOrderSuccessfulTest() {
        // Arrange
        String nameRestaurant = "Restaurant A";
        Long clientId = 123L;
        Long plateId = 456L;
        Long orderId = 789L;

        List<PlateOrder> plateOrderList = new ArrayList<>();
        plateOrderList.add(new PlateOrder(orderId, plateId, 2));

        when(authenticationUserInfoServicePort.getIdUserFromToken()).thenReturn(clientId);
        when(orderPersistencePort.clientHasOrder(clientId)).thenReturn(false);
        when(platePersistencePort.findById(plateId)).thenReturn(Optional.of(plateId));
        when(restaurantPersistencePort.getByNameRestaurant(nameRestaurant)).thenReturn(1L);

        // Act
        orderUseCase.createOrder(nameRestaurant, plateOrderList);

        // Assert
        verify(authenticationUserInfoServicePort).getIdUserFromToken();
        verify(orderPersistencePort).clientHasOrder(clientId);
        verify(platePersistencePort).findById(plateId);
        verify(restaurantPersistencePort).getByNameRestaurant(nameRestaurant);
        verify(orderPersistencePort).createOrder(any(Order.class), eq(plateOrderList));
        verifyNoMoreInteractions(authenticationUserInfoServicePort, orderPersistencePort, platePersistencePort, restaurantPersistencePort);
    }
    @Test
    @DisplayName("Test: createOrder - Failure (Exception)")
    public void createOrderFailureTest() {
        // Arrange
        String nameRestaurant = "Restaurant A";
        Long clientId = 123L;
        Long plateId = 456L;
        Long orderId = 789L;

        List<PlateOrder> plateOrderList = new ArrayList<>();
        plateOrderList.add(new PlateOrder(orderId, plateId, 2));

        when(authenticationUserInfoServicePort.getIdUserFromToken()).thenReturn(clientId);
        when(orderPersistencePort.clientHasOrder(clientId)).thenReturn(true);

        // Act and Assert
        assertThrows(ClientHasOrderException.class, () -> orderUseCase.createOrder(nameRestaurant, plateOrderList));

        verify(authenticationUserInfoServicePort).getIdUserFromToken();
        verify(orderPersistencePort).clientHasOrder(clientId);
        verifyNoMoreInteractions(authenticationUserInfoServicePort, orderPersistencePort, platePersistencePort, restaurantPersistencePort);
    }

    // More?

    @Test
    @DisplayName("Test: getPaginationOrderEmployee - Successful")
    public void testGetPaginationOrderEmployee() {
        // Arrange

        Long idRestaurant = 1L;
        OrderStatusRequestDto orderStatus = OrderStatusRequestDto.PENDING;
        Integer sizePage = 10;
        List<OrderEntity> orderEntities = Collections.singletonList(new OrderEntity(5L, 5L, LocalDate.now(), OrderStatusEntity.PENDING, 5L, 5L));
        Page<OrderEntity> orderEntityPage = new PageImpl<>(orderEntities);

        // Create a custom matcher for the method arguments
        ArgumentMatcher<Long> idRestaurantMatcher = id -> id.equals(idRestaurant);
        ArgumentMatcher<OrderStatusRequestDto> orderStatusMatcher = orderStatusDto -> orderStatusDto.equals(orderStatus);
        ArgumentMatcher<Integer> sizePageMatcher = size -> size.equals(sizePage);

        when(orderPersistencePort.getPaginationOrderForEmployee(
                argThat(idRestaurantMatcher),
                argThat(orderStatusMatcher),
                argThat(sizePageMatcher)
        )).then(invocation -> orderEntityPage);

        // Act

        Page<OrderPaginationEmployeeResponseDto> result = orderUseCase.getPaginationOrderForEmployee(idRestaurant, orderStatus, sizePage);

        // Assert

        assertEquals(orderEntityPage.getTotalElements(), result.getTotalElements());
    }

    @Test
    @DisplayName("Test: getPaginationOrderEmployee - Empty List")
    public void testGetPaginationOrderEmployeeEmpty() {
        // Arrange

        Long idRestaurant = 1L;
        OrderStatusRequestDto orderStatus = OrderStatusRequestDto.PENDING;
        Integer sizePage = 10;
        List<OrderEntity> orderEntities = Collections.emptyList();
        Page<OrderEntity> emptyOrderEntityPage = new PageImpl<>(orderEntities);

        // Create a custom matcher for the method arguments
        ArgumentMatcher<Long> idRestaurantMatcher = id -> id.equals(idRestaurant);
        ArgumentMatcher<OrderStatusRequestDto> orderStatusMatcher = orderStatusDto -> orderStatusDto.equals(orderStatus);
        ArgumentMatcher<Integer> sizePageMatcher = size -> size.equals(sizePage);


        when(orderPersistencePort.getPaginationOrderForEmployee(
                argThat(idRestaurantMatcher),
                argThat(orderStatusMatcher),
                argThat(sizePageMatcher)
        )).then(invocation -> emptyOrderEntityPage);

        // Act

        Page<OrderPaginationEmployeeResponseDto> result = orderUseCase.getPaginationOrderForEmployee(idRestaurant, orderStatus, sizePage);

        // Assert

        assertEquals(0, result.getTotalElements());

    }


}
