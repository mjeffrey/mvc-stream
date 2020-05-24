package be.sysa.demo.mvcstream.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor
@ToString
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(updatable = false)
    protected UUID identifier;

    @Column
    protected Instant lastUpdateTimestamp;

//    @ManyToOne
//    private LegalEntity owner;

    @Column
    private String iban;

    @Column
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column
    private String currencyCode;

    @Version
    private int version;

    @Builder
    public Account(String iban, AccountType accountType, String currencyCode) {
        this.iban = iban;
        this.accountType = accountType;
        this.currencyCode = currencyCode;
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

