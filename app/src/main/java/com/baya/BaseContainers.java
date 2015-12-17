package com.baya;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;



/**
 * Created by prachi on 02-11-2015.
 * for hierarchy of the fragment
 */
public class BaseContainers extends Fragment{
    public void replaceFragment(Fragment fragment, boolean addToBackStack)
    {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (addToBackStack)
        {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.container_framelayout, fragment);
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public boolean popFragment()
    {
        System.out.println("helllo " + getChildFragmentManager().getBackStackEntryCount());

        boolean isPop = false;
        if (getChildFragmentManager().getBackStackEntryCount() > 0)
        {
            isPop = true;
            getChildFragmentManager().popBackStack();
        }
        else
        {
            isPop = false;
        }
        return isPop;
    }
}
