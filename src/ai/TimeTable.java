package ai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import static java.util.Collections.copy;
import java.util.List;
import java.util.Random;

public class TimeTable implements Cloneable{

    int id;
    int timetable_fitness;
    int total_clashes=0;

    int rows = 223;
    int cols = 3169;
    int totalRooms = 46;
    int room_capcaity = 50;
    int generalInfo[] = new int[2];

    List<Integer> roomCap = new ArrayList<Integer>();
    List<Integer> table[][] = new List[3][7]; //days and slots

    char Reg[][] = new char[rows][cols];
    char Rooms[] = new char[totalRooms];
    
    public void setCLash(int clashes){
        this.total_clashes=clashes;
    }

    public TimeTable(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<Integer>[][] getTimeTable() {
        return table;
    }

    //fitness function for Timetable
    int fitness(TimeTable temp) throws IOException {

        timetable_fitness = 5000;
        
        boolean case1=true;
        boolean case2=true;
        int case35=0;
        int case46=0;
        int case7=0;
        int repeat=0;

     

       case1=check_all_courses_exist(temp); //boolean 1000 minus
       
       if(case1 == false){
           timetable_fitness=timetable_fitness-600;
            //System.out.println("Check All Courses = "+1000);
       }
       
       case2=check_room_capacity(temp);     //boolean  500 minus
       
       if(case2 == false){
           timetable_fitness=timetable_fitness-300;
          // System.out.println("Check Room Capacity = "+500);
       }
        
       case35=(2)*check_two_exams_per_slot(temp);   //count multiply by weightage
       
       this.total_clashes=case35/2;
       
       case46=(1/2)*consecutive_paper_in_two_slots(temp); //count multiply by wiegthage  
       
       case7=(1/2)*three_exams_per_day(temp);  //count multiply by weightage
           
       repeat=check_repeat_courses_exist(temp);
       
       if(repeat > 0){
           timetable_fitness=timetable_fitness-500;
       }
              
       int total=case35+case46+case7;
       
        timetable_fitness=timetable_fitness-total;
  
        return timetable_fitness;
    }
    
     //fitness function case 1
    int check_repeat_courses_exist(TimeTable temp) throws IOException
    {
        int[] arr = new int[223];     //check array for all courses check

        for (int i = 0; i < 223; i++) {
            arr[i] = 0;
        }

        int Clist = 0;
        int count=0;
        int get_id = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                Clist = temp.table[i][j].size();

                for (int k = 0; k < Clist; k++) {
                    get_id = temp.table[i][j].get(k);
                    
                    if(arr[get_id]==1)
                    {
                       count++;
                       arr[get_id]=2;
                    }
                    else
                    {
                        arr[get_id]=1;
                    }

                }
            }
        }
       return count;
       
    }
    
    //fitness function case 1
    boolean check_all_courses_exist(TimeTable temp) throws IOException
    {
        int fitness=0;
        
        List<Integer> obj = new ArrayList<Integer>();
        feedRoomsData(obj);    //conatin room

        int[] arr = new int[223];     //check array for all courses check

        for (int i = 0; i < 223; i++) {
            arr[i] = 0;
        }

        /*------------------------------------------CASE 1(ALL COURSES EXIST)-----------------------------------------*/
        int total_capacity = 0;
        for (int i = 0; i < obj.size(); i++) {
            total_capacity += obj.get(i);      //total room count
        }

        int Clist = 0;
        int get_id = 0, total_student = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                Clist = temp.table[i][j].size();

                for (int k = 0; k < Clist; k++) {
                    get_id = temp.table[i][j].get(k);
                    arr[get_id] = 1;
                    total_student += getStudentscount(get_id);

                }

//                if (total_student > total_capacity) {
//                    fitness = fitness + 1;
//                } 
                total_student = 0;
            }
        }
           fitness=0;
        for (int i = 0; i < 223; i++) {
            if (arr[i] == 0) {
                fitness = fitness + 1;
            } 
        }
        
        if(fitness > 0)
        {
            return true;
        }
 
        else{
            return false;
        }
    }
    
    //fitness function case 2
     boolean check_room_capacity(TimeTable temp) throws IOException
    {
        int fitness=0;
        
        List<Integer> obj = new ArrayList<Integer>();
        feedRoomsData(obj);    //conatin room

        int[] arr = new int[223];     //check array for all courses check

        for (int i = 0; i < 223; i++) {
            arr[i] = 0;
        }

        /*------------------------------------------CASE 2(ROOM CHECK)-----------------------------------------*/
        int total_capacity = 0;
        for (int i = 0; i < obj.size(); i++) {
            total_capacity += obj.get(i);      //total room count
        }

        int Clist = 0;
        int get_id = 0, total_student = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                Clist = temp.table[i][j].size();

                for (int k = 0; k < Clist; k++) {
                    get_id = temp.table[i][j].get(k);
                    arr[get_id] = 1;
                    total_student += getStudentscount(get_id);

                }

                if (total_student > total_capacity) {
                    fitness = fitness + 1;
                } 
                total_student = 0;
            }
        } 
        if(fitness > 0)
        {
            return false;
        }
        else{
            return true;
        }
    }
     
     //fitness function case 3 and case 5
     int check_two_exams_per_slot(TimeTable temp)
     {
         /*------------------------------------------------CASE 3 && CASE 5(STUDENT HAVING TWO EXAMS/CLASH ON GIVEN SLOT)--------------------------*/
        int fitness=0;
        int count_total_courses=0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {

                List<Integer> st = new ArrayList<>();
                st = temp.table[i][j];       //for each List on index of timetable

                List<Integer> StudentInfo[] = new List[st.size()];          //make array equal to number of courses for students

                for (int k = 0; k < st.size(); k++) {
                    StudentInfo[k] = getStudentsList(st.get(k));
                    count_total_courses++;

                }
                count_total_courses=0;

                fitness =fitness + get_Clashes_on_given_slot(StudentInfo, st.size());
            }
        }
        return fitness;
     }
     
      //helper function case 3 and case 5
    int get_Clashes_on_given_slot(List<Integer>[] arr, int size) {
        int score = 0;

        for (int i = 0; i < size; i++) {    //pick up first list of student
            for (int j = 0; j < arr[i].size(); j++) {         //size of list of student
                score = score + get_clash_info(arr, arr[i].get(j), i, size);
            }
        }

        return score;
    }

    //helper function case 3 and case 5
    int get_clash_info(List<Integer>[] arr, int st_id, int List_student_index, int size) {
        int fitness = 0;
        boolean flag=true;
        
        for (int i = 0; i < size && i != List_student_index && flag==true; i++) {

            for (int j = 0; j < arr[i].size() && flag==true; j++) {
                if (arr[i].get(j) == st_id) {
                    fitness = fitness + 1;
                    flag=false;
                }
            }
        }
        return fitness;
    }

    //fitness function case 4 and case 6
    int consecutive_paper_in_two_slots(TimeTable temp)
    {
        int fitness=0;
        /*--------------------------------------------CASE 4 && CASE 6(CONSECUTIVE PAPER IN TWO SLOTS)--------------------------------------------------------------------*/
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                List<Integer> st = new ArrayList<>();
                st = temp.table[i][j];       //for each List on index of timetable

                List<Integer> StudentInfo[] = new List[st.size()];          //make array equal to number of courses for students

                for (int k = 0; k < st.size(); k++) {
                    StudentInfo[k] = getStudentsList(st.get(k));

                }

                

                if (j < 6) {
                    List<Integer> st1 = new ArrayList<>();
                    st1 = temp.table[i][j + 1];       //for each List on index of timetable

                    List<Integer> StudentInfo1[] = new List[st1.size()];          //make array equal to number of courses for students

                    for (int k = 0; k < st1.size(); k++) {
                        StudentInfo1[k] = getStudentsList(st1.get(k));
                    }

                    fitness = fitness + get_consecutive_slot_fitness(StudentInfo, st.size(), StudentInfo1, st1.size());
                }

            }
        }
        return fitness;
    }
    
    //helper function case 4 and case 6
    int get_consecutive_slot_fitness(List<Integer>[] arr1, int size1, List<Integer>[] arr2, int size2) {
        int score = 0;

        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < arr1[i].size(); j++) {
                score = score + get_fitness_score(arr2, size2, arr1[i].get(j));
            }

        }
        return score;
    }

     //helper function case 4 and case 6
    int get_fitness_score(List<Integer>[] arr, int size, int student_id) {
        int score = 0;
        boolean flag=true;
        for (int i = 0; i < size && flag==true; i++) {
            for (int j = 0; j < arr[i].size() && flag==true; j++) {
                if (student_id == arr[i].get(j)) {
                    score = score + 1;
                    flag=false;
                }

            }
        }
        return score;
    }

    //fitness function case 7
    int three_exams_per_day(TimeTable temp)
    {
        /*--------------------------------------------CASE 7(THREE EXAMS IN ONE DAY)---------------------------------------------------------------------------*/
       

        int fitness=0;
        for (int i = 0; i < 3; i++) {
             List<Integer> student = new ArrayList<>();
            for (int j = 0; j < 7; j++) {

                List<Integer> st = new ArrayList<>();
                st = temp.table[i][j];       //for each List on index of timetable

                List<Integer> StudentInfo[] = new List[st.size()];          //make array equal to number of courses for students

                for (int k = 0; k < st.size(); k++) {
                    StudentInfo[k] = getStudentsList(st.get(k));

                    for (int l = 0; l < StudentInfo[k].size(); l++) {

                        student.add(StudentInfo[k].get(l));
                        
                    }
                }

                if (j == 6) {
                    //System.out.println("Total Student in day "+(i+1)+" = "+student.size());
                    fitness = fitness + get_three_exams_Fitness(student,i);
                }

            }
        }
         return fitness;
    }
    
    //helper function case 7
    int get_three_exams_Fitness(List<Integer> arr,int index) {
        int score = 0;
        int fitness = 0;

        for (int i = 0; i < arr.size(); i++) {

            score = get_count_three_exam(arr, arr.get(i),i);
            if (score > 3) {
                fitness = fitness + 1;
            } 
        }

        return fitness;
    }

    //helper function case 7
    int get_count_three_exam(List<Integer> arr, int s_id,int index) {
        int count = 0;
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) == s_id && i != index) {

                count++;
            }
        }
        return count;
    }

   //get student list of particular id
    public List getStudentsList(int id) {
        List temp = new ArrayList();

        int r = id;

        for (int j = 0; j < cols; j++) {
            if (this.Reg[r][j] == '1') {
                temp.add(j);
            }
        }

        return temp;
    }

    public int getStudentscount(int id) {

        int r = id;
        int c1 = 0;

        for (int j = 0; j < cols; j++) {
            if (this.Reg[r][j] == '1') {

                c1++;
            }
        }

        return c1;
    }

    void feedRoomsData(List<Integer> roomCap) throws FileNotFoundException, IOException {

        File file = new File("capacity.txt");    //creates a new file instance  
        FileReader fr = new FileReader(file);   //reads the file  
        BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream  
        StringBuffer sb = new StringBuffer();    //constructs a string buffer with no characters  
        String line;
        int temp = 0;
        while ((line = br.readLine()) != null) {
            temp = Integer.parseInt(line);
            this.roomCap.add(temp);
        }
        fr.close();
    }

    void feedinfo() throws FileNotFoundException, IOException {

        File f = new File("general.txt");
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        int c, i = 0;

        while ((c = fr.read()) != -1) {
            if ((char) c > '0' && (char) c < '8') {
                if (i < 2) {
                    generalInfo[i++] = Character.getNumericValue((char) c);
                }
            }
        }
    }

    void populate() throws FileNotFoundException, IOException {

        File f = new File("registration.txt");
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        int c, k = 0, j = 0;

        while ((c = fr.read()) != -1) {
            if ((char) c == '0' || (char) c == '1') {
                Reg[k][j] = (char) c;
                j++;
                if (j >= 3169) {
                    j = 0;
                    k++;
                }
            }
        }

        int[] Div = getSubjects();

        for(int i=0;i<3;i++)
        {
            //Div[1] = Div[0] + Div[1];
            //Div[2] = Div[1] + Div[2];
            if(i > 0){
                Div[i]=Div[i-1]+Div[i];
            }
        }
        int day = 0;
        int i = 0;

        int rowT = 0;
        int colT = 0;

        for (i = 0; i < 3; i++) {
            for (j = 0; j < 7; j++) {
                table[i][j] = new ArrayList<Integer>();
            }
        }

        for (i = 0; i < 223; i++) {
            Random rand = new Random();
            colT = rand.nextInt(7);

            table[rowT][colT].add(i);

            if (i == Div[day]) {
                day++;
                rowT++;
            }
        }

//        int index = 0;
//        for (i = 0; i < 3; i++) {
//            for (j = 0; j < 7; j++) {
//                int s = table[i][j].size();
//
//                System.out.println("\nList No " + index);
//
//                for (k = 0; k < s; k++) {
//
//                    System.out.print(table[i][j].get(k) + " ");
//
//                }
//                System.out.println(" ");
//                index++;
//
//            }
//        }
    }

    int[] getSubjects() throws IOException {
//        int count = rows / 3;
//        if (rows % 3 == 0) {
//            int[] arr = {count, count, count};
//            return arr;
//        } else {
//            int[] arr = {count, count, count + 1};
//            return arr;
//        }

        feedinfo();
        
        int []arr=new int[generalInfo[0]];  //array to store course dividie
        int days=generalInfo[0];   //total days to distribute
        
        int count=rows;             //total courses to divide
        
        boolean flag=true;
        int inc=0;
        if(count%days == 0){
            int store=count/days;
            for(int i=0;i<days;i++){
                arr[i]=count/days;
            }
        }
        else{
            
            while(flag==true)
            {
                count--;
                inc++;
                if(count%days==0){
                   for(int i=0;i<days;i++){
                       arr[i]=count/days;
                       flag=false;
                   }
                }
            }
           
        }
        
        for(int i=0;i<inc;i++){
            Random rand=new Random();
            int st=rand.nextInt(days);
            arr[st]++;
        }
        
        
        
        return arr;

    }

    void printTimeTable() {
        for (int i = 0; i < 3; i++) { //Reg.length
            for (int m = 0; m < 7; m++) { //Reg[i].length
                
                System.out.print("Slot = "+m+"   ");
                System.out.println("Day = "+ i +"\n");
                
                List<Integer> st=new ArrayList();
                st=this.table[i][m];
                
                for(int k=0;k<st.size();k++){
                    System.out.print(this.table[i][m].get(k)+"  ");
                }
            }
            System.out.print("\n");
        }
    }
    
    //get clashes courses list
     List<Integer> check_two_exams_per_slot_courses(TimeTable temp){
         int fitness=0;
        int count_total_courses=0;
        
        List<Integer> courses=new ArrayList();
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {

                List<Integer> st = new ArrayList<>();
                st = temp.table[i][j];       //for each List on index of timetable

                List<Integer> StudentInfo[] = new List[st.size()];          //make array equal to number of courses for students

                for (int k = 0; k < st.size(); k++) {
                    StudentInfo[k] = getStudentsList(st.get(k));
                    count_total_courses++;

                }
                count_total_courses=0;

                clashes_List_courses(StudentInfo, st.size(),st,courses);
            }
        }
        return courses;
     }
     
    int clashes_List_courses(List<Integer>[] arr, int size,List<Integer> listcourses,List<Integer> courses) {
        int score = 0;

        for (int i = 0; i < size; i++) {    //pick up first list of student
            for (int j = 0; j < arr[i].size(); j++) {         //size of list of student
                get_clash_info_List(arr, arr[i].get(j), i,listcourses.get(i), size,courses);
            }
        }

        return score;
    }

    int get_clash_info_List(List<Integer>[] arr, int st_id, int List_student_index,int course_id, int size,List<Integer> courses) {
        int fitness = 0;
        boolean flag=true;
        
        for (int i = 0; i < size && i != List_student_index && flag==true; i++) {

            for (int j = 0; j < arr[i].size() && flag==true; j++) {
                if (arr[i].get(j) == st_id) {
                    
                     if(return_exist_course(courses, course_id) == true){
                         courses.add(course_id);
                         flag=false;
                     }
                    
                    
                    fitness = fitness + 1;
                    flag=false;
                }
            }
        }
        return fitness;
    }
        
    boolean return_exist_course(List<Integer> courses,int id){
        
        boolean flag=true;
        for(int i=0;i<courses.size() && flag==true;i++){
             if(courses.get(i)==id){
                 flag=false;
             }
        }
        return flag;
    }
    
    public void allocate(){
        for(int i=0;i<3;i++){
            for(int j=0;j<7;j++){
                this.table[i][j]=new ArrayList<Integer>();
            }
    }
     }
    
    @Override
   public Object clone() {
        try {
            super.clone();
            TimeTable c=new TimeTable(1);
            c.allocate();
            
            for(int i=0;i<3;i++){
                for(int j=0;j<7;j++){  
                    for(int k=0;k<this.table[i][j].size();k++){
                    
                        c.table[i][j].add(this.table[i][j].get(k));
                        
                    }
                   
                    
                }
            }
            c.total_clashes=this.total_clashes;
            c.timetable_fitness=this.timetable_fitness;
            c.rows=this.rows;
            c.cols=this.cols;
            c.totalRooms=this.totalRooms;
            return c;
            
        } 
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        
        return null;
    }

   public TimeTable Set_timetable(TimeTable temp){
   
       int []a=new int[223];
       List<Integer> obj=new ArrayList();
       
       for(int i=0;i<223;i++){
           a[i]=0;
       }
       
      int total=0;
       
        for(int i=0;i<3;i++){
           for(int j=0;j<7;j++){
               total +=temp.table[i][j].size();
           }
        }
        System.out.println("Before Total Courses = "+total);
        
       
       int count=0;
       int s1=0;
       for(int i=0;i<3;i++){
           for(int j=0;j<7;j++){
               
               for(int k=0;k<temp.table[i][j].size();k++){
                   int s=temp.table[i][j].get(k);
                   count++;
                   
                   if(a[s] == 0){
                       a[s]=1;
                   }
                   else
                   {
                       count--;
                       s1++;
                       temp.table[i][j].remove(k);
                       
                   }
                   
               }
           }
       }
       System.out.println("Total Remove = "+s1);
       System.out.println("Count = "+count);
       
       for(int i=0;i<223;i++){
           if(a[i] == 0){
               obj.add(i);
           }
       }
       
        total=0;
       
        for(int i=0;i<3;i++){
           for(int j=0;j<7;j++){
               total +=temp.table[i][j].size();
           }
        }
           
       System.out.println("Before Total Courses = "+total);
       
       
       Random rand=new Random();

       System.out.println("NON Register Course = "+obj.size());
       for(int i=0;i<obj.size();i++){
       
           int r=rand.nextInt(3);
            int c=rand.nextInt(7);
            
            temp.table[r][c].add(obj.get(i));
           
       }
       total=0;
       
        for(int i=0;i<3;i++){
           for(int j=0;j<7;j++){
               total +=temp.table[i][j].size();
           }
        }
           
       System.out.println("Total Courses = "+total);
       
      return temp;
   
   }
    
    

}
