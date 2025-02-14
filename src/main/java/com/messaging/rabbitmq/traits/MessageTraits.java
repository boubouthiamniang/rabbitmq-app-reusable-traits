package com.messaging.rabbitmq.traits;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Map;

import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.xml.XmlMapper;
import com.google.protobuf.Message;
import com.rabbitmq.client.AMQP;

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
        
        // Use SpecificDatumWriter instead of DatumWriter
        DatumWriter<T> writer = new SpecificDatumWriter<>(avroObj.getSchema());
        
        Encoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
        writer.write(avroObj, encoder);
        encoder.flush();
        
        return outputStream.toByteArray();
    }

    public static AMQP.BasicProperties createMessageProperties(
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

        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();

        if (contentType != null) builder.contentType(contentType);
        if (contentEncoding != null) builder.contentEncoding(contentEncoding);
        if (headers != null) builder.headers(headers);
        if (deliveryMode != null) builder.deliveryMode(deliveryMode);
        if (priority != null) builder.priority(priority);
        if (correlationId != null) builder.correlationId(correlationId);
        if (replyTo != null) builder.replyTo(replyTo);
        if (expiration != null) builder.expiration(expiration);
        if (messageId != null) builder.messageId(messageId);
        if (timestamp != null) builder.timestamp(timestamp);
        if (type != null) builder.type(type);
        if (userId != null) builder.userId(userId);
        if (appId != null) builder.appId(appId);
        if (clusterId != null) builder.clusterId(clusterId);

        return builder.build();
    }
}
