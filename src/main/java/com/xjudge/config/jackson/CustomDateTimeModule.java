//package com.xjudge.config.jackson;
//
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import com.xjudge.util.deserializer.DurationDeserializer;
//import com.xjudge.util.deserializer.InstantDeserializer;
//import com.xjudge.util.serializer.DurationSerializer;
//import com.xjudge.util.serializer.InstantSerializer;
//
//import java.time.Duration;
//import java.time.Instant;
//
//public class CustomDateTimeModule extends SimpleModule {
//    public CustomDateTimeModule() {
//        addSerializer(Instant.class, new InstantSerializer());
//        addSerializer(Duration.class, new DurationSerializer());
//        addDeserializer(Instant.class, new InstantDeserializer());
//        addDeserializer(Duration.class, new DurationDeserializer());
//    }
//}