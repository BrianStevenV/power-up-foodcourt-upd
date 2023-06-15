package com.example.foodcourtmicroservice.OrderServiceTest;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderEntity;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderStatusEntity;
import com.example.foodcourtmicroservice.adapters.driving.http.controller.Feign.ITraceabilityFeignClient;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.EmployeeAssignedOrderRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderStatusRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.OrderPaginationEmployeeResponseDto;
import com.example.foodcourtmicroservice.domain.api.IAuthenticationUserInfoServicePort;
import com.example.foodcourtmicroservice.domain.exceptions.CancelToOrderException;
import com.example.foodcourtmicroservice.domain.exceptions.ClientHasOrderException;
import com.example.foodcourtmicroservice.domain.exceptions.IdOrderAndIdRestaurantAndOrderStatusPendingIsFalseException;
import com.example.foodcourtmicroservice.domain.exceptions.MarkOrderDeliveredException;
import com.example.foodcourtmicroservice.domain.exceptions.PlateBelongOtherRestaurantException;
import com.example.foodcourtmicroservice.domain.exceptions.PlateStatusDisabledException;
import com.example.foodcourtmicroservice.domain.model.Order.Order;
import com.example.foodcourtmicroservice.domain.model.Order.OrderStatus;
import com.example.foodcourtmicroservice.domain.model.Order.PlateOrder;
import com.example.foodcourtmicroservice.domain.model.Plate;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    @Mock
    private IAuthenticationUserInfoServicePort authenticationUserInfoServicePort;
    @Mock
    private ITraceabilityFeignClient traceabilityFeignClient;

    private OrderUseCase orderUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderUseCase = new OrderUseCase(orderPersistencePort, platePersistencePort, restaurantPersistencePort, authenticationUserInfoServicePort, traceabilityFeignClient);
    }


    @Test
    @DisplayName("Test: createOrder - Success")
    public void createOrderSuccessfulTest() {
        // Arrange

        String nameRestaurant = "Restaurant A";
        Long idClient = 1L;
        Long idRestaurant = 2L;

        Long idPlate1 = 1L;
        Long idPlate2 = 2L;

        List<PlateOrder> plateOrderList = new ArrayList<>();
        PlateOrder plateOrder1 = new PlateOrder(1L, 1L, 5);
        PlateOrder plateOrder2 = new PlateOrder(2L, 2L, 5);
        plateOrderList.add(plateOrder1);
        plateOrderList.add(plateOrder2);

        when(authenticationUserInfoServicePort.getIdUserFromToken()).thenReturn(idClient);
        when(restaurantPersistencePort.getByNameRestaurant(nameRestaurant)).thenReturn(idRestaurant);
        when(orderPersistencePort.clientHasOrder(idClient)).thenReturn(false);

        Plate plate1 = new Plate(idPlate1,"Plate 1", "Plate 1", 50.0, "image", true, 3L, 3L);
        Plate plate2 = new Plate(idPlate2, "Plate 2", "Plate 2", 70.0, "image", true, 3L, 3L);

        when(platePersistencePort.findByIdAndIdRestaurant(1L, idRestaurant)).thenReturn(plate1);
        when(platePersistencePort.findByIdAndIdRestaurant(2L, idRestaurant)).thenReturn(plate2);
        when(platePersistencePort.findByStatus(idPlate1)).thenReturn(true);
        when(platePersistencePort.findByStatus(idPlate2)).thenReturn(true);

        // Act

        orderUseCase.createOrder(nameRestaurant, plateOrderList);

        // Assert

        verify(authenticationUserInfoServicePort, times(1)).getIdUserFromToken();
        verify(restaurantPersistencePort, times(1)).getByNameRestaurant(nameRestaurant);
        verify(orderPersistencePort, times(1)).clientHasOrder(idClient);
        verify(platePersistencePort, times(1)).findByIdAndIdRestaurant(1L, idRestaurant);
        verify(platePersistencePort, times(1)).findByIdAndIdRestaurant(2L, idRestaurant);
        verify(orderPersistencePort, times(1)).createOrder(any(Order.class), eq(plateOrderList));
    }

    @Test
    @DisplayName("Test: createOrder - Failure (ClientHasOrderException)")
    public void createOrderClientHasOrderExceptionTest() {
        // Arrange

        String nameRestaurant = "Restaurant A";
        Long idClient = 1L;
        Long idRestaurant = 2L;

        when(authenticationUserInfoServicePort.getIdUserFromToken()).thenReturn(idClient);
        when(restaurantPersistencePort.getByNameRestaurant(nameRestaurant)).thenReturn(idRestaurant);
        when(orderPersistencePort.clientHasOrder(idClient)).thenReturn(true);

        List<PlateOrder> plateOrderList = new ArrayList<>();

        // Act & Assert

        assertThrows(ClientHasOrderException.class, () ->
                orderUseCase.createOrder(nameRestaurant, plateOrderList));

        verify(authenticationUserInfoServicePort, times(1)).getIdUserFromToken();
        verify(restaurantPersistencePort, times(1)).getByNameRestaurant(nameRestaurant);
        verify(orderPersistencePort, times(1)).clientHasOrder(idClient);
        verify(platePersistencePort, never()).findByIdAndIdRestaurant(anyLong(), anyLong());
        verify(orderPersistencePort, never()).createOrder(any(Order.class), anyList());
    }

    @Test
    @DisplayName("Test: createOrder - Failure (PlateStatusDisabledException)")
    public void createOrderPlateStatusDisabledExceptionTest() {
        // Arrange

        String nameRestaurant = "Restaurant A";
        Long idClient = 1L;
        Long idRestaurant = 2L;
        Long idPlate = 3L;

        when(authenticationUserInfoServicePort.getIdUserFromToken()).thenReturn(idClient);
        when(restaurantPersistencePort.getByNameRestaurant(nameRestaurant)).thenReturn(idRestaurant);
        when(orderPersistencePort.clientHasOrder(idClient)).thenReturn(false);

        Plate plate = new Plate();
        plate.setEnabled(true); // El estado del plato está habilitado
        when(platePersistencePort.findByIdAndIdRestaurant(idPlate, idRestaurant)).thenReturn(plate);

        List<PlateOrder> plateOrderList = new ArrayList<>();
        PlateOrder plateOrder = new PlateOrder();
        plateOrder.setIdPlate(idPlate);
        plateOrderList.add(plateOrder);

        when(platePersistencePort.findByStatus(idPlate)).thenReturn(false); // La validación de estado falla

        // Act & Assert

        assertThrows(PlateStatusDisabledException.class, () ->
                orderUseCase.createOrder(nameRestaurant, plateOrderList));

        verify(authenticationUserInfoServicePort, times(1)).getIdUserFromToken();
        verify(restaurantPersistencePort, times(1)).getByNameRestaurant(nameRestaurant);
        verify(orderPersistencePort, times(1)).clientHasOrder(idClient);
        verify(platePersistencePort, times(1)).findByIdAndIdRestaurant(idPlate, idRestaurant);
        verify(platePersistencePort, times(1)).findByStatus(idPlate);
        verify(orderPersistencePort, never()).createOrder(any(Order.class), anyList());
    }


    @Test
    @DisplayName("Test: createOrder - Failure (PlateBelongsOtherRestaurantExceptions)")
    public void createOrderPlateBelongsOtherRestaurantExceptionTest() {
        // Arrange

        String nameRestaurant = "Restaurant A";
        Long idClient = 1L;
        Long idRestaurant = 2L;

        when(authenticationUserInfoServicePort.getIdUserFromToken()).thenReturn(idClient);
        when(restaurantPersistencePort.getByNameRestaurant(nameRestaurant)).thenReturn(idRestaurant);
        when(orderPersistencePort.clientHasOrder(idClient)).thenReturn(false);

        List<PlateOrder> plateOrderList = new ArrayList<>();
        PlateOrder plateOrder = new PlateOrder();
        plateOrder.setIdPlate(1L);
        plateOrderList.add(plateOrder);

        when(platePersistencePort.findByIdAndIdRestaurant(1L, idRestaurant)).thenReturn(null);

        // Act & Assert

        assertThrows(PlateBelongOtherRestaurantException.class, () ->
                orderUseCase.createOrder(nameRestaurant, plateOrderList));

        verify(authenticationUserInfoServicePort, times(1)).getIdUserFromToken();
        verify(restaurantPersistencePort, times(1)).getByNameRestaurant(nameRestaurant);
        verify(orderPersistencePort, times(1)).clientHasOrder(idClient);
        verify(platePersistencePort, times(1)).findByIdAndIdRestaurant(1L, idRestaurant);
        verify(orderPersistencePort, never()).createOrder(any(Order.class), anyList());
    }


    @Test
    @DisplayName("Test: getPaginationOrderEmployee - Successful")
    public void testGetPaginationOrderEmployee() {
        // Arrange

        Long idRestaurant = 1L;
        OrderStatusRequestDto orderStatus = OrderStatusRequestDto.PENDING;
        Integer sizePage = 10;
        List<OrderEntity> orderEntities = Collections.singletonList(new OrderEntity(5L, 5L, LocalDate.now(), OrderStatusEntity.PENDING, 5L, 5L, null));
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

    @Test
    @DisplayName("Test: employeeAssignedOrder - Success")
    public void employeeAssignedOrderSuccessfulTest() {
        // Arrange

        Long idEmployee = 1L;
        Long idRestaurant = 2L;
        List<Long> idOrders = new ArrayList<>();
        idOrders.add(1L);
        idOrders.add(2L);

        EmployeeAssignedOrderRequestDto requestDto = new EmployeeAssignedOrderRequestDto();
        requestDto.setIdRestaurant(idRestaurant);
        requestDto.setIdOrder(idOrders);

        when(authenticationUserInfoServicePort.getIdUserFromToken()).thenReturn(idEmployee);
        when(orderPersistencePort.validateIdAndIdRestaurantAndStatusOrder(1L, idRestaurant, 0)).thenReturn(new Order());
        when(orderPersistencePort.validateIdAndIdRestaurantAndStatusOrder(2L, idRestaurant, 0)).thenReturn(new Order());

        // Act

        orderUseCase.employeeAssignedOrder(requestDto);

        // Assert

        verify(authenticationUserInfoServicePort, times(1)).getIdUserFromToken();
        verify(orderPersistencePort, times(2)).validateIdAndIdRestaurantAndStatusOrder(anyLong(), anyLong(), anyInt());
        verify(orderPersistencePort, times(2)).saveOrder(any(Order.class));
    }

    @Test
    @DisplayName("Test: employeeAssignedOrder - Failure (IdOrderAndIdRestaurantAndOrderStatusPendingException")
    public void employeeAssignedOrderIdOrderAndIdRestaurantAndOrderStatusPendingExceptionTest() {
        // Arrange

        Long idEmployee = 1L;
        Long idRestaurant = 2L;
        List<Long> idOrders = new ArrayList<>();
        idOrders.add(1L);
        idOrders.add(2L);

        EmployeeAssignedOrderRequestDto requestDto = new EmployeeAssignedOrderRequestDto();
        requestDto.setIdRestaurant(idRestaurant);
        requestDto.setIdOrder(idOrders);

        when(authenticationUserInfoServicePort.getIdUserFromToken()).thenReturn(idEmployee);
        when(orderPersistencePort.validateIdAndIdRestaurantAndStatusOrder(anyLong(), anyLong(), anyInt())).thenReturn(null);

        // Act & Assert

        assertThrows(IdOrderAndIdRestaurantAndOrderStatusPendingIsFalseException.class, () ->
                orderUseCase.employeeAssignedOrder(requestDto));

        verify(authenticationUserInfoServicePort, times(1)).getIdUserFromToken();
        verify(orderPersistencePort, times(1)).validateIdAndIdRestaurantAndStatusOrder(anyLong(), anyLong(), anyInt());
        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
    }


    @Test
    @DisplayName("Test: markOrderDelivered - Success")
    public void markOrderDeliveredSuccessfulTest() {
        // Arrange

        Long orderId = 1L;
        Long codeOrderVerification = 123456L;
        Order order = new Order();
        order.setId(orderId);
        order.setCodeVerification(codeOrderVerification);

        when(orderPersistencePort.validateIdAndStatusOrderAndCodeVerification(orderId, codeOrderVerification)).thenReturn(order);

        // Act

        assertDoesNotThrow(() -> orderUseCase.markOrderDelivered(orderId, codeOrderVerification));

        // Assert

        verify(orderPersistencePort, times(1)).saveOrder(order);
        assertEquals(OrderStatus.DELIVERED, order.getOrderStatusEntity());
    }

    @Test
    @DisplayName("Test: markOrderDelivered - Failure (MarkOrderDeliveredException)")
    public void markOrderDeliveredFailureTest() {
        // Arrange

        Long orderId = 1L;
        Long codeOrderVerification = 123456L;

        when(orderPersistencePort.validateIdAndStatusOrderAndCodeVerification(orderId, codeOrderVerification)).thenReturn(null);

        // Act & Assert

        assertThrows(MarkOrderDeliveredException.class, () -> orderUseCase.markOrderDelivered(orderId, codeOrderVerification));

        verify(orderPersistencePort, never()).saveOrder(any());
    }

    @Test
    @DisplayName("Test: cancelToOrder - Success")
    public void cancelToOrderSuccessfulTest() {
        // Arrange

        Long id = 1L;
        Long idClient = 123L;
        Order order = new Order();
        order.setId(id);
        order.setIdClient(idClient);
        order.setOrderStatusEntity(OrderStatus.PENDING);

        when(authenticationUserInfoServicePort.getIdUserFromToken()).thenReturn(idClient);
        when(orderPersistencePort.ValidateIdAndStatusOrderAndIdClient(id, idClient)).thenReturn(order);


        // Act

        assertDoesNotThrow(() -> orderUseCase.cancelToOrder(id));

        // Assert

        verify(orderPersistencePort, Mockito.times(1)).saveOrder(order);
        assertEquals(OrderStatus.CANCELED, order.getOrderStatusEntity());
    }

    @Test
    @DisplayName("Test: cancelToOrder - Failure (CancelToOrderException)")
    public void cancelToOrderFailureTest() {
        // Arrange

        Long id = 1L;
        Long idClient = 123L;

        when(authenticationUserInfoServicePort.getIdUserFromToken()).thenReturn(idClient);
        when(orderPersistencePort.ValidateIdAndStatusOrderAndIdClient(id, idClient)).thenReturn(null);

        // Act and Assert

        assertThrows(CancelToOrderException.class, () -> orderUseCase.cancelToOrder(id));
    }


}
