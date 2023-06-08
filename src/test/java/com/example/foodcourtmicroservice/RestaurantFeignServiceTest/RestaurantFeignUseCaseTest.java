package com.example.foodcourtmicroservice.RestaurantFeignServiceTest;

import com.example.foodcourtmicroservice.adapters.driving.http.controller.RestaurantFeignClient;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.RoleResponseDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.UserResponseDto;
import com.example.foodcourtmicroservice.domain.exceptions.NoProviderException;
import com.example.foodcourtmicroservice.domain.model.Restaurant;
import com.example.foodcourtmicroservice.domain.spi.IRestaurantExternalPersistencePort;
import com.example.foodcourtmicroservice.domain.usecase.FeignClientRestaurantUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestPropertySource(locations = "classpath:application-dev.yml")
@SpringBootTest
public class RestaurantFeignUseCaseTest {
    @Mock
    private IRestaurantExternalPersistencePort restaurantExternalPersistencePort;
    @Mock
    private RestaurantFeignClient restaurantFeignClient;
    private FeignClientRestaurantUseCase feignClientRestaurantUseCase;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        feignClientRestaurantUseCase = new FeignClientRestaurantUseCase(restaurantFeignClient,restaurantExternalPersistencePort);
    }

    @Test
    @DisplayName("Test: getUserByDni - Success")
    public void getUserByDniMethodSuccessfulTest() {
        // Arrange

        UserResponseDto userResponseDto = new UserResponseDto(20L,"123", "Prueba", "Prueba apellido",
                "email@example.com", "3126805081", LocalDate.of(2023, 3, 30),
                "string", new RoleResponseDto("PROVIDER_ROLE", "PROVIDER_ROLE"));

        Long idNumber = 20L;

        // Act

        when(restaurantFeignClient.getUserByDni(anyLong())).thenReturn(userResponseDto);
        UserResponseDto result = restaurantFeignClient.getUserByDni(idNumber);

        // Assert
        assertNotNull(result);
        assertEquals("123", result.getDniNumber());
        assertEquals("Prueba", result.getName());
        assertEquals("Prueba apellido", result.getSurname());
        verify(restaurantFeignClient).getUserByDni(idNumber);
    }

    @Test
    @DisplayName("Test: getUserByDni - Failure (NoProviderException)")
    public void getUserByDniMethodFailureTest(){
        // Arrange
        UserResponseDto userResponseDto = new UserResponseDto(20L,"123", "Prueba", "Prueba apellido",
                "email@example.com", "3126805081", LocalDate.of(2023, 3, 30),
                "string", new RoleResponseDto("PROVIDER_ROLE", "PROVIDER_ROLE"));

        Long idNumber = 20L;

        // Act

        when(restaurantFeignClient.getUserByDni(anyLong()))
                .thenThrow(NoProviderException.class);

        // Assert
        assertThrows(NoProviderException.class,
                () -> restaurantFeignClient.getUserByDni(idNumber));

        verify(restaurantFeignClient).getUserByDni(idNumber);
    }

    @Test
    @DisplayName("Test: saveRestaurantServiceFeign - Success")
    public void saveRestaurantServiceFeignSuccessTest(){
        // Arrange

        Long idNumber = 20L;

        UserResponseDto userResponseDto = new UserResponseDto(20L,"123", "Prueba", "Prueba apellido",
                "email@example.com", "3126805081", LocalDate.of(2023, 3, 30),
                "string", new RoleResponseDto("PROVIDER_ROLE", "PROVIDER_ROLE"));

        Restaurant restaurantRequest = new Restaurant(20L,"Prueba","Java Street","3192621110",
                "Image","34512",4L);

        // Act

        when(restaurantFeignClient.getUserByDni(anyLong())).thenReturn(userResponseDto);
        UserResponseDto result = restaurantFeignClient.getUserByDni(idNumber);
        feignClientRestaurantUseCase.saveRestaurantServiceFeign(restaurantRequest);

        // Assert

        verify(restaurantExternalPersistencePort).saveRestaurantPersistenceFeign(restaurantRequest);

    }

    @Test
    @DisplayName("Test: saveRestaurantServiceFeign - Failure (NoProviderException)")
    public void saveRestaurantServiceFeignExceptionTest() {
        // Arrange

        UserResponseDto userResponseDto = new UserResponseDto(20L, "123", "Prueba", "Prueba apellido",
                "email@example.com", "3126805081", LocalDate.of(2023, 3, 30),
                "string", new RoleResponseDto("PROVIDER_ROLE", "PROVIDER_ROLE"));

        Restaurant restaurantRequest = new Restaurant(20L, "Prueba", "Java Street", "3192621110",
                "Image", "34512", 4L);

        // Act

        when(restaurantFeignClient.getUserByDni(anyLong())).thenReturn(userResponseDto);
        doThrow(new NoProviderException()).when(restaurantExternalPersistencePort).saveRestaurantPersistenceFeign(restaurantRequest);

        // Assert

        assertThrows(NoProviderException.class, () -> {
            feignClientRestaurantUseCase.saveRestaurantServiceFeign(restaurantRequest);
        });

        verify(restaurantExternalPersistencePort).saveRestaurantPersistenceFeign(restaurantRequest);
    }

    @Test
    @DisplayName("Test: saveRestaurantServiceFeign - Failure (Invalid Role)")
    public void saveRestaurantServiceFeignInvalidUserRoleTest() {
        // Arrange

        UserResponseDto userResponseDto = new UserResponseDto(20L, "123", "Prueba", "Prueba apellido",
                "email@example.com", "3126805081", LocalDate.of(2023, 3, 30),
                "string", new RoleResponseDto("INVALID_ROLE", "INVALID_ROLE")); // Rol inválido

        Restaurant restaurantRequest = new Restaurant(20L, "Prueba", "Java Street", "3192621110",
                "Image", "34512", 4L);

        // Act

        when(restaurantFeignClient.getUserByDni(anyLong())).thenReturn(userResponseDto);

        // Assert

        assertThrows(NoProviderException.class, () -> {
            feignClientRestaurantUseCase.saveRestaurantServiceFeign(restaurantRequest);
        });
    }



}
