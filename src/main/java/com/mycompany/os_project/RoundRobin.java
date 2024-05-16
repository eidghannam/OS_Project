package com.mycompany.os_project;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class RoundRobin {
    ArrayList<ProcessControlBlock> processes;
    int contextSwitchingTime;
    int quantumTime;

    public RoundRobin(ArrayList<ProcessControlBlock> processes, int contextSwitchingTime, int quantumTime) {
        this.processes =  new ArrayList<>(processes);
        this.contextSwitchingTime = contextSwitchingTime;
        this.quantumTime = quantumTime;
    }

    // Sort the processes by arrival time
    public void sortProcesses() {
        for (int i = 0; i < processes.size(); i++) {
            for (int j = i + 1; j < processes.size(); j++) {
                if (processes.get(i).getArrivalTime() > processes.get(j).getArrivalTime()) {
                    ProcessControlBlock temp = processes.get(i);
                    processes.set(i, processes.get(j));
                    processes.set(j, temp);
                }
            }
        }
    }

    // Make the Gantt chart and handle the context switching time and quantum time
    public void makeGanttChart() {
        sortProcesses();
        int currentTime = 0;
        int totalBusyTime = 0;
        int totalTime = 0;

        Queue<ProcessControlBlock> queue = new LinkedList<>();
        int[] remainingBurstTimes = new int[processes.size()];
        for (int i = 0; i < processes.size(); i++) {
            remainingBurstTimes[i] = processes.get(i).getBurstTime();
        }

        // Enqueue the first process
        queue.add(processes.get(0));

        // Index to track arrival of new processes
        int index = 1;
        System.out.print(currentTime);
        while (!queue.isEmpty()) {
            ProcessControlBlock currentProcess = queue.poll();

            // If there's idle time before the next process arrives
            if (currentTime < currentProcess.getArrivalTime()) {
                System.out.print(" -> Idle -> " + currentProcess.getArrivalTime());
                totalTime += currentProcess.getArrivalTime() - currentTime;
                currentTime = currentProcess.getArrivalTime();
            }

            // Process execution
            if (remainingBurstTimes[currentProcess.getId() - 1] > quantumTime) {
                currentTime += quantumTime;
                remainingBurstTimes[currentProcess.getId() - 1] -= quantumTime;
                totalBusyTime += quantumTime;
                totalTime += quantumTime;
                System.out.print(" -> P" + currentProcess.getId() + " -> " + currentTime);

                // Context switch
                currentTime += contextSwitchingTime;
                totalTime += contextSwitchingTime;
                System.out.print(" -> CS -> " + currentTime);
            } else {
                currentTime += remainingBurstTimes[currentProcess.getId() - 1];
                totalBusyTime += remainingBurstTimes[currentProcess.getId() - 1];
                totalTime += remainingBurstTimes[currentProcess.getId() - 1];
                System.out.print(" -> P" + currentProcess.getId() + " -> " + currentTime);
                remainingBurstTimes[currentProcess.getId() - 1] = 0;
                currentProcess.setCompletionTime(currentTime);

                // No context switch after the last process
                if (!queue.isEmpty() || index < processes.size()) {
                    currentTime += contextSwitchingTime;
                    totalTime += contextSwitchingTime;
                    System.out.print(" -> CS -> " + currentTime);
                }
            }

            // Enqueue newly arrived processes during execution
            while (index < processes.size() && processes.get(index).getArrivalTime() <= currentTime) {
                queue.add(processes.get(index));
                index++;
            }

            // Re-enqueue the current process if it has remaining burst time
            if (remainingBurstTimes[currentProcess.getId() - 1] > 0) {
                queue.add(currentProcess);
            }
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
