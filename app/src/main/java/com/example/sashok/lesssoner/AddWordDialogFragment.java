package com.example.sashok.lesssoner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
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

/**
 * Created by sashok on 23.3.17.
 */

public class AddWordDialogFragment extends AlertDialog implements View.OnClickListener, View.OnFocusChangeListener {
    Cursor cursor;
    EditText pol_word;
    LinearLayout root_layout;
    Context ctx;
    protected AddWordDialogFragment(final Context context)
    {
        super(context);
        ctx=context;
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
        for (int i=0;i<root_layout.getChildCount();i++){
            RelativeLayout layout= (RelativeLayout) root_layout.getChildAt(i);
            View text= layout.getChildAt(0);
            if (text instanceof  EditText)
            {
                EditText t=(EditText)text;
                Log.i("TAG",t.getText().toString());
            }

        }
        //addToDb(word);
        //logic here
        dismiss();

    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
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
