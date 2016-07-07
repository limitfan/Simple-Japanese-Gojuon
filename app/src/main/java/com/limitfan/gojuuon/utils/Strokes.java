package com.limitfan.gojuuon.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Vector;

import android.content.Context;

import com.limitfan.gojuuon.R;

public class Strokes {
	HashMap<String,Vector<String>> hm=new HashMap<String,Vector<String>>();
	Context c;
	private void readStrokes(Context c){
        hm.clear();
		InputStreamReader isr;
		int cnt=0;
		try {
			isr = new InputStreamReader(c.getResources().openRawResource(R.raw.strokedata));

			BufferedReader br=new BufferedReader(isr);
			String line=null;
			String cur="";
			
			
			while((line=br.readLine())!=null)
			{
				if(!line.startsWith("M")){
					line=line.trim();
					String[]arr=line.split(" ");
					cur=arr[0];
					hm.put(arr[0], new Vector<String>());
                	  
				}
				else{
					line=line.trim();
					hm.get(cur).add(line);
			//
				//	System.out.println(cur.hashCode());
				//	System.out.println("Current:["+cur+"]"+" "+"["+line+"]");
				}
			}

		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Counter:"+cnt);
	

	}
	public Strokes(Context c){
        this.c=c;
		readStrokes(c);
	}
   public Vector<String> getStrokeDescription(String s){
	   Vector<String> ret=hm.get(s);
	   return ret;
   }
  
}
