package com.user.app.commons.utils;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.user.app.Model.CsvData;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadCsvFile {

    public static List<CsvData> readCSV(String fileName, String courseId) throws IOException {
        // Create an object of filereader
        // class with CSV file as a parameter.
        FileReader filereader = new FileReader(fileName);
//            FileReader filereader = new FileReader("C:\\Users\\Dell1\\Music\\csv1.csv");

        // create csvReader object passing
        // file reader as a parameter
        // create csvReader object and skip first Line
        CSVReader csvReader = new CSVReaderBuilder(filereader)
                .withSkipLines(1)
                .build();
        List<String[]> allData = csvReader.readAll();
//        List<String> list = new ArrayList<>();
        List<CsvData> CsvDataList = new ArrayList<>();
        // print Data
        for (String[] row : allData) {
            for (int i = 0; i <= row.length; i++) {
                if (i % 6 == 0 && i > 0) {
                    if (row[i - 6] != null && !row[i - 6].equals(""))
                        CsvDataList.add(new CsvData(courseId,
                                row[i - 6].replaceAll("[^\\x00-\\x7F]", "")
                                        .replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "")
                                        .replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", ""),
                                row[i - 5], row[i - 4], row[i - 3],
                                row[i - 2], row[i - 1]));
                }
            }
//            list.add(Arrays.toString(row));
        }
        return CsvDataList;
    }

    public int countOdds(int low, int high) {
        int count = 0;
        for (int i = 0; i <= high; i++) {
            if (i >= low && i % 2 == 0) count++;
        }
        return count;
    }

}

