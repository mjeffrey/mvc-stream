package be.sysa.demo.mvcstream.model;


import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "legal_entities")
@Getter
@NoArgsConstructor
@ToString
public class LegalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(updatable = false)
    protected UUID identifier;

    @Column
    protected Instant lastUpdateTimestamp;

    @Column
    private String name;

    @Column
    private String countryCode;

    @Column
    private String enterpriseNumber;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn( name = "owner_legal_entity_id", nullable = false, updatable = false)
    private Set<Account> accounts;

    @Version
    private int version;

    @Builder
    public LegalEntity(String name, String countryCode, String enterpriseNumber, @Singular Set<Account> accounts) {
        this.name = name;
        this.countryCode = countryCode;
        this.enterpriseNumber = enterpriseNumber;
        this.accounts = accounts;
    }

    @PrePersist
    public void insert() {
        if (identifier == null ){
            identifier = UUID.randomUUID();
        }
        lastUpdateTimestamp = Instant.now();
    }
    @PreUpdate
    public void update() {
        lastUpdateTimestamp = Instant.now();
    }

}
