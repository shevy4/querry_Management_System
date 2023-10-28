package com.query.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.query.server.entity.StudentSupervisor;

public interface SupervisorRepository extends JpaRepository<StudentSupervisor, String> {

}
