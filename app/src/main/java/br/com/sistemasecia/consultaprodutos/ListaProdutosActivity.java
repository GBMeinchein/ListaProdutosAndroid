package br.com.sistemasecia.consultaprodutos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by maq8 on 18/03/2015.
 */
//Classe da lista dos produtos
public class ListaProdutosActivity extends Activity {

    AdaptadorProdutos<Produto> adaptador = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btLimpa = (Button) findViewById(R.id.btLimpa);

        final EditText etFiltraProduto = (EditText) findViewById(R.id.etProduto);
        final DbHelper dbHelper = new DbHelper(this);
        ArrayList<Produto> itens = new ArrayList<Produto>();
        final ListView lista = (ListView) findViewById(R.id.lvListaProdutos);

        //array itens recebe todos os produtos do banco de dados
        itens = dbHelper.selectTodosProdutosArray();

        //adaptador, da calsse AdaptadorProdutos que vai receber a lista de itens,
        //para depois passar seus dados para o listview
        adaptador = new AdaptadorProdutos<Produto>(itens);

        //seta para o lista(listview da tela) o adapter com a lista de produtos
        lista.setAdapter(adaptador);

        btLimpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFiltraProduto.setText("");
            }
        });

        etFiltraProduto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //sempre filtra o adaptador quando o texto do filtro é mudado
                adaptador.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    //classe adaptador adaptador produtos para customizar o adapter, para deixar com duas linhas
    class AdaptadorProdutos<T> extends ArrayAdapter {
        String prefix1, prefix2, prefix3 = "", prefix4 = "", prefix5 = "";
        String prefixAux;
        int tamanho;
        String valor, valorSemUltimaVirgula, primeiroCaracter;

       //Itens de exibição / filtrados
       private List<Produto> itens_exibicao;
        //Essa lista contem todos os itens.
       private List<Produto> itens;
        //Utilizado no getView para carregar e construir um item.
       private LayoutInflater layoutInflater;
       //private ArrayList<T> mOriginalValues = (ArrayList<T>) listaProdutos;
       AdaptadorProdutos(Context context, int resource) {
           super(context, resource, 0, new ArrayList<T>());
       }
        public AdaptadorProdutos(List<Produto> itens) {
            super(ListaProdutosActivity.this, R.layout.activity_lista, itens);
            this.itens_exibicao = itens;
            this.itens = itens;
        }

        public AdaptadorProdutos(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId, new ArrayList<T>());
        }
        public AdaptadorProdutos(Context context, int resource, List<Produto> itens) {
            super(context, resource, 0, Arrays.asList(itens));
                 this.itens = itens;
                 this.itens_exibicao = itens;
                 layoutInflater = LayoutInflater.from(context);
        }

        public AdaptadorProdutos(Context context, int resource, int textViewResourceId, T[] objects) {
            super(context, resource, textViewResourceId, Arrays.asList(objects));
        }

        public View getView(int arg0, View arg1, ViewGroup arg2) {

            View linha = arg1;
            ArmazenadorProdutos armazenador = null;

            if (linha == null) {
                LayoutInflater inflater = getLayoutInflater();
                linha = inflater.inflate(R.layout.activity_lista, arg2, false);
                armazenador = new ArmazenadorProdutos(linha);
                linha.setTag(armazenador);
            } else {
               armazenador = (ArmazenadorProdutos) linha.getTag();
            }

            armazenador.popularFormulario(itens_exibicao.get(arg0));
            return linha;

        }
        @Override
        public int getCount() {
            return itens_exibicao.size();
        }

        @Override
        public Object getItem(int arg0) {
            return itens_exibicao.get(arg0);
        }

        public Filter getFilter() {
            Filter filter = new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence filtro) {
                    FilterResults results = new FilterResults();
                    //se não foi realizado nenhum filtro insere todos os itens.
                    if (filtro == null || filtro.length() == 0) {
                        results.count = itens.size();
                        results.values = itens;
                    } else {

                        prefixAux = filtro.toString();
                        tamanho = prefixAux.length();
                        valor = prefixAux.substring(tamanho - 1, tamanho);
                        primeiroCaracter = prefixAux.substring(0, 1);
                        valorSemUltimaVirgula = prefixAux.substring(0, tamanho - 1);
                        int numeroDeVirgulas = 0;
                        boolean segundaVirgula = false;
                        filtro = filtro.toString().toLowerCase();

                        if (filtro.toString().contains(",") && primeiroCaracter.equals(",") == false && (valor.equals(",") == false || valorSemUltimaVirgula.contains(","))) {
                            //cria um array para armazenar os objetos filtrados.
                            List<Produto> itens_filtrados = new ArrayList<Produto>();

                            for(int x = 0, y = 1; y < tamanho; x++, y++){

                                if(prefixAux.substring(x, y).equals(",")){
                                    numeroDeVirgulas = numeroDeVirgulas + 1;
                                }
                            }

                            if(numeroDeVirgulas < 4) {
                                //Toast.makeText(getContext(), String.valueOf(numeroDeVirgulas)+String.valueOf(tamanho), Toast.LENGTH_SHORT).show();
                                Scanner scan = new Scanner(filtro.toString()).useDelimiter(",");
                                while (scan.hasNext()) {
                                    prefix1 = scan.next();
                                    prefix2 = scan.next();

                                    if (numeroDeVirgulas > 1) {
                                        segundaVirgula = true;
                                        prefix3 = scan.next();
                                    }
                                    if (numeroDeVirgulas > 2) {
                                        prefix4 = scan.next();
                                    }
                                    if (numeroDeVirgulas > 3) {
                                        prefix5 = scan.next();
                                    }
                                   // Toast.makeText(getContext(),prefix1 + prefix2, Toast.LENGTH_SHORT).show();
                                }
                                scan.close();
                            }else{
                                Toast.makeText(getContext(), "Não é possível utilizar mais de quatro vírgulas para o filtro!", Toast.LENGTH_SHORT).show();
                            }

                            //percorre toda lista verificando se contem a palavra do filtro na descricao do objeto.
                            for (int i = 0; i < itens.size() - 1; i++) {
                                Produto data = itens.get(i);

                                filtro = filtro.toString().toLowerCase();
                                String condicao = data.getDescricao().toLowerCase();

                                if (condicao.contains(prefix1) && condicao.contains(prefix2) && (condicao.contains(prefix3) || numeroDeVirgulas <= 1) && (condicao.contains(prefix4) || numeroDeVirgulas <= 2) && (condicao.contains(prefix5) || numeroDeVirgulas <= 3)) {
                                    //se conter adiciona na lista de itens filtrados.
                                    itens_filtrados.add(data);
                                }
                            }
                            // Define o resultado do filtro na variavel FilterResults
                            results.count = itens_filtrados.size();
                            results.values = itens_filtrados;
                        }else if (segundaVirgula == false){
                            //cria um array para armazenar os objetos filtrados.
                            List<Produto> itens_filtrados = new ArrayList<Produto>();
                            String filtroAux = filtro.toString();
                            if(valor.equals(",")){
                                filtro = filtroAux.substring(0, tamanho-1);
                            }
                            //percorre toda lista verificando se contem a palavra do filtro na descricao do objeto.
                            for (int i = 0; i < itens.size() - 1; i++) {
                                Produto data = itens.get(i);

                                filtro = filtro.toString().toLowerCase();
                                String condicao = data.getDescricao().toLowerCase();

                                if (condicao.contains(filtro)) {
                                    //se conter adiciona na lista de itens filtrados.
                                    itens_filtrados.add(data);
                                }
                            }
                            // Define o resultado do filtro na variavel FilterResults
                            results.count = itens_filtrados.size();
                            results.values = itens_filtrados;
                        }
                    }
                    return results;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                    itens_exibicao = (List<Produto>) results.values; // Valores filtrados.
                    notifyDataSetChanged();  // Notifica a lista de alteração
                }

            };
            return filter;
        }
    }

    //classe que com as duas linhas do listview e recebe a descriçao e os demais dados
    static class ArmazenadorProdutos {
        private TextView nome;
        private TextView codigo;

        ArmazenadorProdutos(View linha) {
            nome = (TextView) linha.findViewById(R.id.descricao);
            codigo = (TextView) linha.findViewById(R.id.detalhes);
        }

        void popularFormulario(Produto r) {
             nome.setText(r.getDescricao());
             codigo.setText(r.toString());
    }

}
}


