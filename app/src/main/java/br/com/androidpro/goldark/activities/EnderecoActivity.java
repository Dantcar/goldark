package br.com.androidpro.goldark.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.com.androidpro.goldark.R;
import br.com.androidpro.goldark.rest.Endereco;
import br.com.androidpro.goldark.rest.RestApi;

import br.com.androidpro.goldark.rest.Status;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import butterknife.InjectView;

/**
 * @author Thiago Pagonha
 * @version 25/08/15.
 */
public class EnderecoActivity extends ProgressActivity {

    private String enderecoId;
    private String usuarioId;

    static class ViewHolder {

        @InjectView(R.id.save) Button saveButton;
        @InjectView(R.id.erase) Button eraseButton;
        @InjectView(R.id.rua) TextView rua;

        public ViewHolder(Activity activity) {
            ButterKnife.inject(this,activity);
        }
    }

    private ViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_endereco);
        super.onCreate(savedInstanceState);
        showProgress(true);

        viewHolder = new ViewHolder(this);

        viewHolder.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOrUpdateEndereco();
            }
        });

        viewHolder.eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eraseEndereco();
            }
        });

        Intent intent = getIntent();
        usuarioId = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        RestApi.getApi().getAndroidPro().retrieveEndereco(usuarioId, new Callback<Endereco>() {
            @Override
            public void success(Endereco endereco, Response response) {
                showProgress(false);
                populateData(endereco);
            }

            @Override
            public void failure(RetrofitError error) {
                showProgress(false);
                Toast.makeText(EnderecoActivity.this,"Não existe endereço cadastrado, insira um por gentileza",Toast.LENGTH_SHORT);
            }
        });

    }

    private void eraseEndereco() {
        showProgress(true);

        RestApi.getApi().getAndroidPro().eraseEndereco(enderecoId, new Callback<Status>() {
            @Override
            public void success(Status status, Response response) {
                showProgress(false);
                Toast.makeText(EnderecoActivity.this,"Apagado com sucesso",Toast.LENGTH_SHORT);
            }

            @Override
            public void failure(RetrofitError error) {
                showProgress(false);
                Toast.makeText(EnderecoActivity.this,"Erro ao apagar",Toast.LENGTH_SHORT);
            }
        });
    }

    private void saveOrUpdateEndereco() {
        showProgress(true);
        Endereco endereco = new Endereco();
        endereco.setRua(viewHolder.rua.getText().toString());

        if(enderecoId!=null) {
            RestApi.getApi().getAndroidPro().updateEndereco(enderecoId,endereco, new Callback<Endereco>() {
                @Override
                public void success(Endereco endereco, Response response) {
                    showProgress(false);
                    Toast.makeText(EnderecoActivity.this,"Atualizado com sucesso",Toast.LENGTH_SHORT);
                }

                @Override
                public void failure(RetrofitError error) {
                    showProgress(false);
                    Toast.makeText(EnderecoActivity.this, "Erro na atualização", Toast.LENGTH_SHORT);
                }
            });
        } else {
            // -- Necessário para saber qual de qual usuário é o endereço
            endereco.setUsuario(usuarioId);
            RestApi.getApi().getAndroidPro().createEndereco(endereco, new Callback<Endereco>() {
                @Override
                public void success(Endereco endereco, Response response) {
                    showProgress(false);
                    enderecoId = endereco.getId();
                    Toast.makeText(EnderecoActivity.this,"Criado com sucesso",Toast.LENGTH_SHORT);
                }

                @Override
                public void failure(RetrofitError error) {
                    showProgress(false);
                    Toast.makeText(EnderecoActivity.this, "Erro na criação", Toast.LENGTH_SHORT);
                }
            });
        }
    }

    private void populateData(Endereco endereco) {
        viewHolder.rua.setText(endereco.getRua());
    }
}
