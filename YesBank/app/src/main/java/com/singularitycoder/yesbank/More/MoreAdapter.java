package com.singularitycoder.yesbank.More;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.singularitycoder.yesbank.HelperGeneral;
import com.singularitycoder.yesbank.HomeActivity;
import com.singularitycoder.yesbank.R;

import java.util.ArrayList;

public class MoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "MoreAdapter";

    private static final int MORE_HEADER = 0;
    private static final int MORE_ITEM = 1;

    private ArrayList<MoreItem> moreList;
    private Context context;
    private ItemClickListener itemClickListener;


    public MoreAdapter(ArrayList<MoreItem> moreList, Context context) {
        this.moreList = moreList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case MORE_HEADER:
                v = layoutInflater.inflate(R.layout.item_more_header, parent, false);
                return new MoreHeaderViewHolder(v);
            default:
                v = layoutInflater.inflate(R.layout.item_more, parent, false);
                return new MoreViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MoreItem moreItem = moreList.get(position);
        if (holder instanceof MoreViewHolder) {
            MoreViewHolder moreViewHolder = (MoreViewHolder) holder;
            moreViewHolder.tvMoreItemName.setText(moreItem.getMoreItemName());
            moreViewHolder.tvMoreItemName.setTextColor(context.getResources().getColor(moreItem.getIntTextColor()));
            moreViewHolder.imgMoreItemImage.setImageResource(moreItem.getMoreItemImage());
            moreViewHolder.imgMoreItemImage.setColorFilter(context.getResources().getColor(moreItem.getIntIconColor()));
        } else if (holder instanceof MoreHeaderViewHolder) {
            MoreHeaderViewHolder moreHeaderViewHolder = (MoreHeaderViewHolder) holder;
            HelperGeneral.glideImage(context, moreItem.getStrMoreProfilePic(), moreHeaderViewHolder.imgProfileImage);
            moreHeaderViewHolder.tvName.setText(moreItem.getStrMoreName());
            moreHeaderViewHolder.tvPhone.setText(moreItem.getStrMorePhone());
            moreHeaderViewHolder.tvLicenseNumber.setText(moreItem.getStrMoreLicenseNumber());
            moreHeaderViewHolder.tvMoreViewProfile.setOnClickListener(view -> ((AppCompatActivity) context)
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeActivity.ProfileFragment())
                    .addToBackStack("MoreFragment")
                    .commit());
        }
    }

    @Override
    public int getItemCount() {
        return moreList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? MORE_HEADER : MORE_ITEM;
    }

    class MoreHeaderViewHolder extends RecyclerView.ViewHolder{
        private CircularImageView imgProfileImage;
        private TextView tvName, tvPhone, tvLicenseNumber, tvMoreViewProfile;

        public MoreHeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProfileImage = itemView.findViewById(R.id.img_profile_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvLicenseNumber = itemView.findViewById(R.id.tv_license);
            tvMoreViewProfile = itemView.findViewById(R.id.tv_show_profile);
        }
    }

    class MoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private TextView tvMoreItemName;
        private ImageView imgMoreItemImage;

        public MoreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMoreItemName = itemView.findViewById(R.id.tv_more);
            imgMoreItemImage = itemView.findViewById(R.id.img_more);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(view, getAdapterPosition(), itemView.findViewById(R.id.img_more));
        }
    }

   public interface ItemClickListener {
        void onItemClick(View view, int position, ImageView imageView);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
