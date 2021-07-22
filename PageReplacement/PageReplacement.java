import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PageReplacement {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter # of page frames (3-6): ");
        int pageFrame = in.nextInt();
        int FIFOfaults = 0;
        int LRUfaults = 0;
        int OPTfaults = 0;
        int[] referenceString = new int[30];
        createAndWriteFile(pageFrame);
        referenceString = readFile(referenceString);
        /*for(int i=0; i<50; i++){


            int[] referenceString = new int[30];
            createAndWriteFile(pageFrame);
            referenceString = readFile(referenceString);
            FIFOfaults += FIFO(referenceString, pageFrame);
            LRUfaults += LRU(referenceString, pageFrame);
            OPTfaults += Optimal(referenceString, pageFrame);
        }
        System.out.println("FIFO faults: " + FIFOfaults + " LRU faults: " + LRUfaults + " Optimal faults: " + OPTfaults);
        double FIFOavg = FIFOfaults/50.0;
        double LRUavg = LRUfaults/50.0;
        double OPTavg = OPTfaults/50.0; */
        //System.out.println("FIFO avg: " + FIFOavg + " LRU avg: " + LRUavg + " Optimal avg: " + OPTavg);
       /* for(int i = 0; i<referenceString.length; i++){
            System.out.print(referenceString[i] + " ");
        }
        System.out.println(); */
        int test[] = new int[]{0,1,2,3,0,1,4,0,1,6,5,4,7,6,5};
        System.out.print("FIFO faults: " + FIFO(test, pageFrame) + " ");
        System.out.print("LRU faults: " + LRU(test, pageFrame) + " ");
       // System.out.print("Optimal faults: " + Optimal(test, pageFrame));
        //System.out.print("Optimal faults: " + Optimal(referenceString, pageFrame));
    }

    public static void createAndWriteFile(int pageFrameVal){
        try {
            File myObj = new File("ReferenceString.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                //System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        Random rand = new Random();
        try {
            FileWriter myWriter = new FileWriter("ReferenceString.txt");
            myWriter.write("NumberOfPageFrame Value:\n" + pageFrameVal + "\nReference String:\n");
            for(int i=0; i<30; i++){
                myWriter.write(rand.nextInt(8)+"");
            }
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static int[] readFile(int[] refString){
        try {
            String line4 = Files.readAllLines(Paths.get("C:\\Users\\jprob\\IdeaProjects\\OSProj1\\ReferenceString.txt")).get(3);
            //System.out.println(line4);
            for(int i=0; i<30; i++){
                refString[i] = (int) line4.charAt(i) - '0';
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return refString;
    }

    public static int FIFO(int[] ref, int pf){
        int faults = 0;
        //Queue is useful for FIFO
        Queue<Integer> storage = new LinkedList<>();
        //Hashset is useful to avoid duplicates in page frame
        HashSet<Integer> pageFrame = new HashSet<>(pf);
        for(int i=0; i<ref.length; i++){
            //fill up page frame if it is empty
            if(pageFrame.size() < pf){
                if(!pageFrame.contains(ref[i])){
                    pageFrame.add(ref[i]);
                    faults++;
                    storage.add(ref[i]);
                }
            }
            //when page frame is full check storage for oldest index
            else{
                //if page frame does not contain current reference int
                if(!pageFrame.contains(ref[i])){
                    //remove oldest reference int from page frame and replace with new one
                    int old = storage.peek();
                    storage.remove();
                    pageFrame.remove(old);
                    storage.add(ref[i]);
                    pageFrame.add(ref[i]);
                    faults++;
                }
            }
        }
        return faults;
    }

    public static int LRU(int[] ref, int pf){
        int faults = 0;
        //ArrayList is useful for LRU
        ArrayList<Integer> storage = new ArrayList<>();
        //Hashset is useful to avoid duplicates in page frame
        HashSet<Integer> pageFrame = new HashSet<>(pf);
        for(int i=0; i<ref.length; i++){
            //fill up page frame if it is empty
            if(pageFrame.size() < pf){
                if(!pageFrame.contains(ref[i])){
                    pageFrame.add(ref[i]);
                    faults++;
                    storage.add(ref[i]);
                }
            }
            //when page frame is full check storage for oldest index
            else{
                //if page frame does not contain current reference int
                if(!pageFrame.contains(ref[i])){
                    //remove LRU int from page frame and replace with new one
                    pageFrame.remove(storage.get(0));
                    pageFrame.add(ref[i]);
                    storage.remove(0);
                    storage.add(ref[i]);
                    faults++;
                }
                //if page frame does contain current ref int, make sure it is considered recently used
                else{
                    for(int j=0; j<storage.size(); j++){
                        if(storage.get(j) == ref[i]){
                            storage.remove(j);
                            storage.add(ref[i]);
                        }
                    }
                }
            }
        }
        return faults;
    }

    public static int Optimal(int[] ref, int pf){
        int faults = 0;
        ArrayList<Integer> pageFrame = new ArrayList<>();
        int occurences = 0;
        // for optimal page replacement I chose to count hits rather than faults
        for(int i=0; i<ref.length; i++){
            // if the page frame contains the current reference int, add to occurrences
            if(pageFrame.contains(ref[i])){
                occurences++;
            }
            // if the page frame is has room, add nonduplicate reference int to occurrences
            else if(pageFrame.size() < pf){
                pageFrame.add(ref[i]);
            }
            // if page frame is full and does not contain current reference int, calculate optimal
            else{
                int longestTime = -1;
                int farthest = i+1;
                for(int j=0; j<pageFrame.size(); j++){
                    int counter;
                    for(counter= i+1; counter<ref.length; counter++){

                        if(pageFrame.get(j) == ref[counter]){
                            if(counter > farthest){
                                farthest = counter;
                                longestTime = j;
                            }
                            break;
                        }
                    }
                    if(counter == ref.length){
                        boolean check = false;
                        for(int a=0; a<pageFrame.size(); a++){
                            if(pageFrame.get(a) == ref[i])
                                check = true;
                        }
                        if(!check)
                            pageFrame.set(j, ref[i]);
                    }
                }
                if(longestTime == -1){
                    boolean check = false;
                    for(int a=0; a<pageFrame.size(); a++){
                        if(pageFrame.get(a) == ref[i])
                            check = true;
                    }
                    if(!check)
                        pageFrame.set(0,ref[i]);
                }
                else{
                    boolean check = false;
                    for(int a=0; a<pageFrame.size(); a++){
                        if(pageFrame.get(a) == ref[i])
                            check = true;
                    }
                    if(!check)
                        pageFrame.set(longestTime,ref[i]);
                }
            }
        }
        faults = 30-occurences;
        return faults;
    }
}
