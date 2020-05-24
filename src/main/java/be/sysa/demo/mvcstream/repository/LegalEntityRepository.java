package be.sysa.demo.mvcstream.repository;

import be.sysa.demo.mvcstream.model.LegalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
public interface LegalEntityRepository extends JpaRepository<LegalEntity, Long> {
//    @Query("select l from LegalEntity l left join Account a on l = a.owner")
    @Transactional( readOnly = true )
    List<IbisLegalEntityView> findByLastUpdateTimestampAfter(Instant timestamp);
}
