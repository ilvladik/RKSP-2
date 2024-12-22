package ru.ilvladik.task1;

import ru.ilvladik.task1.sensors.CO2Sensor;
import ru.ilvladik.task1.sensors.TemperatureSensor;

public class Main {
    // Ильин Владислав Виктрович
    public static void main(String[] args) {
        TemperatureSensor temperatureSensor = new TemperatureSensor(); //
        CO2Sensor co2Sensor = new CO2Sensor();
        Alarm alarm = new Alarm();
        temperatureSensor.subscribe(alarm);
        co2Sensor.subscribe(alarm);
        temperatureSensor.start();
        co2Sensor.start();
    }
}