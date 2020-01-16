package com.singularitycoder.yesbank.Notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.singularitycoder.yesbank.HelperGeneral;
import com.singularitycoder.yesbank.R;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "NotificationsAdapter";

    ArrayList<NotificationItem> notificationsList;
    Context context;

    public NotificationsAdapter(ArrayList<NotificationItem> notificationsList, Context context) {
        this.notificationsList = notificationsList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NotificationItem notificationItem = notificationsList.get(position);
        NotificationViewHolder notificationViewHolder = (NotificationViewHolder) holder;
        HelperGeneral.glideImage(context, notificationItem.getStrImage(), notificationViewHolder.imgPersonImage, "");
        notificationViewHolder.tvPersonName.setText(notificationItem.getStrName());
        notificationViewHolder.tvNotifDate.setText(notificationItem.getStrDateTime());
        notificationViewHolder.tvNotifMessage.setText(notificationItem.getStrNotifMessage());
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        private CircularImageView imgPersonImage;
        private TextView tvPersonName;
        private TextView tvNotifMessage;
        private TextView tvNotifDate;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPersonImage = itemView.findViewById(R.id.img_notif_person);
            tvPersonName = itemView.findViewById(R.id.tv_notif_name);
            tvNotifMessage = itemView.findViewById(R.id.tv_notif_message);
            tvNotifDate = itemView.findViewById(R.id.tv_notif_date);
        }
    }
}
