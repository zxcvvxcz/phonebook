import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.*;

enum Months{
    January(31), February(28),    March(31),   April(30),
    May(31),     June(30),        July(31),    August(31),
    September(30), October(31),  November(30), December(31);

    private final int days;
    Months(int days) {
         this.days=days;
    }

    public int getDays() { return days;}
}
public class Phonebook {
    private static final String PHONE_NUMBER_REGEX="^(02|010)-\\d{4}-\\d{4}";
    private static final String BIRTHDAY_REGEX="^[0-9]{6}";
    private static final String AGE_REGEX="[0-9]+";

    public static void main(String[] args) throws IOException{
        List<Person> phoneBook=new ArrayList<>();

        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        String input=br.readLine();

        while(!input.equals("exit")){
            if(input.equals("")){
                System.out.println("Phone book");
                System.out.println("1.Add person");
                System.out.println("2.Remove person");
                System.out.println("3.Print phone book");
            }
            else if(input.equals("1"))  { add(phoneBook); }
            else if(input.equals("2"))  { remove(phoneBook); }
            else if(input.equals("3"))  { print(phoneBook);}
            input=br.readLine();
        }
    }
    private static void add(List<Person> phoneBook) throws IOException{
        System.out.println("Select Type");
        System.out.println("1.Person");
        System.out.println("2.Work");
        System.out.println("3.Family");
        System.out.println("4.Friend");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputAdd = br.readLine();
        String name;
        String phoneNumberString;
        int phoneNumber;
        System.out.print("Name:");
        name = br.readLine();
        String[] fullName = name.split(" ");
        while(fullName.length!=2){
            System.out.println("이름 입력이 잘못되었습니다.");
            System.out.print("Name:");
            name = br.readLine();
            fullName = name.split(" ");
        }
        while(true) {
            System.out.print("Phone_number:");
            phoneNumberString = br.readLine();

            Pattern p1 = Pattern.compile(PHONE_NUMBER_REGEX);
            Matcher m1 = p1.matcher(phoneNumberString);
            if (m1.matches()) {
                phoneNumberString = phoneNumberString.replace("-", "");
                phoneNumber = Integer.parseInt(phoneNumberString);
                break;
            } else {
                    System.out.println("잘못된 번호입니다. 처음부터 다시 입력해주세요.");
                }
            }
        switch (inputAdd) {
            case "1":
                Person newPerson=new Person(fullName[0],fullName[1],phoneNumber);
                phoneBook.add(newPerson);
                break;
            case "2":
                System.out.print("Team:");
                String teamAdd=br.readLine();
                Work newWork=new Work(fullName[0],fullName[1],phoneNumber,teamAdd);
                phoneBook.add(newWork);
                break;
            case "3":
                System.out.print("Birthday:");
                String birthdayAdd=br.readLine();
                Pattern pBirthday=Pattern.compile(BIRTHDAY_REGEX);
                Matcher mBirthday=pBirthday.matcher(birthdayAdd);
                boolean correctBirthday=true;
                int month=Integer.parseInt(birthdayAdd.substring(2,4));
                int date=Integer.parseInt(birthdayAdd.substring(4,6));
                if(month>12 || month<=0)
                    correctBirthday=false;
                else {
                    if (date <= 0)
                        correctBirthday = false;
                    for (Months m : Months.values()) {
                        if (m.ordinal() == month - 1) {
                            if (m.getDays() < date)
                                correctBirthday = false;
                            break;
                        }
                    }
                }
                while(!correctBirthday) {
                    while (!mBirthday.matches() || !correctBirthday) {
                        System.out.println("잘못된 생일입니다.");
                        System.out.print("Birthday:");
                        birthdayAdd = br.readLine();
                        mBirthday = pBirthday.matcher(birthdayAdd);
                        correctBirthday = true;
                    }
                    month = Integer.parseInt(birthdayAdd.substring(2, 4));
                    date = Integer.parseInt(birthdayAdd.substring(4, 6));
                    if (month > 12 || month <= 0)
                        correctBirthday = false;
                    else {
                        if (date <= 0)
                            correctBirthday = false;
                        for (Months m : Months.values()) {
                            if (m.ordinal() == month - 1) {
                                if (m.getDays() < date)
                                    correctBirthday = false;
                                break;
                            }
                        }
                    }
                }
                Family newFamily=new Family(fullName[0],fullName[1],phoneNumber,birthdayAdd);
                phoneBook.add(newFamily);
                break;
            case "4":
                System.out.print("Age:");
                String ageAdd=br.readLine();
                Pattern pAge=Pattern.compile(AGE_REGEX);
                Matcher mAge=pAge.matcher(ageAdd);
                int ageAddInt;
                while(true) {
                    if (mAge.matches()) {
                        ageAddInt = Integer.parseInt(ageAdd);
                        break;
                    } else {
                        System.out.println("잘못된 나이 입력입니다.");
                    }
                }
                Friend newFriend=new Friend(fullName[0],fullName[1],phoneNumber,ageAddInt);
                phoneBook.add(newFriend);
                break;
            case "quit":
                return;
            default:
                System.out.println("잘못된 입력입니다.");
        }
        System.out.println("Successfully added new person.");
    }


    private static void remove(List<Person> phoneBook) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter index of person:");
        String idx = br.readLine();
        int Index=phoneBook.size()+1;
        boolean canRemove = false;
        while (!canRemove) {
            try {
                Index = Integer.parseInt(idx)-1;
                canRemove = true;
            } catch (NumberFormatException e) {
                System.out.println("0 이상의 정수를 입력하세요.");
                canRemove = false;
            }
        }
        if (Index>0-1 && phoneBook.size() > Index) {
            phoneBook.remove(Index);
            System.out.println("A person is successfully deleted from the Phone Book!");
        } else {
            System.out.println("Person does not exist!");
        }
    }



    private static void print(List<Person> phoneBook){
        System.out.println("Phone Book Print");
        int i=1;
        for(Person p:phoneBook){
            System.out.print(i+".");
            p.print();
            i++;
        }
    }
}

class Person {
    private String firstName;
    private String lastName;
    private int phoneNumber;

    public Person(String firstName,String lastName,int phoneNumber){
        this.firstName=firstName;
        this.lastName=lastName;
        this.phoneNumber=phoneNumber;
    }

    public void setFirstName(String firstName){     //set firstName String
        this.firstName=firstName;
    }

    public String getFirstName(){                   //return firstName
        return firstName;
    }

    public void setLastName(String lastName){
        this.lastName=lastName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setPhoneNumber(int phoneNumber){
        this.phoneNumber=phoneNumber;
    }
    //02-XXXX-XXXX or 010-XXXX-XXXX
    public int getPhoneNumber(){
        return phoneNumber;
    }

    public void print(){
        String phoneNumberString=Integer.toString(phoneNumber);
        if(phoneNumberString.substring(0,1).equals("2")){
            phoneNumberString="0"+phoneNumberString.substring(0,1)+"-"+phoneNumberString.substring(1,5)+"-"+phoneNumberString.substring(5,9);
        }
        else if(phoneNumberString.substring(0,2).equals("10")){
            phoneNumberString="0"+phoneNumberString.substring(0,2)+"-"+phoneNumberString.substring(2,6)+"-"+phoneNumberString.substring(6,10);
        }
        System.out.println(getFirstName()+" "+getLastName()+ "_"+phoneNumberString);
    }

}

class Work extends Person{
    public Work(String firstName,String lastName,int phoneNumber, String team){
        super(firstName,lastName,phoneNumber);
        this.team=team;
    }

    public void setTeam(String team){               //set team
        this.team=team;
    }

    public String getTeam(){                        //get team
        return team;
    }

    @Override
    public void print(){                    //print the work object, using the parent function to get the attribute
        String phoneNumberString=Integer.toString(getPhoneNumber());
        if(phoneNumberString.substring(0,1).equals("2")){
            phoneNumberString="0"+phoneNumberString.substring(0,1)+"-"+phoneNumberString.substring(1,5)+"-"+phoneNumberString.substring(5,9);
        }
        else if(phoneNumberString.substring(0,2).equals("10")){
            phoneNumberString="0"+phoneNumberString.substring(0,2)+"-"+phoneNumberString.substring(2,6)+"-"+phoneNumberString.substring(6,10);
        }
        System.out.println(getFirstName()+" "+getLastName()+"_"+phoneNumberString+"_"+getTeam());
    }

    private String team;                    //team that the person is in

}

class Friend extends Person{
    public Friend(String firstName,String lastName,int phoneNumber, int age){
        super(firstName,lastName,phoneNumber);
        this.age=age;
    }
    public void setAge(){                   //set age
        this.age=age;
    }
    public int getAge(){                    //return age
        return age;
    }

    @Override
    public void print(){                    //print the work object, using the parent function to get the attribute
        String phoneNumberString=Integer.toString(getPhoneNumber());
        if(phoneNumberString.substring(0,1).equals("2")){
            phoneNumberString="0"+phoneNumberString.substring(0,1)+"-"+phoneNumberString.substring(1,5)+"-"+phoneNumberString.substring(5,9);
        }
        else if(phoneNumberString.substring(0,2).equals("10")){
            phoneNumberString="0"+phoneNumberString.substring(0,2)+"-"+phoneNumberString.substring(2,6)+"-"+phoneNumberString.substring(6,10);
        }
        System.out.println(getFirstName()+" "+getLastName()+"_"+phoneNumberString+"_"+getAge());
    }

    private int age;                        //age of a friend

}

class Family extends Person{
    private String birthday;

    public Family(String firstName,String lastName,int phoneNumber, String birthday){
        super(firstName, lastName, phoneNumber);
        this.birthday=birthday;
    }

    public void setBirthday(String birthday){       //set birthday(YYMMDD) string
        this.birthday=birthday;
    }
    public String getBirthday(){                    //return birthday
        return birthday;
    }
    public int dDay(){                      //calculate the date difference between the birthday and current time

        int birthMonth=Integer.parseInt(birthday.substring(2,4));
        int birthDate=Integer.parseInt(birthday.substring(4,6));
        Date today=new Date();
        SimpleDateFormat formatter=new SimpleDateFormat("yyMMdd");
        String todayString=formatter.format(today);
        int todayMonthInt=Integer.parseInt(todayString.substring(2,4));
        int todayDateInt=Integer.parseInt(todayString.substring(4,6));
        Months[] m=Months.values();

        int dDay=0;
        for(int i=todayMonthInt-1;i!=birthMonth-1;i++){
            dDay+=m[i].getDays();
            if(i==11)
                i-=12;
        }
        dDay+=birthDate-todayDateInt;
        if(dDay<0)
            dDay+=365;
        return dDay;
    }

    @Override
    public void print(){                    //print the work object, using the parent function to get the attribute
        String phoneNumberString=Integer.toString(getPhoneNumber());
        if(phoneNumberString.substring(0,1).equals("2")){
            phoneNumberString="0"+phoneNumberString.substring(0,1)+"-"+phoneNumberString.substring(1,5)+"-"+phoneNumberString.substring(5,9);
        }
        else if(phoneNumberString.substring(0,2).equals("10")){
            phoneNumberString="0"+phoneNumberString.substring(0,2)+"-"+phoneNumberString.substring(2,6)+"-"+phoneNumberString.substring(6,10);
        }
        System.out.println(getFirstName()+" "+getLastName()+"_"+phoneNumberString+"_"+getBirthday()+"_"+dDay());
    }
}
