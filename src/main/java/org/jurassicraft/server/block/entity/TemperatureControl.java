package org.jurassicraft.server.block.entity;

public interface TemperatureControl {
    void setTemperature(int index, int value);

    int getTemperature(int index);

    int getTemperatureCount();
}
