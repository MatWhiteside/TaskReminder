import java.awt.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Matthew
 */

public class TaskReminder {

    private static WindowsNotification windowsNotification; // Contains the methods to create SystemIcon and display notifications
    private static FileManager fileManager; // Deals with storage and reading files
    private static AddTask addTask; // Add task UI
    private static TaskOverview taskOverview;

    /**
     * Starting point for the application, initialises SystemTray icon and UI
     * @param args No args need to be passed in.
     */
    public static void main(String[] args) {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        // Creates new file manager
        try {
            fileManager = new FileManager();
        } catch (IOException ioe) {
            ioe.getMessage();
        }

        taskOverview = new TaskOverview();
        taskOverview.start();

        windowsNotification = new WindowsNotification("icons/notifIcon.png");   // Initialise SystemTray icon

        // Checks infinitely while the application is open if any notifications need to be shown
        while (true) {
            checkTaskTimes();   // Checks if any reminders need to be shown

            try {
                Thread.sleep(5000); // Waits for 5 seconds
            } catch (InterruptedException e) {
                e.getMessage();
            }
        }
    }

    /**
     * Converts two Strings in the format HH:mm and dd/MM/yyyy into a Date object.
     * NOTE: The JSpinner on the form makes sure formatting cannot be incorrect on the form.
     * @param timeToConvert Formatted: HH:mm (24h time)
     * @param dateToConvert Formatted: dd/MM/yyyy
     * @return Date object that accurately represents the time of the task to be shown
     */
    public static Date toDate(String timeToConvert, String dateToConvert) {
        int hour = Integer.parseInt(timeToConvert.substring(0, 2));
        int minutes = Integer.parseInt(timeToConvert.substring(3, 5));

        int day = Integer.parseInt(dateToConvert.substring(0, 2));
        int month = Integer.parseInt(dateToConvert.substring(3, 5));
        int year = Integer.parseInt(dateToConvert.substring(6, 10));

        Calendar dateCalendar = Calendar.getInstance();

        // Month-1 as it starts from 0 and as inputted it starts from 1
        // Shows task on the 0th second of the minute
        dateCalendar.set(year, month-1, day, hour, minutes, 0);

        Date toReturn = new Date();
        toReturn.setTime(dateCalendar.getTimeInMillis());

        return toReturn;
    }

    /**
     * @return fileManager object that's already been initialised.
     */
    public static FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @return {@link AddTask} object that's already been initialised.
     */
    public static TaskOverview getTaskOverview() {
        return taskOverview;
    }

    // Checks if the current time is after a task time implying a task needs to be shown.
    // If a task is shown it's removed from taskIds and properties.
    private static void checkTaskTimes() {
        long currentTime = Calendar.getInstance().getTimeInMillis();    // Get the current time

        try {
            // For each id in taskIds.txt
            for (int id : fileManager.readIds()) {
                // If the task is overdue, show a notification and remove it from the file.
                if (fileManager.getDate(id) < currentTime) {
                    windowsNotification.sendNotification(fileManager.getSubject(id), fileManager.getDescription(id));
                    fileManager.remove(id);
                    taskOverview.updateTasks();
                }
            }
        } catch (IOException ioe) {
            ioe.getMessage();
        }
    }
}
