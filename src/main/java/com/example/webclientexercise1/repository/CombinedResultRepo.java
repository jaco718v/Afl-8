package com.example.webclientexercise1.repository;

import com.example.webclientexercise1.entity.CombinedResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CombinedResultRepo extends JpaRepository<CombinedResult,String> {

  CombinedResult findCombinedResultByName(String name);
}
