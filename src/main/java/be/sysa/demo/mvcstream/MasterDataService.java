package be.sysa.demo.mvcstream;

import be.sysa.demo.mvcstream.repository.MasterDataRepository;
import be.sysa.demo.mvcstream.repository.MasterDataView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.time.Instant;
import java.util.stream.Stream;

@Service
public class MasterDataService {
    private ObjectMapper objectMapper;
    private JsonFactory factory = new JsonFactory();

    public MasterDataService(ObjectMapper globalObjectMapper) {
        this.objectMapper = globalObjectMapper.copy();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        JsonFactory factory = new JsonFactory();
        factory.setCodec(this.objectMapper);
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    public  <T, V extends MasterDataView> void streamLegalEntities(OutputStream out, MasterDataRepository<T,V> masterDataRepository, Instant after){

        // Streams using Hibernate ScrollableResults so we don;t get all results loaded at once.
        try (Stream<V> legalEntityView = masterDataRepository.findByLastUpdateTimestampAfter(after)) {
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
    private void writeEntity(JsonGenerator generator, MasterDataView entity) {
        generator.writeStartObject();
        generator.writeStringField("status", "current");
        generator.writeStringField("identifier", entity.getIdentifier().toString());
        generator.writeStringField("lastChangeTimestamp", entity.getLastUpdateTimestamp().toString() ); // TODO format
        generator.writeNumberField("version", entity.getVersion() );
        generator.writeObjectField("payload", entity );
        generator.writeEndObject();
    }

}
