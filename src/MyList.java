import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;

// simple fast arraylist implementation by removing unnecessary parts of the built-in collection in java
// fast because it only supports adding and get operation there is no exception control since all edge cases are handled in main
public class MyList implements Iterable<Integer>{
    private static final int DEFAULT_SIZE = 10;

    private int size;
    private int[] array;

    public MyList() {
        this(DEFAULT_SIZE);
    }

    public MyList(int size) {
        array = new int[size];
    }

    // transfering data to a new bigger array
    private void grow(){
        array = Arrays.copyOf(array, array.length + DEFAULT_SIZE);
    }

    public void add(int i){
        if (array.length > size++)
            grow();
        array[size-1] = i;
    }

    public int get(int i){
        return array[i];
    }

    public int size(){
        return size;
    }

    public boolean contains(int i){
        for (int value : array)
            if (value == i)
                return true;
        return false;
    }

    // implements Iterable<Integer> to use the iterator in the main class by loops etc.
    @Override
    public Iterator<Integer> iterator() {
        return Arrays.stream(array).iterator();
    }

    @Override
    public void forEach(Consumer<? super Integer> action) {
        Iterable.super.forEach(action);
    }
}
