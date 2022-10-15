import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class gymMemberShipYesOrNo {
    public static Scanner scanner = new Scanner(System.in);
    private String people;

    public gymMemberShipYesOrNo() {
        Path p = Paths.get("customers.txt");  // the file path
        String s;
        printMessageWhoYouLookingFor();  // just a message
        String person = extractThePersonYouLookFor();  // use scanner ask user for a name.
        try (Scanner scan = new Scanner(p)) {
            while (scan.hasNextLine()) {
                s = scan.nextLine();
                if (s.contains(person)) {
                    if (compareTimeGymMemberShip(scan.nextLine(), s)) {
                        // if a person is in the file and gym membership is paid, return a bool and a message.

                        return;
                    }
                }
            }
            System.out.println(person + " is not a member");  // if person is not found in file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String timeToday() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // return a string with current date and time
        return dtf.format(LocalDateTime.now());
    }

    public String extractThePersonYouLookFor() {  // returns a string of a name.
        return scanner.nextLine();
    }

    public void printMessageWhoYouLookingFor() {
        System.out.println("Who are you looking for?");
    }

    public boolean compareTimeGymMemberShip(String theDate, String l) {
        // compares if two dates is more or less than 12 months apart.
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate p = LocalDate.now();
        LocalDate s = LocalDate.parse(theDate, dtf);
        Period period = Period.between(s, p);
        if (period.toTotalMonths() < 12) {
            System.out.println("Is a paying member");
            this.people = l;
            return true;
        } else if (period.toTotalMonths() > 12) {
            System.out.println("Is a member but have not paid in: " + period.toTotalMonths() + " months time");
            return true;
        } else {
            return false;
        }
    }

    public boolean writeTrainingLogToFile() {
        if(getPeople() != null) {
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
                try {
                    if(loggingMessageOfPeople() != null){
                        FileWriter writer = new FileWriter(textLogFilePath, true);
                        writer.write(loggingMessageOfPeople() + "\n");
                        writer.close();
                    }
                } catch (IOException e) {
                    System.out.println("Something went wrong");
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    public String getPeople() {
        if(this.people != null){
            return this.people;
        }
        return null;
    }

    public String loggingMessageOfPeople(){
        return getPeople() + " " + timeToday();
    }
}








