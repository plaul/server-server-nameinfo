package dat.servertoserver.repository;

import dat.servertoserver.entity.NameInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NameInfoRepository extends JpaRepository<NameInfo,String> {
}
