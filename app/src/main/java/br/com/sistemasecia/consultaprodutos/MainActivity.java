package br.com.sistemasecia.consultaprodutos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;


//Classe inicial, primeira classe que é executada quando aberto o app
public class MainActivity extends Activity {

    private ProgressDialog p_dialog ;
    private Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_celular);

        Button btEntrar = (Button) findViewById(R.id.btEntrar);
        final EditText etSenha = (EditText) findViewById(R.id.etSenha);
        final CheckBox cbAtualizaProdutos = (CheckBox) findViewById(R.id.cbAtualizaProdutos);
        //final ProgressBar pbAtualizaProdutos = (ProgressBar) findViewById(R.id.pbAtualizaProdutos);
        //final int mProgressStatus = 0;
        //final ProgressDialog progress = new ProgressDialog(this);
        final DbHelper dbHelper = new DbHelper(this);
        final Context contexto = this;

        btEntrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            final String senha = etSenha.getText().toString();
            if(senha.equals("930")) {//confere a senha
                p_dialog = ProgressDialog.show(contexto, "Carregando a lista de produtos", "Aguarde...", false, true);
            }else{
                Toast.makeText(getApplicationContext(), "Senha incorreta!", Toast.LENGTH_SHORT).show();
            }
            new Thread() {
                @Override
                public void run() {

            if(senha.equals("930")) {//confere a senha

                //Quando clica no botao, verifica se quer atualizar os produtos
                if (cbAtualizaProdutos.isChecked()) {

                    File file = android.os.Environment.getExternalStorageDirectory();
                    final String nomeArquivo = file + "/listaProdutos.txt";
                   // p_dialog =ProgressDialog.show(contexto, "Titulo", "Aguarde...", false, true);
                   // InicioProgressDialogo();

                    Scanner scanner = null;
                    String codigo, descricao, preco, unidade, saldo;
                    //Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
                    //Lê o arquivo txt e pega todos os campos separadamente para inserir no banco de dados
                    try
                    {
                        dbHelper.deletaTodosProdutos();
                        Produto prod = new Produto();
                        scanner = new Scanner(new FileReader(nomeArquivo))
                                .useDelimiter("\\||\\n");
                        //Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();
                        while (scanner.hasNext()) {
                            // empresa = scanner.next();
                            codigo = scanner.next();
                            descricao = scanner.next();
                            preco = scanner.next();
                            unidade = scanner.next();
                            saldo = scanner.next();

                            for (int i = 0; codigo.length() < 16; i++) {
                                codigo += " ";
                            }

                            for (int i = 0; unidade.length() < 2; i++) {
                                unidade += " ";
                            }
                            String saldoAux = String.valueOf(saldo);
                            for (int i = 0; saldoAux.length() < 2; i++) {
                                unidade += " ";
                            }

                            prod.setSaldo(Double.parseDouble(saldo));
                            prod.setUnidade(unidade);
                            prod.setPreco(Double.parseDouble(preco));
                            prod.setDescricao(descricao);
                            prod.setCodigo(codigo);

                            dbHelper.insertProduto(prod);

                            //Produto produtoVelho = dbHelper.selectProdutoPeloCodigo(prod);

                            //if (produtoVelho.getPreco() != prod.getPreco() || produtoVelho.getSaldo() != prod.getSaldo()) {
                            //    dbHelper.updatePrecoProduto(prod);
                            //}
                            //sleep(200);
                            // jumpTime += 5;
                            // progress.setProgress(jumpTime);
                            //Thread.sleep(1);
                        }
                        scanner.close();

                    } catch (
                            FileNotFoundException e
                            )

                    {
                        Toast.makeText(getApplicationContext(), "O arquivo para atualização não foi encontrado!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                        Intent it = new Intent(MainActivity.this, ListaProdutosActivity.class);
                        startActivity(it);
                p_dialog.dismiss();
            }
                }
            }.start();
            }
        });

        }
    private void exemplo_simples() {
        AlertDialog alerta;
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Titulo");
        //define a mensagem
        builder.setMessage("Qualifique este software");
        //define um botão como positivo
        //cria o AlertDialog

        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    private void InicioProgressDialogo(){
        h.post(new Runnable() {

            @Override
            public void run() {
            // TODO Auto-generated method stub
            //aqui faz todo o processamento e após o seu termino, chamar o método para fechar a janela
            try

            {
                Thread.sleep(2000);
            }

            catch(
            InterruptedException e
            )

            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           // FimProgressDialogo();
            p_dialog.dismiss();
            }
        });
    }

    private void FimProgressDialogo(){
        h.post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                p_dialog.dismiss();
            }
        });

    }

}





