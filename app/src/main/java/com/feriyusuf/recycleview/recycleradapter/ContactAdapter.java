package com.feriyusuf.recycleview.recycleradapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feriyusuf.recycleview.R;
import com.feriyusuf.recycleview.model.Contact;
import com.feriyusuf.recycleview.util.OnLoadMoreListener;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 3/11/18.
 */

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private Activity activity;
    private List<Contact> contacts;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;



    public ContactAdapter(RecyclerView recyclerView, List<Contact> contacts, Activity activity){
        Log.d(TAG, "ContactAdapter Method: FYZ");

        this.contacts = contacts;
        this.activity = activity;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.d(TAG, "@Override onScrolled: FYZ");

                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if(!isLoading && totalItemCount <= (lastVisibleItem+visibleThreshold)){
                    isLoading = true;
                    if (onLoadMoreListener != null){
                        onLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener){
        Log.d(TAG, "setOnLoadMoreListener: FYZ");

        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position){
        Log.d(TAG, "getItemViewType: FYZ");

        return contacts.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: FYZ");

        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(activity).inflate(R.layout.recycler_row_item_contact, parent, false);

            Log.d(TAG, "onCreateViewHolder: FYZ - viewType == VIEW_TYPE_ITEM");

            return new UserViewHolder(view); //Inner class
        } else if(viewType == VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(activity).inflate(R.layout.recycler_loading_progress, parent,false);

            Log.d(TAG, "onCreateViewHolder: FYZ - viewType == VIEW_TYPE_LOADING");

            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FYZ");

        if(holder instanceof UserViewHolder){
            Log.d(TAG, "onBindViewHolder: FYZ - holder instanceof UserViewHolder");

            Contact contact = contacts.get(position);
            UserViewHolder userViewHolder = (UserViewHolder) holder;
            userViewHolder.phone.setText(contact.getEmail());
            userViewHolder.email.setText(contact.getPhone());
        } else if(holder instanceof LoadingViewHolder){
            Log.d(TAG, "onBindViewHolder: FYZ - holder instanceof LoadingViewHolder");

            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return contacts == null ? 0 : contacts.size();
    }



    private class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView phone;
        public TextView email;

        public UserViewHolder(View view) {
            super(view);
            phone = (TextView) view.findViewById(R.id.txt_phone);
            email = (TextView) view.findViewById(R.id.txt_email);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.loadingMoreRowItem);
        }
    }

    public void setLoaded() {
        isLoading = false;
    }
}
