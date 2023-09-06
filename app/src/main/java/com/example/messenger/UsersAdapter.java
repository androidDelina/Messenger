package com.example.messenger;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private List<User> usersList = new ArrayList<>();
    private onUserClickListener onUserClickListener = null;

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
        Log.e("UsersAdapter", "setUsersList");
        notifyDataSetChanged();
    }

    public void setOnUserClickListener(UsersAdapter.onUserClickListener onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.user_item,
                parent,
                false
        );
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = usersList.get(position);
        Log.e("UsersAdapter", usersList.toString() + " USERSLIST");
        String userInfo = user.getName() +
                " " +
                user.getLastName() +
                ", " +
                user.getAge();

        holder.textViewUserInfo.setText(userInfo);
        int colorBackCircle;
//        Log.e("UsersAdapter", user.toString());
//        Log.e("UsersAdapter", user.getIsOnline().toString());
//        Log.e("UsersAdapter", usersList.toString() + " USERSLIST");
//        if (user.getIsOnline() != null) {
//            if (user.getIsOnline())
//                colorBackCircle = R.drawable.circle_green;
//            else
//                colorBackCircle = R.drawable.circle_red;
//        }

        if (user.getIsOnline())
            colorBackCircle = R.drawable.circle_green;
        else
            colorBackCircle = R.drawable.circle_red;

//        colorBackCircle = R.drawable.circle_red;
        Drawable background = ContextCompat.getDrawable(holder.itemView.getContext(), colorBackCircle);
        holder.view_circle.setBackground(background);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUserClickListener != null) {
                    onUserClickListener.onUserClick(user);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class UsersViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUserInfo;
        private View view_circle;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserInfo = itemView.findViewById(R.id.textViewUserInfo);
            view_circle = itemView.findViewById(R.id.view_circle);
        }
    }

    interface onUserClickListener {
        void onUserClick(User user);
    }
}
