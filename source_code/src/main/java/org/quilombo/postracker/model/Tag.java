package org.quilombo.postracker.model;

public class Tag {

    public int BUFFER_SIZE = 1;

    private String id;
    private double[] xBuffer;
    private double[] yBuffer;
    private double[] zBuffer;
    private double x;
    private double y;
    private double z;
    private int index = 0;

    public Tag(String id) {
        this.id = id;
        xBuffer = new double[BUFFER_SIZE];
        yBuffer = new double[BUFFER_SIZE];
        zBuffer = new double[BUFFER_SIZE];
    }

    public void setPos(double x, double y, double z) {
        xBuffer[index] = x;
        yBuffer[index] = y;
        zBuffer[index] = z;
        this.x = average(xBuffer);
        this.y = average(yBuffer);
        this.z = average(zBuffer);
        index++;
        if (index == BUFFER_SIZE)
            index = 0;
    }

    private double average(double[] numbers) {
        double total = 0;
        for (int t = 0; t < numbers.length; t++) {
            total += numbers[t];
        }
        return total / (double) (numbers.length);
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

}
