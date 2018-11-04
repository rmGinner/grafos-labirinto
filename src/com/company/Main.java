package com.company;

import com.company.graphsapi.Digraph;
import com.company.graphsapi.Graph;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    private static Scanner file;

    private enum DIRECTION{NORTH, EAST, SOUTH, WEST};
    /**
     * For file "caso25a.txt"
     *
     * Start point is in: line 8 - column 0
     * Final point is in: line 25 - column 17
     * */
    public static void main(String[] args) throws IOException {
        String fileName = "caso25a.txt";


        readFileToScanner(fileName);

        Position position = new Position();
        identifyStartAndEnd(fileName,position);

        createGraphStructure(fileName,position);

    }

    private static void createGraphStructure(String fileName, Position position) throws IOException {
        readFileToScanner(fileName);
        Integer n = Integer.valueOf(file.nextLine().trim().split(" ")[0]);
        Digraph digraph = new Digraph(n * n);

        int i = 0;

        while (file.hasNextLine()){
            for(String ch : file.nextLine().split(" ")){
                digraph.setValueToVertex(i,ch.trim().charAt(0));
                i++;
            }
        }

        Integer positionRunner = position.getStartPosition();
        DIRECTION direction = DIRECTION.NORTH;

        for(int j = 0; j <digraph.V();j++){
            direction = getNextDirectionBy(hexToBin(String.valueOf(digraph.getCodeFromVertex(positionRunner))), direction);

            if(direction.equals(DIRECTION.NORTH)){
                digraph.addEdge(positionRunner, positionRunner - n);
                positionRunner -= n;
                direction = DIRECTION.SOUTH;
            }else if(direction.equals(DIRECTION.EAST)){
                digraph.addEdge(positionRunner, positionRunner + 1);
                positionRunner += 1;
                direction = DIRECTION.WEST;
            }else if(direction.equals(DIRECTION.SOUTH)){
                digraph.addEdge(positionRunner, positionRunner + n);
                positionRunner += n;
                direction = DIRECTION.NORTH;
            }else if(direction.equals(DIRECTION.WEST)){
                digraph.addEdge(positionRunner, positionRunner - 1);
                positionRunner -= 1;
                direction = DIRECTION.EAST;
            }else{
                throw  new IllegalArgumentException("Posição corrente retornou null;");
            }

        }


        for(int j = 0; j < digraph.V();j++){
            for(Integer v : digraph.adj(j)){
                System.out.printf("%d -> %d\n",j,v);
            }
        }

        //digraph.printVertexes();
    }

    private static DIRECTION getNextDirectionBy(String vertexBinValue, DIRECTION currentDirection){
        //North
        if(currentDirection.equals(DIRECTION.NORTH)){
            if(vertexBinValue.charAt(1) == '0'){
                return DIRECTION.EAST;
            }else if(vertexBinValue.charAt(2) == '0'){
                return DIRECTION.SOUTH;
            }else if(vertexBinValue.charAt(3) == '0'){
                return DIRECTION.WEST;
            }
        //East
        }else if(currentDirection.equals(DIRECTION.EAST)){
            if(vertexBinValue.charAt(0) == '0'){
                return DIRECTION.NORTH;
            }else if(vertexBinValue.charAt(2) == '0'){
                return DIRECTION.SOUTH;
            }else if(vertexBinValue.charAt(3) == '0'){
                return DIRECTION.WEST;
            }
        //South
        }else if(currentDirection.equals(DIRECTION.SOUTH)){
            if(vertexBinValue.charAt(0) == '0'){
                return DIRECTION.NORTH;
            }else if(vertexBinValue.charAt(1) == '0'){
                return DIRECTION.EAST;
            }else if(vertexBinValue.charAt(3) == '0'){
                return DIRECTION.WEST;
            }
        //West
        }else{
            if(vertexBinValue.charAt(0) == '0'){
                return DIRECTION.NORTH;
            }else if(vertexBinValue.charAt(1) == '0'){
                return DIRECTION.EAST;
            }else if(vertexBinValue.charAt(2) == '0'){
                return DIRECTION.SOUTH;
            }
        }

        return null;
    }


    private static void readFileToScanner(String fileName) throws FileNotFoundException {
        file = new Scanner(new File(fileName));
    }

    private static void identifyStartAndEnd(String fileName, Position position) throws IOException {
        Integer line = 0, startLinePosition = null, startCharPosition = null,endLinePosition = null, endCharPosition = null;
        String[] letters;
        readFileToScanner(fileName);

            Long totalLines = Files.newBufferedReader(Paths.get(fileName), Charset.forName("utf8")).lines().count() - 1;

            file.nextLine();

            while (file.hasNextLine()){
                String stringLine = file.nextLine();
                letters = stringLine.split(" ");

                for(int i = 0; i < letters.length; i++){
                    //Is first line?
                    if(line == 0) {
                        //Up bit
                        if(hexToBin(letters[i]).charAt(0) == '0'){
                            startLinePosition = line;
                            startCharPosition = i;

                            position.setStartPosition((line + 1) * (i + 1) - 1);

                        }
                     //Is last line?
                    }else if(line == totalLines.intValue() - 1){
                        //Down bit
                        if(hexToBin(letters[i]).charAt(2) == '0'){
                            if (!line.equals(startLinePosition) && !Integer.valueOf(i).equals(startCharPosition)) {
                                if (startCharPosition == null && startLinePosition == null) {
                                    startLinePosition = line;
                                    startCharPosition = i;

                                    position.setStartPosition((line + 1) * (i + 1) - 1);
                                } else {
                                    endLinePosition = line;
                                    endCharPosition = i;

                                    position.setEndPosition(((line + 1) * (endCharPosition + 1)) - 1);
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

                                    position.setStartPosition((line + 1) * (i + 1) - 1);
                                } else {
                                    endLinePosition = line;
                                    endCharPosition = 0;

                                    position.setEndPosition(((line + 1) * (endCharPosition + 1)) - 1);
                                }
                            }
                        }

                        //Right bit
                        if(hexToBin(letters[letters.length - 1]).charAt(1) == '0') {
                            if (!line.equals(startLinePosition) && !Integer.valueOf(i).equals(startCharPosition)) {
                                if (startCharPosition == null && startLinePosition == null) {
                                    startLinePosition = line;
                                    startCharPosition = i;

                                    position.setStartPosition((line + 1) * (i + 1) - 1);
                                } else {
                                    endLinePosition = line;
                                    endCharPosition = letters.length - 1;

                                    position.setEndPosition(((line + 1) * (endCharPosition + 1)) - 1);
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

        System.out.println("Start Line Position: " + startLinePosition + "\n");
        System.out.println("Start Char Position: " + startCharPosition+ "\n");
        System.out.println("End Line Position: " + endLinePosition+ "\n");
        System.out.println("End Char Position: " + endCharPosition+ "\n");
    }

    private static String hexToBin(String hex){
        return StringUtils.leftPad(new BigInteger(hex,16).toString(2),4,'0');
    }
}
