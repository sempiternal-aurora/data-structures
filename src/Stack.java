import java.util.Collection;

public abstract class Stack<E> {
    public abstract void push(E value);
    public abstract void pushAll(Collection<E> values);
    public abstract int size();
    public abstract boolean contains(E value);
    public abstract E peek();
    public abstract E pop();

}
