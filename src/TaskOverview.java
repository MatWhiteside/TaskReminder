import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * @author Matthew
 */
public class TaskOverview extends JFrame {

    private static ViewTasks viewTasks;

    /**
     * Starts the UI to schedule a task.
     */
    public void start() {
        setTitle("Task Reminder");
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/notificon.png"));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.getMessage();
        }

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setBackground(Color.WHITE);

        JPanel addTask = new JPanel();
        JPanel taskOverview = new JPanel();

        addTask = new AddTask(addTask).start();
        addTask.setPreferredSize(new Dimension(800, 600));
        addTask.setMinimumSize(new Dimension(250, 400));
        addTask.setMaximumSize(new Dimension(400, 800));

        viewTasks = new ViewTasks(taskOverview);
        JPanel right = viewTasks.start();

        JSplitPane pane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT,
                addTask, right );

        add(pane);
        setSize(800, 600);
        setVisible(true);
    }

    public static void updateTasks() throws IOException {
        viewTasks.updateTasks();
    }
}
