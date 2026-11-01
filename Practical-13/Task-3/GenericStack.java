public class GenericStack {

    public static void main(String[] args) {
        MyStack<Integer> intStack = new MyStack<>(3);
        intStack.push(10);
        intStack.push(20);
        intStack.push(30);
        intStack.push(40);   // grows automatically
        System.out.println("Integer stack size : " + intStack.size());
        System.out.println("peek               : " + intStack.peek());
        while (!intStack.isEmpty()) {
            System.out.println("pop                : " + intStack.pop());
        }
        System.out.println("pop on empty stack : " + intStack.pop());

        MyStack<String> strStack = new MyStack<>(2);
        strStack.push("MiniBank");
        strStack.push("Generics");
        System.out.println("String stack pop   : " + strStack.pop());
        System.out.println("String stack pop   : " + strStack.pop());
        System.out.println("String stack peek on empty : " + strStack.peek());
    }
}

class MyStack<T> {
    private T[] items;
    private int top = 0;

    @SuppressWarnings("unchecked")
    MyStack(int capacity) {
        items = (T[]) new Object[capacity];
    }

    void push(T item) {
        if (top == items.length) {
            grow();
        }
        items[top++] = item;
    }

    // Empty case handled sensibly: returns null instead of crashing
    T pop() {
        if (isEmpty()) {
            return null;
        }
        T item = items[--top];
        items[top] = null;
        return item;
    }

    T peek() {
        return isEmpty() ? null : items[top - 1];
    }

    boolean isEmpty() {
        return top == 0;
    }

    int size() {
        return top;
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        T[] bigger = (T[]) new Object[items.length * 2];
        System.arraycopy(items, 0, bigger, 0, items.length);
        items = bigger;
    }
}
