import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class GymMemberShipYesOrNo {
    private String people;
    private  String name;
    private  boolean printMessages;

    public GymMemberShipYesOrNo(String name, boolean printMessages) {
        if(!name.isEmpty()){
            this.name = name;
        } else {
            this.name = "";
        }
        this.printMessages = printMessages;
    }

    public boolean readingFromTextFile() {
        Path filePath = Paths.get("customers.txt");  // the file path
        String firstLineNames;
        String secondLineDates;

        try (Scanner scan = new Scanner(filePath)) {
            while (scan.hasNextLine()) {
                firstLineNames = scan.nextLine();
                if (firstLineNames.contains(this.name)) {
                    secondLineDates = scan.nextLine();
                    if (compareTimeGymMemberShip(secondLineDates, firstLineNames)) {
                        // if a person is in the file and gym membership is paid, return a bool and a message.
                        return true;
                    }
                }
            }
            if(printMessages){
                JOptionPane.showMessageDialog(null, this.name + "is not in the file / not a member");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public String timeToday() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // return a string with current date and time
        return dtf.format(LocalDateTime.now());
    }

    /*public String extractThePersonYouLookFor() {  // returns a string of a name.
        System.out.println("Who you looking for?");
        Scanner scan = new Scanner(System.in);
        String name = scan.nextLine();
        return name;
    }*/

    /*public void printMessageWhoYouLookingFor() {

        System.out.println("Who are you looking for?");
    }*/

    private boolean compareTimeGymMemberShip(String theDates, String theNames) {
        // compares if two dates is more or less than 12 months apart.
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();
        LocalDate parseSecondLineGymDates = LocalDate.parse(theDates, dtf);
        Period period = Period.between(parseSecondLineGymDates, currentDate);
        if (period.toTotalMonths() < 12) {
            this.people = theNames;
//            System.out.println(getPeople() + " is a paying member");
            if(printMessages){
                JOptionPane.showMessageDialog(null, getPeople() + " is a paying member");
            }
            return true;
        } else if (period.toTotalMonths() > 12) {
            if(printMessages){
                JOptionPane.showMessageDialog(null, theNames + " is a member but have not paid in: " + period.getYears() + " in years");
            }
//            System.out.println(getPeople() + " is a member but have not paid in: " + period.getYears() + " in years");
            return true;
        } else {
            return false;
        }
    }

    public String getPeople() {
        if (this.people != null) {
            return this.people;
        }
        return null;
    }

    public String loggingMessageOfPeople() {

        return getPeople() + " " + timeToday();
    }

    public boolean writeTrainingLogToFile() {
        if (getPeople() != null) {  // if getPeople() == null means that no person was extracted from the text file.
            Path file = Paths.get(getPeople());
            String textLogFilePath = String.valueOf(file);
            if (!Files.exists(file)) {
                try {
                    Files.createFile(file);
                } catch (IOException e) {
                    System.out.println("Could not create file");
                    e.printStackTrace();
                }
            }
            if (Files.exists(file)) {
                try (FileWriter writer = new FileWriter(textLogFilePath, true)) {  // try with resources.
                    writer.write(loggingMessageOfPeople() + "\n");

                } catch (IOException e) {
                    System.out.println("Something went wrong " + e.getMessage());
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    public boolean logToFile(boolean printIt){
       if(printIt){
           writeTrainingLogToFile();
           return true;
       }
       return false;
    }
}








