package be.sysa.demo.mvcstream.repository;

import be.sysa.demo.mvcstream.model.LegalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.stream.Stream;

@Repository
public interface LegalEntityRepository extends JpaRepository<LegalEntity, Long> {
    Stream<IbisLegalEntityView> findByLastUpdateTimestampAfter(Instant timestamp);
}
