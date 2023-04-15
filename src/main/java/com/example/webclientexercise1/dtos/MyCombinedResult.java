package com.example.webclientexercise1.dtos;

import com.example.webclientexercise1.entity.CombinedResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyCombinedResult {

  private String name;
  private String gender;

  private int age;

  private String country_id;

  private double probability;




  public MyCombinedResult(CombinedResult c){
    this.name = c.getName();
    this.gender = c.getGender();
    this.age = c.getAge();
    this.country_id = c.getCountry_id();
    this.probability = c.getProbability();
  }
  public MyCombinedResult(String name, Age a,  Gender g, Nationality n) {
    this.name = name;
    this.gender = g.getGender();
    this.age = a.getAge();
    this.country_id = n.getCountry().get(0).getCountry_id();
    this.probability = n.getCountry().get(0).getProbability();
  }
}
