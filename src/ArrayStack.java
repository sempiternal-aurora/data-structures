import java.util.Collection;
import java.util.EmptyStackException;

public class ArrayStack<E> extends Stack<E> {
    private E[] elements;
    private int endStack = 0;
    final int INITIAL_SIZE;
    final double GROWTH_FACTOR;

    ArrayStack() {
        INITIAL_SIZE = 16;
        GROWTH_FACTOR = 1;
        elements = (E[]) new Object[INITIAL_SIZE];
    }

    ArrayStack(int initialSize, int growthFactor) {
        INITIAL_SIZE = initialSize;
        GROWTH_FACTOR = growthFactor;
        elements = (E[]) new Object[INITIAL_SIZE];
    }
    @Override
    public void push(E value) {
        if (endStack == elements.length) {
            E[] oldElements = elements;
            elements = (E[]) new Object[(int) (elements.length * (1 + GROWTH_FACTOR))];
            System.arraycopy(oldElements, 0, elements, 0, endStack);
        }
        elements[endStack] = value;
        endStack++;
    }

    public void pushArray(E[] values) {
        int newLength = elements.length;
        while (endStack + values.length > newLength) {
            newLength = (int) (newLength * (1 + GROWTH_FACTOR));
        }
        if (newLength != elements.length) {
            E[] oldElements = elements;
            elements = (E[]) new Object[newLength];
            System.arraycopy(oldElements, 0, elements, 0, endStack);
        }
        System.arraycopy(values, 0, elements, endStack, values.length);
        endStack += values.length;
    }
    @Override
    public void pushAll(Collection<E> values) {
        pushArray((E[]) values.toArray());
    }

    @Override
    public int size() {
        return endStack;
    }

    @Override
    public boolean contains(E value) {
        for (int i = 0; i < endStack; i++) {
            if (value.equals(elements[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public E peek() {
        if (endStack == 0) {
            throw new EmptyStackException();
        } else {
            return elements[endStack - 1];
        }
    }

    @Override
    public E pop() {
        if (endStack == 0) {
            throw new EmptyStackException();
        } else {
            endStack--;
            E elem = elements[endStack];
            elements[endStack] = null;
            return elem;
        }
    }
}
