package com.example.sashok.lesssoner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sashok on 23.3.17.
 */

public class AddWordDialogFragment extends AlertDialog implements View.OnClickListener, View.OnFocusChangeListener {
    Cursor cursor;
    EditText pol_word;
    LinearLayout root_layout;
    Context ctx;
    SQLiteDatabase chatDBlocal;
    MainActivity.DataChanged listener;
    public final String DB_NAME="words.db";
    public final String CREATE_PL_WORDS_TABLE="CREATE TABLE IF NOT EXISTS "+ Word.TABLE_NAME_PL_WORD +
            " (_id integer primary key  AUTOINCREMENT unique,"+ Word.TABLE_COLUMN_NAME_PL_WORD+" text not null,"+ Word.TABLE_COLUMN_PL_FAVOURITE+" INTEGER DEFAULT 0,"+ Word.TABLE_COLUMN_DEGREE+" INTEGER DEFAULT 0 )";
    public final String CREATE_RUS_WORDS_TABLE="CREATE TABLE IF NOT EXISTS "+ Word.TABLE_NAME_RUS_WORD +
            " (_id integer primary key AUTOINCREMENT unique ,"+ Word.TABLE_COLUMN_NAME_RUS_WORD+" text not null,"+Word.TABLE_COLUMN_PL_ID+" Integer not null,"+
            " CONSTRAINT receive_key FOREIGN key("+Word.TABLE_COLUMN_PL_ID+") REFERENCES "+Word.TABLE_NAME_PL_WORD+"("+Word.TABLE_COLUMN_NAME_ID+") ON DELETE SET NULL)";

    protected AddWordDialogFragment(final Context context, MainActivity.DataChanged listener)
    {
        super(context);
        ctx=context;
        this.listener=listener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.add_word, null);
        setView(view);
        pol_word=(EditText)view.findViewById(R.id.word_pol_box);
        RelativeLayout add_trans=(RelativeLayout)view.findViewById(R.id.add_trans_layout);
        add_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                root_layout=(LinearLayout)view.findViewById(R.id.add_edit_text_layout);
                root_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard(view);
                    }
                });
                final RelativeLayout layout=new RelativeLayout(context);
                ViewGroup.LayoutParams rel_layout_params = view.findViewById(R.id.layout_delete_edit).getLayoutParams();
                layout.setLayoutParams(rel_layout_params);

                final ImageView delete_btn=(ImageView)view.findViewById(R.id.delete_button);
                ViewGroup.LayoutParams delete_view_params = delete_btn.getLayoutParams();
                final ImageView new_delete_btn=new ImageView(context);
                new_delete_btn.setBackgroundResource(R.drawable.cancel_btn);
                new_delete_btn.setLayoutParams(delete_view_params);
                new_delete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Animation animation= AnimationUtils.loadAnimation(context,R.anim.slide_right);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                new Handler().post(new Runnable() {
                                    public void run() {
                                        root_layout.removeView(layout);
                                    }
                                });

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        layout.startAnimation(animation);

                    }
                });

                EditText editText=new EditText(context);
                ViewGroup.LayoutParams lp = pol_word.getLayoutParams();
                editText.setHintTextColor(pol_word.getCurrentHintTextColor());
                ContextCompat.getDrawable(ctx, R.drawable.edit_text_style);
                editText.setBackgroundResource(R.drawable.edit_text_style);
                editText.setHint(R.string.add_word_rus);
                editText.requestFocus();
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                layout.addView(editText,lp);
                layout.addView(new_delete_btn,delete_view_params);
                root_layout.addView(layout,rel_layout_params);
            }
        });
        view.findViewById(R.id.add_button).setOnClickListener(this);
        setTitle(R.string.add_word_title);
//        pass_form.setOnEditorActionListener(new EditText.OnEditorActionListener(){
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    TextView view1=(TextView) findViewById(R.id.reg_button);
//                    view1.requestFocus();
//                    hideKeyboard(view1);
//                    return true;
//                }
//                return false;
//            }
//        }    );
    }
    @Override
    public void onClick(View view) {
        if (root_layout==null || root_layout.getChildCount()==1){
            Toast.makeText(ctx,"Добавьте перевод",Toast.LENGTH_LONG).show();

        }
        else {
            Word new_word=new Word();
            for (int i = 0; i < root_layout.getChildCount(); i++) {
                RelativeLayout layout = (RelativeLayout) root_layout.getChildAt(i);
                View text = layout.getChildAt(0);
                if (text instanceof EditText) {
                    EditText t = (EditText) text;
                    if (!t.getText().toString().isEmpty()) {
                        if (i==0){
                            new_word.pl_word=t.getText().toString();
                        }
                        else {
                            new_word.translation.add(t.getText().toString());
                        }
                    }

                }
            }
            if (new_word.pl_word!=null && new_word.translation.size()!=0){
                    chatDBlocal =ctx.openOrCreateDatabase(DB_NAME,
                            Context.MODE_PRIVATE, null);
                    chatDBlocal.execSQL(CREATE_PL_WORDS_TABLE);
                    chatDBlocal.execSQL(CREATE_RUS_WORDS_TABLE);
                    ContentValues values=new ContentValues();
                    values.put(Word.TABLE_COLUMN_DEGREE,new_word.degree);
                    values.put(Word.TABLE_COLUMN_PL_FAVOURITE,new_word.favourite);
                    values.put(Word.TABLE_COLUMN_NAME_PL_WORD,new_word.pl_word);
                    chatDBlocal.insert(Word.TABLE_NAME_PL_WORD,null,values);
                    Cursor cursor = chatDBlocal.rawQuery("SELECT _id from "+Word.TABLE_NAME_PL_WORD +" ORDER BY _id DESC LIMIT 1;", null);
                    int id=-1;
                    if (cursor.moveToFirst()){
                        id=cursor.getInt(cursor.getColumnIndex(Word.TABLE_COLUMN_NAME_ID));
                        for (String str:new_word.translation) {
                            values=new ContentValues();
                            values.put(Word.TABLE_COLUMN_NAME_RUS_WORD,str);
                            values.put(Word.TABLE_COLUMN_PL_ID,id);
                            chatDBlocal.insert(Word.TABLE_NAME_RUS_WORD,null,values);
                        }
                    }
                    listener.onDataChanged();
                    dismiss();
            }
            else Toast.makeText(ctx,"Добавьте перевод",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (!b)
        {
            hideKeyboard(view);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)ctx.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
