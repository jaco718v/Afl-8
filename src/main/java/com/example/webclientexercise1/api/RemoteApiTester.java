package com.example.webclientexercise1.api;

import com.example.webclientexercise1.dtos.Age;
import com.example.webclientexercise1.dtos.Gender;
import com.example.webclientexercise1.dtos.MyCombinedResult;
import com.example.webclientexercise1.dtos.Nationality;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RemoteApiTester implements CommandLineRunner {

  private Mono<String> callSlowEndpoint(){
    Mono<String> slowResponse = WebClient.create()
        .get()
        .uri("http://localhost:8080/random-string-slow")
        .retrieve()
        .bodyToMono(String.class)
        .doOnError(e-> System.out.println("UUUPS : "+e.getMessage()));
    return slowResponse;
  }

  public void callSlowEndpointBlocking(){
    long start = System.currentTimeMillis();
    List<String> ramdomStrings = new ArrayList<>();

    Mono<String> slowResponse = callSlowEndpoint();
    ramdomStrings.add(slowResponse.block()); //Three seconds spent

    slowResponse = callSlowEndpoint();
    ramdomStrings.add(slowResponse.block());//Three seconds spent

    slowResponse = callSlowEndpoint();
    ramdomStrings.add(slowResponse.block());//Three seconds spent
    long end = System.currentTimeMillis();
    ramdomStrings. add(0,"Time spent BLOCKING (ms): "+(end-start));

    System.out.println(ramdomStrings.stream().collect(Collectors.joining(",")));
  }


  public void callSlowEndpointNonBlocking(){
    long start = System.currentTimeMillis();
    Mono<String> sr1 = callSlowEndpoint();
    Mono<String> sr2 = callSlowEndpoint();
    Mono<String> sr3 = callSlowEndpoint();

    var rs = Mono.zip(sr1,sr2,sr3).map(t-> {
      List<String> randomStrings = new ArrayList<>();
      randomStrings.add(t.getT1());
      randomStrings.add(t.getT2());
      randomStrings.add(t.getT3());
      long end = System.currentTimeMillis();
      randomStrings.add(0,"Time spent NON-BLOCKING (ms): "+(end-start));
      return randomStrings;
    });
    List<String> randoms = rs.block(); //We only block when all the three Mono's has fulfilled
    System.out.println(randoms.stream().collect(Collectors.joining(",")));
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

  List<String> names = Arrays.asList("lars", "peter", "sanne", "kim", "david", "maja");


  public void getGendersBlocking() {
    long start = System.currentTimeMillis();
    List<Gender> genders = names.stream().map(name -> getGenderForName(name).block()).toList();
    long end = System.currentTimeMillis();
    System.out.println("Time for six external requests, BLOCKING: "+ (end-start));
  }

  public void getGendersNonBlocking() {
    long start = System.currentTimeMillis();
    var genders = names.stream().map(name -> getGenderForName(name)).toList();
    Flux<Gender> flux = Flux.merge(Flux.concat(genders));
    List<Gender> res = flux.collectList().block();
    long end = System.currentTimeMillis();
    System.out.println("Time for six external requests, NON-BLOCKING: "+ (end-start));
  }

  public MyCombinedResult getCombinedResponse(String name){
    Mono<Age> age = getAgeForName(name);
    Mono<Gender> gender = getGenderForName(name);
    Mono<Nationality> nation = getNationalityForName(name);

    return Mono.zip(age,gender,nation)
        .map(n -> new MyCombinedResult(name,n.getT1(),n.getT2(),n.getT3())).block();


  }




  @Override
  public void run(String... args) throws Exception {
    //String randomStr = callSlowEndpoint().block();
    //System.out.println(randomStr);

    //callSlowEndpointBlocking();

    //callSlowEndpointNonBlocking();

    //System.out.println(getGenderForName("Lars").block().getGender());

    //getGendersBlocking();

    //getGendersNonBlocking();


  }
}

