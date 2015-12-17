package com.baya;

/*
* created by Neeraj
* For sliding menu basic functioanlity
* */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity
{

    protected Fragment mFrag;
    FragmentTransaction t;
    private int mTitleRes;

    public BaseActivity(int titleRes)
    {
        mTitleRes = titleRes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(mTitleRes);
        // set the Behind View - Left
        setBehindContentView(R.layout.menu_frame);
        if (savedInstanceState == null)
        {
            mFrag = new SampleListFragment();
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.menu_frame, mFrag);
            t.commit();
        }
        else
        {
            mFrag = (SampleListFragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        }
        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

    }
}

