package com.example.foodcourtmicroservice.RestaurantServiceTest;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.exceptions.RestaurantEntityNotFoundException;
import com.example.foodcourtmicroservice.domain.spi.IRestaurantPersistencePort;
import com.example.foodcourtmicroservice.domain.usecase.RestaurantUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestPropertySource(locations = "classpath:application-dev.yml")
@SpringBootTest
public class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    private RestaurantUseCase restaurantUseCase;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        restaurantUseCase = new RestaurantUseCase(restaurantPersistencePort);
    }

    @Test
    @DisplayName("Test: getByNameRestaurant - Success")
    public void getByNameRestaurantSuccessfulTest(){
        // Arrange

        String name = "Mazorquita";

        // Act

        restaurantUseCase.getByNameRestaurant(name);

        //Assert

        verify(restaurantPersistencePort).getByNameRestaurant(name);
    }

    @Test
    @DisplayName("Test: getByNameRestaurant - Failure (RestaurantEntityNotFoundException)")
    public void getByNameRestaurantFailureTest() {
        // Arrange

        String name = "Jefferson";

        // Act

        when(restaurantPersistencePort.getByNameRestaurant(name))
                .thenThrow(RestaurantEntityNotFoundException.class);

        // Assert

        assertThrows(RestaurantEntityNotFoundException.class,
                () -> restaurantUseCase.getByNameRestaurant(name));
        verify(restaurantPersistencePort).getByNameRestaurant(name);
    }

    @Test
    @DisplayName("Test: getPaginationRestaurant - Success")
    public void getPaginationRestaurantsSuccessfulTest(){
        // Arrange

        Integer sizePage = 5;
        String filter = "name";

        // Act

        restaurantUseCase.getPaginationRestaurants(sizePage, filter);

        // Assert
        
        verify(restaurantPersistencePort).getPaginationRestaurants(sizePage, filter);
    }


}
