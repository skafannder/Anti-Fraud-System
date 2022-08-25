package antifraud.repository;


import antifraud.model.Ip;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IpRepository extends PagingAndSortingRepository<Ip, Long> {

    Optional<Ip> findByIp(String ip);

    List<Ip> findAll();

    default boolean existsByIp(String ip){
        return findByIp(ip).isPresent();
    }

}