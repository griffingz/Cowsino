package com.ggz.cowsino;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    Profile profile;
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//==================================================================================================
//================================= Log In =========================================================
//==================================================================================================

    public void Validation(View view) {
        String provided_username = ((EditText) findViewById(R.id.editTextTextEmailAddress)).getText().toString();
        String provided_password = ((EditText) findViewById(R.id.editTextTextPassword)).getText().toString();
        if (provided_username.length() == 0) {
            Toast.makeText(getApplicationContext(), "No Username Provided", Toast.LENGTH_LONG).show();
            return;
        }
        if (provided_password.length() == 0) {
            Toast.makeText(getApplicationContext(), "No Password Provided", Toast.LENGTH_LONG).show();
            return;
        }

        File cowsinoDirectory = new File(getExternalFilesDir(null), "Cowsino");
        if (!cowsinoDirectory.exists()) {
            boolean dirBool = cowsinoDirectory.mkdir();
            if (dirBool)
                System.out.println("Cowsino directory created");
        }

        File myObj = new File(cowsinoDirectory, provided_username + ".cow");
        filePath = myObj.getAbsolutePath();
        boolean userExists = LookForUser(myObj);
        if (!userExists) {
            RegisterUser(myObj, provided_username, provided_password, view);
        } else {
            boolean passwordsMatch = CheckPasswords(myObj, provided_password);
            if (passwordsMatch) {
                LoginUser(myObj, view);
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean LookForUser(File myObj) {
        try {
            return !myObj.createNewFile();
        } catch (IOException e) {
            System.err.println("[ERROR] createNewFile failed!");
            e.printStackTrace();
            return false;
        }
    }

    public void RegisterUser(File myObj, String provided_username, String provided_password, View view) {
        String profileString = provided_username + "\n"     // Username
                + provided_password + "\n"                  // Password
                + "100\n"                                   // Money
                + "0\n"                                     // Work Gains
                + "0\n"                                     // Coin Flip Gains
                + "0\n";                                    // Coin Flip Loss
        try {
            FileWriter myWriter = new FileWriter(myObj);
            myWriter.write(profileString);
            myWriter.close();
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to write to file!");
            e.printStackTrace();
        }
        LoginUser(myObj, view);
    }

    public boolean CheckPasswords(File myObj, String provided_password) {
        String stored_password = GetStoredPassword(myObj);
        return stored_password.equals(provided_password);
    }

    public String GetStoredPassword(File myObj) {
        String stored_password = "";
        try {
            Scanner myReader = new Scanner(myObj);
            int lineCount = 0;
            while (myReader.hasNextLine()) {
                String value = myReader.nextLine();
                if (lineCount == 1) {
                    stored_password = value;
                }
                lineCount++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("[ERROR] Could not read stored password from file!");
            e.printStackTrace();
        }
        return stored_password;
    }

    public void LoginUser(File myObj, View view) {
        String stored_username = "ERROR";
        String stored_password = "ERROR";
        long stored_money = 0;
        long storedWorkGains = 0;
        long storedCoinFlipGains = 0;
        long storedCoinFlipLoss = 0;
        try {
            Scanner myReader = new Scanner(myObj);
            int lineCount = 0;
            while (myReader.hasNextLine()) {
                String value = myReader.nextLine();
                if (lineCount == 0)
                    stored_username = value;
                else if (lineCount == 1)
                    stored_password = value;
                else if (lineCount == 2)
                    stored_money = Long.parseLong(value);
                else if (lineCount == 3)
                    storedWorkGains = Long.parseLong(value);
                else if (lineCount == 4)
                    storedCoinFlipGains = Long.parseLong(value);
                else if (lineCount == 5)
                    storedCoinFlipLoss = Long.parseLong(value);
                lineCount++;
            }
            myReader.close();
            profile = new Profile(stored_username, stored_password, stored_money);
            profile.setWorkGains(storedWorkGains);
            profile.setCoinFlipGains(storedCoinFlipGains);
            profile.setCoinFlipLoss(storedCoinFlipLoss);
            LoginPageToHomePage(view);
        } catch (FileNotFoundException e) {
            System.err.println("[ERROR] Could not read stored data from file!");
            e.printStackTrace();
        }
    }

//==================================================================================================
//================================= Page Transfers =================================================
//==================================================================================================

    public void LoginPageToHomePage(View view) {
        TextView usernameTextView = findViewById(R.id.username_textView);
        TextView moneyTextView = findViewById(R.id.money_textView);

        usernameTextView.setText(profile.getUsername());
        moneyTextView.setText(profile.moneyToString());

        CloseLoginPage();
        OpenHomePage();
        hideKeyboardFrom(this, view);
    }

    public void HomePageToWorkPage(View view) {
        CloseHomePage();
        OpenWorkPage();
    }

    public void BackToHomePage(View view) {
        CloseWorkPage();
        CloseGamesPage();
        CloseSettingsPage();
        ClosePasswordChangePage();
        CloseUsernameChangePage();
        CloseFlipACoinPage();
        CloseStatsPage();
        OpenHomePage();
        CloseResetStatsPage();
        CloseDeleteAccountPage();
    }

    public void HomePageToGamesPage(View view) {
        CloseHomePage();
        OpenGamesPage();
    }

    public void HomePageToSettingsPage(View view) {
        CloseHomePage();
        OpenSettingsPage();
    }

    public void HomePageToStatsPage(View view) {
        CloseHomePage();
        OpenStatsPage();

        String[] statStrings = {NumberFormat.getNumberInstance(Locale.US).format(profile.getTotalGains()),
                NumberFormat.getNumberInstance(Locale.US).format(profile.getTotalLoss()),
                NumberFormat.getNumberInstance(Locale.US).format(profile.getCoinFlipGains()),
                NumberFormat.getNumberInstance(Locale.US).format(profile.getWorkGains()),
                NumberFormat.getNumberInstance(Locale.US).format(profile.getCoinFlipGains()),
                NumberFormat.getNumberInstance(Locale.US).format(profile.getCoinFlipLoss())};

        ((TextView) findViewById(R.id.totalGainsTextView)).setText(statStrings[0]);
        ((TextView) findViewById(R.id.totalLossTextView)).setText(statStrings[1]);
        ((TextView) findViewById(R.id.gamblingGainsTextView)).setText(statStrings[2]);
        ((TextView) findViewById(R.id.workGainsTextView)).setText(statStrings[3]);
        ((TextView) findViewById(R.id.coinFlipGainsTextView)).setText(statStrings[4]);
        ((TextView) findViewById(R.id.coinFlipLossTextView)).setText(statStrings[5]);
    }

    public void SettingsPageToLoginPage(View view) {
        CloseSettingsPage();
        OpenLoginPage();
    }

    public void SettingsPageToPasswordChangePage(View view) {
        CloseSettingsPage();
        OpenPasswordChangePage();
    }

    public void SettingsPageToUsernameChangePage(View view) {
        CloseSettingsPage();
        OpenUsernameChangePage();
    }

    public void SettingsPageToResetStatsPage(View view) {
        CloseSettingsPage();
        OpenResetStatsPage();
    }

    public void SettingsPageToDeleteAccountPage(View view) {
        CloseSettingsPage();
        OpenDeleteAccountPage();
    }

    public void GamesPageToFlipACoinPage(View view) {
        CloseGamesPage();
        OpenFlipACoinPage();
        findViewById(R.id.chipHeadsImage).setVisibility(View.GONE);
        findViewById(R.id.chipTailsImage).setVisibility(View.GONE);
    }

    public void OpenLoginPage() {
        findViewById(R.id.loginGroup).setVisibility(View.VISIBLE);
    }

    public void CloseLoginPage() {
        findViewById(R.id.loginGroup).setVisibility(View.GONE);
    }

    public void OpenHomePage() {
        findViewById(R.id.homePageGroup).setVisibility(View.VISIBLE);
    }

    public void CloseHomePage() {
        findViewById(R.id.homePageGroup).setVisibility(View.GONE);
    }

    public void OpenWorkPage() {
        findViewById(R.id.workGroup).setVisibility(View.VISIBLE);
    }

    public void CloseWorkPage() {
        findViewById(R.id.workGroup).setVisibility(View.GONE);
    }

    public void OpenGamesPage() {
        findViewById(R.id.gamesGroup).setVisibility(View.VISIBLE);
    }

    public void CloseGamesPage() {
        findViewById(R.id.gamesGroup).setVisibility(View.GONE);
    }

    public void OpenSettingsPage() {
        findViewById(R.id.settingsGroup).setVisibility(View.VISIBLE);
    }

    public void CloseSettingsPage() {
        findViewById(R.id.settingsGroup).setVisibility(View.GONE);
    }

    public void OpenStatsPage() {
        findViewById(R.id.statsPageGroup).setVisibility(View.VISIBLE);
    }

    public void CloseStatsPage() {
        findViewById(R.id.statsPageGroup).setVisibility(View.GONE);
    }

    public void OpenPasswordChangePage() {
        findViewById(R.id.changePasswordGroup).setVisibility(View.VISIBLE);
    }

    public void ClosePasswordChangePage() {
        findViewById(R.id.changePasswordGroup).setVisibility(View.GONE);
    }

    public void OpenUsernameChangePage() {
        findViewById(R.id.changeUsernameGroup).setVisibility(View.VISIBLE);
    }

    public void CloseUsernameChangePage() {
        findViewById(R.id.changeUsernameGroup).setVisibility(View.GONE);
    }

    public void OpenResetStatsPage() {
        findViewById(R.id.resetStatsGroup).setVisibility(View.VISIBLE);
    }

    public void CloseResetStatsPage() {
        findViewById(R.id.resetStatsGroup).setVisibility(View.GONE);
    }

    public void OpenDeleteAccountPage() {
        findViewById(R.id.deleteAccountGroup).setVisibility(View.VISIBLE);
    }

    public void CloseDeleteAccountPage() {
        findViewById(R.id.deleteAccountGroup).setVisibility(View.GONE);
    }

    public void OpenFlipACoinPage() {
        findViewById(R.id.flipACoinGroup).setVisibility(View.VISIBLE);
    }

    public void CloseFlipACoinPage() {
        findViewById(R.id.flipACoinGroup).setVisibility(View.GONE);
    }

//==================================================================================================
//================================= Profile Modification ===========================================
//==================================================================================================

    public void UpdateUserFile() {
        File myObj = new File(filePath);
        try {
            FileWriter myWriter = new FileWriter(myObj);
            myWriter.write(profile.profileToString());
            myWriter.close();
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to write to file!");
            e.printStackTrace();
        }
    }

    public void ChangeUsername(View view) {
        String provided_username = ((EditText) findViewById(R.id.editTextTextUsernameChange)).getText().toString();
        String old_username = profile.getUsername();
        String old_filePath = filePath;
        if (provided_username.length() == 0) {
            Toast.makeText(getApplicationContext(), "Invalid Username", Toast.LENGTH_LONG).show();
        } else {
            profile.setUsername(provided_username);
            UpdateUserFile();
            File oldFile = new File(filePath);
            File directory = new File(getExternalFilesDir(null), "Cowsino");
            File newFile = new File(directory, provided_username + ".cow");
            filePath = newFile.getAbsolutePath();
            boolean renamed = oldFile.renameTo(newFile);
            if (renamed) {
                TextView usernameTextView = findViewById(R.id.username_textView);
                usernameTextView.setText(profile.getUsername());
                Toast.makeText(getApplicationContext(), "Changed Username Successfully!", Toast.LENGTH_LONG).show();
            } else {
                profile.setUsername(old_username);
                UpdateUserFile();
                filePath = old_filePath;
                System.err.println("[ERROR] File failed to rename.");
                Toast.makeText(getApplicationContext(), "Username Change Failed Unexpectedly.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void ChangePassword(View view) {
        String provided_password = ((EditText) findViewById(R.id.editTextTextPasswordChange)).getText().toString();
        if (provided_password.length() == 0) {
            Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG).show();
        } else {
            profile.setPassword(provided_password);
            UpdateUserFile();
            Toast.makeText(getApplicationContext(), "Changed Password Successfully!", Toast.LENGTH_LONG).show();
        }
    }

    public void ResetStats(View view) {
        profile.setWorkGains(0);
        profile.setCoinFlipGains(0);
        profile.setCoinFlipLoss(0);
        UpdateUserFile();
        BackToHomePage(view);
    }

    public void DeleteAccount(View view) {
        File myObj = new File(filePath);
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
        CloseDeleteAccountPage();
        OpenLoginPage();
    }

//==================================================================================================
//================================= Page Modification ==============================================
//==================================================================================================

//    public static void hideKeyboard(Activity activity) {
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        //Find the currently focused view, so we can grab the correct window token from it.
//        View view = activity.getCurrentFocus();
//        //If no view currently has focus, create a new one, just so we can grab a window token from it
//        if (view == null) {
//            view = new View(activity);
//        }
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//==================================================================================================
//================================= Working ========================================================
//==================================================================================================

    public void WorkForMoney(View view) {
        profile.setMoney(profile.getMoney() + 1);
        profile.setWorkGains(profile.getWorkGains() + 1);
        TextView moneyTextView = findViewById(R.id.money_textView);
        moneyTextView.setText(profile.moneyToString());
        UpdateUserFile();
    }

//==================================================================================================
//================================= Game Mains =====================================================
//==================================================================================================

    public void FlipACoinMain(View view) {
        hideKeyboardFrom(this, view);
        long bet = CollectBet();
        if (bet < 0) {
            Toast.makeText(getApplicationContext(), "Invalid Bet", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean heads = TakePick();
        boolean win = FlipCoin(heads);
        if (win) {
            profile.setMoney(profile.getMoney() + (bet * 2));
            profile.setCoinFlipGains(profile.getCoinFlipGains() + bet);
            TextView moneyTextView = findViewById(R.id.money_textView);
            moneyTextView.setText(profile.moneyToString());
            Toast.makeText(getApplicationContext(), "You won!", Toast.LENGTH_SHORT).show();
        } else {
            profile.setCoinFlipLoss(profile.getCoinFlipLoss() - bet);
            Toast.makeText(getApplicationContext(), "You lost.", Toast.LENGTH_SHORT).show();
        }
        UpdateUserFile();
    }

    public void BlackJackMain(View view) {
        //ToDo: Implement Blackjack
    }

//==================================================================================================
//================================= Flip-A-Coin Methods ============================================
//==================================================================================================

    public long CollectBet() {
        String betString = ((EditText) findViewById(R.id.editTextTextCoinFlipBet)).getText().toString();
        try {
            long bet = Long.parseLong(betString);
            if (bet < 0 || bet > profile.getMoney())
                return -1;
            profile.setMoney(profile.getMoney() - bet);
            TextView moneyTextView = findViewById(R.id.money_textView);
            moneyTextView.setText(profile.moneyToString());
            return bet;
        } catch (NumberFormatException e) {
            System.err.println("[ERROR] Coin Flip Bet not a valid format.");
            return -1;
        }
    }

    public boolean TakePick() {
        boolean heads;
        ToggleButton pickToggle = findViewById(R.id.headsOrTailsToggle);
        String pick = pickToggle.getText().toString();
        heads = pick.equals("Heads");
        return heads;
    }

    public boolean FlipCoin(boolean heads) {
        Random r = new Random();
        int result = r.nextInt(2);
        if (result == 0) {
            findViewById(R.id.chipHeadsImage).setVisibility(View.VISIBLE);
            findViewById(R.id.chipTailsImage).setVisibility(View.GONE);
        } else {
            findViewById(R.id.chipHeadsImage).setVisibility(View.GONE);
            findViewById(R.id.chipTailsImage).setVisibility(View.VISIBLE);
        }
        return (result == 0 && heads) || (result == 1) && !heads;
    }
}
