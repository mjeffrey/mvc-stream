package be.sysa.demo.mvcstream;

import be.sysa.demo.mvcstream.model.Account;
import be.sysa.demo.mvcstream.model.AccountType;
import be.sysa.demo.mvcstream.model.LegalEntity;
import be.sysa.demo.mvcstream.repository.LegalEntityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@AllArgsConstructor
@RestController
@Slf4j
public class IbisLegalEntityController {

    private ObjectMapper objectMapper;
    private IbisMasterDataService masterData;

    @GetMapping(value = "/ibis/legal-entity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StreamingResponseBody> ibisLegalEntity() throws Exception {
        Instant after = Instant.now().minus(1000, ChronoUnit.DAYS); // TODO get from request
        StreamingResponseBody stream = out -> {
            ObjectMapper objectMapper = this.objectMapper.copy();
            masterData.streamLegalEntities(out, objectMapper, after);
        };
        return new ResponseEntity<>(stream, HttpStatus.OK);
    }

    // Below this just for generating some data
    private LegalEntityRepository legalEntityRepository;
    @PostMapping("/ibis/legal-entity/generate")
    @SneakyThrows
    @Deprecated(forRemoval = true, since="Only for testing")
    public void generateLegalEntities(@RequestParam(value = "count", defaultValue = "1") int count) {
        for (int i = 0; i < count; i++) {
            LegalEntity legalEntity = LegalEntity.builder()
                    .countryCode("BE")
                    .enterpriseNumber(enterpriseNumber())
                    .name("MYCOMPANY-" + randomAlphabetic(25))
                    .account(Account.builder().accountType(AccountType.PAYMENT).currencyCode("EUR").build())
                    .account(Account.builder().accountType(AccountType.RESERVE).currencyCode("EUR").iban(iban()).build())
                    .build();
            legalEntityRepository.save(legalEntity);
        }
        log.info("Generated {} legal entities", count);
    }


    private String iban() {
        return "BE" + randomNumeric(15);
    }

    private String enterpriseNumber() {
        return "BE" + randomNumeric(9);
    }
}
