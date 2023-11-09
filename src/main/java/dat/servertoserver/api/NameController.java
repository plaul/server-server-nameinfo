package dat.servertoserver.api;

import dat.servertoserver.service.NameService;
import dat.servertoserver.dtos.NameInfoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nameinfo")
public class NameController {

  NameService nameService;

  public NameController(NameService nameService) {
    this.nameService = nameService;
  }

  @GetMapping("/{name}")
  NameInfoResponse getNameInfo(@PathVariable String name){
    NameInfoResponse res = nameService.getNameResponse(name);
    return res;
  }
}
