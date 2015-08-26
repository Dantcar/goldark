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
import br.com.androidpro.goldark.rest.PageableResult;
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
    private String username;

    static class ViewHolder {

        @InjectView(R.id.save) Button saveButton;
        @InjectView(R.id.erase) Button eraseButton;
        @InjectView(R.id.rua) TextView rua;
        @InjectView(R.id.numero) TextView numero;
        @InjectView(R.id.complemento) TextView complemento;
        @InjectView(R.id.bairro) TextView bairro;
        @InjectView(R.id.cidade) TextView cidade;
        @InjectView(R.id.pais) TextView pais;
        @InjectView(R.id.cep) TextView cep;


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
        username = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        RestApi.getApi().getAndroidPro().retrieveEndereco(username, new Callback<PageableResult<Endereco>>() {
            @Override
            public void success(PageableResult<Endereco> enderecos, Response response) {
                showProgress(false);
                if(enderecos.getData() != null && !enderecos.getData().isEmpty()) {
                    Endereco endereco = enderecos.getData().get(0);
                    enderecoId = endereco.getId();
                    populateData(endereco);
                } else {
                    Toast.makeText(EnderecoActivity.this,"Erro na busca do endereço",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void failure(RetrofitError error) {
                showProgress(false);
                Toast.makeText(EnderecoActivity.this,"Não existe endereço cadastrado, insira um por gentileza",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void eraseEndereco() {
        showProgress(true);

        RestApi.getApi().getAndroidPro().eraseEndereco(enderecoId, new Callback<Status>() {
            @Override
            public void success(Status status, Response response) {
                clearData();
                enderecoId = null;
                showProgress(false);
                Toast.makeText(EnderecoActivity.this, "Apagado com sucesso", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                showProgress(false);
                Toast.makeText(EnderecoActivity.this, "Erro ao apagar", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveOrUpdateEndereco() {
        showProgress(true);
        Endereco endereco = new Endereco();
        endereco.setRua(String.valueOf(viewHolder.rua.getText()));
        endereco.setNumero(Integer.valueOf(String.valueOf(viewHolder.numero.getText())));
        endereco.setComplemento(String.valueOf(viewHolder.complemento.getText()));
        endereco.setBairro(String.valueOf(viewHolder.bairro.getText()));
        endereco.setCidade(String.valueOf(viewHolder.cidade.getText()));
        endereco.setPais(String.valueOf(viewHolder.pais.getText()));
        endereco.setCep(Integer.valueOf(String.valueOf(viewHolder.cep.getText())));

        // -- Necessário para saber qual de qual usuário é o endereço
        endereco.setUsuario(username);

        if(enderecoId!=null) {
            RestApi.getApi().getAndroidPro().updateEndereco(enderecoId,endereco, new Callback<Endereco>() {
                @Override
                public void success(Endereco endereco, Response response) {
                    showProgress(false);
                    Toast.makeText(EnderecoActivity.this,"Atualizado com sucesso",Toast.LENGTH_LONG).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    showProgress(false);
                    Toast.makeText(EnderecoActivity.this, "Erro na atualização",Toast.LENGTH_LONG).show();
                }
            });
        } else {
            RestApi.getApi().getAndroidPro().createEndereco(endereco, new Callback<Endereco>() {
                @Override
                public void success(Endereco endereco, Response response) {
                    showProgress(false);
                    enderecoId = endereco.getId();
                    Toast.makeText(EnderecoActivity.this,"Criado com sucesso",Toast.LENGTH_LONG).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    showProgress(false);
                    Toast.makeText(EnderecoActivity.this, "Erro na criação",Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void populateData(Endereco endereco) {
        viewHolder.rua.setText(endereco.getRua());
        viewHolder.numero.setText(number(endereco.getNumero()));
        viewHolder.complemento.setText(endereco.getComplemento());
        viewHolder.bairro.setText(endereco.getBairro());
        viewHolder.cidade.setText(endereco.getCidade());
        viewHolder.pais.setText(endereco.getPais());
        viewHolder.cep.setText(number(endereco.getCep()));
    }

    private String number(Integer numero) {
        if(numero==null) {
            return "";
        }
        return String.valueOf(numero);
    }

    private void clearData() {
        populateData(new Endereco());
    }
}
