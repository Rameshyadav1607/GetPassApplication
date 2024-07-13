package com.indianoil.VisitorManagement.repository;

import com.indianoil.VisitorManagement.entity.AdminEntity;
import com.indianoil.VisitorManagement.entity.SettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<SettingsEntity, String> {
}
