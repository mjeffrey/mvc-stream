package be.sysa.demo.mvcstream.repository;

import be.sysa.demo.mvcstream.model.LegalEntity;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.stream.Stream;

@Repository
public interface LegalEntityRepository extends MasterDataRepository<LegalEntity, IbisLegalEntityView> {
    Stream<IbisLegalEntityView> findByLastUpdateTimestampAfter(Instant timestamp);
}
