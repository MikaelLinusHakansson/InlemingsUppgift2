import javax.swing.*;
import java.io.BufferedReader;
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

public class NotWellness {
    private String people;
    private String name;
    private final boolean printMessages;

    public NotWellness() {
        printMessages = true;
    }

    public NotWellness(String name) {
        // För testernas skull så jag kan hårdkoda ett namn med hjälp av constructor.
        // (För enkelhetens skull för redovisning).
        this.name = name;
        printMessages = false;
    }
    public boolean readingFromTextFile() {
        if (!this.name.isEmpty()) {
            Path filePath = Paths.get("customers.txt");  // väg till fil hårdkodad för jag antog att det var okej.
            String firstLineNames;  // string som håller första raden.
            String secondLineDates;  // string som håller andra raden.
            String[] extractPeopleToArray;
            try (BufferedReader br = Files.newBufferedReader(filePath)) {
                while ((firstLineNames = br.readLine()) != null) {
                    extractPeopleToArray = firstLineNames.split(", ");
                    // lägger personer och personnummer i en array för att
                    // med mer precision jämföra vad användaren skriver in.
                    secondLineDates = br.readLine();
                    for (String s : extractPeopleToArray) {
                        // Om personen eller dess personnummer finns i listan och matchar användarinput.
                        // Kontrollera om personen har betalt medlemskap.
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
            } catch (NoSuchFileException e2) {  // om filen inte hittas.
                System.out.println("Error missing file: " + e2.getMessage());
            } catch (IOException e) {  // om något oväntat händer.
                e.printStackTrace();
            }
            return false;

        } else {  // om man trycker på OK i JOptionPane
            System.out.println("You pressed 'OK' but did not enter any person");
            return false;
        }
    }

    private boolean compareTimeGymMemberShip(String theDates, String theNames) {
        // metoden jämför om två datum är mer eller mindre 12 månader emellan.
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  // vilket format tiden ska ha.
        LocalDate currentDate = LocalDate.now();  // Hjälper till att jämföra dagens datum med datum från personen.
        LocalDate parseSecondLineGymDates = LocalDate.parse(theDates, dtf);
        // formaterar texten från filen till accepterande format.
        Period period = Period.between(parseSecondLineGymDates, currentDate);
        // kontroll av period mellan nu och uppgifter från filen.
        if (period.toTotalMonths() <= 12) {
            this.people = theNames;
            if (printMessages) {
                JOptionPane.showMessageDialog(null, getPeople() + " is a paying member");
            }
            // Aktiv medlem, returnerar true.
            return true;
        } else if (period.toTotalMonths() > 12) {
            // Medlem som har ett inaktivt konto. (finns i listan men inte betalt).
            if (printMessages) {
                if (period.getYears() > 0 && period.getDays() > 0) {
                    JOptionPane.showMessageDialog(null, theNames
                            + " membership expired: " + period.getYears() + " year(s) " + period.getDays() + " day(s)");
                } else if (period.getYears() == 0) {
                    JOptionPane.showMessageDialog(null, theNames
                            + " membership expired: " + period.getDays() + " day");
                }
            }  // Om personen finns men inte betalt return true.
            return true;
        } else {
            // Om personen inte finns med i dokumentet, return false.
            return false;
        }
    }

    public boolean writeTrainingLogToFile() {
        if (getPeople() != null) {
            // Om getPeople() == null så har programmet inte hämtat någon person
            // från filen och ska därför inte heller skriva till en ny fil.
            var file = Paths.get(getPeople());
            String textLogFilePath = String.valueOf(file);
            if (!Files.exists(file)) {
                // Om filen inte finns så skapar den en ny fil.
                try {
                    Files.createFile(file);
                } catch (IOException e) {
                    System.out.println("Could not create file");
                    e.printStackTrace();
                }
            }
            if (Files.exists(file)) {  // Om filen finns så skriver den till en existerande fil.
                try (FileWriter writer = new FileWriter(textLogFilePath, true)) {  // try with resources.
                    writer.write(loggingMessageOfPeople() + "\n");
                    if (printMessages) {  // skickar ett meddelande till användaren att en person har blivit loggad.
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

    public String getPeople() {
        if (this.people != null) {
            return this.people;
        }
        return null;
    }

    public String timeToday() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // return a string with current date and time.
        return dtf.format(LocalDateTime.now());
    }

    public String loggingMessageOfPeople() {  // hämtar namnet och dagens tid just nu.(LocalDateTime).
        return getPeople() + " " + timeToday();
    }

    public boolean logToFile(boolean printIt) {
        // Enbart för test. Stänger av funktionen att skriva till en fil men funktionen finns fortfarande där.
        if (printIt) {
            writeTrainingLogToFile();
            return true;
        }
        return false;
    }

    public void searchForPerson() {  // Hämtar GUI
        String name = JOptionPane.showInputDialog("Enter name or personal number");
        if (name != null) {
            this.name = name.trim();
        } else {
            System.exit(0);
        }
    }
}
