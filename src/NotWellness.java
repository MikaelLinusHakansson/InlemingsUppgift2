import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class NotWellness {
    private String people;
    private String name;
    private boolean printMessages;

    public NotWellness() {
        printMessages = true;
    }

    public NotWellness(String name) {  // för testernas skull så man kan hårdkoda ett namn i konstruktorn.
        this.name = name;
        printMessages = false;
    }


    public boolean readingFromTextFile() {
        if (!this.name.isEmpty()) {
            Path filePath = Paths.get("customers.txt");  // the file path
            String firstLineNames;
            String secondLineDates;
            String[] extractPeopleToArray;
            try (Scanner scan = new Scanner(filePath)) {
                while (scan.hasNextLine()) {
                    firstLineNames = scan.nextLine();
                    extractPeopleToArray = firstLineNames.split(", ");
                    // personer i listor för att mer precis jämföra vad användaren skriver in.
                    secondLineDates = scan.nextLine();
                    for (String s : extractPeopleToArray) {
                        // Om personen eller dess personnummer finns i listan och matchar användarinput.
                        // Kontrollera om personen har betalt medlemskap
                        if (this.name.equalsIgnoreCase(s)) {
                            if (compareTimeGymMemberShip(secondLineDates, firstLineNames)) {
                                // Om personen finns i filen och gymmedlemskapet är betalt eller inte returnerar en bool.
                                return true;
                            }
                        }
                    }
                }
                if (printMessages) {
                    JOptionPane.showMessageDialog(null, this.name
                            + " is not in the file / not a member");
                }
            } catch (NoSuchFileException e2) {
                System.out.println("Error missing file: " + e2.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;

        } else {
            System.out.println("You pressed 'OK' but did not enter any person");
            return false;
        }
    }

    private boolean compareTimeGymMemberShip(String theDates, String theNames) {
        // compares if two dates is more or less than 12 months apart.
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  // vilket format tiden ska ha.
        LocalDate currentDate = LocalDate.now();
        LocalDate parseSecondLineGymDates = LocalDate.parse(theDates, dtf);  // omvandlar texten i filen till tid
        Period period = Period.between(parseSecondLineGymDates, currentDate);
        // kontroll av period mellan nu och uppgifter från filen
        if (period.toTotalMonths() < 12) {
            this.people = theNames;
            if (printMessages) {
                JOptionPane.showMessageDialog(null, getPeople() + " is a paying member");
            }
            // om perioden < 12 och månader och medlem, true annars false.
            return true;
        } else if (period.toTotalMonths() > 12) {
            // om perioden > 12 men varit medlem, true annars false.
            if (printMessages) {
                JOptionPane.showMessageDialog(null, theNames
                        + " membership expired: " + period.getYears() + " years");
            }
            return true;
        } else {
            // om personen inte finns med i dokumentet, false.
            return false;
        }
    }

    public boolean writeTrainingLogToFile() {
        if (getPeople() != null) {
            // if getPeople() == null means that no person was extracted from the text file.
            var file = Paths.get(getPeople());
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
                    if (printMessages) {
                        JOptionPane.showMessageDialog(null, "Logg successful");
                    }
                    System.out.println("Successfully logged: " + loggingMessageOfPeople());

                } catch (IOException e) {
                    System.out.println("Something went wrong " + e.getMessage());
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    public String timeToday() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // return a string with current date and time
        return dtf.format(LocalDateTime.now());
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

    public boolean logToFile(boolean printIt) {
        if (printIt) {
            writeTrainingLogToFile();
            return true;
        }
        return false;
    }

    public void searchForPerson() {
        String name = JOptionPane.showInputDialog("Enter name or personal number");
        if (name != null) {
            this.name = name.trim();
        } else {
            System.exit(0);
        }
    }
}
