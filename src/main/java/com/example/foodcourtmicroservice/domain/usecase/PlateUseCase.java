package com.example.foodcourtmicroservice.domain.usecase;


import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.PlatePaginationResponseDto;
import com.example.foodcourtmicroservice.domain.api.IAuthenticationUserInfoServicePort;
import com.example.foodcourtmicroservice.domain.api.IPlateServicePort;
import com.example.foodcourtmicroservice.domain.exceptions.DifferentRestaurantException;
import com.example.foodcourtmicroservice.domain.exceptions.PlateNotFoundException;
import com.example.foodcourtmicroservice.domain.model.Plate;
import com.example.foodcourtmicroservice.domain.model.Restaurant;
import com.example.foodcourtmicroservice.domain.spi.ICategoryPersistencePort;
import com.example.foodcourtmicroservice.domain.spi.IPlatePersistencePort;
import com.example.foodcourtmicroservice.domain.spi.IRestaurantPersistencePort;
import org.springframework.data.domain.Page;


public class PlateUseCase implements IPlateServicePort {
    private final IPlatePersistencePort platePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IAuthenticationUserInfoServicePort authenticationUserInfoServicePort;

    public PlateUseCase(IPlatePersistencePort platePersistencePort, IRestaurantPersistencePort restaurantPersistencePort,
                        ICategoryPersistencePort categoryPersistencePort,
                        IAuthenticationUserInfoServicePort authenticationUserInfoServicePort) {
        this.platePersistencePort = platePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
        this.authenticationUserInfoServicePort = authenticationUserInfoServicePort;
    }

    @Override
    public void savePlate(Plate plate, Long idRestaurant, String categoryPlate) {
        Long idProvider = authenticationUserInfoServicePort.getIdUserFromToken();

        verifyingRestaurant(idRestaurant, idProvider);
        Long idCategory = categoryPersistencePort.getCategoryByName(categoryPlate);

        plate.setIdCategory(idCategory);
        plate.setIdRestaurant(idRestaurant);

        platePersistencePort.savePlate(plate);

    }

    @Override
    public void updatePlate(Plate plate) {
        Long idProvider = authenticationUserInfoServicePort.getIdUserFromToken();
        Plate plateFind = platePersistencePort.findByIdPlateEntity(plate.getId());

        verifyingRestaurant(plateFind.getIdRestaurant(), idProvider);
        plate.setPrice(plate.getPrice());
        plate.setDescription(plate.getDescription());

        platePersistencePort.savePlate(plate);
    }

    @Override
    public void statusEnabledPlate(Boolean enabled, Plate plate) {
        Long idOwner = authenticationUserInfoServicePort.getIdUserFromToken();

        verifyingRestaurant(plate.getIdRestaurant(), idOwner);
        Plate updatedPlate = platePersistencePort.statusEnabledPlate(plate);

        if (updatedPlate != null) {
            updatedPlate.setEnabled(enabled);

            platePersistencePort.savePlate(updatedPlate);
        } else {
            throw new PlateNotFoundException();
        }

    }
    @Override
    public Long getByNameCategory(String nameCategory) {
        return categoryPersistencePort.getCategoryByName(nameCategory);
    }

    @Override
    public Page<PlatePaginationResponseDto> getPaginationPlates(Long idRestaurant, Integer pageSize, String sortBy, Long idCategory) {
            if(idCategory == null){
                return platePersistencePort.getPaginationPlatesWithoutCategory(idRestaurant,pageSize,sortBy);
            }   else{
                return platePersistencePort.getPaginationPlates(idRestaurant,pageSize,sortBy,idCategory);
            }
    }

    private Boolean verifyingRestaurant(Long idRestaurant, Long idProvider){
        Restaurant restaurant = restaurantPersistencePort.findByIdAndIdOwner(idRestaurant, idProvider);
        if(restaurant != null){
            return true;
        }   else{
            throw new DifferentRestaurantException();
        }
    }



}
