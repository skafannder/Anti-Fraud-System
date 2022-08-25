package antifraud.repository;

import antifraud.model.Card;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends PagingAndSortingRepository<Card, Long> {

    Optional<Card> findByNumber(String number);

    List<Card> findAll();

    default boolean existsByNumber(String number) {
        return findByNumber(number).isPresent();
    }

}