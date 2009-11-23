package com.eastsunset.bmw.chosungkeyboard;

import java.util.ArrayList;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastsunset.bmw.R;
import com.eastsunset.bmw.bus.SeoulBus;

public class ChosungKeyboard implements OnClickListener
{
    public enum KeyboardType { han, num, hannum };
    
    private View chosungKeyboardView;
    private EditText textBox;
    private OnClickListener externalOnClickListener;
    
    private KeyboardType keyboardType;
    private Button externalConfirmButton;
    private int characterLimit;
    
    public ChosungKeyboard(Activity activity, EditText textBox, OnClickListener ocl){
        //this.textBox = textBox;
        
        this.externalOnClickListener = ocl;
        this.textBox = textBox;
        chosungKeyboardView = (RelativeLayout) activity.getLayoutInflater().inflate(com.eastsunset.bmw.R.layout.chosungkeyboard, null);
        ((RelativeLayout)chosungKeyboardView.findViewById(R.id.keyboardbg)).setBackgroundColor(0xFFFFFFFF);
        setListener();
    }
    
    public View getKeyboard(KeyboardType keyboardType){
        this.keyboardType = keyboardType;
        
        if (keyboardType == KeyboardType.han) setHanKeyboard();
        else if (keyboardType == KeyboardType.num) setNumKeyboard();
        else if (keyboardType == KeyboardType.hannum) {
            setHanKeyboard();
            chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.mode).setVisibility(View.VISIBLE);
        }
        return chosungKeyboardView;
    }
    
    public void setTextBox(EditText editText){
        this.textBox = editText;
    }
    
    private void setNumKeyboard(){
        
        keyboardType = KeyboardType.num;
        
        ((RelativeLayout)chosungKeyboardView.findViewById(R.id.keyboardbg)).setBackgroundColor(0xFFFFFFFF);
        
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.q).setVisibility(View.INVISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.a).setVisibility(View.INVISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.z).setVisibility(View.INVISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.t).setVisibility(View.INVISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.g).setVisibility(View.INVISIBLE);
        
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.w).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.e).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.r).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.s).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.d).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.f).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.x).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.c).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.v).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.bs).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.zero).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.confirm).setVisibility(View.VISIBLE);
        
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
        
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.bs)).setText("<-");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.mode)).setText("한글");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.confirm)).setText("지우기");
        
    }
    
private void setHanKeyboard(){
    
        keyboardType = KeyboardType.han;
        
        ((RelativeLayout)chosungKeyboardView.findViewById(R.id.keyboardbg)).setBackgroundColor(0xFFFFFFFF);
        
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.q).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.a).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.z).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.t).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.g).setVisibility(View.VISIBLE);
        
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.w).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.e).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.r).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.s).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.d).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.f).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.x).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.c).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.v).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.bs).setVisibility(View.VISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.zero).setVisibility(View.INVISIBLE);
        chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.confirm).setVisibility(View.VISIBLE);
        
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
        
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.bs)).setText("<-");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.mode)).setText("숫자");
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.confirm)).setText("지우기");
        
    }



    public void keyBoardHide(boolean hide){
    
    if(hide) {
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.q)).setVisibility(View.INVISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.a)).setVisibility(View.INVISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.z)).setVisibility(View.INVISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.t)).setVisibility(View.INVISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.g)).setVisibility(View.INVISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.w)).setVisibility(View.INVISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.e)).setVisibility(View.INVISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.r)).setVisibility(View.INVISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.s)).setVisibility(View.INVISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.d)).setVisibility(View.INVISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.f)).setVisibility(View.INVISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.x)).setVisibility(View.INVISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.c)).setVisibility(View.INVISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.v)).setVisibility(View.INVISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.bs)).setVisibility(View.INVISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.zero)).setVisibility(View.INVISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.confirm)).setText("다시검색");
        ((RelativeLayout)chosungKeyboardView.findViewById(R.id.keyboardbg)).setBackgroundColor(0x00FFFFFF);
    } else {
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.w)).setVisibility(View.VISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.e)).setVisibility(View.VISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.r)).setVisibility(View.VISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.s)).setVisibility(View.VISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.d)).setVisibility(View.VISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.f)).setVisibility(View.VISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.x)).setVisibility(View.VISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.c)).setVisibility(View.VISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.v)).setVisibility(View.VISIBLE);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.bs)).setVisibility(View.VISIBLE);
        
        if(keyboardType == KeyboardType.num){
            ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.zero)).setVisibility(View.VISIBLE);
        } else if(keyboardType == KeyboardType.han){
            ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.q)).setVisibility(View.VISIBLE);
            ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.a)).setVisibility(View.VISIBLE);
            ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.z)).setVisibility(View.VISIBLE);
            ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.t)).setVisibility(View.VISIBLE);
            ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.g)).setVisibility(View.VISIBLE);
        }
        
        ((RelativeLayout)chosungKeyboardView.findViewById(R.id.keyboardbg)).setBackgroundColor(0xFFFFFFFF);
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.confirm)).setText("지우기");
    }
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
        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.confirm)).setOnClickListener(this);
//        ((Button)chosungKeyboardView.findViewById(com.eastsunset.bmw.R.id.confirm)).setTag("busSearchConfirmButton");
        
}

    public void onClick(View v)
    {
        
        if (((Button)v).getText().equals("한글")) {
            setHanKeyboard();
            externalOnClickListener.onClick(v);
        }
        else if (((Button)v).getText().equals("숫자")) {
            setNumKeyboard();
            externalOnClickListener.onClick(v);
        }
        else if (((Button)v).getText().equals("<-")) {
            removeLastCharacter();
            checkCharacterLength();
        }
        else if (((Button)v).getText().equals("지우기")) {
            externalOnClickListener.onClick(v);
            removeText();
        }
        else if (((Button)v).getText().equals("다시검색")) {
            externalOnClickListener.onClick(v);
            removeText();
            checkCharacterLength();
            keyBoardHide(false);
        }
        else if (((Button)v).getText().equals("삭제")) {
            removeText();
            externalOnClickListener.onClick(v);
        } else {
            addCharacter((String)((Button)v).getText());
            checkCharacterLength();
        }
    }

    private void checkCharacterLength()
    {
        
        if(textBox.getText().length() == 0)
        {
            externalConfirmButton.setEnabled(false);
            return;
        }  
        if(textBox.getText().charAt(0) < 58)
        {
            if(textBox.getText().length() > 2) 
            {
                externalConfirmButton.setEnabled(true);
            }
            else 
            {
                externalConfirmButton.setEnabled(false);
            }
            
        // 문자
        } else {
            if(textBox.getText().length() > 2) 
            {
                externalConfirmButton.setEnabled(true);
            } 
            else 
            {
                externalConfirmButton.setEnabled(false);
            }
        }
    }
    
    
    private void addCharacter(String addCharacter){
        if(characterLimit != textBox.getText().length())
            textBox.setText(textBox.getText() + addCharacter);
    }
    
    private void removeLastCharacter(){ 
        if(textBox.getText().length() != 0 )
            textBox.setText(textBox.getText().subSequence(0, textBox.getText().length()-1));
    }
    private void removeText(){
        textBox.setText("");
    }

    public void addConfirmButton(Button confirmButton)
    {
        externalConfirmButton = confirmButton;
    }
    
    public void setCharacterLimit(int limit)
    {
        this.characterLimit = limit;
    }
    
    
}

