package com.felco.dz3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.felco.dz3.databinding.ActivityMainBinding;
import com.felco.dz3.databinding.ActivitySecondBinding;

public class MainActivity extends AppCompatActivity {
    String TAG = "App";
    private ActivityMainBinding binding;
    SharedPreferences sPref;
    final String S_PREF_NAME = "mypref";
    final String KEY_NAME = "name";

    private void saveText(){
        sPref = getSharedPreferences(S_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(KEY_NAME, binding.tvName.getText().toString());
        editor.apply();
        Log.d(TAG, "saveText: Text saved");
    }

    private void loadText(){
        sPref = getSharedPreferences(S_PREF_NAME,Context.MODE_PRIVATE);
        String savedText = sPref.getString(KEY_NAME,"prefDon'tExist");
        binding.tvName.setText(savedText);
        Toast.makeText(this, "Text loaded",Toast.LENGTH_SHORT).show();
    }

    ActivityResultLauncher<Intent> startSecondActivityForResult = registerForActivityResult
            (       new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if(result.getResultCode() == Activity.RESULT_OK){
                                Intent intent = result.getData();
                                if(intent != null){
                                    String name = intent.getStringExtra("Name");
                                    binding.tvName.setText(name);
                                    saveText();
                                }
                            }else{
                                String textError = "Error";
                                binding.tvName.setText(textError);
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        loadText();
        //CharSequence text = "Приложение запущено";
        //int duration = Toast.LENGTH_SHORT;
        //Toast toast = Toast.makeText(this /* MyActivity */, text, duration);
        //toast.show();
        //Log.d(TAG, "onCreate: Toast launched");

        Intent intent = new Intent(this, SecondActivity.class);
        binding.gotoBtnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSecondActivityForResult.launch(intent);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: destroyed");
    }
}