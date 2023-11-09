package dat.servertoserver.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class NationalityResponse{
  public ArrayList<Country> country;
  public String name;


}