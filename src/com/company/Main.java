package com.company;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    /**
     * For file "caso25a.txt"
     *
     * Start point is in: line 8 - column 0
     * Final point is in: line 25 - column 17
     * */
    public static void main(String[] args) {
        identifyStartAndEnd("caso4.txt");
    }

    private static void createGraphStructure(){


    }
    private static void identifyStartAndEnd(String fileName){
        Integer line = 0, startLinePosition = null, startCharPosition = null,endLinePosition = null, endCharPosition = null;
        String[] letters;

        try(Scanner sc = new Scanner(new File(fileName))){
            Long totalLines = Files.newBufferedReader(Paths.get(fileName), Charset.forName("utf8")).lines().count() - 1;

            sc.nextLine();

            while (sc.hasNextLine()){
                String stringLine = sc.nextLine();
                letters = stringLine.split(" ");

                for(int i = 0; i < letters.length; i++){
                    //Is first line?
                    if(line == 0) {
                        //Up bit
                        if(hexToBin(letters[i]).charAt(0) == '0'){
                            startLinePosition = line;
                            startCharPosition = i;
                        }
                     //Is last line?
                    }else if(line == totalLines.intValue() - 1){
                        //Down bit
                        if(hexToBin(letters[i]).charAt(2) == '0'){
                            if (!line.equals(startLinePosition) && !Integer.valueOf(i).equals(startCharPosition)) {
                                if (startCharPosition == null && startLinePosition == null) {
                                    startLinePosition = line;
                                    startCharPosition = i;
                                } else {
                                    endLinePosition = line;
                                    endCharPosition = i;
                                }
                            }
                        }
                    }else{
                        //Left bit
                        if(hexToBin(letters[0]).charAt(3) == '0'){
                            if(!line.equals(startLinePosition) && !Integer.valueOf(i).equals(startCharPosition)) {
                                if (startCharPosition == null && startLinePosition == null) {
                                    startLinePosition = line;
                                    startCharPosition = i;
                                } else {
                                    endLinePosition = line;
                                    endCharPosition = 0;
                                }
                            }
                        }

                        //Right bit
                        if(hexToBin(letters[letters.length - 1]).charAt(1) == '0') {
                            if (!line.equals(startLinePosition) && !Integer.valueOf(i).equals(startCharPosition)) {
                                if (startCharPosition == null && startLinePosition == null) {
                                    startLinePosition = line;
                                    startCharPosition = i;
                                } else {
                                    endLinePosition = line;
                                    endCharPosition = letters.length - 1;
                                }
                            }
                        }
                    }

                    if(startLinePosition != null && endLinePosition != null && startCharPosition != null && endCharPosition != null){
                        break;
                    }
                }

                if(startLinePosition != null && endLinePosition != null && startCharPosition != null && endCharPosition != null){
                    break;
                }
                line++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Start Line Position: " + startLinePosition + "\n");
        System.out.println("Start Char Position: " + startCharPosition+ "\n");
        System.out.println("End Line Position: " + endLinePosition+ "\n");
        System.out.println("End Char Position: " + endCharPosition+ "\n");
    }

    private static String hexToBin(String hex){
        return StringUtils.leftPad(new BigInteger(hex,16).toString(2),4,'0');
    }
}
