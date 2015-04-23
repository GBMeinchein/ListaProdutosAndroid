package br.com.sistemasecia.consultaprodutos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * Created by maq8 on 09/03/2015.
 */

//classe do produto que recebe todos os dados do produto
public class Produto {

    //private int empresa;
    private String codigo;
    private String descricao;
    private double preco;
    private String unidade;
    private double saldo;

    public Produto(){

    }

    public Produto(String codigo, String descricao, double preco, String unidade, double saldo){

        //this.empresa = empresa;
        this.codigo = codigo;
        this.descricao = descricao;
        this.preco = preco;
        this.unidade = unidade;
        this.saldo = saldo;

    }

    //public int getEmpresa() {
   //     return empresa;
   // }

   // public void setEmpresa(int empresa) {
   //     this.empresa = empresa;
  // }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    @Override
    public String toString() {

        //DecimalFormat df = new DecimalFormat("####.00");
        //df.format(preco);
        String precoAux;
        DecimalFormat fmt = new DecimalFormat("0.00");
        String string = fmt.format(preco);
        String[] part = string.split("[,]");
        precoAux = part[0] + "." + part[1];
        String reais = "R$";
        for(int i = 0; (precoAux.length() + reais.length()) < 9; i++){
            reais += " ";
        }

        int saldoInteiro = (int) saldo;
        return  " " + codigo + "|" +
                reais + precoAux +
                "|" + unidade +
                "|Sld " + saldoInteiro;
    }
}
