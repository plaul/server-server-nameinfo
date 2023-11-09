package dat.servertoserver.entity;

import dat.servertoserver.dtos.NameInfoResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class NameInfo{
  @Id
  String name;

  String gender;
  double genderProbability;
  int age;
  int ageCount;
  String country;
  double countryProbability;

  public NameInfo(NameInfoResponse nr) {
    this.name = nr.getName();
    this.age = nr.getAge();
    this.ageCount = nr.getAgeCount();
    this.gender = nr.getGender();
    this.genderProbability = nr.getGenderProbability();
    this.country = nr.getCountry();
    this.countryProbability = nr.getCountryProbability();
  }

  @CreationTimestamp
  LocalDateTime created;
  @UpdateTimestamp
  LocalDateTime edited;

}
