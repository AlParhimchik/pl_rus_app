package com.example.sashok.lesssoner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * Created by sashok on 22.3.17.
 */

public class CardViewFragment extends android.support.v4.app.Fragment  implements Animation.AnimationListener {
    Word word;
    CardView cardView;
    private Animation animation1;
    private Animation animation2;
    private TextView textView;
    private Language currentLanguage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState!=null) {
            if (word.pl_word=="" || word==null){
                word=(Word)savedInstanceState.getSerializable("word");
            }
        }
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.card_view, container, false);
        textView=(TextView) rootView.findViewById(R.id.info_text);
        setText();
        animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.card_flip_left_in);
        animation1.setAnimationListener(this);
        animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.card_flip_left_out);
        animation2.setAnimationListener(this);
        cardView=(CardView)rootView.findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.clearAnimation();
                cardView.setAnimation(animation1);
                cardView.startAnimation(animation1);
            }
//
       });

        return rootView;
    }

    private void setText() {

        if (currentLanguage==Language.Russian) {
            if (word.translation.size()!=0) {
                String result="";
                for (int i=0;i<word.translation.size();i++){
                    result+=word.translation.get(i);
                    if (i!=word.translation.size()-1) result+=",\n";
                }
                textView.setText(result);
            }
            else textView.setText("нету перевода");
        }
        else{
            textView.setText(word.pl_word);

        }

    }

    public CardViewFragment(Word word , Language lang){
        this.word=word;
        currentLanguage=lang;

    }

    public CardViewFragment(){


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("word",word);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    public void hello(Language lang){
        currentLanguage=lang;
        setText();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    if (animation==animation1) {

        cardView.clearAnimation();
        cardView.setAnimation(animation2);
        cardView.startAnimation(animation2);
        if (currentLanguage == Language.Russian) {
            currentLanguage = Language.Polish;
        } else {
            currentLanguage = Language.Russian;
        }
        setText();
    }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


}
