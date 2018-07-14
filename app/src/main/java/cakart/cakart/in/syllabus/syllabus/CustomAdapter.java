package cakart.cakart.in.syllabus.syllabus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cakart.cakart.in.syllabus.R;
import cakart.cakart.in.syllabus.model.Node;


public class CustomAdapter extends ArrayAdapter<Node> {

    private ArrayList<Node> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView name;
        ImageView icon;
    }

    public CustomAdapter(ArrayList<Node> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Node Node = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.name.setText(Node.getName());
        if (Node.getUrl() == null) {
            viewHolder.icon.setImageResource(R.drawable.folder_icon);
        } else {
            if(Node.getUrl().endsWith(".pdf")) {
                viewHolder.icon.setImageResource(R.drawable.pdf);
            }else if(Node.getUrl().endsWith(".zip") || Node.getUrl().endsWith(".rar") || Node.getUrl().endsWith(".tar")){
                viewHolder.icon.setImageResource(R.drawable.zip);
            }
        }
        // Return the completed view to render on screen
        return convertView;
    }



}