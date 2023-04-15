package com.example.webclientexercise1.service;

import com.example.webclientexercise1.dtos.Age;
import com.example.webclientexercise1.dtos.Gender;
import com.example.webclientexercise1.dtos.MyCombinedResult;
import com.example.webclientexercise1.dtos.Nationality;
import com.example.webclientexercise1.entity.CombinedResult;
import com.example.webclientexercise1.repository.CombinedResultRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MomondoLikeService {

  CombinedResultRepo combinedResultRepo;
  public MomondoLikeService(CombinedResultRepo combinedResultRepo){
    this.combinedResultRepo=combinedResultRepo;
  }

  Mono<Gender> getGenderForName(String name) {
    WebClient client = WebClient.create();
    Mono<Gender> gender = client.get()
        .uri("https://api.genderize.io?name="+name)
        .retrieve()
        .bodyToMono(Gender.class);
    return gender;
  }

  Mono<Age> getAgeForName(String name) {
    WebClient client = WebClient.create();
    Mono<Age> age = client.get()
        .uri("https://api.agify.io?name="+name)
        .retrieve()
        .bodyToMono(Age.class);
    return age;
  }

  Mono<Nationality> getNationalityForName(String name) { // Might need flux
    WebClient client = WebClient.create();
    Mono<Nationality> nationality = client.get()
        .uri("https://api.nationalize.io?name="+name)
        .retrieve()
        .bodyToMono(Nationality.class);
    return nationality;
  }

  public MyCombinedResult getCombinedResponse(String name){
    long start = System.currentTimeMillis();
    if(combinedResultRepo.existsById(name)){
      long end = System.currentTimeMillis();
      System.out.println("Time for request "+ (end-start));
      return new MyCombinedResult(combinedResultRepo.findCombinedResultByName(name));
    }

    Mono<Age> age = getAgeForName(name);
    Mono<Gender> gender = getGenderForName(name);
    Mono<Nationality> nation = getNationalityForName(name);

    MyCombinedResult result = Mono.zip(age,gender,nation)
        .map(n -> new MyCombinedResult(name,n.getT1(),n.getT2(),n.getT3())).block();

    if(result != null){
      combinedResultRepo.save(new CombinedResult(result));
    }
    long end = System.currentTimeMillis();
    System.out.println("Time for request "+ (end-start));

    return result;
  }

}
