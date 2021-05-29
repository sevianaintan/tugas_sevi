package com.example.databaselokal.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.databaselokal.R;
import com.example.databaselokal.entity.AppDatabase;
import com.example.databaselokal.entity.DataKampus;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContact.view{
    private AppDatabase appDatabase;
    private MainPresenter mainPresenter;
    private MainAdapter mainAdapter;

    private Button btnOK;
    private RecyclerView recyclerView;
    private EditText etNama, etAlamat,etJumlah;

    private  int id = 0;
    boolean edit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDatabase = AppDatabase.inidb(getApplicationContext());

        btnOK = findViewById(R.id.btn_submit);
        etNama = findViewById(R.id.et_nama);
        etAlamat = findViewById(R.id.et_alamat);
        etJumlah = findViewById(R.id.et_mhs);
        recyclerView = findViewById(R.id.rc_main);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mainPresenter = new MainPresenter(this);
        mainPresenter.readData(appDatabase);
    }

    @Override
    public void successAdd(){
        Toast.makeText(this,"Berhasil Menambahkan!", Toast.LENGTH_SHORT).show();
        mainPresenter.readData(appDatabase);
    }

    @Override
    public void successDelete(){
        Toast.makeText(this,"Berhasil Menghapus Data!", Toast.LENGTH_SHORT).show();
        mainPresenter.readData(appDatabase);
    }

    @Override
    public void resetForm(){
        etNama.setText("");
        etJumlah.setText("");
        etAlamat.setText("");
        btnOK.setText("Submit");

    }

    @Override
    public void getData(List<DataKampus> list){
        mainAdapter = new MainAdapter(this,list,this);
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    public void editData(DataKampus item){
        etNama.setText(item.getName());
        etAlamat.setText(item.getAddress());
        etJumlah.setText(item.getMhs());
        id = item.getId();
        edit = true;
        btnOK.setText("UPDATE DATA");
    }

    @Override
    public void deleteData(DataKampus item){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }else {
            builder =  new AlertDialog.Builder(this);
        }
        builder.setTitle("Menghapus Data").setMessage("Anda yakin ingin menghapus data ini?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                resetForm();
                mainPresenter.deleteData(item,appDatabase);
            }
        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    @Override
    public void onClick(View view){
        if (view==btnOK){
            if (etNama.getText().toString().equals("")||etAlamat.getText().toString().equals("")||etJumlah.getText().toString().equals("")){
                Toast.makeText(this,"Harap Isi Semua Data!", Toast.LENGTH_SHORT).show();
            }else {
                if (!edit){
                    mainPresenter.insertData(etNama.getText().toString(),etAlamat.getText().toString(),Integer.parseInt(etJumlah.getText().toString()),appDatabase);
                }else {
                    mainPresenter.editData(etNama.getText().toString(), etAlamat.getText().toString(),Integer.parseInt(etJumlah.getText().toString()),id,appDatabase);
                    edit = false;
                }
                resetForm();
            }
        }
    }
}