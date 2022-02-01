package com.example.sohbetet.Utils;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.sohbetet.R;


public class ChangeFragment {

    private Context mContex;

    public ChangeFragment(Context mContex) {
        this.mContex = mContex;
    }

    public void change(Fragment fragment)
    {
        ((FragmentActivity) mContex).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_tutucu, fragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void changeWithParamter(Fragment fragment, String userid)
    {
        Bundle bundle = new Bundle();
        bundle.putString("userid", userid);
        fragment.setArguments(bundle);

        ((FragmentActivity) mContex).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_tutucu, fragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

}
