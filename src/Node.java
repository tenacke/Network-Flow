import java.util.*;

public class Node {
    private final String name;
    private final Map<String, Integer> neighbours;
    private int level;

    public Node(String name) { // creating a simple node for the graph
        this.name = name;
        neighbours = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void addNeighbour(String neighbour, Integer weight){ // neighbour and its edge are added to a linked list here
        neighbours.put(neighbour, weight);
    }

    public int getEdge(String neighbour){
        return neighbours.getOrDefault(neighbour, 0);
    }

    public Iterator<String> getNeighbours(){
        return neighbours.keySet().iterator();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Node && ((Node) o).getName().equals(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
