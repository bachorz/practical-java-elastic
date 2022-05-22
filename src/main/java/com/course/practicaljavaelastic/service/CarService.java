package com.course.practicaljavaelastic.service;

import com.course.practicaljavaelastic.entity.Car;

import java.util.List;

public interface CarService {

    List<String> BRANDS = List.of("Toyota", "VW", "Skoda", "Honda", "Bmw", "Seat");
    List<String> COLORS = List.of("Red", "Green", "Blue", "Black", "Gold", "Silver");
    List<String> TYPES = List.of("Sedan", "SUV", "MPV", "Variant", "Hatchback", "Convertible");
    List<String> ADDITIONAL_FEATURES = List.of("gps", "roof window", "big screen", "roof bag", "alarm", "media player");
    List<String> FUELS = List.of("Petrol", "Electric", "Hybrid");
    List<String> TIRE_MANUFACTURES = List.of("Goodyear", "Bridgestone", "Dunlop");

    Car generateCar();

}
