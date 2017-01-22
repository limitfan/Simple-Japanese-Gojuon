/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.limitfan.gojuuon.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import com.limitfan.gojuuon.*;
import com.limitfan.gojuuon.acts.ActKana;

import static com.umeng.xp.common.d.R;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 */
public class ScreenSlidePageFragment extends Fragment {

    public static final String ENCODING = "UTF-8";
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(com.limitfan.gojuuon.R.layout.detail_slide_page, container, false);


        ImageView stroke = (ImageView)(rootView.findViewById(com.limitfan.gojuuon.R.id.stroke));
        String romaji=Common.roma[getPageNumber()];


        try{
            String img="";
            if(ActKana.isHira)
                img="kanagraph/hiragana_"+romaji+".jpg";
            else
                img="kanagraph/katakana_"+romaji+".jpg";
            InputStream is=getContext().getAssets().open(img, AssetManager.ACCESS_STREAMING);

            Bitmap bm= BitmapFactory.decodeStream(is);
            stroke.setImageBitmap(bm);
        }
        catch(Exception e){

        }

        ViewGroup listView = (ViewGroup)rootView.findViewById(com.limitfan.gojuuon.R.id.list);
        ViewGroup demo_speak = (ViewGroup)inflater.inflate(com.limitfan.gojuuon.R.layout.demo_list_item, null);
        ((TextView)demo_speak.findViewById(com.limitfan.gojuuon.R.id.text)).setText(com.limitfan.gojuuon.R.string.demo);
        ((ImageView)demo_speak.findViewById(com.limitfan.gojuuon.R.id.icon)).setImageResource(com.limitfan.gojuuon.R.drawable.speak_off);

        listView.addView(demo_speak);

        TextView sample=(TextView)(rootView.findViewById(com.limitfan.gojuuon.R.id.sample));
        setSample(sample);
        demo_speak.setOnClickListener(new SpeakListener());

        //stroke.setImageResource();

        // Set the title view to show the page number.
        //EditText main = ((EditText) rootView.findViewById(R.id.Description));
       // main.setText(getString(R.string.title_template_step, mPageNumber + 1));
        //main.setEnabled(false);
       // main.setBackgroundColor(Color.TRANSPARENT);
       // main.setText(getFromAssets("details/"+(mPageNumber+1)+".txt"));

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }


    public void setSample(TextView sample){
        String txt="";
        String ret="";
        int resID=0;

        try{
            if(ActKana.isHira)
                txt="h"+Common.roma[getPageNumber()];
            else
                txt="k"+Common.roma[getPageNumber()];
            resID=this.getResources().getIdentifier(txt, "string", "com.limitfan.gojuuon");
            ret=this.getString(resID);
        }
        catch(Exception e){

        }

        sample.setText(ret);

    }

    class SpeakListener implements View.OnClickListener {
        String s;
        public SpeakListener(){

        }
        @Override
        public void onClick(View arg0){
            this.s=getSample();
            ((ActKana)getActivity()).speak(s);
            //ExoPlayerFactory.

        }
    }
//
    public String getSample(){
        String txt="";
        String ret="";
        int resID=0;
        try{
            if(ActKana.isHira)
                txt="h"+Common.roma[getPageNumber()];
            else
                txt="k"+Common.roma[getPageNumber()];
            resID=this.getResources().getIdentifier(txt, "string", "com.limitfan.gojuuon");
            ret=this.getString(resID);
        }
        catch(Exception e){

        }
        return ret;

    }

    public String getFromAssets(String fileName){
        String result = "额，好像程序有点问题了。。。";
        try {
            InputStream in = getResources().getAssets().open(fileName, AssetManager.ACCESS_STREAMING);
            //获取文件的字节数
            int length = in.available();
            //创建byte数组
            byte[]  buffer = new byte[length];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            //result = EncodingUtils.getString(buffer, ENCODING);
            //result = EncodingUtils.getString(buffer, ENCODING);
            result = new String(buffer, ENCODING);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
