package com.demo.helper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.util.Objects;

@Component
public class TestHelper {

    public JSONObject getJsonObjectFromFile(String filePath) throws Exception {
        JSONParser jsonParser = new JSONParser();
        return (JSONObject) jsonParser.parse(new FileReader(getFileFromResources(filePath)));
    }

    private File getFileFromResources(String path) {
        return new File(Objects.requireNonNull(getClass().getClassLoader().getResource(path)).getFile());
    }
}
