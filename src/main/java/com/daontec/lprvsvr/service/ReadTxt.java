package com.daontec.lprvsvr.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadTxt {

    public static String readFileToString(String filePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = ReadTxt.class.getResourceAsStream(filePath)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }

    public static String readFileToString2(String filePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = new FileInputStream(filePath)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }
    public String result(){
        String filePath = "C:\\file.txt"; // JAR 파일과 같은 경로에 있는 파일의 상대 경로
//        String filePath = "/file.txt";
        try {
            return readFileToString2(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
