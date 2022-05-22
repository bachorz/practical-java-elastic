package com.course.practicaljavaelastic.common;

import com.course.practicaljavaelastic.entity.Car;
import com.course.practicaljavaelastic.repository.CarElasticRepository;
import com.course.practicaljavaelastic.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

@Component
public class CarElasticDatasource {

    private static final Logger LOG = LoggerFactory.getLogger(CarElasticDatasource.class);

    @Autowired
    private CarElasticRepository carRepository;

    @Autowired
    @Qualifier("randomCarService")
    private CarService carService;

    @Autowired
    @Qualifier("webClientElasticsearch")
    private WebClient webClient;

    @EventListener(ApplicationReadyEvent.class)
    public void populateData() {
        var response = webClient.delete().uri("/practical-java-elastic").retrieve().bodyToMono(String.class).block();
        LOG.info("End delete with response {}", response);

        var cars = createCars();

        carRepository.saveAll(cars);

        LOG.info("Saved {} cars", carRepository.count());
    }

    public ArrayList<Car> createCars() {
        var cars = new ArrayList<Car>();

        for (int i = 0; i < 10_000; i++) {
            cars.add(carService.generateCar());
        }

        return cars;

    }

}
