//package com.xjudge.util.deserializer;
//
//import com.fasterxml.jackson.core.JacksonException;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//
//import java.io.IOException;
//import java.time.Instant;
//
//public class InstantDeserializer extends JsonDeserializer<Instant> {
//    @Override
//    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
//        return Instant.ofEpochSecond(p.getLongValue());
//    }
//}