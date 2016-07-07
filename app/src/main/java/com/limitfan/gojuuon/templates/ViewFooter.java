package com.limitfan.gojuuon.templates;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ViewFooter extends LinearLayout{
   public ViewFooter(Context paraContext,AttributeSet paramAttributeSet)  
   {
	   super(paraContext,paramAttributeSet);
   }
  
   
   private void resetTabsState()
   {
     int i = 0;
     int j = getChildCount();
     while (true)
     {
       if (i >= j)
         return;
       getChildAt(i).setSelected(false);
       i += 1;
     }
   }
   
   public void select(int i){
	   resetTabsState();
	   getChildAt(i).setSelected(true);
   }
   private void startMainTabActivity(View paramView)
   {
	 
   }
   public void switchTab(View paramView, int paramInt)
   {   
		   
	
   }

}
