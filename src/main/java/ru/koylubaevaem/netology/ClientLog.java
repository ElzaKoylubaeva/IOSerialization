package ru.koylubaevaem.netology;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {

    private final List<String[]> logs = new ArrayList<>();

    public void log(int productNum, int amount) {
        String[] data = new String[2];
        data[0] = String.valueOf(productNum);
        data[1] = String.valueOf(amount);
        logs.add(data);
    }

    public void exportAsCSV(File file) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            writer.writeAll(logs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
