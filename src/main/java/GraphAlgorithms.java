import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

class Test {
    static void show(Object object) {
        System.out.println(object);
    }

    static boolean checking( Integer integer) {
        if (integer==null)
            return false;
        if (integer<50)
            return false;
        if (integer%5<5)
            return false;
        return true;
    }
}


public class GraphAlgorithms {
    public static void main(String[] args) {
        Integer matrix[][] = {
                {0, 1, 20, null, null, null, null, null},
                {-1, 0, 9, -1, 2, null, null, null},
                {-20, -9, 0, -3, null, null, -1, null},
                {null, 1, 3, 0, -3, -4, 1, null},
                {null, -2, null, 3, 0, -4, null, null},
                {null, null, null, 4, 4, 0, null, null},
                {null, null, 1, - 1, null, null, 0, 8},
                {null, null, null, null, null, null, -8, 0}};
       /* Integer[][] matrix = {
                {0, 4, 2},
                {-4, 0, -1},
                {-2, 1, 0}
        };*/
        Graph myGraph = new Graph(matrix);
        myGraph.removeNode(5);
        System.out.println(myGraph);
        myGraph.searchPaths(0);
    }
}


class Graph {
    private Integer[][] matrix;
    private int size;
    private volatile Integer[] edges;//only for calculating shortest paths , only for searchPaths function
    private int bazillion = 1000000;//any number with unreal value for edge
    private HashSet<Integer> inPath;//includes nodes in path during searching, uses for detecting circles
    private int circleDepth;//only for detecting invalid paths

    public Graph(Integer[][] matrix) {
        this.matrix = new Integer[20][20];
        inPath = new HashSet<>();
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][i] != 0) {//validating distances
                System.out.println("Distance from node to itself can not be different of 0");
                return;
            }
        }
        size = copyMatrix(this.matrix, matrix);// i don`t find normal way for arrays copy, i am sorry)
        reorganize();
    }

    private int copyMatrix(Integer[][] matrix1, Integer[][] matrix2) {
        int i = 0;
        for (; i < matrix2.length; i++) {
            for (int j = 0; j < matrix2[i].length; j++) {
                matrix1[i][j] = matrix2[i][j];
            }
        }
        return i;//rows number MUST be equal to column numbers
    }

    private void reorganize() {//writing inverse distances in matrix
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (matrix[j][i] != null)
                    matrix[i][j] = -matrix[j][i];
                else
                    matrix[i][j] = null;//null means lack of edge
            }
        }
    }

    public Graph() {
        matrix = new Integer[20][20];
        size = 1;
    }

    public void addNode(Integer[] paths) {
        if (paths.length != size) {
            System.out.println("Invalid number of edges");
            return;
        }
        for (int i = 0; i < size; i++) {
            matrix[size][i] = paths[i];//adding new row of elements
        }
        for (int i = 0; i < size; i++) {
            matrix[i][size] = 0;//adding new column of elements
        }
        size++;
        reorganize();
    }


    public void removeNode(int number) {
        if ((number < 0) || (number >= size)) {
            System.out.println(size + "\telement does not exist");//validating number
            return;
        }
        for (int i = number; i < size - 1; i++) {
            matrix[i] = matrix[i + 1];
        }
        size--;
        for (int i = 0; i < size; i++) {
            for (int j = number; j < size + 1; j++) {
                matrix[i][j] = matrix[i][j + 1];
            }
        }
        reorganize();
    }


    private int goDepth(int number) {
        if (edges[number] != null)
            return edges[number];//if distance was calculated already
        if (!inPath.add(number)) {
            circleDepth++;
        }
        if (circleDepth == 2) {//pointer return to some node and we detect invalid path
//            inPath.clear();//if program have some problems with circles, probably solving is in inPath
            circleDepth = 0;
            return bazillion;
        }
        int[] distances = new int[size];
        int numberEdges = 0;
        for (int i = 0; i < size; i++) {
            if (matrix[number][i] != null) {
                if (matrix[number][i] < 0) {//calculating all paths
                    distances[numberEdges] = -matrix[number][i] + goDepth(i);
                    numberEdges++;
                }
            }
        }
        if (numberEdges == 0) {
            return bazillion;
        }
        int min = distances[0];
        for (int i = 1; i < numberEdges; i++) {
//            System.out.println("Number: " + number + " distance: " + distances[i]);
            if (distances[i] < min)
                min = distances[i];//choosing shortest path to node

        }
        if (min >= bazillion) {
            //edges[number] = null;//start node have NO path to current node
            return bazillion;//it means invalid path and we mark it as bazillion
        }
        System.out.println("Paths before: " + Arrays.toString(edges));
        System.out.println("Distance to " + number + " node was calculated and means " + min);
        edges[number] = min;
        System.out.println("Paths after: " + Arrays.toString(edges) + '\n');
        return min;
    }

    public void searchPaths(Integer startNode) {
        edges = new Integer[size];
        Thread[] threads = new Thread[size];
        circleDepth = 0;
        for (int i = 0; i < size; i++) {
            threads[0].run();
            edges[i] = null;
        }//initial filling array of distances
        edges[startNode] = 0;
        for (int i = 0; i < size; i++) {
            inPath.clear();
            goDepth(i);//checking edges and finding distances

        }
        System.out.println("\nPaths for " + startNode + " node:\t" + Arrays.toString(edges));
    }


    @Override
    public String toString() {
        String graph = "Graph :\n\t\t";
        for (int i = 0; i < size; i++) {
            graph += "a" + i + "\t\t";
        }
        graph += "\n\n\n";
        for (int i = 0; i < size; i++) {
            graph += "a" + i + "\t\t";
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] != null)
                    graph += matrix[i][j] + "\t\t";
                else
                    graph += "N\t\t";
            }
            graph += "\n\n\n";
        }
        return graph;
    }
}