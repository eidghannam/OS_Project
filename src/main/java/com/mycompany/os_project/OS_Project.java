/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.os_project;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Eid
 */
public class OS_Project {

    public static void main(String[] args) {
        
        ArrayList<ProcessControlBlock> processes = new ArrayList<>();
        ArrayList<Integer> arrivalTimes = new ArrayList<>();
        ArrayList<Integer> burstTimes = new ArrayList<>();
        int contextSwitchingTime = 0;
        int quantumTime = 0;
        String fileName = "C:\\Users\\MSI\\Desktop\\os_test.txt";

        File file = new File(fileName);
        try {
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextLine()) {
                String[] arrivalTimesStr = scanner.nextLine().split(" ");
                for (String time : arrivalTimesStr) {
                    arrivalTimes.add(Integer.parseInt(time));
                }
            }
            if (scanner.hasNextLine()) {
                String[] burstTimesStr = scanner.nextLine().split(" ");
                for (String time : burstTimesStr) {
                    burstTimes.add(Integer.parseInt(time));
                }
            }
            if (scanner.hasNextLine()) {
                contextSwitchingTime = Integer.parseInt(scanner.nextLine().trim());
            }
            if (scanner.hasNextLine()) {
                quantumTime = Integer.parseInt(scanner.nextLine().trim());
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // Create ProcessControlBlock objects and add them to the ArrayList
        for (int i = 0; i < arrivalTimes.size(); i++) {
            processes.add(new ProcessControlBlock(i + 1, arrivalTimes.get(i), burstTimes.get(i)));
        }

        // nOW i want the user to choose the scheduling algorithm 1: FCFS, 2: Round Robin, 3: SRT, 4: All, anythingelse: exit
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the scheduling algorithm:");
        System.out.println("1: FCFS");
        System.out.println("2: Round Robin");
        System.out.println("3: SRT");
        System.out.println("4: All");
        System.out.println("Anything else: Exit");
        int choice = scanner.nextInt();
        scanner.close();
        switch (choice) {
            case 1:
                FCFS fcfs = new FCFS(processes, contextSwitchingTime);
                fcfs.makeGanttChart();
                break;
            case 2:
                RoundRobin rr = new RoundRobin(processes, contextSwitchingTime, quantumTime);
                rr.makeGanttChart();
                break;
            case 3:
                SRT srt = new SRT(processes, contextSwitchingTime);
                srt.makeGanttChart();
                break;
            case 4:
                FCFS fcfsAll = new FCFS(processes, contextSwitchingTime);
                fcfsAll.makeGanttChart();
                RoundRobin rrAll = new RoundRobin(processes, contextSwitchingTime, quantumTime);
                rrAll.makeGanttChart();
                SRT srtAll = new SRT(processes, contextSwitchingTime);
                srtAll.makeGanttChart();
                break;
            default:
                System.out.println("Wrote by: Eid Jawad EID Hamamdeh 211023");
                return;
        }
    }
}
