package com.course.practicaljavaelastic.service;

import java.util.List;


public interface CarPromotionService {

    List<String> PROMOTTION_TYPES = List.of("bonus", "discont");
    boolean isValidPromotionType(String promotionType);
}
