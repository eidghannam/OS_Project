package com.mycompany.os_project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SRT {
    ArrayList<ProcessControlBlock> processes;
    int contextSwitchingTime;

    public SRT(ArrayList<ProcessControlBlock> processes, int contextSwitchingTime) {
        this.processes =  new ArrayList<>(processes);
        this.contextSwitchingTime = contextSwitchingTime;
    }

    // Sort the processes by arrival time
    public void sortProcessesByArrival() {
        Collections.sort(processes, Comparator.comparingInt(ProcessControlBlock::getArrivalTime));
    }

    // Make the Gantt chart and handle the context switching time
    public void makeGanttChart() {
        sortProcessesByArrival();
        int currentTime = 0;
        int totalBusyTime = 0;
        int totalTime = 0;
        int completed = 0;
        int n = processes.size();
        int[] remainingBurstTimes = new int[n];
        boolean[] isCompleted = new boolean[n];
        for (int i = 0; i < n; i++) {
            remainingBurstTimes[i] = processes.get(i).getBurstTime();
        }

        System.out.print(currentTime);
        ProcessControlBlock previousProcess = null;
        while (completed != n) {
            int shortest = -1;
            int minRemainingTime = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (processes.get(i).getArrivalTime() <= currentTime && !isCompleted[i] && remainingBurstTimes[i] < minRemainingTime) {
                    minRemainingTime = remainingBurstTimes[i];
                    shortest = i;
                }
            }

            if (shortest == -1) {
                currentTime++;
                totalTime++;
                System.out.print(" -> Idle -> " + currentTime);
                continue;
            }

            if (previousProcess != null && previousProcess != processes.get(shortest)) {
                currentTime += contextSwitchingTime;
                totalTime += contextSwitchingTime;
                System.out.print(" -> CS -> " + currentTime);
            }

            // Execute the process until the next process arrival or its completion
            int nextArrival = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (!isCompleted[i] && processes.get(i).getArrivalTime() > currentTime) {
                    nextArrival = Math.min(nextArrival, processes.get(i).getArrivalTime());
                }
            }

            int executionTime = Math.min(nextArrival - currentTime, remainingBurstTimes[shortest]);
            if (executionTime > 0) {
                currentTime += executionTime;
                remainingBurstTimes[shortest] -= executionTime;
                totalBusyTime += executionTime;
                totalTime += executionTime;
                System.out.print(" -> P" + processes.get(shortest).getId() + " -> " + currentTime);
            }

            if (remainingBurstTimes[shortest] == 0) {
                completed++;
                isCompleted[shortest] = true;
                processes.get(shortest).setCompletionTime(currentTime);
            }

            previousProcess = processes.get(shortest);
        }
        System.out.println();

        // Calculate and print CPU Utilization
        double cpuUtilization = ((double) totalBusyTime / totalTime) * 100;
        System.out.println("\nCPU Utilization: " + cpuUtilization + "%");

        // Print result table
        System.out.println("\nID\tAT\tBT\tCT\tWT\tTAT");
        for (int i = 0; i < processes.size(); i++) {
            ProcessControlBlock process = processes.get(i);
            int tat = process.getCompletionTime() - process.getArrivalTime();
            int wt = tat - process.getBurstTime();
            process.setTurnaroundTime(tat);
            process.setWaitingTime(wt);
            System.out.println(process.getId() + "\t" + process.getArrivalTime() + "\t" + process.getBurstTime() + "\t" + process.getCompletionTime() + "\t" + process.getWaitingTime() + "\t" + process.getTurnaroundTime());
        }
    }
}