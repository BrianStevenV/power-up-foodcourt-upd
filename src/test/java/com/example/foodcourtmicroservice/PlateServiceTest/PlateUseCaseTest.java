package com.example.foodcourtmicroservice.PlateServiceTest;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.exceptions.CategoryNotFoundException;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.PlatePaginationResponseDto;
import com.example.foodcourtmicroservice.domain.api.IAuthenticationUserInfoServicePort;
import com.example.foodcourtmicroservice.domain.exceptions.DifferentRestaurantException;
import com.example.foodcourtmicroservice.domain.exceptions.PlateNotFoundException;
import com.example.foodcourtmicroservice.domain.model.Plate;
import com.example.foodcourtmicroservice.domain.model.Restaurant;
import com.example.foodcourtmicroservice.domain.spi.ICategoryPersistencePort;
import com.example.foodcourtmicroservice.domain.spi.IPlatePersistencePort;
import com.example.foodcourtmicroservice.domain.spi.IRestaurantPersistencePort;
import com.example.foodcourtmicroservice.domain.usecase.PlateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestPropertySource(locations = "classpath:application-dev.yml")
@SpringBootTest
public class PlateUseCaseTest {
    @Mock
    private IPlatePersistencePort platePersistencePort;
    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    private IAuthenticationUserInfoServicePort authenticationUserInfoServicePort;

    private PlateUseCase plateUseCase;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        authenticationUserInfoServicePort = Mockito.mock(IAuthenticationUserInfoServicePort.class);
        plateUseCase = new PlateUseCase(platePersistencePort, restaurantPersistencePort, categoryPersistencePort, authenticationUserInfoServicePort);
    }

    @Test
    @DisplayName("Test: getCategoryByName - Success")
    public void getCategoryByNameMethodSuccessfulTest(){
        // Arrange

        String nameCategory = "VEGAN_CATEGORY";
        Long idCategory = 123L;

        // Act

        when(categoryPersistencePort.getCategoryByName(nameCategory)).thenReturn(idCategory);
        Long actualCategoryId = categoryPersistencePort.getCategoryByName(nameCategory);

        // Assert

        assertEquals(idCategory, actualCategoryId);
        verify(categoryPersistencePort).getCategoryByName(nameCategory);
    }

    @Test
    @DisplayName("Test: getCategoryByName - Failure (CategoryNotFoundException)")
    public void getCategoryByNameMethodFailureTest(){
        // Arrange

        String nameCategory = "NO_CATEGORY";

        // Act

        when(categoryPersistencePort.getCategoryByName(nameCategory)).thenThrow(CategoryNotFoundException.class);

        // Act

        assertThrows(CategoryNotFoundException.class, () -> {
            categoryPersistencePort.getCategoryByName(nameCategory);
        });
        verify(categoryPersistencePort).getCategoryByName(nameCategory);
    }



    @DisplayName("Test: savePlate - Success")
    @Test
    public void savePlateSuccess() {
        // Arrange

        Long idRestaurant = 1L;
        String categoryPlate = "SomeCategory";
        Plate plate = new Plate();
        plate.setName("PlateName");

        Long idProvider = 2L;
        when(authenticationUserInfoServicePort.getIdUserFromToken()).thenReturn(idProvider);

        when(restaurantPersistencePort.findByIdAndIdOwner(idRestaurant, idProvider)).thenReturn(new Restaurant());

        Long idCategory = 3L;
        when(categoryPersistencePort.getCategoryByName(categoryPlate)).thenReturn(idCategory);

        // Act

        plateUseCase.savePlate(plate, idRestaurant, categoryPlate);

        // Assert

        verify(authenticationUserInfoServicePort, times(1)).getIdUserFromToken();
        verify(restaurantPersistencePort, times(1)).findByIdAndIdOwner(idRestaurant, idProvider);
        verify(categoryPersistencePort, times(1)).getCategoryByName(categoryPlate);
        verify(platePersistencePort, times(1)).savePlate(plate);

    }

    @DisplayName("Test: savePlate - Failure (DifferentRestaurantException)")
    @Test
    public void savePlateDifferentRestaurantException() {
        // Arrange

        Plate plate = new Plate(123L, "Prueba", "Food Test", 50.0, "Image.http", true, 3L, 14L);
        Long idRestaurant = 14L;
        String categoryPlate = "Meat";
        Long expectedCategoryId = 3L;

        // Act

        when(categoryPersistencePort.getCategoryByName(categoryPlate)).thenReturn(expectedCategoryId);
        doNothing().when(platePersistencePort).savePlate(plate);

        // Assert

        assertThrows(DifferentRestaurantException.class, () -> {
            plateUseCase.savePlate(plate, idRestaurant + 1, categoryPlate);
        });

        verify(platePersistencePort, never()).savePlate(any(Plate.class));
    }



    @Test
    @DisplayName("Test: getFindById - Success")
    public void getFindByIdMethodSuccessfulTest() {
        // Arrange

        Long id = 123L;
        Plate plate = new Plate(id, "Mazorquita", "Great Food", 75.0, "image.http", true, 3L, 14L);

        // Act

        when(platePersistencePort.findByIdPlateEntity(id)).thenReturn(plate);
        Plate retrievedPlate = platePersistencePort.findByIdPlateEntity(id);

        // Assert

        assertNotNull(retrievedPlate);
        assertEquals(id, retrievedPlate.getId());
        assertEquals("Mazorquita", retrievedPlate.getName());
        verify(platePersistencePort).findByIdPlateEntity(id);
    }

    @Test
    @DisplayName("Test: getFindById - Failure")
    public void getFindByIdMethodFailureTest() {
        // Arrange

        Long plateId = 1L;
        when(platePersistencePort.findByIdPlateEntity(plateId)).thenReturn(null);

        // Act

        Plate result = platePersistencePort.findByIdPlateEntity(plateId);

        // Assert

        assertNull(result);
        verify(platePersistencePort, times(1)).findByIdPlateEntity(plateId);

    }

    @Test
    @DisplayName("Test: updatePlate - Success")
    public void updatePlateServiceSuccessfulTest(){
        // Arrange

        Long idProvider = 123L;
        Long plateId = 456L;
        Long restaurantId = 789L;

        Plate existingPlate = new Plate();
        existingPlate.setId(plateId);
        existingPlate.setIdRestaurant(restaurantId);


        Plate updatedPlate = new Plate();
        updatedPlate.setId(plateId);
        updatedPlate.setIdRestaurant(restaurantId);


        when(authenticationUserInfoServicePort.getIdUserFromToken()).thenReturn(idProvider);
        when(platePersistencePort.findByIdPlateEntity(plateId)).thenReturn(existingPlate);
        when(restaurantPersistencePort.findByIdAndIdOwner(restaurantId, idProvider)).thenReturn(new Restaurant());
        doNothing().when(platePersistencePort).savePlate(updatedPlate);

        // Act

        plateUseCase.updatePlate(updatedPlate);

        // Assert

        verify(authenticationUserInfoServicePort).getIdUserFromToken();
        verify(platePersistencePort).findByIdPlateEntity(plateId);
        verify(restaurantPersistencePort).findByIdAndIdOwner(restaurantId, idProvider);
        verify(platePersistencePort).savePlate(updatedPlate);

    }

    @Test
    @DisplayName("Test: updateStatusPlate - Success")
    public void updateStatusPlateSuccessfulTest() {
        // Arrange

        Long idRestaurant = 1L;
        Long idOwner = 2L;

        String token = "validToken";
        String name = "Plate Name";

        Plate plate = new Plate();
        plate.setIdRestaurant(idRestaurant);
        plate.setName(name);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(idRestaurant);
        restaurant.setIdOwner(idOwner);

        Plate updatedPlate = new Plate();
        updatedPlate.setIdRestaurant(idRestaurant);
        updatedPlate.setName(name);
        updatedPlate.setEnabled(true);

        when(authenticationUserInfoServicePort.getIdentifierUserFromToken()).thenReturn(token);
        when(authenticationUserInfoServicePort.getIdUserFromToken()).thenReturn(idOwner);
        when(restaurantPersistencePort.findByIdAndIdOwner(idRestaurant, idOwner)).thenReturn(restaurant);
        when(platePersistencePort.statusEnabledPlate(plate)).thenReturn(updatedPlate);

        // Act

        plateUseCase.statusEnabledPlate(true, plate);

        // Assert

        assertTrue(updatedPlate.getEnabled());
        verify(platePersistencePort, times(1)).savePlate(updatedPlate);

    }



    @Test
    @DisplayName("Test: updateStatusPlate - Failure (PlateNotFoundException)")
    public void updateStatusPlateThrowsPlateNotFoundExceptionTest() {
        // Arrange

        Long idRestaurant = 1L;
        Long idOwner = 2L;

        String token = "validToken";
        String name = "Non-existent Plate Name";

        Plate plate = new Plate();
        plate.setIdRestaurant(idRestaurant);
        plate.setName(name);

        when(authenticationUserInfoServicePort.getIdentifierUserFromToken()).thenReturn(token);
        when(authenticationUserInfoServicePort.getIdUserFromToken()).thenReturn(idOwner);
        when(restaurantPersistencePort.findByIdAndIdOwner(idRestaurant, idOwner)).thenReturn(new Restaurant());
        when(platePersistencePort.statusEnabledPlate(plate)).thenReturn(null);

        // Act & Assert

        assertThrows(PlateNotFoundException.class, () -> {
            plateUseCase.statusEnabledPlate(true, plate);
        });

        verify(platePersistencePort, never()).savePlate(any());
    }

    @Test
    @DisplayName("Test: getPaginationPlates : getPaginationPlatesWithoutCategory() - Failure")
    public void getPaginationPlatesWithoutCategoryFailureTest() {
        // Arrange

        Long idRestaurant = 123L;
        Integer pageSize = 10;
        String sortBy = "name";

        when(platePersistencePort.getPaginationPlatesWithoutCategory(idRestaurant, pageSize, sortBy))
                .thenReturn(null);

        // Act

        Page<PlatePaginationResponseDto> actualPage = plateUseCase.getPaginationPlates(idRestaurant, pageSize, sortBy, null);

        // Assert

        assertEquals(null, actualPage);
    }

    @Test
    @DisplayName("Test: getPaginationPlates : getPaginationPlates() - Failure")
    public void getPaginationPlatesWithCategoryFailureTest() {
        // Arrange

        Long idRestaurant = 123L;
        Integer pageSize = 10;
        String sortBy = "name";
        Long idCategory = 456L;

        when(platePersistencePort.getPaginationPlates(idRestaurant, pageSize, sortBy, idCategory))
                .thenReturn(null);

        // Act

        Page<PlatePaginationResponseDto> actualPage = plateUseCase.getPaginationPlates(idRestaurant, pageSize, sortBy, idCategory);

        // Assert

        assertEquals(null, actualPage);
    }

    @Test
    @DisplayName("Test: getPaginationPlates : getPaginationPlatesWithoutCategory() - Success")
    public void getPaginationPlatesWithoutCategorySuccessfulTest() {
        // Arrange

        Long idRestaurant = 123L;
        Integer pageSize = 10;
        String sortBy = "name";

        List<PlatePaginationResponseDto> plateList = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(0, pageSize);
        Page<PlatePaginationResponseDto> expectedPage = new PageImpl<>(plateList, pageRequest, plateList.size());

        // Act

        when(platePersistencePort.getPaginationPlatesWithoutCategory(idRestaurant, pageSize, sortBy))
                .thenReturn(expectedPage);
        Page<PlatePaginationResponseDto> actualPage = plateUseCase.getPaginationPlates(idRestaurant, pageSize, sortBy, null);

        // Assert

        assertEquals(expectedPage, actualPage);
    }

    @Test
    @DisplayName("Test: getPaginationPlates : getPaginationPlates() - Success")
    public void getPaginationPlatesWithCategorySuccessfulTest() {
        // Arrange

        Long idRestaurant = 123L;
        Integer pageSize = 10;
        String sortBy = "name";
        Long idCategory = 456L;

        List<PlatePaginationResponseDto> plateList = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(0, pageSize);
        Page<PlatePaginationResponseDto> expectedPage = new PageImpl<>(plateList, pageRequest, plateList.size());

        // Act

        when(platePersistencePort.getPaginationPlates(idRestaurant, pageSize, sortBy, idCategory))
                .thenReturn(expectedPage);

        Page<PlatePaginationResponseDto> actualPage = plateUseCase.getPaginationPlates(idRestaurant, pageSize, sortBy, idCategory);

        // Assert

        assertEquals(expectedPage, actualPage);
    }

}
