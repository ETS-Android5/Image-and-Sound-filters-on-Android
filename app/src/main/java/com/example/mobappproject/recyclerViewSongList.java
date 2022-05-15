package com.example.mobappproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerViewSongList extends RecyclerView.Adapter<recyclerViewSongList.songsViewHolder> {

    private ArrayList<songItem> mSongList;

    private onItemClickListener mListener;

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    public static class songsViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextView;

        public songsViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.songIcon);
            mTextView = itemView.findViewById(R.id.songName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }

                }
            });
        }
    }

    public recyclerViewSongList(ArrayList<songItem> songList){
        mSongList = songList;
    }

    @NonNull
    @Override
    public songsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.songitem, parent, false);
        songsViewHolder songVHolder = new songsViewHolder(v, mListener);
        return songVHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull songsViewHolder holder, int position) {
        songItem currentSong = mSongList.get(position);

        holder.mImageView.setImageResource(currentSong.getImgRes());
        holder.mTextView.setText(currentSong.getmName());
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }



}
