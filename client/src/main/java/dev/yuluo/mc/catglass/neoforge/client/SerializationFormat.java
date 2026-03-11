package dev.yuluo.mc.catglass.neoforge.client;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;
import java.util.function.Function;

public enum SerializationFormat {
    HEX_STRING(s -> HexFormat.of().parseHex(s), b -> HexFormat.of().formatHex(b)),
    BASE64(s -> Base64.getDecoder().decode(s), b -> Base64.getEncoder().encodeToString(b)),
    UTF8(s -> s.getBytes(StandardCharsets.UTF_8), b -> new String(b, StandardCharsets.UTF_8)),
    ;

    private final Function<String, byte[]> serializer;
    private final Function<byte[], String> deserializer;

    SerializationFormat(Function<String, byte[]> serializer, Function<byte[], String> deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public byte[] serialize(String value) {
        return serializer.apply(value);
    }

    public String deserialize(byte[] value) {
        return deserializer.apply(value);
    }
}
