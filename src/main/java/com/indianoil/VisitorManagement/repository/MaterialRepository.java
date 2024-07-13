package com.indianoil.VisitorManagement.repository;

import com.indianoil.VisitorManagement.entity.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<MaterialEntity,String> {
}
