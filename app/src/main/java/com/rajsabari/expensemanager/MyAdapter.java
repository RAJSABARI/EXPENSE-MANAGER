package com.rajsabari.expensemanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    TextView titleoutput;
    TextView amountoutput;
    TextView tagoutput;
    TextView timeoutput;



    Context context;


    public MyAdapter(Context context, RealmResults<Expense> expensemanagers) {
        this.context = context;
        this.expensemanagers = expensemanagers;
    }

    RealmResults<Expense> expensemanagers;


    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_vieww,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        Expense em=expensemanagers.get(position);
        holder.titleoutput.setText(em.getTitle());
        holder.tagoutput.setText(em.getTages());
        holder.amountoutput.setText("₹"+em.getAmount());
       String formateTime= DateFormat.getTimeInstance().format(em.createtime);
       holder.timeoutput.setText(formateTime);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu menu = new PopupMenu(context, view);
                menu.getMenu().add("DELETE");
                menu.setOnMenuItemClickListener(item -> {
                    if (item.getTitle().equals("DELETE")) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        em.deleteFromRealm();
                        realm.commitTransaction();
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                });
                menu.show();
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, transcation1.class);
                intent.putExtra("id", em.uuid);//uuid
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return expensemanagers.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleoutput;
        TextView amountoutput;
        TextView tagoutput;
        TextView timeoutput;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleoutput = itemView.findViewById(R.id.titleoutput);
            amountoutput = itemView.findViewById(R.id.amountoutput);
            tagoutput = itemView.findViewById(R.id.tagoutput);
            timeoutput = itemView.findViewById(R.id.timeoutput);
        }
    }
}
