package com.company;

import com.company.graphsapi.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Scanner;

/**
 * Program that parse a given file with the specific format to a Maze Graph and find the start and end,
 * and after find the way from start to end vertex.
 *
 * @author Rodrigo Machado <a href='mailto:rodrigosapiranga@hotmail.com'>rodrigosapiranga@hotmail.com</a>
 */
public class Main {

    private static Scanner file;

    /**
     * Enum that represents the navigable directions in graph.
     */
    private enum DIRECTION{NORTH, EAST, SOUTH, WEST};

    /**
     * Main method
     * */
    public static void main(String[] args) throws IOException {
        String fileName = "caso25a.txt";

        Position position = new Position();
        identifyStartAndEnd(fileName,position);

        Graph graph = createGraphStructure(fileName,position);

        BreadthFirstPaths bfs = new BreadthFirstPaths(graph,position.getStartPosition());

        //System.out.println(bfs.hasPathTo(position.getEndPosition()));
        //System.out.println(bfs.pathTo(position.getEndPosition()));
        System.out.println(graph.toDot());

    }

    /**
     * Create the graph strucutre from file and {@link Position} object with start and end vertexes.
     *
     * @param fileName
     * @param position
     * @return
     * @throws IOException
     */
    private static Graph createGraphStructure(String fileName, Position position) throws IOException {
        readFileToScanner(fileName);
        Integer vertexPerline = Integer.valueOf(file.nextLine().trim().split(" ")[0]);
        Integer totalGraphVertexes = vertexPerline * vertexPerline;

        Graph graph = new Graph(totalGraphVertexes);

        for(int currentVertex = 0; file.hasNextLine();currentVertex++){
            for(String ch : file.nextLine().split(" ")){
                graph.setCodeToVertex(currentVertex,ch.trim().charAt(0));
            }
        }

        for(int currentVertex = 0; currentVertex < graph.V();currentVertex++){
            Queue<DIRECTION> directions = getNextDirectionBy(hexToBin(String.valueOf(graph.getCodeFromVertex(currentVertex))));

            for(DIRECTION direction : directions){
                if(currentVertex != position.getStartPosition() && currentVertex != position.getEndPosition()) {
                    validateAndAddEdge(direction,vertexPerline,currentVertex,graph);
                }
            }
        }

        System.out.println(position.getStartPosition());

        System.out.println(position.getEndPosition());


        return graph;
    }

    /**
     * Validate the parameter direction and if the calculated position is valid. After add the edge between two vertexes.
     *
     * @param direction
     * @param totalVertexPerLine
     * @param vertex
     * @param graph
     */
    private static void validateAndAddEdge(DIRECTION direction, Integer totalVertexPerLine, Integer vertex, Graph graph){
        Integer totalGraphVertexes = totalVertexPerLine * totalVertexPerLine;
        Integer firstVertex = 0;
        Integer eastVertex = vertex + 1;
        Integer westVertex = vertex - 1;
        Integer northVertex = vertex - totalVertexPerLine;
        Integer southVertex = vertex + totalVertexPerLine;

        if (direction.equals(DIRECTION.NORTH)) {
            if (northVertex >= firstVertex) {
                graph.addEdge(vertex, northVertex);
            }
        } else if (direction.equals(DIRECTION.EAST)) {
            if (eastVertex < totalGraphVertexes) {
                graph.addEdge(vertex, eastVertex);
            }
        } else if (direction.equals(DIRECTION.SOUTH)) {
            if (southVertex < totalGraphVertexes) {
                graph.addEdge(vertex, southVertex);
            }
        } else  {
            if (westVertex >= totalVertexPerLine) {
                graph.addEdge(vertex, westVertex);
            }
        }

    }

    /**
     * Given the calculated bin vertex by parameter (EX: F -> 1111), returns all vertex possible directions.
     *
     * @param vertexBinValue
     * @return
     */
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

    /**
     * Reads file to a Scanner object.
     *
     * @param fileName
     * @throws FileNotFoundException
     */
    private static void readFileToScanner(String fileName) throws FileNotFoundException {
        file = new Scanner(new File(fileName));
    }

    /***
     * Identify the start and end of the maze.
     *
     * @param fileName
     * @param position
     * @throws IOException
     */
    private static void identifyStartAndEnd(String fileName, Position position) throws IOException {
        Integer line = 0, startLinePosition = null, startCharPosition = null,endLinePosition = null, endCharPosition = null;
        String[] letters;
        readFileToScanner(fileName);

        String[] heightAndWidth = file.nextLine().split(" ");
        Integer totalLines = Integer.valueOf(heightAndWidth[0].trim());

        while (file.hasNextLine()){
            String stringLine = file.nextLine();
            letters = stringLine.split(" ");

            for(int i = 0; i < letters.length; i++){
                //Is first line?
                if(line == 0) {
                    //Up bit is the start or end
                    if(hexToBin(letters[i]).charAt(0) == '0'){
                        startLinePosition = line;
                        startCharPosition = i;
                    }
                 //Is last line?
                }else if(line == totalLines - 1){
                    //Down bit is the start or end
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
                    //Left bit is the start or end
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

                    //Right bit is the start or end
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

        position.setStartPosition((startLinePosition * Integer.valueOf(heightAndWidth[0].trim())) + startCharPosition);
        position.setEndPosition((endLinePosition * Integer.valueOf(heightAndWidth[0].trim())) + endCharPosition);

        System.out.println("Start Line Position: " + startLinePosition + "\n");
        System.out.println("Start Char Position: " + startCharPosition+ "\n");
        System.out.println("End Line Position: " + endLinePosition+ "\n");
        System.out.println("End Char Position: " + endCharPosition+ "\n");
    }

    /***
     * Converts hex to bin value.
     *
     * @param hex
     * @return
     */
    private static String hexToBin(String hex){
        return StringUtils.leftPad(new BigInteger(hex,16).toString(2),4,'0');
    }
}
