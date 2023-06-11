package com.example.foodcourtmicroservice.adapters.driving.http.controller;

import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.EmployeeAssignedOrderRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.OrderPaginationEmployeeResponseDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderStatusRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.PlateRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.PlateStatusUpdateRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.RestaurantRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.UpdatePlateRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.PlatePaginationResponseDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.RestaurantPaginationResponseDto;
import com.example.foodcourtmicroservice.adapters.driving.http.handlers.IPlateHandler;
import com.example.foodcourtmicroservice.adapters.driving.http.handlers.IRestaurantHandler;
import com.example.foodcourtmicroservice.adapters.driving.http.handlers.IOrderHandler;
import com.example.foodcourtmicroservice.configuration.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/foodCourt/")
@RequiredArgsConstructor
@SecurityRequirement(name = "jwt")
public class FoodCourtRestController {
    private final IRestaurantHandler restaurantHandler;
    private final IPlateHandler plateHandler;
    private final IOrderHandler orderHandler;
    @Operation(summary = "Add a new Restaurant",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Restaurant created",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Map"))),
                    @ApiResponse(responseCode = "409", description = "Restaurant already exists",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))})
    @PreAuthorize("hasAuthority('ADMINISTRATOR_ROLE')")
    @PostMapping("restaurant/")
    public ResponseEntity<Map<String, String>> createRestaurant(@Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
        restaurantHandler.saveRestaurantFeign(restaurantRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.RESTAURANT_CREATED_MESSAGE));
    }
    @Operation(summary = "Add a new Plate",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Plate created",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Map"))),
                    @ApiResponse(responseCode = "409", description = "Plate already exists",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))})
    @PreAuthorize("hasAuthority('PROVIDER_ROLE')")
    @PostMapping("plate/{nameRestaurant}/{categoryPlate}")
    public ResponseEntity<Map<String,String>> createPlate(@PathVariable("nameRestaurant") String nameRestaurant, @PathVariable("categoryPlate") String categoryPlate, @Valid @RequestBody PlateRequestDto plateRequestDto){
        Long idRestaurant = restaurantHandler.getByNameRestaurant(nameRestaurant);
        plateHandler.savePlate(plateRequestDto, idRestaurant, categoryPlate);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.PLATE_CREATED_MESSAGE));

    }
    @Operation(summary = "Updated a Plate",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Plate updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Map"))),
                    @ApiResponse(responseCode = "409", description = "Plate already exists",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))})
    @PreAuthorize("hasAuthority('PROVIDER_ROLE')")
    @PatchMapping("plate/")
    public ResponseEntity<Map<String,String>> updatePlate(@Valid @RequestBody UpdatePlateRequestDto updatePlateRequestDto){
        plateHandler.updatePlate(updatePlateRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.UPDATE_PLATE_OK));
    }

    @Operation(summary = "Status Updated Plate",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Status updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Map"))),
                    @ApiResponse(responseCode = "409", description = "Plate already exists",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))})
    @PreAuthorize("hasAuthority('PROVIDER_ROLE')")
    @PatchMapping("plate/status/{enabled}")  //Si funciona?
    public ResponseEntity<Map<String, String>> updateStatusPlate(@PathVariable Boolean enabled, @Valid @RequestBody PlateStatusUpdateRequestDto plateStatus){
        plateHandler.statusEnabledPlate(enabled, plateStatus);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.UPDATE_STATUS_PLATE));
    }

    @Operation(summary = "Restaurant Pagination",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pagination successful",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Map"))),
                    @ApiResponse(responseCode = "409", description = "Error n@",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))})
    @PreAuthorize("hasAuthority('CLIENT_ROLE')")
    @GetMapping("pagination/restaurant")
    public Page<RestaurantPaginationResponseDto> getPaginationRestaurant(@RequestParam Integer pageSize, @RequestParam String sortBy){
        return restaurantHandler.getPaginationRestaurants(pageSize, sortBy);
    }
    @Operation(summary = "Plate Pagination",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pagination successful",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Map"))),
                    @ApiResponse(responseCode = "409", description = "Error n@",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))})
    @PreAuthorize("hasAuthority('CLIENT_ROLE')")
    @GetMapping("pagination/plate")
    public Page<PlatePaginationResponseDto> getPaginationPlate(@RequestParam String nameRestaurant, @RequestParam Integer pageSize,
                                                               @RequestParam String sortBy, @RequestParam(required = false) String nameCategory){
        Long idRestaurant = restaurantHandler.getByNameRestaurant(nameRestaurant);
        Long idCategory = null;
        if (nameCategory != null) {
            idCategory = plateHandler.getByNameCategory(nameCategory);
        }
        return plateHandler.getPaginationPlates(idRestaurant, pageSize, sortBy, idCategory);
    }

    @Operation(summary = "Add a new Order",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order created",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Map"))),
                    @ApiResponse(responseCode = "409", description = "Order already exists",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))})
    @PreAuthorize("hasAuthority('CLIENT_ROLE')")
    @PostMapping("order/")
    public ResponseEntity<Map<String, String>> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto){
        orderHandler.createOrder(orderRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.ORDER_CREATED_MESSAGE));
    }


    @Operation(summary = "Order Pagination",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pagination successful",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Map"))),
                    @ApiResponse(responseCode = "409", description = "Error n@",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))})
    @PreAuthorize("hasAuthority('EMPLOYEE_ROLE')")
    @GetMapping("pagination/order")
    public Page<OrderPaginationEmployeeResponseDto> getPaginationOrderForEmployee(@RequestParam Long idRestaurant, @RequestParam OrderStatusRequestDto orderStatusRequestDto
            , @RequestParam Integer sizePage){
        return orderHandler.getPaginationOrderForEmployee(idRestaurant, orderStatusRequestDto, sizePage);
    }

    @Operation(summary = "Assign employee to order",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Assignation successful",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Map"))),
                    @ApiResponse(responseCode = "409", description = " Assignation error",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))})
    @PreAuthorize("hasAuthority('EMPLOYEE_ROLE')")
    @PatchMapping("orders/employee")
    public ResponseEntity<Map<String, String>> assignEmployeeToOrder(@Valid @RequestBody EmployeeAssignedOrderRequestDto employeeAssignedOrderRequestDto){
        orderHandler.employeeAssignedOrder(employeeAssignedOrderRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.EMPLOYEE_TO_ORDER));
    }

}
