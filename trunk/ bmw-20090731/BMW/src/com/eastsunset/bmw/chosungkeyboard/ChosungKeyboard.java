package com.eastsunset.bmw.chosungkeyboard;

import java.util.ArrayList;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastsunset.bmw.R;

public class ChosungKeyboard implements OnClickListener
{
    public enum Keyboard { han, num };
    
    private View chosungKeyboardView;
    private EditText textBox;
    private OnClickListener externalClickListener;
    private ChosungDBManager chosungDBManager;
    
    public ChosungKeyboard(Activity activity, EditText textBox, OnClickListener ocl, SQLiteDatabase liteDatabase){
        //this.textBox = textBox;
        
        this.externalClickListener = ocl;
        chosungDBManager= new ChosungDBManager(liteDatabase);
        chosungKeyboardView = (RelativeLayout) activity.getLayoutInflater().inflate(com.eastsunset.bmw.R.layout.chosungkeyboard, null);
        this.textBox = (EditText)chosungKeyboardView.findViewById(R.id.busSearchTextBox);
        setListener();
    }
    
    public View getKeyboard(Keyboard keyboard){
        
        if (keyboard == Keyboard.han) setHanKeyboard();
        else setNumKeyboard();
        return chosungKeyboardView;
    }
    
    private void addCharacter(String addCharacter){
        textBox.setText(textBox.getText() + addCharacter);
    }
    
    private void removeLastCharacter(){
        if(textBox.getText().length() != 0 )
            textBox.setText(textBox.getText().subSequence(0, textBox.getText().length()-1));
    }
    
    private void setNumKeyboard(){
        
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.q).setVisibility(View.INVISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.a).setVisibility(View.INVISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.z).setVisibility(View.INVISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.t).setVisibility(View.INVISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.g).setVisibility(View.INVISIBLE);
        
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.w)).setText("1");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.e)).setText("2");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.r)).setText("3");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.s)).setText("4");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.d)).setText("5");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.f)).setText("6");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.x)).setText("7");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.c)).setText("8");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.v)).setText("9");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.zero)).setText("0");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.zero)).setVisibility(View.VISIBLE);
        
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.bs)).setText("<-");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.mode)).setText("한글");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.confirm)).setText("입력");
        
    }
    
private void setHanKeyboard(){
        
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.q).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.a).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.z).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.t).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.g).setVisibility(View.VISIBLE);

        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.q)).setText("ㅂ");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.a)).setText("ㅁ");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.z)).setText("ㅋ");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.t)).setText("ㅅ");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.g)).setText("ㅎ");
        
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.w)).setText("ㅈ");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.e)).setText("ㄷ");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.r)).setText("ㄱ");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.s)).setText("ㄴ");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.d)).setText("ㅇ");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.f)).setText("ㄹ");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.x)).setText("ㅌ");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.c)).setText("ㅊ");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.v)).setText("ㅍ");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.zero)).setVisibility(View.INVISIBLE);
        
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.bs)).setText("<-");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.mode)).setText("숫자");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.confirm)).setText("입력");
        
    }

private void setListener() {
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.q)).setOnClickListener(this);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.w)).setOnClickListener(this);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.e)).setOnClickListener(this);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.r)).setOnClickListener(this);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.t)).setOnClickListener(this);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.a)).setOnClickListener(this);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.s)).setOnClickListener(this);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.d)).setOnClickListener(this);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.f)).setOnClickListener(this);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.g)).setOnClickListener(this);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.z)).setOnClickListener(this);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.x)).setOnClickListener(this);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.c)).setOnClickListener(this);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.v)).setOnClickListener(this);
    
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.bs)).setOnClickListener(this);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.mode)).setOnClickListener(this);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.zero)).setOnClickListener(this);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.confirm)).setOnClickListener(this.externalClickListener);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.confirm)).setTag("busSearchConfirmButton");
        
}

    @Override
    public void onClick(View v)
    {
             if (((Button)v).getText().equals("<-"))   removeLastCharacter();
             else if (((Button)v).getText().equals("한글")) setHanKeyboard();
             else if (((Button)v).getText().equals("숫자")) setNumKeyboard();
             else                                           addCharacter((String)((Button)v).getText());
             
             if(textBox.getText().toString().length() > 2){
                 getChosungWords();
             }
    }

    private void getChosungWords()
    {
        ArrayList<String> chosungSearchResult = chosungDBManager.searchChosung(textBox.getText().toString(), 7, 8);
        try{
             ((TextView)chosungKeyboardView.findViewById(R.id.words1)).setText(chosungSearchResult.get(0));
             ((TextView)chosungKeyboardView.findViewById(R.id.words2)).setText(chosungSearchResult.get(1));
             ((TextView)chosungKeyboardView.findViewById(R.id.words3)).setText(chosungSearchResult.get(2));
             ((TextView)chosungKeyboardView.findViewById(R.id.words4)).setText(chosungSearchResult.get(3));
             ((TextView)chosungKeyboardView.findViewById(R.id.words5)).setText(chosungSearchResult.get(4));
             ((TextView)chosungKeyboardView.findViewById(R.id.words6)).setText(chosungSearchResult.get(5));
        } catch (IndexOutOfBoundsException e){
            // todo: write message
        }
    }
}

