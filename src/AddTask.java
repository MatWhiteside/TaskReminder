import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Matthew
 */
public class AddTask {

    private JPanel panel;
    private FileManager fileManager;

    public AddTask(JPanel panel) {
        this.panel = panel;
    }

    /**
     * Starts the UI to schedule a task.
     */
    public JPanel start() {

        Font globalFont = new Font("Yu Gothic Light", Font.BOLD, 20);

        Border line = BorderFactory.createMatteBorder(1,1,1,1, Color.gray);
        Border padding = new EmptyBorder(5,5,5,5);
        CompoundBorder textBoxBorder = new CompoundBorder(line, padding);

        // Time components
        JLabel timeLabel = new JLabel();
        timeLabel.setText("Time: ");
        JSpinner timeSpinner = new JSpinner( new SpinnerDateModel() );  // Formatted to HH:mm
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date());
        timeSpinner.setFont(globalFont);
        timeSpinner.setBorder(textBoxBorder);
        timeLabel.setFont(globalFont);

        JComponent timeSpinnerEditor = timeSpinner.getEditor();
        JFormattedTextField timeSpinnerFTF = ((JSpinner.DefaultEditor) timeSpinnerEditor).getTextField();
        timeSpinnerFTF.setColumns(6);

        // Date components
        JLabel dateLabel = new JLabel();
        dateLabel.setText("Date: ");
        JSpinner dateSpinner = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(new Date());
        dateSpinner.setFont(globalFont);
        dateSpinner.setBorder(textBoxBorder);
        dateSpinner.setVerifyInputWhenFocusTarget(true);
        dateLabel.setFont(globalFont);

        JComponent dateSpinnerEditor = dateSpinner.getEditor();
        JFormattedTextField dateSpinnerFTF = ((JSpinner.DefaultEditor) dateSpinnerEditor).getTextField();
        DefaultFormatter formatter = (DefaultFormatter) dateSpinnerFTF.getFormatter();
        formatter.setCommitsOnValidEdit(true);
        dateSpinnerFTF.setColumns(6);

        // Subject components
        JLabel subjectLabel = new JLabel();
        subjectLabel.setText("Subject: ");
        JTextField subjectTextField = new JTextField(10);
        subjectTextField.setBorder(textBoxBorder);
        subjectTextField.setFont(globalFont);
        subjectLabel.setFont(globalFont);


        // Description components
        JLabel descriptionLabel = new JLabel();
        descriptionLabel.setText("Description: ");
        JTextArea descriptionTextArea = new JTextArea(3, 20);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setBorder(textBoxBorder);
        descriptionTextArea.setFont(globalFont);
        descriptionLabel.setFont(globalFont);

        // Submit button
        JButton button = new JButton();
        button.setText("Create event");
        button.setFont(globalFont);

        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);


        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        GridBagConstraints individualPanelConstrains = new GridBagConstraints();

        // Make the component wide enough to fill its display area horizontally, but do not change its height
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        individualPanelConstrains.fill = GridBagConstraints.BOTH;

        // External padding of the component
        gridBagConstraints.insets = new Insets(20, 20, 0, 20);
        individualPanelConstrains.insets = new Insets(0, 0, 20, 0);


        // Fill panel on y axis, take half of x axis
        individualPanelConstrains.weighty = 1;
        individualPanelConstrains.weightx = 0.5;

        // Start at the top
        individualPanelConstrains.gridy = 0;

        // Create the time panel, needed so the label and box are positioned side by side
        JPanel time = new JPanel();
        time.setBackground(Color.WHITE);
        time.setLayout(new GridBagLayout());

        individualPanelConstrains.gridx = 0;
        time.add(timeLabel, individualPanelConstrains);

        individualPanelConstrains.gridx = 1;
        time.add(timeSpinner, individualPanelConstrains);

        // Create the date panel, needed so the label and box are positioned side by side
        JPanel date = new JPanel();
        date.setBackground(Color.WHITE);
        date.setLayout(new GridBagLayout());

        individualPanelConstrains.gridx = 0;
        date.add(dateLabel, individualPanelConstrains);

        individualPanelConstrains.gridx = 1;
        date.add(dateSpinner, individualPanelConstrains);


        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panel.add(time, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        panel.add(date, gridBagConstraints);

        // Add subject components to frame
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panel.add(subjectLabel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panel.add(subjectTextField, gridBagConstraints);

        // Add description components to frame
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panel.add(descriptionLabel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        panel.add(descriptionTextArea, gridBagConstraints);

        // Add submit button to frame
        gridBagConstraints.insets = new Insets(20, 20, 20, 20);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        panel.add(button, gridBagConstraints);

        // Get already initialised file manager instead of creating a new one
        fileManager = TaskReminder.getFileManager();

        // Schedules event on button click
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Creates date object using TaskReminder.toDate()
                String time = new SimpleDateFormat("HH:mm").format(timeSpinner.getValue());
                String date = new SimpleDateFormat("dd/MM/yyyy").format(dateSpinner.getValue());
                Date taskDate = TaskReminder.toDate(time, date);
                String title = subjectTextField.getText();
                String description = descriptionTextArea.getText();


                // Sets properties of the reminder and adds the reminders id to taskIds.txt. Increments the id
                try {
                    fileManager.setProperties(fileManager.getId(), String.valueOf(taskDate.getTime()), title, description);
                    fileManager.writeId(fileManager.getId());
                    fileManager.incrementId();
                    TaskOverview.updateTasks();
                } catch (IOException ioe) {
                    ioe.getMessage();
                }
            }
        });

        return panel;
    }
}
