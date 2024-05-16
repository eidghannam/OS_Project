package com.mycompany.os_project;



import java.util.ArrayList;

public class FCFS {
    ArrayList<ProcessControlBlock> processes;
    int contextSwitchingTime;

    public FCFS(ArrayList<ProcessControlBlock> processes, int contextSwitchingTime) {
        this.processes = new ArrayList<>(processes);
        this.contextSwitchingTime = contextSwitchingTime;
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

    // Make the Gantt chart and handle the context switching time
    // Example of Gantt chart format: 0 -> P1 -> 4 -> CS -> 6 -> P2 -> 8 -> CS -> 10 -> P3 -> 12 -> CS -> 14 -> P4 -> 16 -> CS -> 18 -> P5 -> 20
    public void makeGanttChart() {
        sortProcesses();
        int currentTime = 0;
        int totalBusyTime = 0;
        int totalTime = 0;

        System.out.print(currentTime);
        for (int i = 0; i < processes.size(); i++) {
            ProcessControlBlock process = processes.get(i);

            // Handle idle state if current time is less than the arrival time of the next process
            if (currentTime < process.getArrivalTime()) {
                System.out.print(" -> Idle -> " + process.getArrivalTime());
                totalTime += process.getArrivalTime() - currentTime;
                currentTime = process.getArrivalTime();
            }

            // Print process execution
            System.out.print(" -> P" + process.getId() + " -> " + (currentTime + process.getBurstTime()));
            totalBusyTime += process.getBurstTime();
            totalTime += process.getBurstTime();
            currentTime += process.getBurstTime();

            // Update process completion time
            process.setCompletionTime(currentTime);

            // Print context switching
            if (i < processes.size() - 1) {
                currentTime += contextSwitchingTime;
                totalTime += contextSwitchingTime;
                System.out.print(" -> CS -> " + currentTime);
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
