package dat.servertoserver;

import dat.servertoserver.dtos.GenderResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Configuration
public class RemoteApiTester implements CommandLineRunner {

  private Mono<String> callSlowEndpoint() {
    Mono<String> slowResponse = WebClient.create()
            .get()
            .uri("http://localhost:8080/random-string-slow")
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(e -> System.out.println("UPPPS: " + e.getMessage()));
    return slowResponse;
  }

  public void callSlowEndpointBlocking() {
    long start = System.currentTimeMillis();
    List<String> randomStrings = new ArrayList<>();
    Mono<String> slowResponse = callSlowEndpoint();
    randomStrings.add(slowResponse.block());

    slowResponse = callSlowEndpoint();
    randomStrings.add(slowResponse.block());

    slowResponse = callSlowEndpoint();
    randomStrings.add(slowResponse.block());

    long end = System.currentTimeMillis();
    randomStrings.add(0, "Time spent BLOCKING (ms): " + (end - start));
    System.out.println(randomStrings.stream().collect(Collectors.joining(",")));
  }

  public void callSlowEndpointNonBlocking() {
    long start = System.currentTimeMillis();
    Mono<String> sr1 = callSlowEndpoint();
    Mono<String> sr2 = callSlowEndpoint();
    Mono<String> sr3 = callSlowEndpoint();
    var rs = Mono.zip(sr1, sr2, sr3).map(tuple3 -> {
      List<String> randomStrings = new ArrayList<>();
      randomStrings.add(tuple3.getT1());
      randomStrings.add(tuple3.getT2());
      randomStrings.add(tuple3.getT3());
      long end = System.currentTimeMillis();
      randomStrings.add(0, "Time Spent NON BLOCKING: " + (end - start));
      System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXX");
      return randomStrings;
    });
    List<String> randoms = rs.block();
    System.out.println(randoms.stream().collect(Collectors.joining(",")));
  }

  public Mono<GenderResponse> getGenderForName(String name){
    WebClient webClient = WebClient.create();
    Mono<GenderResponse> gender = webClient.get()
            .uri("https://api.genderize.io?name="+name)
            .retrieve()
            .bodyToMono(GenderResponse.class);
    return gender;

  }

  List<String> names = Arrays.asList("Lars","Peter","Sanne", "Kim","David","Maja");
  public void getGendersBlocking(){
    long start = System.currentTimeMillis();
    List<GenderResponse> genders = names.stream().map(name ->
            getGenderForName(name).block()).toList();
    long end = System.currentTimeMillis();
    System.out.println("Time spent on six external requests: "+ (end-start));
  }

  public void getGendersNonBlocking(){
    long start = System.currentTimeMillis();
    var genders = names.stream().map(name -> getGenderForName(name)).toList();
    Flux<GenderResponse> flux = Flux.merge(Flux.concat(genders));
    List<GenderResponse> res = flux.collectList().block();
    long end = System.currentTimeMillis();
    System.out.println("Time for six external requests, NON-BLOCKING: "+(end-start));
  }

  @Override
  public void run(String... args) throws Exception {
//    String randomString = callSlowEndpoint().block();
//    System.out.println(randomString);
//    callSlowEndpointBlocking();
    callSlowEndpointNonBlocking();

//    String name = "Kim";
//    Gender gender = getGenderForName(name).block();
//    System.out.println(name +" is probably "+gender.getGender());

//    getGendersBlocking();
//    getGendersNonBlocking();

  }
}
