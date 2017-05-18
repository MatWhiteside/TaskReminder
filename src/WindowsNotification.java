import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Matthew
 */
public class WindowsNotification {

    private static TrayIcon trayIcon;

    /**
     * If OS is supported create {@link SystemTray} icon.
     * @param pathToIcon Path to image to be used for the icon
     */
    public WindowsNotification(String pathToIcon) {
        if (isSupported()) {
            createSystemTray(pathToIcon);
        } else {
            JOptionPane.showMessageDialog(null, "Oops! Your operating system isn't supported.", "OS not supported!",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Return if the OS is supported or not.
     * @return true = supported, false = not supported.
     */
    public static boolean isSupported() {
        return SystemTray.isSupported();
    }

    /*
     * Creates an icon for the application in the SystemTray
     * pathToIcon = Path to image to be used for the icon
     */
    private void createSystemTray(String pathToIcon) {
        // System tray
        SystemTray mainTray = SystemTray.getSystemTray();

        // Create icon to add to the system tray
        Image trayIconImage = Toolkit.getDefaultToolkit().getImage(pathToIcon); // Load image
        trayIcon = new TrayIcon(trayIconImage);
        trayIcon.setImageAutoSize(true);

        // Create empty frame for popup menu
        final Frame FRAME = new Frame("");
        FRAME.setUndecorated(true);
        FRAME.setType(Window.Type.UTILITY);

        // Create the popup menu
        final PopupMenu POPUP = createPopupMenu();
        trayIcon.setPopupMenu(POPUP);

        // When the mouse is clicked on the icon
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1) {
                    FRAME.add(POPUP);   // Add popup to frame
                    POPUP.show(FRAME, e.getXOnScreen(), e.getYOnScreen());  // Show popup
                }
            }
        });

        try {
            // Add the application to the computers tray
            FRAME.setResizable(false);
            FRAME.setVisible(true);
            mainTray.add(trayIcon);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PopupMenu createPopupMenu() {
        // Create new popup menu
        final PopupMenu POPUP = new PopupMenu();

        // Item to open the application
        MenuItem openItem = new MenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaskReminder.getTaskOverview().setVisible(true); // Set the application visible
            }
        });

        // Item to exit the application
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exits the whole application
            }
        });

        // Add components to pop-up menu
        POPUP.add(openItem);
        POPUP.addSeparator();
        POPUP.add(exitItem);

        return POPUP;
    }

    /**
     * Used to send push notifications. Image used is the applications image.
     * @param title Title of the notification
     * @param subtitle Description of the notification
     */
    public void sendNotification(String title, String subtitle) {
        trayIcon.displayMessage(title, subtitle, TrayIcon.MessageType.INFO);
    }
}
