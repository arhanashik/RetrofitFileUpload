package com.blackspider.retrofitfileupload.ui.main.adapter;
/*
 *  ****************************************************************************
 *  * Created by : Arhan Ashik on 11/27/2018 at 6:14 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * Last edited by : Arhan Ashik on 11/27/2018.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.blackspider.retrofitfileupload.R;
import com.blackspider.retrofitfileupload.data.remote.response.RemoteFileEntity;
import com.blackspider.retrofitfileupload.databinding.ListItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ArtistViewHolder> {
    private List<RemoteFileEntity> mFiles;

    public FilesAdapter(){
        mFiles = new ArrayList<>();
    }

    public void setData(List<RemoteFileEntity> files){
        mFiles.clear();
        mFiles.addAll(files);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        ListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item, viewGroup,
                false);

        return new ArtistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder artistViewHolder, int i) {
        artistViewHolder.bind(mFiles.get(i));
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder{
        private ListItemBinding mBinding;

        ArtistViewHolder(@NonNull ListItemBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
        }

        void bind(RemoteFileEntity fileEntity){
            Glide.with(mBinding.getRoot().getContext())
                    .load(fileEntity.getUrl())
                    .apply(new RequestOptions().error(R.drawable.ic_avater))
                    .into(mBinding.img);
        }
    }
}
