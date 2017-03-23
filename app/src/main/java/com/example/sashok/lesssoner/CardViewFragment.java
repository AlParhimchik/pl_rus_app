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
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * Created by sashok on 22.3.17.
 */

public class CardViewFragment extends android.support.v4.app.Fragment  implements Animation.AnimationListener {
    String str;
    CardView cardView;
    private Animation animation1;
    private Animation animation2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState==null) {
        Log.i("TAG","NULL");
        }
        else {
            if (str=="" || str==null){
                str=savedInstanceState.getString("text");
            }
        }
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.card_view, container, false);
        final TextView textView=(TextView) rootView.findViewById(R.id.info_text);
        textView.setText(str);
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
//                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.card_flip_left_in);
//                animation.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        Animation animation_out = AnimationUtils.loadAnimation(getActivity(), R.anim.card_flip_left_out);
//                        animation_out.setAnimationListener(new Animation.AnimationListener() {
//                            @Override
//                            public void onAnimationStart(Animation animation) {
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animation animation) {
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animation animation) {
//                                textView.setText("loxi vse");
//                            }
//                        });
//
//                        cardView.startAnimation(animation_out);
//                    }
//                });
//
//                cardView.startAnimation(animation);
//            }
       });

        return rootView;
    }
    public CardViewFragment(String string){
        str=string;

    }
    public CardViewFragment(){


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("text",this.str);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
    if (animation==animation1){

        cardView.clearAnimation();
        cardView.setAnimation(animation2);
        cardView.startAnimation(animation2);
    }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
