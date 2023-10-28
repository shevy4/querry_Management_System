package com.query.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.query.server.entity.StudentAdvisor;

@Repository
public interface StudentAdvisorRepository extends JpaRepository<StudentAdvisor, String> {

    StudentAdvisor findByAdvisorName(String advisorName);

}
