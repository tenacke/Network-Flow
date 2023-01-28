import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class InputGenerator {
    private final Random random;
    private FileWriter writer;
    private static final String path = "emre_inputs_v2\\"; // the location where the inputs will be created at
    private static final int inputNo = 1; // starts with input<inputNo>.txt
    private static final int inputNum = 20; // ends with input<inputNum>.txt
    private static final int inputWidth = 1000; // a measure of how big the graph is
    private static final double sparsity = 3D; // a measure of how sparse the graph is
    private static final double sinkProbability = 0.3D; // a measure of how often an edge is toward to sink
    // in other words the ratio of the total number of vertices and the number of edges of one vertex
    private static final int edgeBound = 100; // upper limit for weights
    public InputGenerator() {
        random = new Random();
    }

    public static void main(String[] args) throws IOException {
        if (new File(path).mkdir())
            System.out.println("Directory Created");
        for (int i = inputNo; i <= inputNum; i++) {
            InputGenerator ins = new InputGenerator();
            ins.writer = new FileWriter(path + "input_" + i + ".txt");
            int cityNumber = ins.random.nextInt(5, inputWidth);
            ins.writer.write(cityNumber + "\n");
            ins.createInputs(cityNumber);
        }
    }

    private void createInputs(int cityNumber) throws IOException {
        StringBuilder source = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int weight = random.nextInt((int) (edgeBound*cityNumber/(5*sparsity)));
            source.append(weight).append(" ");
        }
        writer.write(source.append("\n").toString());
        for (int i = 0; i < 6; i++) { // for regions
            Set<Integer> neighbours = new HashSet<>();
            StringBuilder builder = new StringBuilder();
            builder.append("r").append(i);
            randomizeEdges(cityNumber, neighbours, builder);
            writer.write(builder.append("\n").toString());
        }
        for (int i = 0; i < cityNumber; i++) { // for cities
            Set<Integer> neighbours = new HashSet<>();
            StringBuilder builder = new StringBuilder();
            builder.append("c").append(i);
            randomizeEdges(cityNumber, neighbours, builder);
            writer.write(builder.append("\n").toString());
        }
        writer.close();
    }

    private void randomizeEdges(int cityNumber, Set<Integer> neighbours, StringBuilder builder) {
        int edgeNumber = random.nextInt((int) (cityNumber/sparsity));
        for (int j = 0; j < edgeNumber; j++) {
            int neighbour = random.nextInt((int) (cityNumber+(sinkProbability*cityNumber)));
            while (neighbours.contains(neighbour))
                neighbour = random.nextInt((int) (cityNumber+(sinkProbability*cityNumber)));
            neighbours.add(neighbour);
            int weight = random.nextInt(edgeBound);
            String neighbourName = neighbour >= cityNumber ? "KL" : "c" + neighbour;
            builder.append(" ").append(neighbourName).append(" ").append(weight);
        }
    }
}
