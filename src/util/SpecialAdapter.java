package util;

import java.util.HashMap;
import java.util.List;

import domain.Lab;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
 
public class SpecialAdapter extends ArrayAdapter<Lab> {
    private int[] colors = new int[] { 0x30FF0000, 0x300000FF };
     
    public SpecialAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to) {
        super(context, resource);
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = super.getView(position, convertView, parent);
      int colorPos = position % colors.length;
      view.setBackgroundColor(colors[colorPos]);
      return view;
    }
}
