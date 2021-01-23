package com.example.temquerachar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, TextToSpeech.OnInitListener {
    EditText valor, pessoas;
    TextView resultado;
    FloatingActionButton btnComp, btnFala;
    TextToSpeech ttsPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valor = (EditText) findViewById(R.id.valor);
        pessoas = (EditText) findViewById(R.id.pessoas);
        resultado = (TextView) findViewById(R.id.resultado);
        btnComp = (FloatingActionButton) findViewById(R.id.btnComp);
        btnFala = (FloatingActionButton) findViewById(R.id.btnFala);

        valor.addTextChangedListener(this);
        pessoas.addTextChangedListener(this);

        btnComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, "O valor da conta dividida foi " + resultado.getText().toString());
                startActivity(i);
            }
        });

        btnFala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ttsPlayer != null){
                    ttsPlayer.speak("O valor por pessoa é de " + resultado.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "ID1");
                }
            }
        });

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 1122);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1122){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                ttsPlayer = new TextToSpeech(this,this);
            } else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        float valorNumber, pessoasNumber, res;
        try{
            valorNumber = Float.parseFloat(valor.getText().toString());
            pessoasNumber = Float.parseFloat(pessoas.getText().toString());
            if(valorNumber == 0 || pessoasNumber == 0) throw new Exception();
            res = valorNumber/pessoasNumber;
            DecimalFormat df = new DecimalFormat("#.00");
            resultado.setText("R$ " + df.format(res));
        } catch (Exception e){
            resultado.setText("R$ 0.00");
        }
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            Toast.makeText(this, "TSS ativado", Toast.LENGTH_SHORT).show();
        } else if (status == TextToSpeech.ERROR){
            Toast.makeText(this, "TSS não ativado", Toast.LENGTH_SHORT).show();
        }
    }
}