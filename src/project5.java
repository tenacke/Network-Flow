import java.io.*;
import java.util.*;

public class project5 {
    private final BufferedReader input;
    private final BufferedWriter output;
    private final int[][] graph;
    private final MyList[] adjacents; // int ArrayList
    private final int SIZE;
    private int[] levels;

    public project5(String input, String output) throws IOException {
        // initialization
        this.input = new BufferedReader(new FileReader(input));
        this.output = new BufferedWriter(new FileWriter(output));
        SIZE = Integer.parseInt(this.input.readLine()) + 8;
        this.graph = new int[SIZE][SIZE];
        this.adjacents = new MyList[SIZE];
        Arrays.stream(graph).forEach(a -> Arrays.fill(a, -1));
    }

    private void readInputs() throws IOException {
        // input reading with buffered-reader

        // setting source
        String[] troops = input.readLine().split(" ");
        adjacents[0] = new MyList(6);

        // adding regions
        for (int i = 1; i < 7; i++) {
            String[] newLine = input.readLine().split(" ");
            graph[0][i] = Integer.parseInt(troops[i-1]);
            adjacents[0].add(i);
            adjacents[i] = new MyList(newLine.length/2);
            readNeighbours(newLine, i);
        }

        // adding cities
        for (int i = 7; i < SIZE-1; i++) {
            String[] newLine = input.readLine().split(" ");
            adjacents[i] = new MyList(newLine.length/2);
            readNeighbours(newLine, i);
        }

        // setting KL
        adjacents[SIZE-1] = new MyList(); // for KL
//        System.out.println("Initialization: " + (double) (System.nanoTime() - a)/1000000000L);
    }

    private void readNeighbours(String[] newLine, int current) {
        // set the weighted graph for all neighoburs of a node
        for (int j = 1; j <= newLine.length/2; j++) {
            String name = newLine[2*j-1];
            int node = name.equals("KL") ? SIZE-8 : Integer.parseInt(name.substring(1));
            int weight = Integer.parseInt(newLine[2*j]);
            graph[current][node+=7] = weight;
            adjacents[current].add(node);
        }
    }

    private String getName(int i){
        // translating index number to output manner
        if (i == 0)
            return "";
        else if (i == SIZE-1)
            return "KL";
        else if (1 <= i && i <= 6)
            return "r" + (i-1);
        else
            return "c" + (i-7);
    }

    private LinkedList<String> findVulnerableEdges(){
        // using last level graph to find the min cut

        // initialization
        LinkedList<String> vulnerableEdges = new LinkedList<>();
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[SIZE];
        queue.add(0);

        // starts bfs
        while (!queue.isEmpty()){
            int current = queue.poll();

            // looping the neighbours
            for (int neighbour : adjacents[current]) {
                // already visited
                if (visited[neighbour])
                    continue;

                // true when edge is not considered as vulnerable since there is still path and continues bfs from this node
                if (graph[current][neighbour] > 0){
                    queue.add(neighbour);
                    visited[neighbour] = true;
                }
                // true in the min cut since it is impossible to visit neighbour node in the whole bfs
                else if (levels[neighbour] == 0)
                    vulnerableEdges.add((getName(current) + " " + getName(neighbour)).strip());
            }
        }
        return vulnerableEdges;
    }

    private boolean setLevelGraph(){
        // setting a level graph by using bfs

        // initialization
        levels = new int[SIZE];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        levels[0] = 1;

        // queue traversing
        while (!queue.isEmpty()){
            int current = queue.poll();

            // neighbour visiting and setting levels
            for (int neighbour : adjacents[current]) {
                // already visited handling
                if (levels[neighbour] > 0 || graph[current][neighbour] <= 0)
                    continue;

                // setting levels
                levels[neighbour] = levels[current] + 1;

                // keeping the algorithm iterating when there exist a path to KL
                if (neighbour == SIZE-1)
                    return true;
                queue.add(neighbour);
            }
        }
        return false;
    }

    private int searchPath(int current, int[] deadEnd, int flow) {
        // recursive dfs search for an augmenting path

        // Initial Condition
        if (current == SIZE - 1) // we found the KL
            return flow;

        // initialization
        int edgeNumber = adjacents[current].size();

        // dfs start until there is no working edges
        while (deadEnd[current] < edgeNumber) {
            int neighbour = adjacents[current].get(deadEnd[current]);
            int weight = graph[current][neighbour];

            // runs when edge is working
            if (weight > 0 && levels[current] == levels[neighbour] - 1) {
                // recursive for finding the path
                int bottleneck = searchPath(neighbour, deadEnd, Math.min(flow, weight));

                // runs when there is path
                if (bottleneck > 0) {
                    // augment edge
                    augmentEdge(current, neighbour, bottleneck);

                    // return nonzero value for informing past edges to augment
                    return bottleneck;
                }
            }
            deadEnd[current]++;
        }
        // return 0 when there is no working edge with this node
        return 0;
    }

    private void augmentEdge(int current, int neighbour, int bottleneck) {
        // augments edges
        graph[current][neighbour] -= bottleneck;

        // runs to initialize an adjacent edge if needed
        if (!adjacents[neighbour].contains(current)) {
            adjacents[neighbour].add(current);
            graph[neighbour][current]++;
        }
        graph[neighbour][current] += bottleneck;
    }

    private int findFlow(){
        // calls bfs and dfs methods to find the maximum flow

        int flow = 0;
        // keeps looping until no level graph can be found
        while (setLevelGraph()){
            // initialize new dead end array to hold non-working edges,
            // so we won't use that edge in this level graph again
            int[] deadEnd = new int[SIZE];

            // search for path until there is no augmenting path
            int f;
            do {
                f = searchPath(0, deadEnd, Integer.MAX_VALUE);
                flow += f;
            } while (f != 0);
        }
        return flow;
    }

    private String minCut(){
        // generating the output string for min cut
        StringBuilder builder = new StringBuilder();

        // calls bfs method to find the min cut edges
        LinkedList<String> vulnerableEdges = findVulnerableEdges();
        for (String edge : vulnerableEdges) {
            builder.append(edge).append("\n");
        }
        return builder.toString();
    }

    private void writeOutputs() throws IOException {
        // runs flow and cut methods and writes to the file
        output.write(findFlow() + "\n");
        output.write(minCut());
        output.close();
    }

    public static void main(String[] args) throws IOException {
//        long a = System.nanoTime();
        project5 p5 = new project5(args[0], args[1]);
        p5.readInputs();
        p5.writeOutputs();
//        System.out.println("Whole program: " + (double) (System.nanoTime() - a)/1000000000L);
    }
}
