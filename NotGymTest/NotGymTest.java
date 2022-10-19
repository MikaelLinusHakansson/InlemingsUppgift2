import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class NotWellnessTest {

    @Test
    void readingFromTextFile() {

        NotWellness isPayingMember = new NotWellness("Alhambra Aromes");  // finns i textfilen
        assertTrue(isPayingMember.readingFromTextFile());  // Borde vara true för namnet finns i textfil

        NotWellness notMember = new NotWellness("asd");
        assertFalse(notMember.readingFromTextFile()); // borde vara false för namnet finns inte i textfilen


        NotWellness isMemberButNotPaying = new NotWellness("8204021234");
        assertTrue(isMemberButNotPaying.readingFromTextFile());
        // borde vara sant för personen (Bear Belle) finns i textfilen
    }

    @Test
    void timeToday() {  // denna returnerar en localDateTime.now();
        NotWellness test = new NotWellness("Alhambra Aromes");
        LocalDateTime ldt = LocalDateTime.now();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        assertEquals(dtf.format(ldt).length(), test.timeToday().length());
        // kollar så att så formatet stämmer, borde vara sant


        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        assertNotEquals(dtf2.format(ldt).length(), test.timeToday().length());
        // falskt för sekunder saknas i formatet.

    }

    @Test
    void getPeople() {
        NotWellness test10 = new NotWellness("Alhambra Aromes");
        NotWellness test11 = new NotWellness("asd");

        assertNull(test10.getPeople()); // borde vara null innan readFromTextFile
        test10.readingFromTextFile();  // läser in från textfilen och lägger till värde.
        assertNotNull(test10.getPeople());  // Borde vara != null (namn från textFil är sparat).


        assertFalse(test11.readingFromTextFile());
        // test11 är inte en person i textfilen och readingFromTextFile, borde returnera false.
        assertNull(test11.getPeople());  // inget namn är sparat och värdet borde vara null
    }

    @Test
    void loggingMessageOfPeople() {
        NotWellness test = new NotWellness("Alhambra Aromes");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        test.readingFromTextFile();
        String s = "7703021234, Alhambra Aromes " + dtf.format(LocalDateTime.now());
        // kollar så att formatet stämmer överens
        assertEquals(test.loggingMessageOfPeople(), s);
    }

    @Test
    void writeTrainingLogToFile() {
        var member = new NotWellness("Alhambra Aromes");
        var member2 = new NotWellness("asd");
        member.readingFromTextFile();
        member.logToFile(false);
        Path pathTest = Paths.get(member.getPeople());  // kolla om filen finns med namnet i member.
        assertTrue(Files.exists(pathTest));  // kollar om den nya filen finns.

        member2.readingFromTextFile();
        member2.logToFile(true);
        assertFalse(member2.writeTrainingLogToFile());  // writeTrainingLogToFile returnerar false om filen ej skapas

    }
}