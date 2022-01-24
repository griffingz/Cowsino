package com.ggz.cowsino;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//==================================================================================================
//================================= Log In =========================================================
//==================================================================================================

    public void LoginPageToHomePage(View view) {

        //ToDo: Profile persistence and password verification

        EditText usernameEditText = findViewById(R.id.editTextTextEmailAddress);
        String username = usernameEditText.getText().toString();
        profile = new Profile(username);

        TextView usernameTextView = findViewById(R.id.username_textView);
        TextView moneyTextView = findViewById(R.id.money_textView);

        usernameTextView.setText(profile.getUsername());
        moneyTextView.setText(profile.moneyToString());

        CloseLoginPage();
        OpenHomePage();
    }

//==================================================================================================
//================================= Page Transfers =================================================
//==================================================================================================

    public void HomePageToLoginPage(View view) {
        CloseHomePage();
        OpenLoginPage();
    }

    public void HomePageToWorkPage(View view) {
        CloseHomePage();
        OpenWorkPage();
    }

    public void BackToHomePage(View view) {
        CloseWorkPage();
        CloseGamesPage();
        OpenHomePage();
    }

    public void HomePageToGamesPage(View view) {
        CloseHomePage();
        OpenGamesPage();
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

//==================================================================================================
//================================= Working ========================================================
//==================================================================================================

    public void WorkForMoney(View view) {
        profile.setMoney(profile.getMoney() + 1);
        TextView moneyTextView = findViewById(R.id.money_textView);
        moneyTextView.setText(profile.moneyToString());
    }

//==================================================================================================
//================================= Games ==========================================================
//==================================================================================================

    public void FlipACoinMain(View view) {
        //ToDo: Implement Flip-A-Coin
    }

    public void BlackJackMain(View view) {
        //ToDo: Implement Blackjack
    }
}