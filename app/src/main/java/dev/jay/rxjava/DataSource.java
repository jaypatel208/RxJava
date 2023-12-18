package dev.jay.rxjava;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
    public static List<Task> createTasksList() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Take bike to garage", true, 2));
        tasks.add(new Task("Turn the motor on", true, 3));
        tasks.add(new Task("Send her message", false, 1));
        tasks.add(new Task("Eat breakfast", false, 0));
        tasks.add(new Task("Sleep again", false, 4));
        return tasks;
    }
}
