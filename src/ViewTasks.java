import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author Matthew
 */
public class ViewTasks {
    private JPanel panel;
    private FileManager fileManager;
    private JPanel mainPanel;

    public ViewTasks(JPanel panel) {
        this.panel = panel;
    }

    /**
     * Starts the UI to schedule a task.
     */
    public JPanel start() {
        panel.setLayout(new BorderLayout());
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        panel.setBackground(Color.WHITE);
        fileManager = TaskReminder.getFileManager();

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel title = new JLabel("Scheduled tasks");
        title.setFont(new Font("Times New Roman", Font.PLAIN, 60));
        panel.add(title, BorderLayout.NORTH);

        panel.add(new JScrollPane(mainPanel));

        try {
            updateTasks();
        } catch (IOException e) {
            e.getMessage();
        }

        return panel;
    }

    public void updateTasks() throws IOException {
        mainPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;

        JPanel paddder = new JPanel();
        paddder.setBackground(Color.WHITE);
        mainPanel.add(paddder, gbc);

        gbc.weighty = 0;

        for (int id : fileManager.readIds()) {
            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());

            JButton button = new JButton(fileManager.getSubject(id) + ", " + fileManager.getDescription(id));
            button.setBackground(Color.white);
            button.setBorder(new EmptyBorder(10,10,10,10));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    JOptionPane.showMessageDialog(null, "Description: " + fileManager.getDescription(id) +
                            "\nDate: " + sdf.format(fileManager.getDate(id)), fileManager.getSubject(id),
                            JOptionPane.WARNING_MESSAGE);
                }
            });

            p.add(button);
            p.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));

            mainPanel.add(p, gbc, 0);
        }

        panel.validate();
        panel.repaint();
    }
}
