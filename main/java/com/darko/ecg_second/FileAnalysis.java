package com.darko.ecg_second;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileAnalysis {
    private File file;

    public FileAnalysis(File file){
        this.file = file;
    }

    public String ECGvalidity(){
        Scanner input;
        boolean wrongFormat = false;
        int counter = 0, a1 = 0, b1 = 0, b2 = 0, b3 = 0, b4 = 0, b5 = 0, c1 = 0;

        try {
            input = new Scanner(file);
            String line;
            while (input.hasNext()) {

                float value, vStart = 0, vEnd = 0;
                boolean first = true;
                line = input.nextLine();
                counter++;

                if (!line.matches("^[-+]?[0-9]{1,}\\.?[0-9]+$")) {
                    wrongFormat = true;
                    break;
                }

                value = Float.parseFloat(line);
                if (first) {
                    vStart = vEnd = value;
                    first = false;
                }

                if (value >= 0 && value < 1.4)
                    b1++;
                else if (value >= 1.4 && value < 1.9)
                    b2++;
                else if (value >= 1.9 && value < 2.5)
                    b3++;
                else if (value >= 2.5 && value < 3.5)
                    b4++;
                else if (value >= 3.5 && value <= 5)
                    b5++;
                else
                    a1++;

                if (value >= vEnd)
                    vEnd = value;
                else {
                    if (vEnd - vStart >= 0.4)
                        c1++;
                    vStart = vEnd = value;
                }
            }

            if (counter != 3464)
                wrongFormat = true;
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (wrongFormat)
            return "Wrong Format!";

        // Built Model
        String positive = "The Signal Is ECG!", negative = "The Signal Is Not ECG!";
        if (b3 >= 483.2) {
            if (c1 >= 915.6 && c1 <= 1220.8) {
                if (b2 >= 509.4 && b2 <= 1018.8)
                    return negative;
                else
                    return positive;
            } else
                return positive;
        } else
            return negative;
    }
}
