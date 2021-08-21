package NEODatabase;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class Login {
    private ArrayList<String> cipherKey;
    private String password;
    private String apiKey;
    private boolean savePassword;
    private boolean saveAPIKey;
    private boolean isInitialized;

    private Text passwordLabel;
    private Text apiKeyLabel;
    private Text showPasswordSpace;
    private Text showAPIKeySpace;
    private Text status;
    private PasswordField passwordField;
    private PasswordField apiKeyField;
    private CheckBox savePasswordCheckBox;
    private CheckBox saveAPIKeyCheckBox;
    private Button loginButton;
    private Button showPasswordAndAPIKeyButton;

    private Login() {
        cipherKey = new ArrayList<>();
        password = "";
        apiKey = "";
        savePassword = false;
        saveAPIKey = false;
        isInitialized = false;

        passwordLabel = new Text();
        passwordLabel.setText("Password: ");

        apiKeyLabel = new Text();
        apiKeyLabel.setText("API Key: ");

        showPasswordSpace = new Text();
        showAPIKeySpace = new Text();
        status = new Text();

        passwordField = new PasswordField();
        apiKeyField = new PasswordField();

        savePasswordCheckBox = new CheckBox("Save Password");
        saveAPIKeyCheckBox = new CheckBox("Save API Key");

        loginButton = new Button("Login");
        showPasswordAndAPIKeyButton = new Button("Show Password & API Key");
    }

    private void saveInitialization() throws FileNotFoundException, IllegalArgumentException {
        PrintWriter txtWriter = new PrintWriter("login.txt");

        String cipherKeyString = "";
        ArrayList<String> cipherKey = generateCipherKey();
        for (String character : cipherKey) {
            cipherKeyString += character;
        }

        txtWriter.write(cipherKeyString + "\n");

        txtWriter.write(encode(password, cipherKey) + "\n");
        txtWriter.write(encode(apiKey, cipherKey) + "\n");

        if (savePassword) {
            txtWriter.write("1\n");
        }
        else {
            txtWriter.write("0\n");
        }

        if (saveAPIKey) {
            txtWriter.write("1");
        }
        else {
            txtWriter.write("0");
        }

        txtWriter.close();
    }

    private void loadInitialization() {
        try {
            File txtFile = new File("login.txt");
            Scanner txtReader = new Scanner(txtFile);

            String cipherKeyString = txtReader.nextLine();

            this.cipherKey = new ArrayList<>(Arrays.asList(
                    cipherKeyString.split("")
            ));

            this.password = decode(txtReader.nextLine(), cipherKey);
            this.apiKey = decode(txtReader.nextLine(), cipherKey);

            if (txtReader.nextLine().equals("1")) {
                this.savePassword = true;
                this.passwordField.setText(password);
                this.savePasswordCheckBox.setSelected(true);
            }
            else {
                this.savePassword = false;
            }

            if (txtReader.nextLine().equals("1")) {
                this.saveAPIKey = true;
                this.apiKeyField.setText(apiKey);
                this.saveAPIKeyCheckBox.setSelected(true);
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            status.setText("PLEASE CREATE PASSWORD & ENTER API KEY");
        }
        catch (IllegalArgumentException illegalArgumentException) {
            status.setText("PASSWORD AND API KEY MUST ONLY CONTAIN LETTERS AND DIGITS");
        }
    }

    private void showPasswordAndAPIKey() {
        showPasswordSpace.setText(passwordField.getText());
        showAPIKeySpace.setText(apiKeyField.getText());
    }

    private void attemptLogin() {
        if (isInitialized) {
            boolean passwordMatch = false;
            boolean apiKeyMatch = false;

            if (passwordField.getText().equals(password)) {
                passwordMatch = true;
            }

            if (apiKeyField.getText().equals(apiKey)) {
                apiKeyMatch = true;
            }

            if (passwordMatch && apiKeyMatch) {
                savePassword = savePasswordCheckBox.isSelected();
                saveAPIKey = saveAPIKeyCheckBox.isSelected();

                try {
                    saveInitialization();
                } catch (FileNotFoundException fileNotFoundException) {
                    status.setText("CREDENTIAL SAVE ERROR");
                    return;
                } catch (IllegalArgumentException illegalArgumentException) {
                    status.setText("PASSWORD AND API KEY MUST ONLY CONTAIN LETTERS AND DIGITS");
                    return;
                }

                status.setText("LOGIN SUCCESSFUL");

                NEOViewer.display();
            }
        }
        else {
            password = passwordField.getText();
            apiKey = apiKeyField.getText();

            savePassword = savePasswordCheckBox.isSelected();
            saveAPIKey = saveAPIKeyCheckBox.isSelected();

            try {
                saveInitialization();
            } catch (FileNotFoundException fileNotFoundException) {
                status.setText("CREDENTIAL SAVE ERROR");
                return;
            } catch (IllegalArgumentException illegalArgumentException) {
                status.setText("PASSWORD AND API KEY MUST ONLY CONTAIN LETTERS AND DIGITS");
                return;
            }

            status.setText("LOGIN SUCCESSFUL");

            NEOViewer.display();
        }
    }

    public static Scene setupLoginWindow() {
        Login login = new Login();
        login.loadInitialization();

        HBox passwordRow = new HBox(login.passwordLabel, login.passwordField);
        passwordRow.setAlignment(Pos.CENTER);

        VBox passwordSection = new VBox(passwordRow, login.showPasswordSpace);
        passwordSection.setAlignment(Pos.TOP_LEFT);

        HBox apiKeyRow = new HBox(login.apiKeyLabel, login.apiKeyField);
        apiKeyRow.setAlignment(Pos.CENTER);

        VBox apiKeySection = new VBox(apiKeyRow, login.showAPIKeySpace);
        apiKeySection.setAlignment(Pos.TOP_RIGHT);

        HBox passwordAndAPIKeySections = new HBox(passwordSection, apiKeySection);
        passwordAndAPIKeySections.setAlignment(Pos.TOP_CENTER);

        HBox checkBoxesRow = new HBox(login.savePasswordCheckBox, login.saveAPIKeyCheckBox);
        checkBoxesRow.setAlignment(Pos.CENTER);

        HBox buttonsRow = new HBox(login.loginButton, login.showPasswordAndAPIKeyButton);
        buttonsRow.setAlignment(Pos.CENTER);

        VBox configuration = new VBox(passwordAndAPIKeySections, checkBoxesRow, buttonsRow, login.status);
        configuration.setAlignment(Pos.CENTER);

        login.loginButton.setOnAction(actionEvent -> login.attemptLogin());
        login.showPasswordAndAPIKeyButton.setOnAction(actionEvent -> login.showPasswordAndAPIKey());

        return new Scene(configuration, 480, 360);
    }

    private static ArrayList<String> generateCipherKey() {
        ArrayList<String> cipherKey = new ArrayList<>();
        ArrayList<String> characters = new ArrayList<>(Arrays.asList(
                ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "0123456789").split("")
        ));

        while (characters.size() > 0) {
            String characterToAdd = characters.remove(
                    (int) (Math.random() * characters.size())
            );

            cipherKey.add(characterToAdd);
        }

        return cipherKey;
    }

    private static String encode(String message, ArrayList<String> cipherKey) throws IllegalArgumentException {
        String encodedMessage = "";

        ArrayList<String> characters = new ArrayList<>(Arrays.asList((
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "0123456789").split("")
        ));

        String[] messageCharacters = message.split("");
        for (String character : messageCharacters) {
            if (!characters.contains(character)) {
                throw new IllegalArgumentException("Invalid Input: Must only use letters and numbers.");
            }

            String characterToAdd = cipherKey.get(characters.indexOf(character));
            encodedMessage += characterToAdd;
        }

        return encodedMessage;
    }

    private static String decode(String encodedMessage, ArrayList<String> cipherKey) throws IllegalArgumentException {
        String decodedMessage = "";

        ArrayList<String> characters = new ArrayList<>(Arrays.asList((
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "0123456789").split("")
        ));

        String[] encodedMessageCharacters = encodedMessage.split("");
        for (String character : encodedMessageCharacters) {
            if (!characters.contains(character)) {
                throw new IllegalArgumentException("Invalid Input: Must only use letters and numbers.");
            }

            String characterToAdd = characters.get(cipherKey.indexOf(character));
            decodedMessage += characterToAdd;
        }

        return decodedMessage;
    }
}
