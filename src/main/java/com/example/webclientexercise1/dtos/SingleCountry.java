package com.example.webclientexercise1.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SingleCountry {
  private String country_id;

  private double probability;
}
