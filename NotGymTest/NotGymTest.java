import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class NotWellnessTest {
    NotWellness test = new NotWellness("Alhambra Aromes");

    @Test
    void readingFromTextFile() {
        NotWellness isPayingMember = new NotWellness("Alhambra Aromes");  // finns i textfilen
        NotWellness notMember = new NotWellness("asd");
        NotWellness isMemberButNotPaying = new NotWellness("Bear Belle");

        assertTrue(isPayingMember.readingFromTextFile());  // Borde vara true för namnet finns i textfil
        assertFalse(notMember.readingFromTextFile()); // borde vara false för namnet finns inte i textfilen
        assertTrue(isMemberButNotPaying.readingFromTextFile());  // borde vara sant för namnet finns i textfilen
    }

    @Test
    void timeToday() {
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        assertEquals(dtf.format(ldt).length(), test.timeToday().length());  // kollar så att så formatet stämmer
        assertNotEquals(dtf2.format(ldt).length(), test.timeToday().length());
    }

    @Test
    void getPeople() {
        NotWellness test10 = new NotWellness("Alhambra Aromes");
        NotWellness test11 = new NotWellness("asd");

        assertFalse(test11.readingFromTextFile());  // test11 är inte en person i text filen och readingFromTextFile borde returnera false.
        assertNull(test11.getPeople());  // inget namn är sparat och värdet borde vara null
        assertNull(test10.getPeople()); // borde vara null innan readFromTextFile
        test10.readingFromTextFile();
        assertNotNull(test10.getPeople());  // Borde vara != null (namn från textFil sparad.)
    }

    @Test
    void loggingMessageOfPeople() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        test.readingFromTextFile();
        String s = "7703021234, Alhambra Aromes" + " " + dtf.format(LocalDateTime.now());  // kollar så att formatet stämmer överens
        assertEquals(test.loggingMessageOfPeople(), s);
    }

    @Test
    void writeTrainingLogToFile() {
        var member = new NotWellness("Alhambra Aromes");
        var member2 = new NotWellness("asd");
        member.readingFromTextFile();
        member.logToFile(true);
        Path pathTest = Paths.get(member.getPeople());
        assertTrue(Files.exists(pathTest));  // kollar om den nya filen finns.

        member2.readingFromTextFile();
        member2.logToFile(true);
        assertFalse(member2.writeTrainingLogToFile());  // writeTrainingLogToFile returnerar false om filen ej skapas

    }

}