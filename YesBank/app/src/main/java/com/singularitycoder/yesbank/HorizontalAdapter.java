package com.singularitycoder.yesbank;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Hithesh on 16,January,2020.
 */
public class HorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<HorizontalItem> list;
    Context context;

    public HorizontalAdapter(ArrayList<HorizontalItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_horizontal, parent, false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HorizontalItem horizontalItem = list.get(position);
        if (null != holder) {
            HorizontalViewHolder horizontalViewHolder = (HorizontalViewHolder) holder;
            horizontalViewHolder.image.setImageResource(horizontalItem.getIntImage());
            horizontalViewHolder.image.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(horizontalItem.getImageColor())));
            horizontalViewHolder.text.setText(horizontalItem.getStrText());
            horizontalViewHolder.text.setTextColor(context.getResources().getColor(horizontalItem.getTextColor()));
            horizontalViewHolder.linLayCard.setBackgroundColor(context.getResources().getColor(horizontalItem.getCardColor()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HorizontalViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ImageView image;
        LinearLayout linLayCard;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.tv_text);
            image = itemView.findViewById(R.id.iv_image);
            linLayCard = itemView.findViewById(R.id.lin_lay_horiz);
        }
    }
}
