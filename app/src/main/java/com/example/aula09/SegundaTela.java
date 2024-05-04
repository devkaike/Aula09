package com.example.aula09;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SegundaTela extends AppCompatActivity {
    private EditText editCodigo, editNome, editTel, editEmail;
    private Button btnSalvarT2;
    private PessoaDAO bd = new PessoaDAO(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.segunda_tela);

        editCodigo = findViewById(R.id.editTextCodigoT2);
        editNome = findViewById(R.id.editTextNomeT2);
        editTel = findViewById(R.id.editTextTelefoneT2);
        editEmail = findViewById(R.id.editTextEmailT2);
        btnSalvarT2 = findViewById(R.id.buttonSalvarT2);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("pessoa")) {
            Pessoa pessoa = intent.getParcelableExtra("pessoa");

            if (pessoa != null) {
                editCodigo.setText(String.valueOf(pessoa.getCod()));
                editNome.setText(pessoa.getNome());
                editTel.setText(pessoa.getTel());
                editEmail.setText(pessoa.getEmail());
            }
        }

        btnSalvarT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = editCodigo.getText().toString();
                String nome = editNome.getText().toString();
                String email = editEmail.getText().toString();
                String tel = editTel.getText().toString();
                if (nome.isEmpty()) editNome.setError("Campo obrigatório");
                else {
                    if (codigo.isEmpty()) {
                        bd.adicionarPessoa(new Pessoa(nome, tel, email));
                        Toast.makeText(SegundaTela.this,
                                "Adicionado com Sucesso", Toast.LENGTH_SHORT).show();
                    } else {
                        bd.atualizaPessoa(new Pessoa(Integer.parseInt(codigo), nome, tel, email));
                        Toast.makeText(SegundaTela.this,
                                "Atualizado com Sucesso", Toast.LENGTH_SHORT).show();
                    }
                    // Configura os dados atualizados para retorno
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("dadosAtualizados", new Pessoa(Integer.parseInt(codigo), nome, tel, email));
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Finaliza a SegundaTela e retorna para a MainActivity
                }
            }
        });


    }
    private void limparCampos(){
        editCodigo.setText("");
        editNome.setText("");
        editEmail.setText("");
        editTel.setText("");
    }
}
