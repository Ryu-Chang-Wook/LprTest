package com.daontec.lprvsvr.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadText {
    private static final String BASE_PATH = new File("").getAbsolutePath();
    private static String RESOURCE_PATH = "/src/resource";
    private static String FILE_NAME = "/file.txt";
    private static String FILE_PATH;

    public String rawString(){

        FILE_PATH = BASE_PATH + RESOURCE_PATH + FILE_NAME;
        if (Files.exists(Paths.get(FILE_PATH))) {
            System.out.println("Run in IDE!");
        } else {
            FILE_PATH = BASE_PATH + FILE_NAME;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Cannot Read File!");
            e.printStackTrace();
        }
        return null;
    }

}
