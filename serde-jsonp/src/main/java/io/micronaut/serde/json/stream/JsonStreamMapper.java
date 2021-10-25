package io.micronaut.serde.json.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.function.Consumer;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.type.Argument;
import io.micronaut.jackson.databind.JacksonDatabindMapper;
import io.micronaut.json.JsonMapper;
import io.micronaut.json.JsonStreamConfig;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.serde.Deserializer;
import io.micronaut.serde.SerdeRegistry;
import io.micronaut.serde.Serializer;
import jakarta.inject.Singleton;
import jakarta.json.Json;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;
import org.reactivestreams.Processor;

@Singleton
@Replaces(JacksonDatabindMapper.class)
public class JsonStreamMapper implements JsonMapper {
    private final SerdeRegistry registry;

    public JsonStreamMapper(SerdeRegistry registry) {
        this.registry = registry;
    }

    @Override
    public <T> T readValueFromTree(JsonNode tree, Argument<T> type) throws IOException {
        return null;
    }

    @Override
    public <T> T readValue(InputStream inputStream, Argument<T> type) throws IOException {
        return null;
    }

    @Override
    public <T> T readValue(byte[] byteArray, Argument<T> type) throws IOException {
        final Deserializer<? extends T> deserializer = this.registry.findDeserializer(type);
        final JsonParser parser = Json.createParser(new ByteArrayInputStream(byteArray));
        try {
            return deserializer.deserialize(
                    new JsonParserDecoder(parser),
                    registry,
                    type
            );
        } finally {
            parser.close();
        }
    }

    @Override
    public Processor<byte[], JsonNode> createReactiveParser(Consumer<Processor<byte[], JsonNode>> onSubscribe,
                                                            boolean streamArray) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public JsonNode writeValueToTree(Object value) throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void writeValue(OutputStream outputStream, Object object) throws IOException {
        final JsonGenerator generator = Json.createGenerator(
                Objects.requireNonNull(outputStream, "Output stream cannot be null")
        );
        if (object == null) {
            try {
                generator.writeNull();
                generator.flush();
            } finally {
                generator.close();
            }
        } else {
            try {
                @SuppressWarnings("unchecked")
                final Argument type = Argument.of(object.getClass());
                final Serializer<Object> serializer = registry.findSerializer(type);
                JsonStreamEncoder encoder = new JsonStreamEncoder(generator);
                serializer.serialize(
                        encoder,
                        registry,
                        object,
                        type
                );
                generator.flush();
            } finally {
                generator.close();
            }
        }
    }

    @Override
    public byte[] writeValueAsBytes(Object object) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        writeValue(output, object);
        return output.toByteArray();
    }

    @Override
    public JsonStreamConfig getStreamConfig() {
        return null;
    }
}
