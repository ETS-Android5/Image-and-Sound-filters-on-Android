package com.example.mobappproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //variables
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;
    private OnPickListener mOnPickListener;

    public RecyclerViewAdapter(ArrayList<String> mNames, ArrayList<String> mImageUrls, Context mContext, OnPickListener onPickListener) {
        this.mNames = mNames;
        this.mImageUrls = mImageUrls;
        this.mContext = mContext;
        this.mOnPickListener = onPickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filtericonlist, parent, false);

        return new ViewHolder(view, mOnPickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        Glide.with(mContext)
                .asBitmap()
                .load(Integer.parseInt(mImageUrls.get(position)))
                .into(holder.image);

        holder.name.setText(mNames.get(position));
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView image;
        TextView name;
        OnPickListener onPickListener;

        public ViewHolder(@NonNull View itemView, OnPickListener onPickListener) {
            super(itemView);
            name=itemView.findViewById(R.id.filterName);
            image=itemView.findViewById(R.id.filterImage);
            this.onPickListener = onPickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPickListener.onPickClick(getAdapterPosition());
        }
    }

    public interface OnPickListener{
        void onPickClick(int position);
    }
}
