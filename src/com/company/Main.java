package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        System.out.println('f' & 8);
        System.out.println("\n\n");
        System.out.println(('f' - 'a' +10) & 8);
    }


    private void readFile(String fileName){

        try(BufferedReader br = Files.newBufferedReader(Paths.get(fileName),Charset.forName("utf8"))){

        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
