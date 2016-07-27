package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.ui.view.AspectRatioImageView;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.os.Handler;

/**
 * Created by alena on 15.07.16.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    /* fields for swipe to remove feature*/
    private static final int PENDING_REMOVAL_TIMEOUT = 3000;
    List<User> itemsPendingRemoval;

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<User, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be
/*--------------------------------------------------------------------------------*/
    private static final String TAG = ConstantManager.TAG_PREFIX + "UserAdapter";
    private Context mContext;
    private List<User> mUsers;
    private CustomClickListener mCustomClickListener;
    private View infoPanel;

    public UserAdapter(List<User> users, CustomClickListener customClickListener) {
        mUsers = users;
        this.mCustomClickListener = customClickListener;
        itemsPendingRemoval = new ArrayList<>();
    }



    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View convertView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        return new UserViewHolder(convertView, mCustomClickListener);

    }

    @Override
    public void onBindViewHolder(final UserAdapter.UserViewHolder holder, int position) {
/*------------------------------------------------------ */
        UserViewHolder viewHolder = holder;
/*------------------------------------------------------ */
        final User user = mUsers.get(position);
        final String userPhoto;

        if (user.getPhoto().isEmpty()){
            userPhoto = "null";
            Log.e(TAG, "onBindViewHpolder: user with name " + user.getFullName() + "has no photo");
        }else {
            userPhoto = user.getPhoto();
        }


        DataManager.getInstance().getPicasso()
                .load(userPhoto)
                .error(holder.mDummy)
                .placeholder(holder.mDummy)
                .fit()
                .centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.userPhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "load from cache");
                    }

                    @Override
                    public void onError() {
                        DataManager.getInstance().getPicasso()
                                .load(userPhoto)
                                .error(holder.mDummy)
                                .placeholder(holder.mDummy)
                                .fit()
                                .centerCrop()
                                .into(holder.userPhoto, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.d(TAG, "Could not fetch image");
                                    }
                                });
                    }
                });

        holder.mFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getRating()));
        holder.mCodeLines.setText(String.valueOf(user.getCodeLines()));
        holder.mProjects.setText(String.valueOf(user.getProjects()));

        if (user.getBio() == null || user.getBio().isEmpty()){
            holder.mBio.setVisibility(View.GONE);
        }else{
            holder.mBio.setVisibility(View.VISIBLE);
            holder.mBio.setText(user.getBio());
        }


    /* swipe to delete*/
        if (itemsPendingRemoval.contains(user)) {
            // we need to show the "undo" state of the row

            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.mFullName.setVisibility(View.GONE);
            viewHolder.userPhoto.setVisibility(View.GONE);
            viewHolder.mBio.setVisibility(View.GONE);
            viewHolder.infoPanel.setVisibility(View.GONE);
            viewHolder.lineView.setVisibility(View.GONE);
            viewHolder.mShowMore.setVisibility(View.GONE);
            viewHolder.undoButton.setVisibility(View.VISIBLE);
            viewHolder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(user);
                    pendingRunnables.remove(user);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(user);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(mUsers.indexOf(user));
                }
            });
        } else {
            // we need to show the "normal" state
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.mFullName.setVisibility(View.VISIBLE);
            viewHolder.userPhoto.setVisibility(View.VISIBLE);
            viewHolder.mBio.setVisibility(View.VISIBLE);
            viewHolder.infoPanel.setVisibility(View.VISIBLE);
            viewHolder.lineView.setVisibility(View.VISIBLE);
            viewHolder.mShowMore.setVisibility(View.VISIBLE);
            viewHolder.undoButton.setVisibility(View.GONE);
            viewHolder.undoButton.setOnClickListener(null);
        }

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
/* -------------------------------------------*/
        Button undoButton;
/* -------------------------------------------*/
        protected AspectRatioImageView userPhoto;
        protected TextView mFullName, mRating, mCodeLines, mProjects, mBio;
        protected Button mShowMore;
        protected View infoPanel;
        protected Drawable mDummy;
        protected View lineView;

        private CustomClickListener mCustomClickListener;

        public UserViewHolder(View itemView, CustomClickListener customClickListener) {
            super(itemView);
            this.mCustomClickListener = customClickListener;

            lineView = (View)itemView.findViewById(R.id.line);
            infoPanel = (View) itemView.findViewById(R.id.info_panel);
            userPhoto = (AspectRatioImageView) itemView.findViewById(R.id.user_photo_img);
            mFullName = (TextView) itemView.findViewById(R.id.user_full_name_txt);
            mRating = (TextView) itemView.findViewById(R.id.rating_txt);
            mCodeLines = (TextView) itemView.findViewById(R.id.code_lines_txt);
            mProjects = (TextView) itemView.findViewById(R.id.projects_txt);
            mBio = (TextView) itemView.findViewById(R.id.bio_txt);
            mShowMore = (Button)itemView.findViewById(R.id.more_info_btn);
            undoButton = (Button) itemView.findViewById(R.id.undo_button);

            mDummy = userPhoto.getContext().getResources().getDrawable(R.drawable.login_bg);
            mShowMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mCustomClickListener!=null){
                mCustomClickListener.onUserItemClickListener(getAdapterPosition());
            }
        }
    }

    public interface CustomClickListener{
        void onUserItemClickListener(int position);
    }

    public void pendingRemoval(int position) {
        final User user = mUsers.get(position);
        if (!itemsPendingRemoval.contains(user)) {
            itemsPendingRemoval.add(user);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(mUsers.indexOf(user));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(user, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        final User user = mUsers.get(position);
        if (itemsPendingRemoval.contains(user)) {
            itemsPendingRemoval.remove(user);
        }
        if (mUsers.contains(user)) {
            mUsers.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        User user = mUsers.get(position);
        return itemsPendingRemoval.contains(user);
    }
}
