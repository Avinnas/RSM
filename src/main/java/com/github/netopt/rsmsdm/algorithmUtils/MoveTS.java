package com.github.netopt.rsmsdm.algorithmUtils;

public class MoveTS {
    int value;
    int pathImprovement;
    int index;
    int pathNumber;

    public MoveTS(int value, int pathImprovement, int index, int pathNumber) {
        this.value = value;
        this.pathImprovement = pathImprovement;
        this.index = index;
        this.pathNumber = pathNumber;
    }

    public int getValue() {
        return value;
    }

    public int getPathImprovement() {
        return pathImprovement;
    }

    public int getIndex() {
        return index;
    }

    public int getPathNumber() {
        return pathNumber;
    }
}
