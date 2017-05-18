import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author Matthew
 */
public class FileManager {

    // Properties of the reminder are stored in tasks.properties eg, title, date etc
    private Properties properties;

    /**
     * Initialises and loads the properties file.
     * @throws IOException File not found.
     */
    public FileManager() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream("tasks.properties"));
    }

    /**
     * Adds a task id to taskIds.txt - apprends instead of clearning file.
     * @param id ID to add.
     * @throws IOException File not found.
     */
    public void writeId(int id) throws IOException {
        PrintWriter pw = new PrintWriter(new java.io.FileWriter("taskIds.txt", true));
        pw.println(id);
        pw.close();
    }

    /**
     * Removes an ID from taskIds.txt
     * @param id ID to remove.
     * @throws IOException File not found.
     */
    public void remove(int id) throws IOException {

        // Remove properties of the given ID
        properties.remove(id+"_date");
        properties.remove(id+"_sub");
        properties.remove(id+"_desc");
        saveProperties();

        // Open two files
        File inputFile = new File("taskIds.txt");
        File tempFile = new File("taskIds_temp.txt");

        // Create a reader and writer
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(tempFile));

        // Line to remove is value of ID
        String lineToRemove = String.valueOf(id);
        String currentLine;

        // Copy each line to the new file apart from the one to be deleted
        while((currentLine = reader.readLine()) != null) {
            // Trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(lineToRemove)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));   // line.separator = \n
        }

        // Close reader and writer
        writer.close();
        reader.close();
        inputFile.delete(); // Delete old file
        tempFile.renameTo(inputFile);   // Rename temp file to new file
    }

    /**
     * Creates and returns an ArrayList of all ids in taskIds.txt in the format {1,2,3,4, ...}
     * @return ArrayList of all ids in taskIds.txt in the format {1,2,3,4, ...}
     * @throws IOException File not found.
     */
    public ArrayList<Integer> readIds() throws IOException {
        // Open up file
        FileReader fr = new FileReader("taskIds.txt");
        BufferedReader br = new BufferedReader(fr);

        // Create empty ArrayList
        ArrayList<Integer> ids = new ArrayList<>();

        // Add values to the ArrayList until an empty line is reached
        String line;
        while ((line = br.readLine()) != null) {
            ids.add(Integer.valueOf(line));
        }

        br.close();
        fr.close();
        return ids;
    }

    /**
     * Writes the properties of a task to the tasks.properties file
     * @param id ID of the task, used for the key of properties
     * @param taskDate Date of the task in milliseconds (crucial)
     * @param taskSubject Subject / title of the task
     * @param taskDescription Description / subtitle of the task
     * @throws IOException File not found.
     */
    public void setProperties(int id, String taskDate, String taskSubject, String taskDescription) throws IOException {
        properties.setProperty(id+"_date", taskDate);
        properties.setProperty(id+"_sub", taskSubject);
        properties.setProperty(id+"_desc", taskDescription);
        saveProperties();
    }

    // Saves the properties file, used after updates
    private void saveProperties() throws IOException{
        File props = new File("tasks.properties");
        OutputStream os = new FileOutputStream (props);
        properties.store(os, "");
    }

    /**
     * @param id Task ID of the date to return
     * @return Date in milliseconds of the given ID
     */
    public long getDate(int id) {
        return Long.valueOf(properties.getProperty(id+"_date"));
    }

    /**
     * @param id Task ID of the subject / title to return
     * @return Subject / title of the given ID
     */
    public String getSubject(int id) {
        return properties.getProperty(id+"_sub");
    }

    /**
     * @param id Task ID of the description / subtitle to return
     * @return Description / subtitle of the ID
     */
    public String getDescription(int id) {
        return properties.getProperty(id+"_desc");
    }

    /**
     * @return Fetches the ID to be used from currentTaskId.txt
     * @throws IOException File not found.
     */
    public int getId() throws IOException{
        // Open the file
        FileReader fr = new FileReader("currentTaskId.txt");
        BufferedReader br = new BufferedReader(fr);

        // Only one line so no need to loop through lines
        int currentId = Integer.valueOf(br.readLine());

        // Close readers
        br.close();
        fr.close();
        return currentId;
    }

    /**
     * Increments the ID in currentTaskId.txt - wipes file before writing
     * @throws IOException File not found.
     */
    public void incrementId() throws IOException {
        int currentId = getId();    // Read the current ID
        currentId++;

        // Print the incremented ID into the file
        PrintWriter pw = new PrintWriter(new java.io.FileWriter("currentTaskId.txt", false));
        pw.println(currentId);
        pw.close();
    }

}
