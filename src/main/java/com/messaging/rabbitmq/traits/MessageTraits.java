package com.messaging.rabbitmq.traits;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.xml.XmlMapper;
import com.rabbitmq.client.BasicProperties;

public interface MessageTraits {

    /** Convert plain text to UTF-8 byte array **/
    default byte[] getBytesFromText(String data) {
        return data.getBytes(StandardCharsets.UTF_8);
    }

    /** Convert JSON or XML file content to UTF-8 byte array **/
    default byte[] getBytesFromXmlOrJsonPath(String fileLocation) throws IOException {
        String message = Files.readString(Path.of(fileLocation), StandardCharsets.UTF_8);
        return message.getBytes(StandardCharsets.UTF_8);
    }

    /** Convert a Java object to UTF-8 JSON byte array **/
    default byte[] getBytesFromJson(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj).getBytes(StandardCharsets.UTF_8);
    }

     /** Convert a Java object to UTF-8 XML byte array **/
     default byte[] getBytesFromXml(Object obj) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.writeValueAsString(obj).getBytes(StandardCharsets.UTF_8);
    }

    /** Convert a Protobuf object to byte array (binary format, no encoding needed) **/
    default <T extends Message> byte[] getBytesFromProtobuf(T protoObj) {
        return protoObj.toByteArray();
    }

    /** Convert an Avro object to byte array (binary format, no encoding needed) **/
    default <T extends SpecificRecord> byte[] getBytesFromAvro(T avroObj) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DatumWriter<T> writer = new SpecificDatumWriter<>(avroObj.getSchema());
        Encoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
        writer.write(avroObj, encoder);
        encoder.flush();
        return outputStream.toByteArray();
    }

    public static BasicProperties createMessageProperties(
        String contentType,
        String contentEncoding,
        Map<String, Object> headers,
        Integer deliveryMode,
        Integer priority,
        String correlationId,
        String replyTo,
        String expiration,
        String messageId,
        Date timestamp,
        String type,
        String userId,
        String appId,
        String clusterId) {
        
        return new BasicProperties.Builder()
            .contentType(contentType != null ? contentType : "text/plain")
            .contentEncoding(contentEncoding)
            .headers(headers)
            .deliveryMode(deliveryMode != null ? deliveryMode : 1)
            .priority(priority != null ? priority : 0)
            .correlationId(correlationId)
            .replyTo(replyTo)
            .expiration(expiration)
            .messageId(messageId)
            .timestamp(timestamp)
            .type(type)
            .userId(userId)
            .appId(appId)
            .clusterId(clusterId)
            .build();
    }
}
