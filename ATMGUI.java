package bank;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * I created ATMGUI as the main screen of the ATM system.
 *
 * This class uses Java Swing to let the user log in, withdraw, deposit, check
 * the balance and log out. The account rules are still handled by the account
 * classes, while this class focuses on the screen and user interaction.
 *
 * I also catch the custom exceptions here so the user sees a clear message on
 * the ATM screen instead of the program crashing.
 *

 * <p>This class implements a Swing-based ATM interface that allows users to:</p>
 * <ul>
 *   <li>Log in with an account number and PIN</li>
 *   <li>Withdraw money (enforces ATM note rules: $20, $50, $100)</li>
 *   <li>Deposit money</li>
 *   <li>Check their account balance</li>
 *   <li>Perform multiple transactions without re-entering credentials</li>
 *   <li>Log out safely</li>
 * </ul>
 *
 * <p>All business logic exceptions ({@link InsuficientBalanceException},
 * {@link NegativeValueException}) are caught here and displayed to the user
 * as on-screen messages.</p>
 *
 * <p><b>Test Login Credentials:</b></p>
 * <pre>
 *   Account 1001 / PIN 1235  (Savings)
 *   Account 1002 / PIN 2222  (NetSaver)
 *   Account 1003 / PIN 3333  (Cheque)
 *   Account 1005 / PIN 5555  (Fixed)
 * </pre>
 *
 * @author luizagomes
 */


public class ATMGUI extends JFrame {

    //  Colours and fonts used to give the ATM a consistent visual style.
    private static final Color CLR_DARK_BG    = new Color(10, 15, 10);
    private static final Color CLR_PANEL_BG   = new Color(15, 25, 15);
    private static final Color CLR_SCREEN_BG  = new Color(5, 12, 5);
    private static final Color CLR_ACCENT     = new Color(0, 200, 80);
    private static final Color CLR_ACCENT2    = new Color(100, 255, 100);
    private static final Color CLR_BTN_NUM    = new Color(20, 45, 20);
    private static final Color CLR_BTN_ACT    = new Color(10, 100, 40);
    private static final Color CLR_BTN_DANGER = new Color(120, 20, 20);
    private static final Color CLR_BTN_OK     = new Color(0, 130, 50);
    private static final Color CLR_TEXT_MAIN  = new Color(0, 230, 80);
    private static final Color CLR_TEXT_DIM   = new Color(60, 120, 60);
    private static final Color CLR_ERROR      = new Color(220, 50, 50);
    private static final Color CLR_SUCCESS    = new Color(50, 220, 80);

    private static final Font FONT_SCREEN = new Font("Courier New", Font.PLAIN, 15);
    private static final Font FONT_TITLE  = new Font("Arial", Font.BOLD, 13);
    private static final Font FONT_BTN    = new Font("Arial", Font.BOLD, 12);
    private static final Font FONT_SMALL  = new Font("Arial", Font.PLAIN, 11);

       // Backend objects used by the GUI.
    /* This object gives the GUI access to the accounts stored in ATMBank. */
    private ATMBank bank;

    /* This keeps track of the account currently logged in. It is null when no user is logged in. */
    private Account currentAccount;

 // Main screen widgets used to show messages and receive input.
    private JTextArea screenArea;
    private JTextField inputField;
    private JLabel clockLabel;

    // State tracking helps the program know what the user is doing.
    /* I use this variable to know which screen/action the user is currently on. */
    private String currentState;

    private static final String STATE_ENTER_ACCOUNT = "ENTER_ACCOUNT";
    private static final String STATE_ENTER_PIN      = "ENTER_PIN";
    private static final String STATE_MENU           = "MENU";
    private static final String STATE_WITHDRAW       = "WITHDRAW";
    private static final String STATE_DEPOSIT        = "DEPOSIT";

 /* I store the account number here temporarily while the user is entering the PIN. */
    private String tempAccountNumber = "";

    // Constructor section.

    /*
     * This constructor sets up the ATM window.
     *
     * I initialise the bank, clear the current account, set the first screen state,
     * build the interface, start the clock and show the welcome message.
     */
    public ATMGUI() {
        bank = new ATMBank();
        currentAccount = null;
        currentState = STATE_ENTER_ACCOUNT;

        setTitle("AITBank ATM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(CLR_DARK_BG);
        setLayout(new BorderLayout(10, 10));

        buildUI();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        startClock();
        showWelcome();
    }

    // UI construction section.

    /*
     * This method builds the main parts of the interface.
     *
     * I separated the UI into smaller methods so the code is easier to understand
     * and each section has its own responsibility.
     */
    
    private void buildUI() {
        add(buildHeader(), BorderLayout.NORTH);
        add(buildCentre(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

 // I added padding so the interface does not look too crowded.
        ((JPanel) getContentPane()).setBorder(
            new EmptyBorder(12, 16, 12, 16));
    }

     
    /**
     * This method builds the top area with the bank name and the clock. 
     *
     * @return the constructed header panel
     */
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CLR_DARK_BG);
        header.setBorder(new EmptyBorder(0, 0, 8, 0));

        JLabel bankName = new JLabel("AITBank");
        bankName.setFont(new Font("Courier New", Font.BOLD, 22));
        bankName.setForeground(CLR_ACCENT);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setBackground(CLR_DARK_BG);
        left.add(bankName);

        clockLabel = new JLabel();
        clockLabel.setFont(FONT_SMALL);
        clockLabel.setForeground(CLR_TEXT_DIM);

        header.add(left, BorderLayout.WEST);
        header.add(clockLabel, BorderLayout.EAST);

        // separator line
        JSeparator sep = new JSeparator();
        sep.setForeground(CLR_ACCENT);
        header.add(sep, BorderLayout.SOUTH);

        return header;
    }

    /* This method builds the middle layout, where the screen, action buttons and keypad are placed. */
    /**
     * Builds the centre section: the ATM screen and the keypad side-by-side.
     *
     * @return the constructed centre panel
     */
    private JPanel buildCentre() {
        JPanel centre = new JPanel(new GridBagLayout());
        centre.setBackground(CLR_DARK_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.fill = GridBagConstraints.BOTH;

         // I place the ATM screen on the left.
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.weighty = 1.0;
        centre.add(buildScreen(), gbc);

        // I place the action buttons on the right.
        gbc.gridx = 1; gbc.weightx = 0;
        centre.add(buildActionButtons(), gbc);

         // I place the keypad at the bottom across the full width.
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.weighty = 0;
        gbc.insets = new Insets(10, 0, 0, 0);
        centre.add(buildKeypad(), gbc);

        return centre;
    }

      /*
     * This method builds the ATM display area.
     *
     * I used a text area for the messages and an input field at the bottom so the
     * user can type account numbers, PINs and amounts.
     *
     *
     * @return the constructed screen panel
     */
    private JPanel buildScreen() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(CLR_PANEL_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(CLR_ACCENT, 1, true),
            new EmptyBorder(10, 12, 10, 12)
        ));
        panel.setPreferredSize(new Dimension(320, 260));

        screenArea = new JTextArea();
        screenArea.setEditable(false);
        screenArea.setBackground(CLR_SCREEN_BG);
        screenArea.setForeground(CLR_TEXT_MAIN);
        screenArea.setFont(FONT_SCREEN);
        screenArea.setLineWrap(true);
        screenArea.setWrapStyleWord(false);
        screenArea.setBorder(new EmptyBorder(8, 8, 8, 8));
        screenArea.setCaretColor(CLR_ACCENT);

        JScrollPane scroll = new JScrollPane(screenArea);
        scroll.setBorder(null);
        scroll.setBackground(CLR_SCREEN_BG);
        scroll.getViewport().setBackground(CLR_SCREEN_BG);

        inputField = new JTextField();
        inputField.setBackground(new Color(10, 25, 10));
        inputField.setForeground(CLR_ACCENT);
        inputField.setFont(new Font("Courier New", Font.BOLD, 16));
        inputField.setCaretColor(CLR_ACCENT);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(CLR_ACCENT, 1),
            new EmptyBorder(4, 8, 4, 8)
        ));
        inputField.setHorizontalAlignment(JTextField.RIGHT);

        // I allow the keyboard Enter key to work like the ATM ENTER button.
        inputField.addActionListener(e -> handleEnter());

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(inputField, BorderLayout.SOUTH);

        return panel;
    }

    /* This method builds the right-side buttons for Withdraw, Deposit, Balance and Log Out. 
     * 
     * @return the constructed action button panel
     */
    private JPanel buildActionButtons() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 0, 8));
        panel.setBackground(CLR_DARK_BG);
        panel.setPreferredSize(new Dimension(110, 260));

        String[] labels = {"Withdraw", "Deposit", "Balance", "Log Out"};
        Color[]  colors = {CLR_BTN_ACT, CLR_BTN_ACT, CLR_BTN_ACT, CLR_BTN_DANGER};

        for (int i = 0; i < labels.length; i++) {
            final String label = labels[i];
            JButton btn = makeButton(label, colors[i], FONT_BTN);
            btn.addActionListener(e -> handleActionButton(label));
            panel.add(btn);
        }

        return panel;
    }

      /*
     * This method builds the numeric keypad.
     *
     * I made the buttons add their value into the input field, similar to how a
     * real ATM keypad works.
     *
     * @return the constructed keypad panel
     */
    private JPanel buildKeypad() {
        JPanel panel = new JPanel(new GridLayout(4, 4, 6, 6));
        panel.setBackground(CLR_DARK_BG);

        String[] keys = {
            "1","2","3","00",
            "4","5","6","CLR",
            "7","8","9",".",
            "","0","","ENTER"
        };

        for (String key : keys) {
            if (key.isEmpty()) {
                JPanel spacer = new JPanel();
                spacer.setBackground(CLR_DARK_BG);
                panel.add(spacer);
            } else if (key.equals("ENTER")) {
                JButton btn = makeButton(key, CLR_BTN_OK, FONT_BTN);
                btn.addActionListener(e -> handleEnter());
                panel.add(btn);
            } else if (key.equals("CLR")) {
                JButton btn = makeButton(key, CLR_BTN_DANGER, FONT_BTN);
                btn.addActionListener(e -> inputField.setText(""));
                panel.add(btn);
            } else {
                JButton btn = makeButton(key, CLR_BTN_NUM, FONT_BTN);
                final String k = key;
                btn.addActionListener(e -> inputField.setText(inputField.getText() + k));
                panel.add(btn);
            }
        }

        return panel;
    }

    /* This method builds the small instruction text at the bottom of the ATM window. 
     *
     * @return the constructed footer panel
     */
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(CLR_DARK_BG);
        JLabel hint = new JLabel("Use the keypad or your keyboard  •  Press ENTER to confirm");
        hint.setFont(FONT_SMALL);
        hint.setForeground(CLR_TEXT_DIM);
        footer.add(hint);
        return footer;
    }

    /**
     * 
     * This method creates buttons with the same style.
     *
     * I made this helper so I do not need to repeat the same button formatting code
     * every time I create a new button.
     *
     * @param text  the button label
     * @param bg    the background colour
     * @param font  the font to use
     * @return the constructed JButton
     */
    private JButton makeButton(String text, Color bg, Font font) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(font);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bg.brighter(), 1, true),
            new EmptyBorder(6, 4, 6, 4)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // I added a hover effect so the button reacts when the mouse moves over it.
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.brighter());
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });

        return btn;
    }

    // Clock section.

    /* This method starts the clock and updates it every second. */
    private void startClock() {
        Timer timer = new Timer(1000, e -> {
            String time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss"));
            clockLabel.setText(time + "  ");
        });
        timer.start();
    }

    // Helper methods for changing what appears on the ATM screen.

    /* This helper replaces everything currently shown on the ATM screen. */
    
    /**
     * Replaces the screen content with the given text.
     *
     * @param text the text to display
     */
    private void showScreen(String text) {
        screenArea.setForeground(CLR_TEXT_MAIN);
        screenArea.setText(text);
    }

    /* This helper adds one extra line to the current screen text. 
     *
     * @param line the line to append
     */
    private void appendScreen(String line) {
        screenArea.append(line + "\n");
    }

    /**
     * This method shows an error message in red.
     *
     * After a short pause, it sends the user back to the menu if they are logged in,
     * or back to the welcome screen if they are not logged in.
     *
     * @param message the error message to display
     */
    private void showError(String message) {
        screenArea.setForeground(CLR_ERROR);
        screenArea.setText("\n  ✖  ERROR\n\n  " + message);
        inputField.setText("");

        // After the delay, I send the user back to the correct screen.
        Timer t = new Timer(2500, e -> {
            screenArea.setForeground(CLR_TEXT_MAIN);
            if (currentAccount != null) {
                showMenu();
            } else {
                showWelcome();
            }
        });
        t.setRepeats(false);
        t.start();
    }

    /**
     * This method shows a success message in green.
     *
     * After a short pause, it returns the user to the main menu.
     *
     * @param message the success message to display
     */
    private void showSuccess(String message) {
        screenArea.setForeground(CLR_SUCCESS);
        screenArea.setText("\n  ✔  SUCCESS\n\n  " + message);
        inputField.setText("");

        Timer t = new Timer(2500, e -> {
            screenArea.setForeground(CLR_TEXT_MAIN);
            showMenu();
        });
        t.setRepeats(false);
        t.start();
    }

    /* This method shows the first screen and asks the user to enter an account number. */
    
    /**
     * Displays the welcome / enter account number screen.
     */
    private void showWelcome() {
        currentState = STATE_ENTER_ACCOUNT;
        currentAccount = null;
        tempAccountNumber = "";
        inputField.setText("");
        showScreen(
            "─────────────────────────\n" +
            "   Welcome to AITBank\n" +
            "─────────────────────────\n\n" +
            "  Please enter your\n" +
            "  account number\n" +
            "  and press ENTER.\n\n" +
            "─────────────────────────"
        );
    }

    /* This method shows the PIN screen after a valid account number is entered. */
    private void showPinScreen() {
        currentState = STATE_ENTER_PIN;
        inputField.setText("");
        showScreen(
            "─────────────────────────\n" +
            "   Account: " + tempAccountNumber + "\n" +
            "─────────────────────────\n\n" +
            "  Enter your PIN\n" +
            "  and press ENTER.\n\n" +
            "─────────────────────────"
        );
    }

/*
     * This method shows the main menu after login.
     *
     * I display the customer name, account number, account type and balance so the
     * user can clearly see which account is being used.
     */
    private void showMenu() {
        currentState = STATE_MENU;
        inputField.setText("");

        String line = "─────────────────────────";
        showScreen(
            line + "\n" +
            "  " + currentAccount.getHolderName() + "\n" +
            line + "\n" +
            "  Account:  " + currentAccount.getAccountNumber() + "\n" +
            "  Type:     " + currentAccount.getAccountType() + "\n" +
            "  Balance:  $" + String.format("%.2f", currentAccount.getBalance()) + "\n" +
            line + "\n\n" +
            "  Select an option:\n" +
            "  → Withdraw\n" +
            "  → Deposit\n" +
            "  → Balance\n" +
            "  → Log Out\n"
        );
    }

     /* This method shows the screen where the user enters the withdrawal amount. */
    private void showWithdrawScreen() {
        currentState = STATE_WITHDRAW;
        inputField.setText("");
        showScreen(
            "─────────────────────────\n" +
            "        WITHDRAW\n" +
            "─────────────────────────\n\n" +
            "  Enter amount to\n" +
            "  withdraw and press\n" +
            "  ENTER.\n\n" +
            "  (ATM dispenses $20,\n" +
            "   $50 and $100 notes)\n" +
            "─────────────────────────"
        );
    }

      /* This method shows the screen where the user enters the deposit amount. */
    private void showDepositScreen() {
        currentState = STATE_DEPOSIT;
        inputField.setText("");
        showScreen(
            "─────────────────────────\n" +
            "         DEPOSIT\n" +
            "─────────────────────────\n\n" +
            "  Enter amount to\n" +
            "  deposit and press\n" +
            "  ENTER.\n\n" +
            "─────────────────────────"
        );
    }

    /* This method shows the current balance for a few seconds and then returns to the menu. */

    private void showBalanceScreen() {
        screenArea.setForeground(CLR_ACCENT2);
        showScreen(
            "─────────────────────────\n" +
            "     BALANCE INQUIRY\n" +
            "─────────────────────────\n\n" +
            "  Account:  " + currentAccount.getAccountNumber() + "\n" +
            "  Type:     " + currentAccount.getAccountType() + "\n\n" +
            "  Balance:\n" +
            "  $" + String.format("%.2f", currentAccount.getBalance()) + "\n\n" +
            "─────────────────────────"
        );

        Timer t = new Timer(3000, e -> {
            screenArea.setForeground(CLR_TEXT_MAIN);
            showMenu();
        });
        t.setRepeats(false);
        t.start();
    }

 // Input handling section.

    /*
     * This method decides what ENTER should do depending on the current state.
     *
     * For example, ENTER can mean submit account number, submit PIN, withdraw, or
     * deposit. The currentState variable tells the program which handler to use.
     */
    private void handleEnter() {
        String input = inputField.getText().trim();

        switch (currentState) {
            case STATE_ENTER_ACCOUNT:
                handleAccountNumberEntry(input);
                break;
            case STATE_ENTER_PIN:
                handlePinEntry(input);
                break;
            case STATE_WITHDRAW:
                handleWithdraw(input);
                break;
            case STATE_DEPOSIT:
                handleDeposit(input);
                break;
            default:
                break;
        }
    }

        /* This method reacts when the user clicks Withdraw, Deposit, Balance or Log Out. */

    /**
     * Handles the action buttons on the right side of the ATM.
     *
     * @param action the label of the button pressed
     */
    private void handleActionButton(String action) {
        switch (action) {
            case "Withdraw":
                if (currentAccount != null) showWithdrawScreen();
                break;
            case "Deposit":
                if (currentAccount != null) showDepositScreen();
                break;
            case "Balance":
                if (currentAccount != null) showBalanceScreen();
                break;
            case "Log Out":
                handleLogout();
                break;
        }
    }

    
    /*
     * This method checks the account number entered by the user.
     *
     * I first make sure the input is not empty, then I convert it to an integer and
     * search for the account in the bank.
     */
    /**
     * Handles account number input during login.
     *
     * @param input the text entered by the user
     */
    private void handleAccountNumberEntry(String input) {
        if (input.isEmpty()) {
            showError("Please enter your account number.");
            return;
        }
        try {
            int accNum = Integer.parseInt(input);
            Account found = bank.findAccount(accNum);
            if (found == null) {
                showError("Account not found.\nPlease try again.");
                inputField.setText("");
                currentState = STATE_ENTER_ACCOUNT;
            } else {
                tempAccountNumber = input;
                showPinScreen();
            }
        } catch (NumberFormatException e) {
            showError("Invalid account number.\nNumbers only, please.");
        }
    }

    
     /* This method checks the PIN during login.
     *
     * I use the account number saved earlier and call bank.login(). If the details
     * are correct, the user goes to the menu. If not, an error is shown.
    /**
     * Handles PIN input during login authentication.
     *
     * @param input the PIN entered by the user
     */
    private void handlePinEntry(String input) {
        if (input.isEmpty()) {
            showError("Please enter your PIN.");
            return;
        }
        try {
            int pin = Integer.parseInt(input);
            int accNum = Integer.parseInt(tempAccountNumber);
            Account authenticated = bank.login(accNum, pin);

            if (authenticated == null) {
                showError("Incorrect PIN.\nPlease try again.");
                showPinScreen();
            } else {
                currentAccount = authenticated;
                showMenu();
            }
        } catch (NumberFormatException e) {
            showError("Invalid PIN. Numbers only.");
        }
    }
 /*
     * This method handles the withdrawal process.
     *
     * I convert the user input into a number, call withdraw() on the current account,
     * and catch any errors so they can be shown on the ATM screen.
     *
     * @param input the withdrawal amount entered
     */
    private void handleWithdraw(String input) {
        if (input.isEmpty()) {
            showError("Please enter an amount.");
            return;
        }
        try {
            double amount = Double.parseDouble(input);
            currentAccount.withdraw(amount);
            showSuccess(String.format("$%.2f withdrawn.\nNew balance: $%.2f",
                amount, currentAccount.getBalance()));

        } catch (NumberFormatException e) {
            showError("Invalid amount. Enter numbers only.");
        } catch (NegativeValueException e) {
            showError(e.getMessage());
        } catch (InsuficientBalanceException e) {
            showError(e.getMessage());
        }
    }

    /**
     * This method handles the deposit process.
     *
     * I convert the input into a number, call deposit() on the current account, and
     * show either a success message or an error message.
     *
     * @param input the deposit amount entered
     */
    private void handleDeposit(String input) {
        if (input.isEmpty()) {
            showError("Please enter an amount.");
            return;
        }
        try {
            double amount = Double.parseDouble(input);
            currentAccount.deposit(amount);
            showSuccess(String.format("$%.2f deposited.\nNew balance: $%.2f",
                amount, currentAccount.getBalance()));

        } catch (NumberFormatException e) {
            showError("Invalid amount. Enter numbers only.");
        } catch (NegativeValueException e) {
            showError(e.getMessage());
        }
    }

       /* This method logs the user out by clearing the current account and showing the welcome screen again. */

    private void handleLogout() {
        currentAccount = null;
        showWelcome();
    }

   // Main entry point section.

    /*
     * This is the main method that starts the ATM GUI.
     *
     *<p>I used SwingUtilities.invokeLater() because Swing interfaces should be created
     * on the Event Dispatch Thread.</p>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ATMGUI());
    }
}
