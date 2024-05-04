package com.example.aula09;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editCodigo, editNome, editTel, editEmail;

    private Button btnLimpar, btnSalvar, btnExcluir;

    private ListView lista;

    private PessoaDAO bd = new PessoaDAO(this);

    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;

    private void listarPessoas(){
        List<Pessoa> pessoas = bd.obterPessoas();
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, arrayList);
        lista.setAdapter(adapter);
        for(Pessoa p: pessoas){
            arrayList.add(p.getCod()+" - "+p.getNome());
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PessoaDAO dao = new PessoaDAO(getApplicationContext());
        editCodigo = findViewById(R.id.editTextCodigo);
        editNome = findViewById(R.id.editTextNome);
        editTel = findViewById(R.id.editTextTelefone);
        editEmail = findViewById(R.id.editTextEmail);
        btnLimpar = findViewById(R.id.buttonLimpar);
        btnSalvar = findViewById(R.id.buttonSalvar);
        btnExcluir = findViewById(R.id.buttonExcluir);
        lista = findViewById(R.id.listView);

        listarPessoas();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String conteudo = (String) lista.getItemAtPosition(position);
                String codigo = conteudo.substring(0, conteudo.indexOf("-")).trim();
                Pessoa pessoa = dao.obterPessoa(Integer.parseInt(codigo));
                editCodigo.setText(String.valueOf(pessoa.getCod()));
                editNome.setText(pessoa.getNome());
                editEmail.setText(pessoa.getEmail());
                editTel.setText(pessoa.getTel());

                Intent intent = new Intent(MainActivity.this, SegundaTela.class);
                intent.putExtra("pessoa", pessoa);
                startActivityForResult(intent, 1); // Inicia a SegundaTela e espera por um resultado
            }
        });



        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparCampos();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = editCodigo.getText().toString();
                String nome = editNome.getText().toString();
                String email = editEmail.getText().toString();
                String tel = editTel.getText().toString();
                if(nome.isEmpty()) editNome.setError("Campo obrigatório");
                else if (codigo.isEmpty()) {
                    bd.adicionarPessoa(new Pessoa(nome, tel, email));
                    Toast.makeText(MainActivity.this,
                            "Adicionado com Sucesso", Toast.LENGTH_SHORT).show();
                    limparCampos();
                    listarPessoas();
                } else {
                    bd.atualizaPessoa(new Pessoa(Integer.parseInt(codigo), nome, tel, email));
                    Toast.makeText(MainActivity.this,
                            "Atualizado com Sucesso", Toast.LENGTH_SHORT).show();
                    limparCampos();
                    listarPessoas();
                }
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = editCodigo.getText().toString();
                if (codigo.isEmpty()) editNome.setError("Nenhuma pessoa foi selecionada!");
                else {
                    Pessoa pessoa = new Pessoa();
                    pessoa.setCod(Integer.parseInt(codigo));
                    bd.apagarPessoa(pessoa);
                    Toast.makeText(MainActivity.this,
                            "Pessoa excluída com sucesso!", Toast.LENGTH_SHORT).show();
                    limparCampos();
                    listarPessoas();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                // Recebe os dados atualizados da SegundaTela
                Pessoa pessoaAtualizada = data.getParcelableExtra("dadosAtualizados");
                // Atualiza a lista de pessoas
                listarPessoas();limparCampos();
                // Exibe uma mensagem para indicar que os dados foram atualizados
                Toast.makeText(this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void limparCampos(){
        editCodigo.setText("");
        editNome.setText("");
        editEmail.setText("");
        editTel.setText("");
    }

}