package com.example.webclientexercise1.entity;

import com.example.webclientexercise1.dtos.MyCombinedResult;
import com.example.webclientexercise1.dtos.SingleCountry;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class CombinedResult {
  @Id
  private String name;
  private String gender;

  private int age;

  private String country_id;

  private double probability;

  public CombinedResult(MyCombinedResult c) {
    this.name = c.getName();
    this.gender = c.getGender();
    this.age = c.getAge();
    this.country_id = c.getCountry_id();
    this.probability = c.getProbability();
  }

}
