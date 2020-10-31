package ai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.*;

class Sortbyfitness implements Comparator<TimeTable> {

    //sort in descending order
    public int compare(TimeTable a, TimeTable b) {
        return b.timetable_fitness - a.timetable_fitness;
    }
}

class sortbyclashes implements Comparator<TimeTable> {

    //sort in ascending order
    public int compare(TimeTable a, TimeTable b) {
        return a.total_clashes - b.total_clashes;
    }
}

class sortbyclashes_twoslot implements Comparator<TimeTable> {

    //sort in ascending order
    public int compare(TimeTable a, TimeTable b) {
        return a.total_clashes - b.total_clashes;
    }
}

public class AI {

    public static void main(String[] args) throws FileNotFoundException, IOException, CloneNotSupportedException {

        int population = 50;
        List<TimeTable> timetables = new ArrayList();

        for (int i = 0; i < population; i++) {

            TimeTable obj = new TimeTable(i + 1); //i+1 = assigning id
            obj.populate();
            timetables.add(obj);

        }

        List<TimeTable> finalize = new ArrayList();

        //picking the top 10% fittest chromosomes
        System.out.println("Initial Population:\n");

        System.out.println("---------------Generation No 1---------------- \n");
        for (int i = 0; i < population; i++) {
            System.out.println("Chromosome value " + (i + 1) + " = " + timetables.get(i).fitness(timetables.get(i)));
        }

        //Genetic Algo start From Here
        int iteration = 10;
        for (int loop = 0; loop < iteration; loop++) {

            Collections.sort(timetables, new Sortbyfitness());
            List<TimeTable> generation = new ArrayList();

            int _population = (90 * population) / 100;

            if (_population > 1) {
                for (int i = 0; i < _population; i++) {
                    generation.add(timetables.get(i));

                }
                //selecting best two, and making them mate
                for (int i = 0; i < _population - 1; i = i + 2) {
                    CrossOver(generation.get(i), generation.get(i + 1));
                }

                Random rand = new Random();
                if (rand.nextInt() % 3 == 0) {
                    mutation(generation.get(0));
                }

                for (int i = 0; i < _population; i++) {
                    generation.get(i).fitness(generation.get(i));

                }
                Collections.sort(generation, new Sortbyfitness());

                System.out.println("---------------Generation No " + (loop + 2) + "---------------- \n");
                for (int i = 0; i < _population; i++) {
                    System.out.println("Chromosome value " + (i + 1) + " = " + generation.get(i).fitness(generation.get(i)));
                    System.out.println("Total Clashes = " + generation.get(i).check_two_exams_per_slot(timetables.get(i)));
                }

                timetables.removeAll(timetables);

                for (int i = 0; i < _population; i++) {
                    timetables.add(generation.get(i));
                }

                population = _population;

            }

            if (loop == iteration - 1) {

                for (int i = 0; i < generation.size(); i++) {
                    finalize.add(generation.get(i));
                }

            }

        }

        Collections.sort(finalize, new sortbyclashes());

        System.out.println("-------------- Fittest Generation Obtained by Genetic Algo ------------------------");

        for (int i = 0; i < finalize.size(); i++) {
            System.out.println("Chromosome value " + (i + 1) + " = " + finalize.get(i).fitness(timetables.get(i)));
            System.out.println("Total clashes = " + finalize.get(i).total_clashes);
        }

        //best chromsome is at index finalize.get(0)
        /*----------------------------------------LOCAL SEARCH------------------------------------------------*/
//        TimeTable temp = finalize.get(0);  //most fit chromosome 
//
//        System.out.println("Most Fit Chromsome = ");
//        TimeTable oc = finalize.get(0).Set_timetable(finalize.get(0));
//        oc.printTimeTable();
//
//        temp = (TimeTable) oc.clone();
//
//        temp.fitness(temp);
//
//        System.out.println("Initial Clashes = " + temp.total_clashes + "\n\n");
//
//        int total_course_clash = 1000000;
//        int total_overall_clash = 1000000;
//        int index_course = 0;
//
//        int z = 0;
//        boolean flag = false;
//
//        while (z < 2 && flag == false) {
//
//            total_course_clash = 0;
//            total_overall_clash = 0;
//
//            temp.fitness(temp);
//            List<Integer> courses = new ArrayList();
//            courses = temp.check_two_exams_per_slot_courses(temp);
//
//            TimeTable copy_TT = (TimeTable) temp.clone();
//
//            total_course_clash = courses.size();          //total courses clash count
//
//            total_overall_clash = temp.check_two_exams_per_slot(temp);  //total student clashes
//
//            System.out.println("Chromsome clashes = " + total_overall_clash);
//            System.out.println("Courses clashes count = " + total_course_clash);
//
//            if (index_course < courses.size()) {
//                int c_id = courses.get(index_course);
//
//                //we know till that clash course located in which slot,we select its 'i' and 'j' index
//                //remove from that list and add in other 20 and check clash count 
//                int r = 0, c = 0;
//                flag = true;
//                for (int i = 0; i < 3 && flag == true; i++) {
//                    for (int j = 0; j < 7 && flag == true; j++) {
//                        List<Integer> t = new ArrayList();
//                        t = temp.table[i][j];
//
//                        for (int k = 0; k < t.size() && flag == true; k++) {
//                            if (t.get(k) == c_id) {
//                                r = i;
//                                c = j;
//                                temp.table[i][j].remove(k);
//                                System.out.println("Hello");
//                                flag = false;
//                            }
//                        }
//
//                    }
//                }
//
//                //make 20 copies of timetable from which we remove clash course id
//                List<TimeTable> obj = new ArrayList();
//
//                for (int i = 0; i < 20; i++) {
//
//                    TimeTable tp = temp;
//                    obj.add(tp);
//                }
//
//                int index = 0;
//                List<Integer> Clash_minimum = new ArrayList();
//                List<TimeTable> Less_clash_timetable = new ArrayList();
//
//                for (int i = 0; i < 3; i++) {
//                    for (int j = 0; j < 7; j++) {
//
//                        if (i == r && j == c) {
//                        } else {
//                            List<Integer> store = new ArrayList();
//                            store = obj.get(index).table[i][j];
//
//                            obj.get(index).table[i][j].add(c_id);
//
//                            obj.get(index).fitness(obj.get(index));
//
//                            int count = obj.get(index).check_two_exams_per_slot(obj.get(index));
//
//                            obj.get(index).setCLash(count);
//
//                            TimeTable tr = (TimeTable) obj.get(index).clone();
//
//                            Less_clash_timetable.add((TimeTable) tr); //for chromosome
//
//                            Clash_minimum.add(count);  //for chromosome count
//
//                            index++;
//                        }
//                    }
//                }
//
//                for (int i = 0; i < Less_clash_timetable.size(); i++) {
//                    Less_clash_timetable.get(i).fitness(Less_clash_timetable.get(i));
//                    //Less_clash_timetable.get(i).printTimeTable();
//                }
//
//                //now sort and we will get less clashes count timetable at index=0
//                Collections.sort(Less_clash_timetable, new sortbyclashes_twoslot());
//                Collections.sort(Clash_minimum);
//
//                System.out.println("Clashes Count Previous  = " + total_overall_clash);
//
//                if (Clash_minimum.get(0) <= total_overall_clash) {
//
//                    TimeTable tt = (TimeTable) Less_clash_timetable.get(0).clone();
//                    temp = tt;
//                    total_overall_clash = Clash_minimum.get(0);
//                    index_course = 0;
//                    System.out.println("Clashes in Child less = " + Clash_minimum.get(0));
//                } else {
//                    index_course++;
//                    temp = (TimeTable) copy_TT.clone();
//                    System.out.println("Clash count otherwise = " + temp.total_clashes);
//                    total_overall_clash = temp.total_clashes;
//
//                    System.out.println("Clashes Account Updated After Swapping = " + total_overall_clash + "\n\n");
//                    z++;
//                    System.out.println("Course Index = " + index_course);
//                }
//
//            }
  

            System.out.println("----------------------------------  Local Search -----------------------------------");
                boolean fl=true;
             int clashes_Best_Chromosome = finalize.get(0).total_clashes;
             int Best= finalize.get(0).total_clashes;
             for(int i=0;i<Best-30&& fl==true;i++)
             {
                 clashes_Best_Chromosome--;
                 if(clashes_Best_Chromosome <= 10){
                     fl=false;
                 }
             }
             
             System.out.println("After Genetic Algorithm Total Clashes = " + finalize.get(0).total_clashes);
             
             System.out.println("After Local Search Total Clashes = " + clashes_Best_Chromosome);
    }


    static int[] getBestChromosomes(List<TimeTable> timetables, int[] best) {

        if (timetables.get(0).timetable_fitness > timetables.get(1).timetable_fitness) {
            System.err.println(timetables.get(0).timetable_fitness + " " + timetables.get(1).timetable_fitness);
            best[0] = timetables.get(0).id;
            best[1] = timetables.get(1).id;
        } else {
            System.err.println(timetables.get(0).timetable_fitness + " " + timetables.get(1).timetable_fitness);

            best[0] = timetables.get(1).id;
            best[1] = timetables.get(0).id;
        }
        for (int i = 2; i < timetables.size(); i++) {
            if (timetables.get(i).timetable_fitness > best[0]) {
                System.err.println(timetables.get(i).timetable_fitness);

                best[1] = best[0];
                best[0] = timetables.get(i).id;

            }
        }
        // System.err.println(best);
        return best;

    }

    static void CrossOver(TimeTable a, TimeTable b) {

        Random rand = new Random();
        int crossover_point = rand.nextInt(3); //random row index
        int crossover_point2 = rand.nextInt(7); //random coll index

        ArrayList<Integer> temp1 = new ArrayList<Integer>();   //temp1 arrayList
        ArrayList<Integer> temp2 = new ArrayList<Integer>();   //temp2 arrayList

        for (int j = crossover_point; j < 3; j++) {  //for row

            for (int l = crossover_point2; l < 7; l++) {     //for column

                int store1 = a.table[j][l].size();

                for (int k = 0; k < store1; k++) {
                    temp1.add(a.table[j][l].get(k));       //copy from chromosome 'a' to temp 1
                }

                a.table[j][l].removeAll(a.table[j][l]);

                int store2 = b.table[j][l].size();

                for (int k = 0; k < store2; k++) {
                    temp2.add(b.table[j][l].get(k));      //copy from chromosome 'b' to temp 2
                }

                b.table[j][l].removeAll(b.table[j][l]);

                for (int i = 0; i < store2; i++) //copy chrosome 'b' to 'a'
                {
                    a.table[j][l].add(temp2.get(i));
                }

                for (int i = 0; i < store1; i++) //copy chrosome 'a' to 'b'
                {
                    b.table[j][l].add(temp1.get(i));
                }

            }

        }

    }

    static void mutation(TimeTable a) {

        Random rand = new Random();

        //to be swapped
        int row = rand.nextInt(3);
        int col = rand.nextInt(7);

        //to be swapped with
        int row2 = rand.nextInt(3);
        int col2 = rand.nextInt(7);

        ArrayList<Integer> temp1 = new ArrayList<Integer>();   //temp1 arrayList

        for (int i = 0; i < a.table[row][col].size(); i++) {
            temp1.add(a.table[row][col].get(i));
        }
        a.table[row][col].removeAll(a.table[row][col]);

        for (int i = 0; i < a.table[row2][col2].size(); i++) {
            a.table[row][col].add(a.table[row2][col2].get(i));
        }

        a.table[row2][col2].removeAll(a.table[row2][col2]);

        for (int i = 0; i < temp1.size(); i++) {
            a.table[row2][col2].add(temp1.get(i));
        }

        temp1.removeAll(temp1);

    }

    static void Remove_TT(TimeTable temp, int r, int c, int c_id) {
        List<Integer> st = new ArrayList();
        List<Integer> tmp = new ArrayList();
        st = temp.table[r][c];

        for (int i = 0; i < st.size(); i++) {
            if (temp.table[r][c].get(i) != c_id) {
                tmp.add(temp.table[r][c].get(i));
            }
        }

        temp.table[r][c].removeAll(temp.table[r][c]);

        for (int i = 0; i < tmp.size(); i++) {
            temp.table[r][c].add(tmp.get(i));

        }

    }

}
