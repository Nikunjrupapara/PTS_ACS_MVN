package com.yesmarketing.ptsacs.services.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesmarketing.acsapi.admin.model.YesmailProfileMetadata;
import com.yesmarketing.acsapi.model.ResourceMetadata;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SubscriberSagasTestHelper {

    public static ResourceMetadata getProfileMetadata(String company) throws Exception {
        String name = String.format("%s-profile-metadata", company);
        String json = loadFile("src", new String[] {"test", "resources", "json", "metadata"}, name);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper.readValue(json, ResourceMetadata.class);
    }

    private static String loadFile(String first, String[] more, String name) throws Exception {
        String json = "";
        Path resourceDir = Paths.get(first, more);
        String dirPath = resourceDir.toFile().getAbsolutePath();
        String filePath = String.format("%s/%s.json", dirPath, name);
        json = new String(Files.readAllBytes(Paths.get(filePath)));
        return json;
    }

    private SubscriberSagasTestHelper() {}
}
