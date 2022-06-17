package com.yesmarketing.ptsacs.services.resource;

import static com.yesmarketing.acsapi.model.AdobeCampaignConstants.ACS_DATETIME_REGEX;
import static com.yesmarketing.acsapi.model.AdobeCampaignConstants.ACS_DATE_REGEX;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.yesmarketing.ptsacs.common.configuration.JacksonConfiguration;
import lombok.Data;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class TestDateSerializationDeserialization {

    private ObjectMapper objectMapper;

    @Data
    static class TestDto {
        LocalDateTime localDateTime;

        LocalDate localDate;

        Date date;
    }

    @BeforeEach
    void setup() {
        objectMapper = new JacksonConfiguration().objectMapper();
    }

    @Nested
    class SerializationTests {
        @Test
        void serialize() throws JsonProcessingException {
            TestDto testDto = new TestDto();
            Date nowDate = new Date();
            testDto.setDate(nowDate);
            LocalDate localDate = LocalDate.now();
            testDto.setLocalDate(localDate);
            LocalDateTime nowLdt = LocalDateTime.now();
            testDto.setLocalDateTime(nowLdt);

            String jsonStr = objectMapper.writeValueAsString(testDto);

            String localDateTimeStr = JsonPath.read(jsonStr, "$.localDateTime");
            String localDateStr = JsonPath.read(jsonStr, "$.localDate");
            String dateStr = JsonPath.read(jsonStr, "$.date");
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(dateStr).as("Date").matches(ACS_DATETIME_REGEX);
                softly.assertThat(localDateStr).as("LocalDate").matches(ACS_DATE_REGEX);
                softly.assertThat(localDateTimeStr).as("LocalDateTime").matches(ACS_DATETIME_REGEX);
            });
        }
    }

    @Nested
    class DeserializationTests {
        @Test
        void deserialize() throws JsonProcessingException {
            String jsonStr = "{ \"date\": \"2020-09-21 00:00:00.000Z\", \"localDate\": \"2020-09-21\", " +
                "\"localDateTime\": \"2020-09-21 16:28:53.123Z\"}";
            TestDto testDto = objectMapper.readValue(jsonStr, TestDto.class);
            Date date = testDto.getDate();
            LocalDate localDate = testDto.getLocalDate();
            LocalDateTime localDateTime = testDto.getLocalDateTime();
            SoftAssertions.assertSoftly(softly -> {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                softly.assertThat(calendar.get(Calendar.YEAR)).as("Date year").isEqualTo(2020);
                softly.assertThat(calendar.get(Calendar.MONTH)).as("Date month").isEqualTo(Calendar.SEPTEMBER);
                softly.assertThat(calendar.get(Calendar.DATE)).as("Date day").isEqualTo(21);
                softly.assertThat(localDate).as("LocalDate").isEqualTo(LocalDate.of(2020, 9, 21));
                softly.assertThat(localDateTime).as("LocalDateTime").isEqualTo(LocalDateTime.of(2020,
                    9, 21, 16, 28, 53, 123000000));
            });
        }
    }

}
