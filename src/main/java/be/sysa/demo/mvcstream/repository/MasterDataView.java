package be.sysa.demo.mvcstream.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.UUID;

public interface MasterDataView {
    @JsonIgnore
    UUID getIdentifier();
    @JsonIgnore
    Timestamp getLastUpdateTimestamp();
    @JsonIgnore
    int getVersion();

}
