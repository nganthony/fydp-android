package com.uwaterloo.fydp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.novoda.imageloader.core.model.ImageTag;
import com.uwaterloo.fydp.R;
import com.uwaterloo.fydp.domain.Bin;
import com.uwaterloo.fydp.util.ImageUtil;

import java.util.List;

/**
 * Created by Anthony on 15-02-10.
 */
public class BinListAdapter extends ArrayAdapter<Bin> {

    Activity activity;
    int resource;
    List<Bin> bins;

    public BinListAdapter(Activity activity, int resource, List<Bin> bins) {
        super(activity, resource, bins);

        this.activity = activity;
        this.resource = resource;
        this.bins = bins;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(resource, null);

            BinViewHolder viewHolder = new BinViewHolder();
            viewHolder.nameTextView = (TextView)rowView.findViewById(R.id.bin_list_item_name);
            viewHolder.descriptionTextView = (TextView)rowView.findViewById(R.id.bin_list_item_description);
            viewHolder.binItemImageView = (ImageView)rowView.findViewById(R.id.bin_list_item_image);

            rowView.setTag(viewHolder);
        }

        final BinViewHolder holder = (BinViewHolder)rowView.getTag();
        final Bin bin = bins.get(position);

        holder.nameTextView.setText(bin.getName());
        holder.descriptionTextView.setText(bin.getDescription());

        if(bin.getImageUrl() != null && !bin.getImageUrl().isEmpty()) {
            ImageTag tag = ImageUtil.getImageTagFactory(activity).build(bin.getImageUrl(), activity);
            holder.binItemImageView.setTag(tag);
            ImageUtil.getImageManager(activity).getLoader().load(holder.binItemImageView);
        }

        return rowView;
    }

    private static class BinViewHolder {
        public TextView nameTextView;
        public TextView descriptionTextView;
        public ImageView binItemImageView;
    }
}
