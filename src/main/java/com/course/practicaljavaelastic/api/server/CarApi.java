package com.course.practicaljavaelastic.api.server;

import com.course.practicaljavaelastic.api.response.ErrorResponse;
import com.course.practicaljavaelastic.entity.Car;
import com.course.practicaljavaelastic.exception.IllegalApiParamException;
import com.course.practicaljavaelastic.repository.CarElasticRepository;
import com.course.practicaljavaelastic.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequestMapping(value = "/api/car/v1")
@RestController
@Tag(name = "Car API", description = "Documentation for Car API")
public class CarApi {

    private Logger LOG = LoggerFactory.getLogger(CarApi.class);

    @Autowired
    private CarService carService;

    @Autowired
    private CarElasticRepository carElasticRepository;

    @GetMapping(value = "/random", produces = MediaType.APPLICATION_JSON_VALUE)
    public Car randomCar(){
        return carService.generateCar();
    }

    @Operation(summary = "Echo car", description = "Echo given car input")
    @PostMapping(value = "/echo", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String echo(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Car to be echoed") @RequestBody Car car){
        LOG.info("Car is {}", car);
        return car.toString();
    }

    @GetMapping(value = "/random-cars", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<Car> randomCars(){
        var result = new ArrayList<Car>();

        for(int i=0; i <ThreadLocalRandom.current().nextInt(1,10); i++) {
            result.add(carService.generateCar());
        }
        return result;
    }

    @GetMapping(value = "/count")
    public String countCar(){
        return "There are : " + carElasticRepository.count() + " cars";
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String saveCar(@RequestBody Car car){
        var id = carElasticRepository.save(car).getId();
        return "Saved with ID : " + id;
    }

    @GetMapping(value = "/{id}")
    public Car getCar(@PathVariable("id") String carId){
        return carElasticRepository.findById(carId).orElse(null);
    }

    @PutMapping(value = "/{id}")
    public String updateCar(@PathVariable("id") String carId, @RequestBody Car updateCar){
        updateCar.setId(carId);
        carElasticRepository.save(updateCar);
        var newCar = carElasticRepository.save(updateCar);
        return  "Update car with ID : " + newCar.getId();
    }

    @GetMapping(value = "/find-json", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Car> findCarsByBrandAndColor(@RequestBody Car car, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "price"));
        return carElasticRepository.findByBrandAndColor(car.getBrand(), car.getColor(), pageable).getContent();
    }

    @GetMapping(value = "/cars/{brand}/{color}")
    @Operation(summary = "Find cars by path", description = "Find cars by path variable")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Everything is OK"),
                    @ApiResponse(responseCode = "400", description = "Wrong input parameter")})
    public ResponseEntity<Object> findCarsByPath(
            @Parameter(description = "Brand to be find") @PathVariable String brand,
            @Parameter(description = "Brand to be color", example = "white") @PathVariable String color,
            @Parameter(description = "Page number (for pagination)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page (for pagination") @RequestParam(defaultValue = "10") int size){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SERVER, "Spring");
        headers.add("X-Custom-Header", "Custom Response Header");

        if(StringUtils.isNumeric(color)){
            ErrorResponse errorResponse = new ErrorResponse("Invalid color : " + color, LocalDateTime.now());
            return new ResponseEntity<Object>(errorResponse, headers, HttpStatus.BAD_REQUEST);
        }
        Pageable pageable = PageRequest.of(page, size);
        List<Car> cars = carElasticRepository.findByBrandAndColor(brand, color, pageable).getContent();
        return ResponseEntity.ok().headers(headers).body(cars);
    }

    @GetMapping(value = "/cars")
    public List<Car> findCarsByParam(@RequestParam String brand, @RequestParam String color, @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        if(StringUtils.isNumeric(color)){
            throw new IllegalArgumentException("Invalid type color : " + color);
        }
        if(StringUtils.isNumeric(brand)){
            throw new IllegalApiParamException("Invalid type color : " + brand);
        }
        Pageable pageable = PageRequest.of(page, size);
        return carElasticRepository.findByBrandAndColor(brand, color, pageable).getContent();
    }

    @GetMapping(value = "/cars/date")
    public List<Car> findCarsReleasedAfter(@RequestParam(name = "first_release_date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate firstReleaseDate){
        return carElasticRepository.findByFirstReleaseDateAfter(firstReleaseDate);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    private ResponseEntity<ErrorResponse> handleInvalidColorException(IllegalArgumentException e){
        String message = "Exception, " + e.getMessage();
        LOG.warn(message);
        ErrorResponse errorResponse = new ErrorResponse(message, LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }



}
