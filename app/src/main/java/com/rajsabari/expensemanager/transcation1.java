package com.rajsabari.expensemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Objects;
import java.util.UUID;

import io.realm.Realm;

public class transcation1 extends AppCompatActivity {
    Realm realm;
    String uuvid;
    String toast = "Added";
    String type = null;
    CardView save;
    EditText title,amountt,tages,description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcation1);
        ImageView finish = findViewById(R.id.finish);
        title = findViewById(R.id.title);
        amountt = findViewById(R.id.amount);
        tages = findViewById(R.id.tags);
        description = findViewById(R.id.description);
        save = findViewById(R.id.save);
        RadioButton income = findViewById(R.id.income_radio);
        RadioButton outcome = findViewById(R.id.outcome_radio);


        income.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    type = "INCOME";
                }
                System.out.println("ldlkfnv");
            }
        });


        outcome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    type = "OUTCOME";
                }
            }
        });


        realm = Realm.getDefaultInstance();
        uuvid = UUID.randomUUID().toString();
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String key = intent.getExtras().getString("id");
            Expense data = getValueFromDatabase(key);
            title.setText(data.title);
            description.setText(data.descritption);
            amountt.setText(data.amount);
            tages.setText(data.tages);
            if (Objects.equals(data.type, "INCOME")) {
                income.setChecked(true);
            } else {
                outcome.setChecked(true);
            }
            uuvid = data.uuid;
            toast = "Updated";
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gettitle = title.getText().toString();
                String getdes = description.getText().toString();
                String gettages = tages.getText().toString();
                String getamount = amountt.getText().toString();
                long createTime = System.currentTimeMillis();

                if (!gettitle.equals("") && !getdes.equals("") && !gettages.equals("") && !getamount.equals("") && type != null) {
                    Expense expensemanager = new Expense();
                    expensemanager.setTitle(gettitle);
                    expensemanager.setDescritption(getdes);
                    expensemanager.setTages(gettages);
                    expensemanager.setUuid(uuvid);
                    expensemanager.setAmount(getamount);
                    expensemanager.setUuid(uuvid);
                    expensemanager.setCreatetime(createTime);
                    expensemanager.setType(type);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(expensemanager);
                        }
                    });
                    finish();
                } else {
                    Toast.makeText(transcation1.this, "Enter the Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private Expense getValueFromDatabase(String id) {
        return realm.where(Expense.class).equalTo("uuid", id).findFirst();
    }
}