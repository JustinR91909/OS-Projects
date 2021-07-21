import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class JobScheduler2 {
    public static void main(String[] args){
        double FCFSavg = 0;
        double SJFavg = 0;
        double RR2avg = 0;
        double RR5avg = 0;
        for(int i=0; i<20; i++) {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter # of jobs: ");
            int jobNum = in.nextInt();
            int[] jobs = new int[jobNum];
            createAndWriteFile(jobNum);
            jobs = readFile(jobs);
            System.out.println();

            //part 1.c
            int[] jobsFCFS = jobs.clone();
            FCFSavg += FCFS(jobsFCFS);
            int[] jobsSJF = jobs.clone();
            SJFavg += SJF(jobsSJF);
            int[] jobsRR2 = jobs.clone();
            RR2avg += RR2(jobsRR2);
            int[] jobsRR5 = jobs.clone();
            RR5avg += RR5(jobsRR5);
        }
        System.out.println("FCFS avg over 20: " + FCFSavg/20);
        System.out.println("SJF avg over 20: " + SJFavg/20);
        System.out.println("RR2 avg over 20: " + RR2avg/20);
        System.out.println("RR5 avg over 20: " + RR5avg/20);
    }

    public static void createAndWriteFile(int jobSize){
        try {
            File myObj = new File("job.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        Random rand = new Random();
        try {
            FileWriter myWriter = new FileWriter("job.txt");
            for(int i=0; i<jobSize; i++){
                myWriter.write("job" + (i+1) + "\n" + (rand.nextInt(30)+1) + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static int[] readFile(int[] jobList){
        try {
            File myObj = new File("C:\\Users\\jprob\\IdeaProjects\\OSProj1\\job.txt");
            Scanner myReader = new Scanner(myObj);
            int counter = 1;
            int index = 0;
            while (myReader.hasNextLine()) {
                if(counter % 2 == 0){
                    int data = Integer.parseInt(myReader.nextLine());
                    jobList[index] = data;
                    index++;
                }
                else{
                    myReader.nextLine();
                }
                counter++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return jobList;
    }

    public static double FCFS(int[] jobList){
        int time = 0;
        int sum = 0;
        System.out.println("First Come First Serve:");
        //print the order that the job's will be completed in
       // System.out.print("Job order: ");
        for(int j = 0; j<jobList.length; j++){
           // System.out.print("Job " + (j+1) + " (" + jobList[j] + "), ");
        }
        System.out.println();
        //print the Gantt Chart for FCFS while also calculating the total sum of the time
        System.out.println("Gantt for FCFS:");
        for(int i = 0; i < jobList.length; i++){
            time += jobList[i];
            //System.out.print("[Job" + (i+1) + ": " + time + "ms]");
            sum += jobList[i]*(jobList.length - i);
        }
        System.out.println("");
        //calculate and display the average time for FCFS
        double avg = ((double)sum)/ jobList.length;
        System.out.println("Average time: " + avg);
        return avg;
    }

    public static double SJF(int[] jobList){
        Arrays.sort(jobList);
        int time = 0;
        int sum = 0;
        System.out.println("Shortest Job First:");
        //print the order that the job's will be completed in
       // System.out.print("Job order: ");
        for(int j = 0; j<jobList.length; j++){
            //System.out.print("Job " + (j+1) + " (" + jobList[j] + "), ");
        }
        System.out.println();
        //print the Gantt Chart for SJF while also calculating the total sum of the time
        System.out.println("Gantt for SJF:");
        for(int i = 0; i < jobList.length; i++){
            time += jobList[i];
           // System.out.print("[P" + (i+1) + ": " + time + "ms]");
            sum += jobList[i]*(jobList.length - i);
        }
        System.out.println("");
        //calculate and display the average time for SJF
        double avg = ((double)sum) / jobList.length;
        System.out.println("Average time: " + avg);
        return avg;
    }
    public static double RR2(int[] jobList){
        System.out.println("Round Robin w/ Time Slice of 2:");
        int zeroCount = 0;
        int time = 0;
        double avg = 0;
        //print the order that the job's will be completed in
      //  System.out.print("Job order: ");
        for(int j = 0; j<jobList.length; j++){
           // System.out.print("Job " + (j+1) + " (" + jobList[j] + "), ");
        }
        //print the Gantt Chart for RR2 while also calculating the total sum of the time
        System.out.println("Gantt Chart for Round Robin (time slice of 2): ");
        while(zeroCount < jobList.length){
            for(int i = 0; i < jobList.length; i++){
                if(jobList[i] == 0){
                    zeroCount++;
                }
                else{
                    if(jobList[i] >= 2){
                        time += 2;
                        //System.out.print("[J" + (i+1) + ": " + time + "]");
                        jobList[i] -= 2;
                        if(jobList[i] == 0){
                            avg += time;
                        }
                    }
                    else{
                        time += 1;
                        //System.out.print("[J" + (i+1) + ": " + time + "]");
                        jobList[i] -= 1;
                        if(jobList[i] == 0){
                            avg += time;
                        }
                    }
                }
            }
            if(zeroCount == jobList.length){}
            else{
                    zeroCount = 0;
            }
            System.out.println();
        }
        //calculate and print the average
        avg = avg/jobList.length;
        System.out.println("average time: " + avg + "ms");
        return avg;
    }
    public static double RR5(int[] jobList){
        System.out.println("Round Robin w/ Time Slice of 5:");
        int zeroCount = 0;
        int time = 0;
        double avg = 0;
        //print the order that the job's will be completed in
       // System.out.print("Job order: ");
        for(int j = 0; j<jobList.length; j++){
            //System.out.print("Job " + (j+1) + " (" + jobList[j] + "), ");
        }
        //print the Gantt Chart for RR2 while also calculating the total sum of the time
        System.out.println("Gantt Chart for Round Robin (time slice of 5): ");
        while(zeroCount < jobList.length){
            for(int i = 0; i < jobList.length; i++){
                if(jobList[i] == 0){
                    zeroCount++;
                }
                else{
                    if(jobList[i] >= 5){
                        time += 5;
                       // System.out.print("[J" + (i+1) + ": " + time + "]");
                        jobList[i] -= 5;
                        if(jobList[i] == 0){
                            avg += time;
                        }
                    }
                    else if(jobList[i] == 4){
                        time += 4;
                       // System.out.print("[J" + (i+1) + ": " + time + "]");
                        jobList[i] -= 4;
                        if(jobList[i] == 0){
                            avg += time;
                        }
                    }
                    else if(jobList[i] == 3){
                        time += 3;
                        //System.out.print("[J" + (i+1) + ": " + time + "]");
                        jobList[i] -= 3;
                        if(jobList[i] == 0){
                            avg += time;
                        }
                    }
                    else if(jobList[i] == 2){
                        time += 2;
                        //System.out.print("[J" + (i+1) + ": " + time + "]");
                        jobList[i] -= 2;
                        if(jobList[i] == 0){
                            avg += time;
                        }
                    }
                    else{
                        time += 1;
                       // System.out.print("[J" + (i+1) + ": " + time + "]");
                        jobList[i] -= 1;
                        if(jobList[i] == 0){
                            avg += time;
                        }
                    }
                }
            }
            if(zeroCount == jobList.length){}
            else{
                zeroCount = 0;
            }
            System.out.println();
        }
        //calculate and print the average
        avg = avg/jobList.length;
        System.out.println("average time: " + avg + "ms");
        return avg;
    }
}