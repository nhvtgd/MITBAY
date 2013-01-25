package com.example.myapp.helper;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.CustomizedListView;
import com.example.myapp.MITBAYActivity;
import com.example.myapp.R;
import com.example.myapp.Sellable;
/**
 * This class is a customized list view adapter to 
 * bind a certain listView with
 * @author trannguyen
 *
 */
public class ListViewAdapter extends BaseAdapter {
   /**The activity where the list selection page appears*/
    private Activity activity;
    /**The collections of sellable objects*/
    private ArrayList<Sellable> data;
    /** To inflate the view from an xml file*/
    private static LayoutInflater inflater=null;

    
    /**
     * This is a constructor that take in the activity where the list View
     * is displayed and the collection of sellable objects
     * @param a
     * @param d
     */
    public ListViewAdapter(Activity a, ArrayList<Sellable> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return ((Sellable) this.getItem(position)).getId();
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.activity_listing, null);

        TextView name = (TextView)vi.findViewById(R.id.item_name_in_post); 
//        TextView category = (TextView)vi.findViewById(R.id.item_category_in_post); 
        TextView location = (TextView)vi.findViewById(R.id.item_location_in_post); 
        TextView description = (TextView) vi.findViewById(R.id.item_description_in_post);
        TextView priceValue = (TextView) vi.findViewById(R.id.item_price_value);
        ImageView image = (ImageView) vi.findViewById(R.id.item_image_in_post);
        Log.d("view", "create view successfully");
        Sellable item = data.get(position);
        
        
        // Setting all values in listview
        name.setText(item.getName());
//        category.setText(item.getType().toString());
        location.setText("Need to set Later");
        description.setText(item.getDescription());
        priceValue.setText(item.getPrice());
        if (item.getImages() == null){
	        if (item.getType().toString().equals(MITBAYActivity.TEXTBOOK)){
	        	image.setImageResource(R.drawable.text_book);
	        }
	        else if (item.getType().toString().equals(MITBAYActivity.FURNITURE)){
	        	image.setImageResource(R.drawable.furniture_icon);
	        }
	        else if (item.getType().toString().equals(MITBAYActivity.TRANSPORTATION)){
	        	image.setImageResource(R.drawable.bike);
	        }
	        else if (item.getType().toString().equals(MITBAYActivity.MISC)){
	        	image.setImageResource(R.drawable.misc);
	        }
        }
        else
        	image.setImageBitmap(item.getImages());
        return vi;
    }
}