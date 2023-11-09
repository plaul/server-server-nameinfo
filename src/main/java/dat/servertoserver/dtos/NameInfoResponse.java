package dat.servertoserver.dtos;

import dat.servertoserver.entity.NameInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
public class NameInfoResponse {
  String name;
  String gender;
  double genderProbability;
  int age;
  int ageCount;
  String country;
  boolean cashedResponse;
  Long timeSpent;
  double countryProbability;

  public NameInfoResponse(AgeResponse ageResponse, GenderResponse genderResponse, NationalityResponse nationalityResponse) {
//    if(ageResponse.count == 0 || genderResponse.count == 0 || nationalityResponse.country.isEmpty()){
//      throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No info found for name: "+ageResponse.name);
//    }
    this.name = ageResponse.getName();
    this.age = ageResponse.getAge();
    this.ageCount = ageResponse.getCount();
    this.gender = genderResponse.getGender();
    this.genderProbability = genderResponse.getProbability();
    this.country = nationalityResponse.getCountry().get(0).getCountry_id();
    this.countryProbability = nationalityResponse.getCountry().get(0).getProbability();
  }
  public NameInfoResponse(NameInfo nf) {
    this.name = nf.getName();
    this.age = nf.getAge();
    this.ageCount = nf.getAgeCount();
    this.gender = nf.getGender();
    this.genderProbability = nf.getGenderProbability();
    this.country = nf.getCountry();
    this.countryProbability = nf.getCountryProbability();
  }
}
