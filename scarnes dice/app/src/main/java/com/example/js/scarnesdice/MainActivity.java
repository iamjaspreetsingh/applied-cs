package com.example.hritik.scarnesdice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public int userScore = 0;
    public int computerScore = 0;
    public int userTurn = 0;
    public int computerTurn = 0;
    public TextView score_view;
    public ImageView diceImage;
    public TextView resultview;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultview = (TextView) findViewById(R.id.result);
        score_view = (TextView) findViewById(R.id.scoreview);
        diceImage = (ImageView) findViewById(R.id.diceImage);
        Button rollButton = (Button) findViewById(R.id.rollButton);
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultview.setVisibility(View.INVISIBLE);
                int n = random.nextInt(6);
                int arr[] = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};
                diceImage.setImageResource(arr[n]);
                if ((n + 1) == 1) {
                    userTurn = 0;
                    hold(null);
                }
                else {
                    userTurn = userTurn + n + 1;
                    score_view.setText("Your Score: " + userScore + " Computer Score: " + computerScore + " Your turn score: " + userTurn + " Computer turn: " + computerTurn);
                }
            }
        });
    }

    public void reset(View view) {
        userScore = 0;
        computerScore = 0;
        userTurn = 0;
        computerTurn = 0;
        score_view.setText("Your Score: " + userScore + " Computer Score: " + computerScore + " Your turn score: " + userTurn + " Computer turn: " + computerTurn);
    }

    public void hold(View view) {
        userScore += userTurn;
        score_view.setText("Your Score: " + userScore + " Computer Score: " + computerScore + " Your turn score: " + userTurn + " Computer turn: " + computerTurn);
        if (userScore > 100) {
            TextView resultview = (TextView) findViewById(R.id.result);
            resultview.setText("user Wins");
            resultview.setVisibility(View.VISIBLE);
            reset(null);
        }

        computerTurn();
    }

    public void computerTurn() {
        int n = random.nextInt(6) + 1;
        while ((n) != 1 && computerTurn < 20) {
            computerTurn += n;
            n = random.nextInt(6) + 1;
        }
        if (n == 1)
            computerTurn = 0;
        computerScore += computerTurn;
        score_view.setText("Your Score: " + userScore + " Computer Score: " + computerScore + " Your turn score: " + userTurn + " Computer turn: " + computerTurn);

        if (computerScore > 100) {
            resultview.setText("Computer Wins");
            resultview.setVisibility(View.VISIBLE);
            reset(null);
        }
    }
}
