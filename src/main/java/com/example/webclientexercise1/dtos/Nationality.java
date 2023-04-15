package com.example.webclientexercise1.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class Nationality {
  private ArrayList<SingleCountry> country;

  private String name;
}
