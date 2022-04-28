package com.example.allergyapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

     public int not;
     public PageAdapter(FragmentManager fm,int not){
         super(fm);
         this.not=not;
     }
     public Fragment getItem(int position){
         switch (position){
             case 0:
                  return new ingredient();
             case 1:
                 return new allergygroups();
         }
         return null;
     }

     public int getCount(){
         return not;
     }
     public int getItemPosition(@NonNull Object object){
         return POSITION_NONE;
     }


}
