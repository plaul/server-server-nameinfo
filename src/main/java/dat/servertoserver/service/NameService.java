package dat.servertoserver.service;


import dat.servertoserver.dtos.AgeResponse;
import dat.servertoserver.dtos.GenderResponse;
import dat.servertoserver.dtos.NameInfoResponse;
import dat.servertoserver.dtos.NationalityResponse;
import dat.servertoserver.entity.NameInfo;
import dat.servertoserver.repository.NameInfoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class NameService {

  final static String GENDERIZE = "https://api.genderize.io?name=";
  final static String AGIFY = "https://api.agify.io?name=";
  final static String NATIONALITY = "https://api.nationalize.io?name=";

  NameInfoRepository nameInfoRepository;

  public NameService(NameInfoRepository nameInfoRepository) {
    this.nameInfoRepository = nameInfoRepository;
  }

  public NameInfoResponse getNameResponse(String name) {

    long start = System.currentTimeMillis();
    long end;
    NameInfo nameInfo = nameInfoRepository.findById(name).orElse(null);
    if (nameInfo != null) {
      NameInfoResponse response = new NameInfoResponse(nameInfo);
      response.setCashedResponse(true);
      end = System.currentTimeMillis();
      response.setTimeSpent(end - start);
      return response;
    }

    WebClient client = WebClient.create();
    Mono<AgeResponse> age = client.get()
            .uri(AGIFY + name)
            .retrieve()
            .bodyToMono(AgeResponse.class);

    Mono<GenderResponse> gender = client.get()
            .uri(GENDERIZE + name)
            .retrieve()
            .bodyToMono(GenderResponse.class);

    Mono<NationalityResponse> nationality = client.get()
            .uri(NATIONALITY + name)
            .retrieve()
            .bodyToMono(NationalityResponse.class);

    Mono<NameInfoResponse> res = Mono.zip(age, gender, nationality)
            .map(tuple -> new NameInfoResponse(tuple.getT1(), tuple.getT2(), tuple.getT3()))
            .onErrorResume(error -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not locate NameInfo for name: " + name)));

    NameInfoResponse nameInfoResponse = res.block();
    nameInfoRepository.save(new NameInfo(nameInfoResponse));
    nameInfoResponse.setCashedResponse(false);
    end = System.currentTimeMillis();
    nameInfoResponse.setTimeSpent(end - start);
    return nameInfoResponse;
  }

}
