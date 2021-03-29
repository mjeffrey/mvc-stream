package be.sysa.demo.mvcstream.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.Instant;
import java.util.stream.Stream;

@NoRepositoryBean
public interface MasterDataRepository<T,V extends MasterDataView> extends JpaRepository<T, Long> {
    Stream<V> findByLastUpdateTimestampAfter(Instant timestamp);
}
