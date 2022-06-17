package com.yesmarketing.ptsacs.common.configuration;

import static com.yesmarketing.ptsacs.services.util.ServicesConstants.ACS_DATETIME_PATTERN;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.joda.cfg.JacksonJodaDateFormat;
import com.fasterxml.jackson.datatype.joda.deser.DateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ACS_DATETIME_PATTERN);
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(formatter);
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(formatter);
        SimpleDateFormat dateFormat = new SimpleDateFormat(ACS_DATETIME_PATTERN);
        return new ObjectMapper()
                .registerModule(configureJodaTime())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule()
                        .addDeserializer(LocalDateTime.class, localDateTimeDeserializer)
                        .addSerializer(LocalDateTime.class, localDateTimeSerializer))
                .setDateFormat(dateFormat)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private JodaModule configureJodaTime() {

        org.joda.time.format.DateTimeFormatter jodaFormatter = DateTimeFormat.forPattern(ACS_DATETIME_PATTERN);
        JacksonJodaDateFormat jacksonJodaDateFormat = new JacksonJodaDateFormat(jodaFormatter);
        DateTimeDeserializer jodaDateTimeDeserializer = new DateTimeDeserializer(DateTime.class, jacksonJodaDateFormat);
        AcsJodaDateTimeSerializer jodaDateTimeSerializer = new AcsJodaDateTimeSerializer();
        JodaModule jodaModule = new JodaModule();
        jodaModule.addDeserializer(ReadableInstant.class, jodaDateTimeDeserializer);
        jodaModule.addSerializer(DateTime.class, jodaDateTimeSerializer);
        return jodaModule;
    }

    public static class AcsJodaDateTimeSerializer extends StdScalarSerializer<DateTime> {

        private static final long serialVersionUID = 3867542185050464472L;

        public AcsJodaDateTimeSerializer() { super(DateTime.class); }

        @Override
        public void serialize(DateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            org.joda.time.format.DateTimeFormatter jodaFormatter = DateTimeFormat.forPattern(ACS_DATETIME_PATTERN);
            String dateAsString = value.toString(jodaFormatter);
            gen.writeString(dateAsString);
        }
    }
}
