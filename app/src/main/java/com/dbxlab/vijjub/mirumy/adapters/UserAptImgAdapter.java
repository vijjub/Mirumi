package com.dbxlab.vijjub.mirumy.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dbxlab.vijjub.mirumy.R;

import java.util.List;

/**
 * Created by vijjub on 8/23/16.
 */
public class UserAptImgAdapter extends PagerAdapter {

    Context context;
    List<String> aptImages;



    public UserAptImgAdapter(Context context,List<String> aptImages){
        this.context = context;
        this.aptImages = aptImages;
    }


    @Override
    public int getCount() {
        return aptImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
//        final WindowManager windowManager = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);
//        final Display display = windowManager.getDefaultDisplay();
//        final DisplayMetrics displayMetrics = new DisplayMetrics();
//        int width = displayMetrics.widthPixels;

        View view = inflater.inflate(R.layout.user_apt_images_list,container,false);
        ImageView imageView = (ImageView)view.findViewById(R.id.aptimage);
        TextView countText = (TextView)view.findViewById(R.id.lblcount);
        int total = getCount();
        String positionPager = "( "+Integer.toString(position+1)+" of "+Integer.toString(total)+" )";
        countText.setText(positionPager);



        Glide.with(context).load(aptImages.get(position))
                .error(R.mipmap.edge)
                .override(300,220)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT).skipMemoryCache(true)
                .into(imageView);

        container.addView(view,0);

        return view;

    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
