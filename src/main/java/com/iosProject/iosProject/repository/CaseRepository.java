package com.iosProject.iosProject.repository;

import com.iosProject.iosProject.entity.CaseEntity;
import com.iosProject.iosProject.entity.ChildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CaseRepository extends JpaRepository<CaseEntity, Long>{



}
