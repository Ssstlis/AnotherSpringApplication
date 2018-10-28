package com.hotel.utils;

public class Container<T> {
    private T variable;

    public Container(T value) {
        variable = value;
    }

    public T getVariable() {
        return variable;
    }

    public Container<T> setVariable(T variable) {
        this.variable = variable;
        return this;
    }
}
