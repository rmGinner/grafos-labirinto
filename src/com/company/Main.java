package com.company;

import com.company.graphsapi.*;
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
import java.util.Objects;
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
        String fileName = "caso4.txt";
        Position position = new Position();

        readFileToScanner(fileName);
        identifyStartAndEnd(fileName,position);
        createGraphStructure(fileName,position);
    }

    private static void addEdgesToGraph(Graph graph, Position position, Integer n){
        for(int j = 0; j < graph.V();j++){
            Queue<DIRECTION> directions = getNextDirectionBy(hexToBin(String.valueOf(graph.getCodeFromVertex(j))));

            for(DIRECTION direction : directions){
                if(j != position.getEndPosition()) {
                    if (direction.equals(DIRECTION.NORTH)) {
                        if ((j - n) >= 0) {
                            graph.addEdge(j, j - n);
                        }
                    } else if (direction.equals(DIRECTION.EAST)) {
                        if ((j + 1) < (n * n)) {
                            graph.addEdge(j, j + 1);
                        }
                    } else if (direction.equals(DIRECTION.SOUTH)) {
                        if ((j + n) < (n * n)) {
                            graph.addEdge(j, j + n);
                        }
                    } else if (direction.equals(DIRECTION.WEST)) {
                        if ((j - 1) >= n) {
                            graph.addEdge(j, j - 1);
                        }
                    } else {
                        throw new IllegalArgumentException("Direção inválida.");
                    }
                }
            }
        }
    }

    private static void addValueToVertex(Graph graph){
        int i = 0;

        while (file.hasNextLine()){
            for(String ch : file.nextLine().split(" ")){
                graph.setValueToVertex(i,ch.trim().charAt(0));
                i++;
            }
        }
    }

    private static void createGraphStructure(String fileName, Position position) throws IOException {
        readFileToScanner(fileName);
        Integer n = Integer.valueOf(file.nextLine().trim().split(" ")[0]);
        Graph graph = new Graph(n * n);

        addValueToVertex(graph);
        addEdgesToGraph(graph,position,n);

        System.out.println(position.getStartPosition());

        System.out.println(position.getEndPosition());

        BreadthFirstPaths bfs = new BreadthFirstPaths(graph,position.getStartPosition());

        System.out.println(bfs.hasPathTo(position.getEndPosition()));
        System.out.println(bfs.pathTo(position.getEndPosition()));
        //System.out.println(graph.toDot());
    }

    private static Queue<DIRECTION> getNextDirectionBy(String vertexBinValue){
        Queue<DIRECTION> queue = new Queue<>();

        if(vertexBinValue.charAt(0) == '0'){
            queue.enqueue(DIRECTION.NORTH);
        }

        if(vertexBinValue.charAt(1) == '0'){
            queue.enqueue(DIRECTION.EAST);
        }

        if(vertexBinValue.charAt(2) == '0'){
            queue.enqueue(DIRECTION.SOUTH);
        }

        if(vertexBinValue.charAt(3) == '0'){
            queue.enqueue(DIRECTION.WEST);
        }

        return queue;
    }


    private static void readFileToScanner(String fileName) throws FileNotFoundException {
        file = new Scanner(new File(fileName));
    }

    private static void identifyStartAndEnd(String fileName, Position position) throws IOException {
        Integer line = 0, startLinePosition = null, startCharPosition = null,endLinePosition = null, endCharPosition = null;
        String[] letters;
        readFileToScanner(fileName);

            Long totalLines = Files.newBufferedReader(Paths.get(fileName), Charset.forName("utf8")).lines().count() - 1;

            String[] heightAndWidth = file.nextLine().split(" ");

            while (file.hasNextLine()){
                String stringLine = file.nextLine();
                letters = stringLine.split(" ");

                for(int i = 0; i < letters.length; i++){
                    //Is first line?
                    if(line == 0) {
                        //Up bit
                        if (hexToBin(letters[i]).charAt(0) == '0') {
                            if (Objects.isNull(startCharPosition)) {
                                startLinePosition = line;
                                startCharPosition = i;
                            } else {
                                endLinePosition = line;
                                endCharPosition = i;
                                break;
                            }
                        }

                        if(i == 0){
                            if(hexToBin(letters[i]).charAt(3) == '0'){
                                if (Objects.isNull(startCharPosition)) {
                                    startLinePosition = line;
                                    startCharPosition = i;
                                } else {
                                    endLinePosition = line;
                                    endCharPosition = i;
                                    break;
                                }
                            }
                        }else if(i == letters.length - 1){
                            if(hexToBin(letters[i]).charAt(1) == '0'){
                                if (Objects.isNull(startCharPosition)) {
                                    startLinePosition = line;
                                    startCharPosition = i;
                                } else {
                                    endLinePosition = line;
                                    endCharPosition = i;
                                    break;
                                }
                            }
                        }
                     //Is last line?
                    }else if(line == totalLines.intValue() - 1){
                        //Down bit
                        if(hexToBin(letters[i]).charAt(2) == '0'){
                            //if (!line.equals(startLinePosition)) {
                                if (startCharPosition == null) {
                                    startLinePosition = line;
                                    startCharPosition = i;
                                } else {
                                    endLinePosition = line;
                                    endCharPosition = i;
                                    break;
                                }
                            /*}else if(Objects.nonNull(startCharPosition)){
                                endLinePosition = line;
                                endCharPosition = i;
                            }*/
                        }
                        if(i == 0){
                            if(hexToBin(letters[i]).charAt(3) == '0'){
                                if (Objects.isNull(startCharPosition)) {
                                    startLinePosition = line;
                                    startCharPosition = i;
                                } else {
                                    endLinePosition = line;
                                    endCharPosition = i;
                                    break;
                                }
                            }
                        }else if(i == letters.length - 1){
                            if(hexToBin(letters[i]).charAt(1) == '0'){
                                if (Objects.isNull(startCharPosition)) {
                                    startLinePosition = line;
                                    startCharPosition = i;
                                } else {
                                    endLinePosition = line;
                                    endCharPosition = i;
                                    break;
                                }
                            }
                        }
                    }else{
                        //Left bit
                        if(hexToBin(letters[0]).charAt(3) == '0'){
                            if (!line.equals(startLinePosition) || !Integer.valueOf(0).equals(startCharPosition)) {
                                if (startCharPosition == null) {
                                    startLinePosition = line;
                                    startCharPosition = 0;
                                } else {
                                    endLinePosition = line;
                                    endCharPosition = 0;
                                    break;
                                }
                            }
                            /*}else if(Objects.nonNull(startCharPosition)){
                                endLinePosition = line;
                                endCharPosition = 0;
                            }*/
                        }

                        //Right bit
                        if(hexToBin(letters[letters.length - 1]).charAt(1) == '0') {
                            if (!line.equals(startLinePosition) || !Integer.valueOf(letters.length - 1).equals(startCharPosition)) {
                                if (startCharPosition == null) {
                                    startLinePosition = line;
                                    startCharPosition = letters.length - 1;
                                } else {
                                    endLinePosition = line;
                                    endCharPosition = letters.length - 1;
                                    break;
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

            position.setStartPosition((startLinePosition * Integer.valueOf(heightAndWidth[0].trim())) + startCharPosition);
        position.setEndPosition((endLinePosition * Integer.valueOf(heightAndWidth[0].trim())) + endCharPosition);

        System.out.println("Start Line Position: " + startLinePosition + "\n");
        System.out.println("Start Char Position: " + startCharPosition+ "\n");
        System.out.println("End Line Position: " + endLinePosition+ "\n");
        System.out.println("End Char Position: " + endCharPosition+ "\n");
    }

    private static String hexToBin(String hex){
        return StringUtils.leftPad(new BigInteger(hex,16).toString(2),4,'0');
    }
}
