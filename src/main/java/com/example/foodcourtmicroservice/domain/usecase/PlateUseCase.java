package com.example.foodcourtmicroservice.domain.usecase;


import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.PlatePaginationResponseDto;
import com.example.foodcourtmicroservice.domain.api.IAuthenticationUserInfoServicePort;
import com.example.foodcourtmicroservice.domain.api.IPlateServicePort;
import com.example.foodcourtmicroservice.domain.exceptions.IdPlateNotFoundException;
import com.example.foodcourtmicroservice.domain.exceptions.PlateNotFoundException;
import com.example.foodcourtmicroservice.domain.model.Plate;
import com.example.foodcourtmicroservice.domain.spi.ICategoryPersistencePort;
import com.example.foodcourtmicroservice.domain.spi.IPlatePersistencePort;
import org.springframework.data.domain.Page;


public class PlateUseCase implements IPlateServicePort {
    private final IPlatePersistencePort platePersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IAuthenticationUserInfoServicePort authenticationUserInfoServicePort;

    public PlateUseCase(IPlatePersistencePort platePersistencePort,ICategoryPersistencePort categoryPersistencePort,
                        IAuthenticationUserInfoServicePort authenticationUserInfoServicePort) {
        this.platePersistencePort = platePersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
        this.authenticationUserInfoServicePort = authenticationUserInfoServicePort;
    }

    @Override
    public void savePlate(Plate plate, Long idRestaurant, String categoryPlate) {
        Long idCategory = categoryPersistencePort.getCategoryByName(categoryPlate);
        plate.setIdCategory(idCategory);
        plate.setIdRestaurant(idRestaurant);
        platePersistencePort.savePlate(plate);
    }

    @Override
    public void updatePlate(Plate plate) {
        if (platePersistencePort.findByIdPlateEntity(plate.getId()).isPresent()) {
            plate.setPrice(plate.getPrice());
            plate.setDescription(plate.getDescription());
            platePersistencePort.savePlate(plate);
        } else {
            throw new IdPlateNotFoundException();
        }
    }

    @Override
    public void statusEnabledPlate(Boolean enabled, Plate plate) {
        String id = authenticationUserInfoServicePort.getIdentifierUserFromToken();
        System.out.println(" ID " + id);
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

}
