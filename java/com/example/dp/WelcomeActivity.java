package com.example.dp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mpager;

    private int[] layouts={R.layout.firstslide,R.layout.secondslide,R.layout.thirdslide,R.layout.fourthslide};

    private MpagerAdapter mpagerAdapter;

    private LinearLayout DotsLayout;
    private ImageView[] dots;

    private Button Btnnext,Btnskip;


    protected void onCreate(Bundle savedInstanceState)
    {

        if(new PreferenceManager(this).checkPreference())
        {
            loadhome();
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mpager=(ViewPager)findViewById(R.id.viewPager);
        mpagerAdapter=new MpagerAdapter(layouts,this);
        mpager.setAdapter(mpagerAdapter);



        DotsLayout=(LinearLayout)findViewById(R.id.dotslayout);

        Btnnext=(Button)findViewById(R.id.btnnext);
        Btnskip=(Button)findViewById(R.id.btnskip);

        Btnnext.setOnClickListener(this);
        Btnskip.setOnClickListener(this);

        createDots(0);

        mpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                    createDots(position);

                    if(position== layouts.length-1)
                    {
                        Btnnext.setText("Start");
                        Btnskip.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        Btnnext.setText("Next");
                        Btnskip.setVisibility(View.VISIBLE);
                    }



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void createDots(int current_position)
    {
        if(DotsLayout!=null)
        {
            DotsLayout.removeAllViews();
        }
        dots=new ImageView[layouts.length];

        for(int i=0;i<layouts.length;i++)
        {
            dots[i]=new ImageView(this);

            if(i==current_position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.default_dots));
            }

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(4,0,4,0);
            DotsLayout.addView(dots[i],params);

        }

    }

    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btnnext:
                loadNextSlide();
                break;
            case R.id.btnskip :
                loadhome();
                new PreferenceManager(this).writePreference();
                break;

        }

    }

    private void loadhome()
    {

        startActivity(new Intent(this,Register.class));
        finish();


    }

    private void loadNextSlide()
    {

        int next_slide=mpager.getCurrentItem()+1;
        if(next_slide < layouts.length)
        {

            mpager.setCurrentItem(next_slide);
        }
        else
        {
            loadhome();
            new PreferenceManager(this).writePreference();
        }

    }




}
