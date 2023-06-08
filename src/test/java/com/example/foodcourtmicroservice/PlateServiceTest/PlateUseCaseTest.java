package com.example.foodcourtmicroservice.PlateServiceTest;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.PlateEntity;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.exceptions.CategoryNotFoundException;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.PlatePaginationResponseDto;
import com.example.foodcourtmicroservice.domain.api.IAuthenticationUserInfoServicePort;
import com.example.foodcourtmicroservice.domain.exceptions.IdPlateNotFoundException;
import com.example.foodcourtmicroservice.domain.exceptions.PlateNotFoundException;
import com.example.foodcourtmicroservice.domain.model.Plate;
import com.example.foodcourtmicroservice.domain.spi.ICategoryPersistencePort;
import com.example.foodcourtmicroservice.domain.spi.IPlatePersistencePort;
import com.example.foodcourtmicroservice.domain.usecase.PlateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestPropertySource(locations = "classpath:application-dev.yml")
@SpringBootTest
public class PlateUseCaseTest {
    @Mock
    private IPlatePersistencePort platePersistencePort;
    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    private IAuthenticationUserInfoServicePort authenticationUserInfoServicePort;

    private PlateUseCase plateUseCase;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        authenticationUserInfoServicePort = Mockito.mock(IAuthenticationUserInfoServicePort.class);
        plateUseCase = new PlateUseCase(platePersistencePort, categoryPersistencePort, authenticationUserInfoServicePort);
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

    @Test
    public void savePlateSuccessfulTest(){
        // Arrange

        Plate plate = new Plate(123L, "Prueba", "Food Test", 50.0, "Image.http", true,3L, 14L);
        Long idRestaurant = 14L;
        String categoryPlate = "Category Test";
        Long expectedCategoryId = 3L;

        // Act

        when(categoryPersistencePort.getCategoryByName(categoryPlate)).thenReturn(expectedCategoryId);
        doNothing().when(platePersistencePort).savePlate(plate);
        plateUseCase.savePlate(plate, idRestaurant, categoryPlate);

        // Assert
        verify(categoryPersistencePort).getCategoryByName(categoryPlate);
        verify(platePersistencePort).savePlate(plate);
    }

    @Test
    @DisplayName("Test: getFindById - Success")
    public void getFindByIdMethodSuccessfulTest(){
        // Arrange

        Long id = 123L;
        PlateEntity plateEntity = new PlateEntity(id, "Mazorquita", "Great Food", 75.0,"image.http",true,3L,14L);


        //Act

        when(platePersistencePort.findByIdPlateEntity(id)).thenReturn(Optional.of(plateEntity));
        Optional<PlateEntity> optionalPlateEntity = platePersistencePort.findByIdPlateEntity(id);
        PlateEntity retrievedPlateEntity = optionalPlateEntity.get();

        // Assert

        assertTrue(optionalPlateEntity.isPresent());
        assertEquals(id, retrievedPlateEntity.getId());
        assertEquals("Mazorquita", retrievedPlateEntity.getName());
        verify(platePersistencePort).findByIdPlateEntity(id);
    }

    @Test
    @DisplayName("Test: getFindById - Failure")
    public void getFindByIdMethodFailureTest() {
        // Arrange

        Long id = 123L;
        Optional<PlateEntity> optionalPlateEntity = platePersistencePort.findByIdPlateEntity(id);

        // Act

        when(platePersistencePort.findByIdPlateEntity(id)).thenReturn(Optional.empty());

        // Assert
        assertFalse(optionalPlateEntity.isPresent());
        verify(platePersistencePort).findByIdPlateEntity(id);
    }

    @Test
    @DisplayName("Test: updatePlate - Failure (IdPlateNotFoundException)")
    public void updatePlateServiceExceptionTest() {
        // Arrange

        Long id = 123L;
        Plate plate = new Plate();
        plate.setId(id);
        plate.setPrice(50.0);
        plate.setDescription("Updated Plate");

        // Act

        when(platePersistencePort.findByIdPlateEntity(id)).thenReturn(Optional.empty());

        // Assert

        assertThrows(IdPlateNotFoundException.class, () -> plateUseCase.updatePlate(plate));
        verify(platePersistencePort).findByIdPlateEntity(id);
        verify(platePersistencePort, never()).savePlate(any(Plate.class));
    }

    @Test
    @DisplayName("Test: updatePlate - Success")
    public void updatePlateServiceSuccessfulTest(){
        // Arrange

        Long id = 123L;
        Plate plate = new Plate();
        plate.setId(id);
        plate.setPrice(50.0);
        plate.setDescription("Updated Plate");

        when(platePersistencePort.findByIdPlateEntity(id)).thenReturn(Optional.of(new PlateEntity()));

        // Act

        plateUseCase.updatePlate(plate);

        // Assert

        verify(platePersistencePort).findByIdPlateEntity(id);
        verify(platePersistencePort).savePlate(plate);
    }

    @Test
    @DisplayName("Test: updateStatusPlate - Success")
    public void updateStatusPlateSuccessfulTest() {
        // Arrange
        Boolean enabled = true;
        Plate plate = new Plate(123L, "Prueba", "Food Test", 50.0, "Image.http", false, 3L, 14L);
        Plate updatedPlate = new Plate(123L, "Prueba", "Food Test", 50.0, "Image.http", enabled, 3L, 14L);

        when(platePersistencePort.statusEnabledPlate(any(Plate.class))).thenReturn(plate);
        ArgumentCaptor<Plate> plateCaptor = ArgumentCaptor.forClass(Plate.class);

        // Act
        plateUseCase.statusEnabledPlate(enabled, plate);

        // Assert
        verify(platePersistencePort).statusEnabledPlate(any(Plate.class));
        verify(platePersistencePort).savePlate(plateCaptor.capture());
        Plate capturedPlate = plateCaptor.getValue();
        assertNotEquals(updatedPlate, capturedPlate);
    }



    @Test
    @DisplayName("Test: updateStatusPlate - Failure (PlateNotFoundException)")
    public void updateStatusPlateThrowsPlateNotFoundExceptionTest() {
        // Arrange

        Boolean enabled = true;
        Plate plate = new Plate(123L, "Prueba", "Food Test", 50.0, "Image.http", true,3L, 14L);

        // Act

        when(platePersistencePort.statusEnabledPlate(any(Plate.class))).thenReturn(null);

        // Assert

        assertThrows(PlateNotFoundException.class, () -> {
            plateUseCase.statusEnabledPlate(enabled,plate);
        });
        verify(platePersistencePort).statusEnabledPlate(any(Plate.class));
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
