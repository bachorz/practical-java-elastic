package com.course.practicaljavaelastic.service;

import org.springframework.stereotype.Service;

@Service
public class DefaultCarPromotionType implements CarPromotionService {
    @Override
    public boolean isValidPromotionType(String promotionType) {
        return PROMOTTION_TYPES.contains(promotionType.toLowerCase());
    }
}
