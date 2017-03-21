package com.martinweigel.joptoforce;

public class MessageBuffer {
    private byte[] history;
    private int first = 0, next = 0;

    public MessageBuffer(int amount) {
        // Create history with one more slot to distinguish empty and full buffer
        history = new byte[amount+1];
        clear();
    }

    protected int increaseIndex(int index) {
        return (index+1) % history.length;
    }

    protected int decreaseIndex(int index) {
        if(index > 0)
            return index - 1;
        else
            return history.length-1;
    }

    public byte getValue(int index) {
        if (index < size()) {
            int realIndex = (first + index) % history.length;
            return history[realIndex];
        } else
            throw new IllegalArgumentException(
                    String.format("Index %d is not available in buffer of size %d", index, size()));
    }

    public void addValue(byte newData) {
        history[next] = newData;
        next = increaseIndex(next);
        if(next == first)
            first = increaseIndex(first);
    }

    public void addValues(byte[] newData) {
        for(int i=0; i<newData.length; i++)
            addValue(newData[i]);
    }

    public void remove() {
        if(first != next)
            first = increaseIndex(first);
    }

    public void remove(int count) {
        for(int i=0; i<count; i++)
            remove();
    }

    public void clear() {
        first = 0;
        next = 0;
    }

    public int size() {
        if(first > next)
            return history.length - first + next;
        else
            return next - first;
    }

    public boolean isEmpty() {
        return first == next;
    }
    public boolean isFull() {
        return size() >= history.length;
    }
}
