package com.example.phonebook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Contact> contacts;
    private MyOnClickListener onClickListener;

    public MyAdapter(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Contact contact = contacts.get(position);

        holder.id.setImageResource(contact.getId());
        holder.name.setText(contact.getName());
        holder.phone.setText(contact.getPhonesString());
        if (onClickListener != null) {
            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickCall(contact);
                }
            });

            holder.sendSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickSms(contact);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView id;
        public TextView name, phone;
        public Button  call, sendSms;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.nameTextView);
            phone = itemView.findViewById(R.id.phoneTextView);
            call = itemView.findViewById(R.id.callButton);
            sendSms = itemView.findViewById(R.id.smsButton);
        }

    }

    // convenience method for getting data at click position
    Contact getItem(int id) {
        return contacts.get(id);
    }

    public void setOnClickListener(MyOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public interface MyOnClickListener {
        void onClickCall(Contact contact);
        void onClickSms(Contact contact);
    }

}
