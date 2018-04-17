package com.google.engedu.wordstack;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 5;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2;
    private Stack<LetterTile> placedTiles = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                /**
                 **
                 **  YOUR CODE GOES HERE
                 **
                 **/
                words.add(word);
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        View word1LinearLayout = findViewById(R.id.word1);
        // word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        View word2LinearLayout = findViewById(R.id.word2);
        // word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                placedTiles.push(tile);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
                /**
                 **
                 **  YOUR CODE GOES HERE
                 **
                 **/
                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);
                    placedTiles.push(tile);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);
                    }
                    /**
                     **
                     **  YOUR CODE GOES HERE
                     **
                     **/
                    return true;
            }
            return false;
        }
    }

    public boolean onStartGame(View view) {

        ViewGroup word1LinearLayout = (ViewGroup) findViewById(R.id.word1);
        word1LinearLayout.removeAllViews();
        ViewGroup word2LinearLayout = (ViewGroup) findViewById(R.id.word2);
        word2LinearLayout.removeAllViews();

        stackedLayout.clear();
        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/

        word1=randomString();
        word2=randomString();
        System.out.println(word1);
        System.out.println(word2);
        String str = shuffle(word1,word2);
        //System.out.println(str+"\n");

        for(int i =str.length()-1;i>=0;i--){
            LetterTile tile = new LetterTile(view.getContext(),str.charAt(i));
            stackedLayout.push(tile);
        }

        return true;
    }


    public boolean onUndo(View view) {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        if(placedTiles.empty())return false;
        LetterTile tile =placedTiles.pop();
        tile.moveToViewGroup(stackedLayout);
        return true;
    }

    public String randomString(){
        int i= random.nextInt(words.size());
        while(words.get(i).length()!=WORD_LENGTH) {
            i = random.nextInt(words.size());
        }
        return words.get(i);
    }

    public String shuffle(String w1, String w2){
        String str="";
        int i=0;
        int j=0;

        while(i!=w1.length() && j!=w2.length()){
            int k = random.nextInt(2);
            if(k==0){
                str+=w1.charAt(i);
                i++;
            }
            else{
                str+=w2.charAt(j);
                j++;
            }
        }
        if(i==w1.length())str+=w2.substring(j);
        else str+=w1.substring(i);

        return str;
    }
}