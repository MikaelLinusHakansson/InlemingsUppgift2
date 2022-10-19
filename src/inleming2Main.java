public class inleming2Main {
    public static void main(String[] args) {
        NotWellness receptionPC = new NotWellness();
        receptionPC.searchForPerson();

        receptionPC.readingFromTextFile();
        receptionPC.writeTrainingLogToFile();
    }
}