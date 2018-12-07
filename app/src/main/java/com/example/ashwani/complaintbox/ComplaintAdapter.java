package com.example.ashwani.complaintbox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Ashwani on 26/03/2018.
 */

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder> {
    ArrayList<Complaint> ComplaintList;
    Context context;


    public ComplaintAdapter(Context context, ArrayList<Complaint> complaintList) {
        this.context = context;
        ComplaintList = complaintList;
    }

    @Override
    public ComplaintViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.complaint_item, parent, false);

        return new ComplaintViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ComplaintViewHolder holder, int position) {
        //setting data to each complaint view
        Complaint complaint = ComplaintList.get(position);

        holder.tvComplaintNo.setText(complaint.getComplaintNo());
        holder.tvSchoolName.setText(complaint.getSchoolName());
        holder.tvProblem.setText(complaint.getProblems());
        holder.tvDate.setText(complaint.getDate());
        holder.extraText.setText(complaint.getDescription());

        if (complaint.getImageLink().length() > 2) {

            Glide.with(context).load(complaint.getImageLink()).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return ComplaintList.size();
    }

    public class ComplaintViewHolder extends RecyclerView.ViewHolder {
        TextView tvComplaintNo, tvSchoolName, tvProblem, tvDate, extraText;
        ImageView imageView;

        public ComplaintViewHolder(View itemView) {
            super(itemView);
            tvComplaintNo = itemView.findViewById(R.id.compalint_number_tv);
            tvSchoolName = itemView.findViewById(R.id.school_name_tv);
            tvProblem = itemView.findViewById(R.id.problem_tv);
            tvDate = itemView.findViewById(R.id.date_tv);
            extraText = itemView.findViewById(R.id.extra_text);
            imageView = itemView.findViewById(R.id.school_icon_imgview);
        }

    }

}
