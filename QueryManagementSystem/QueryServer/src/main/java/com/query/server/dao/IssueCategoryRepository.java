package com.query.server.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.query.server.entity.IssueCategory;

@Repository
public interface IssueCategoryRepository extends JpaRepository<IssueCategory, Integer> {

    List<IssueCategory> findByType(String type);

}
