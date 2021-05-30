package com.geektrust.famliy;

import com.geektrust.famliy.service.FamilyService;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Geektrust {
    public static void main(String[] args) {
        try {
            System.out.println("-------------------Process Started-------------------");
            FamilyService familyService = new FamilyService();
            if (args.length > 0) {
                String path = args[0];
                System.out.println("Input Path: " + path);
                Path file = Paths.get(path);
                List<String> inputs = Files.readAllLines(file);
                List<String> outputs = new ArrayList<>();
                inputs.forEach(i -> {
                    String[] ins = i.split(" ");
                    if (ins[0].trim().equalsIgnoreCase("ADD_CHILD")) {
                        outputs.add(familyService.addChild(ins[1].trim(), ins[2].trim(), ins[3].trim()));
                    } else if (ins[0].trim().equalsIgnoreCase("GET_RELATIONSHIP")) {
                        outputs.add(familyService.getRelationship(ins[1].trim(), ins[2].trim()));
                    }
                });
                String outPath = path.replace("input", "output");
                System.out.println("Output Path: " + outPath);
                if (outputs.size() > 0) {
                    FileWriter writer = new FileWriter(outPath);
                    outputs.forEach(o -> {
                        try {
                            writer.write(o + System.lineSeparator());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    writer.close();
                }
            }
            System.out.println("-------------------Process Completed-------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
