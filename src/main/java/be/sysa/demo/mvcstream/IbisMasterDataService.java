package be.sysa.demo.mvcstream;

import be.sysa.demo.mvcstream.repository.IbisLegalEntityView;
import be.sysa.demo.mvcstream.repository.LegalEntityRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.time.Instant;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class IbisMasterDataService {
    LegalEntityRepository  legalEntityRepository;

    @SneakyThrows
    @Transactional(readOnly = true)
    public void streamLegalEntities(OutputStream out, ObjectMapper objectMapper, Instant after){

        // Streams using Hibernate ScrollableResults so we don;t get all results loaded at once.
        try (Stream<IbisLegalEntityView> legalEntityView = legalEntityRepository.findByLastUpdateTimestampAfter(after)) {
            JsonFactory factory = new JsonFactory();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
            JsonGenerator generator = factory.createGenerator(out, JsonEncoding.UTF8);
            generator.setCodec(objectMapper);
            generator.writeStartArray();
            legalEntityView.forEach(entity->{
                writeEntity(generator, entity);
            });
            generator.writeEndArray();
            generator.close();
        }
    }

    @SneakyThrows
    private void writeEntity(JsonGenerator generator, IbisLegalEntityView entity) {
        generator.writeStartObject();
        generator.writeStringField("status", "current");
        generator.writeStringField("identifier", entity.getIdentifier().toString());
        generator.writeStringField("lastChangeTimestamp", entity.getLastUpdateTimestamp().toString() ); // TODO format
        generator.writeNumberField("version", entity.getVersion() );
        generator.writeObjectField("payload", entity );
        generator.writeEndObject();
    }

}
