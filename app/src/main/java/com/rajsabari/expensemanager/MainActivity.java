package com.rajsabari.expensemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    TextView total, income, expense,noti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView add = findViewById(R.id.transaction);
        total = findViewById(R.id.total_data);
        income = findViewById(R.id.income);
        expense = findViewById(R.id.expense);
        noti=findViewById(R.id.noti);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, transcation1.class));
            }
        });
        RealmResults<Expense> expenseManagers = Realm.getDefaultInstance().where(Expense.class).findAll();
        RecyclerView recyclerView = findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter myAdapter = new MyAdapter(getApplicationContext(), expenseManagers);
        recyclerView.setAdapter(myAdapter);
        updateData(Realm.getDefaultInstance().copyFromRealm(expenseManagers));
        expenseManagers.addChangeListener((RealmResults<Expense> it) -> {
            myAdapter.notifyDataSetChanged();
            updateData(Realm.getDefaultInstance().copyFromRealm(expenseManagers));
        });

    }


    private void updateData(List<Expense> data) {
        if(data.size()  == 0){
            noti.setVisibility(View.VISIBLE);
        }
        else {
            noti.setVisibility(View.GONE);
        }
        long income_data = 0;
        long expense_data = 0;
        for(int i = 0; i < data.size(); i++) {
            if((data.get(i).type).equals("INCOME")){
                income_data = income_data + Long.parseLong((data.get(i).amount));
            }else {
                expense_data = expense_data + Long.parseLong((data.get(i).amount));
            }
        }

         income.setText("₹"+String.valueOf(income_data));
        expense.setText("₹"+String.valueOf(expense_data));

        total.setText("₹"+String.valueOf((income_data - expense_data)));
    }
}