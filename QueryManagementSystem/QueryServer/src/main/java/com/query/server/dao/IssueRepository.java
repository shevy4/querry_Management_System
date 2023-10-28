package com.query.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.query.server.entity.Issue;

public interface IssueRepository extends JpaRepository<Issue, Integer> {

    Issue[] findAllByStudentStudentId(String clientId);

    Issue[] findAllByAdvisorAdvisorId(String clientId);

}
