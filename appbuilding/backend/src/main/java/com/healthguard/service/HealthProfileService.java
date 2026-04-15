package com.healthguard.service;

import com.healthguard.model.HealthProfile;
import com.healthguard.repository.HealthProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HealthProfileService {
    @Autowired
    private HealthProfileRepository healthProfileRepository;

    public HealthProfile submitHealthProfile(String userId, HealthProfile profileData) {
        HealthProfile profile = healthProfileRepository.findByUserId(userId).orElse(new HealthProfile());
        if (profile.getProfileId() == null) {
            profile.setProfileId(UUID.randomUUID().toString());
            profile.setUserId(userId);
        }
        profile.setHasHypertension(profileData.isHasHypertension());
        profile.setHasHeartDisease(profileData.isHasHeartDisease());
        profile.setHasAnxiety(profileData.isHasAnxiety());
        profile.setHasSleepDisorder(profileData.isHasSleepDisorder());
        profile.setUsualSleepTime(profileData.getUsualSleepTime());
        profile.setUsualWakeTime(profileData.getUsualWakeTime());
        profile.setSensitivityLevel(profileData.getSensitivityLevel());
        profile.setExtraNotes(profileData.getExtraNotes());
        return healthProfileRepository.save(profile);
    }

    public HealthProfile getHealthProfile(String userId) {
        return healthProfileRepository.findByUserId(userId).orElse(null);
    }
}