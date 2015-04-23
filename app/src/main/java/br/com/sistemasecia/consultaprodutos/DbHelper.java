package br.com.sistemasecia.consultaprodutos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maq8 on 09/03/2015.
 */
//Classe que interage com o banco de dados
public class DbHelper extends SQLiteOpenHelper{

    private static final String NOME_BASE = "SecBase";

    //Versão do banco de dados, sempre que alterar algo na estrutura da tabela, mudar aqui o numero da versão
    private static final int VERSAO_BASE = 9;

    public DbHelper(Context context) {
        super(context, NOME_BASE, null, VERSAO_BASE);
    }

    @Override
    //Cria a tabela
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateTableProduto = "CREATE TABLE Produto("
                                     + "ProCod VARCHAR(16) PRIMARY KEY,"
                                     + "ProDes VARCHAR(90),"
                                     + "ProPreCon DECIMAL(14, 4),"
                                     + "ProUni VARCHAR(6),"
                                     + "ProSaldo DECIMAL(14, 4)"
                                     //+ "CONSTRAINT PK_EmpPro PRIMARY KEY (ProCod)"
                                     + ")";
        db.execSQL(sqlCreateTableProduto);
    }

    @Override
    //Aqui dropa a tabela quando mudado a versão, para inserir a tabela noamente
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db = getWritableDatabase();
        String sqlDeletaTabela = "DROP TABLE Produto";
        db.execSQL(sqlDeletaTabela);
        onCreate(db);
    }

    //Insere os produtos
    public void insertProduto(Produto produto){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        //cv.put("EmpCod", produto.getEmpresa());
        cv.put("ProCod", produto.getCodigo());
        cv.put("ProDes", produto.getDescricao());
        cv.put("ProPreCon", produto.getPreco());
        cv.put("ProUni", produto.getUnidade());
        cv.put("ProSaldo", produto.getSaldo());

        db.insert("Produto", null, cv);
        db.close();
    }

    //deleta os produtos
    public void deletaTodosProdutos(){
        SQLiteDatabase db = getWritableDatabase();
        String sqlDeletaTodosProdutos = "DELETE FROM Produto";
        db.execSQL(sqlDeletaTodosProdutos);
        db.close();
    }

    public void deletaUmProduto(Produto produto){
        SQLiteDatabase db = getWritableDatabase();
        String sqlDeletaUmProduto = "DELETE FROM Produto WHERE PROCOD = "+produto.getCodigo();
        db.execSQL(sqlDeletaUmProduto);
        db.close();
    }

    //retorna todos os produtos da tabela
    public List<Produto> selectTodosProdutos(){

        List<Produto> listaProdutos = new ArrayList<Produto>();

        SQLiteDatabase db = getReadableDatabase();

        String sqlSelectTodosProdutos = "SELECT * FROM Produto";

        Cursor c = db.rawQuery(sqlSelectTodosProdutos, null);

        if(c.moveToFirst()){
            do {
                Produto produto = new Produto();
                //produto.setEmpresa(c.getInt(0));
                produto.setCodigo(c.getString(0));
                produto.setDescricao(c.getString(1));
                produto.setPreco(c.getDouble(2));
                produto.setUnidade(c.getString(3));
                produto.setSaldo(c.getDouble(4));

                listaProdutos.add(produto);
            }while(c.moveToNext());
        }

        db.close();
        return listaProdutos;

    }

    //retorna todos os produtos da tabela em forma de arrayList
    public ArrayList<Produto> selectTodosProdutosArray(){

        ArrayList<Produto> listaProdutos = new ArrayList<Produto>();

        SQLiteDatabase db = getReadableDatabase();

        String sqlSelectTodosProdutos = "SELECT * FROM Produto ORDER BY ProDes";

        Cursor c = db.rawQuery(sqlSelectTodosProdutos, null);

        if(c.moveToFirst()){
            do {
                Produto produto = new Produto();
                //produto.setEmpresa(c.getInt(0));
                produto.setCodigo(c.getString(0));
                produto.setDescricao(c.getString(1));
                produto.setPreco(c.getDouble(2));
                produto.setUnidade(c.getString(3));
                produto.setSaldo(c.getDouble(4));

                listaProdutos.add(produto);
            }while(c.moveToNext());
        }

        db.close();
        return listaProdutos;

    }

    //retorna apenas os dados do produto passado por parametro
    public List<Produto> selectUmProduto(String filtroProduto){

        List<Produto> listaProduto = new ArrayList<Produto>();

        SQLiteDatabase db = getReadableDatabase();

        String sqlSelectUmProduto = "Select * from Produto where ProDes LIKE '%"+filtroProduto+"%'";

        Cursor c = db.rawQuery(sqlSelectUmProduto, null);

        if(c.moveToFirst()){
            do {
                Produto produto = new Produto();
                //produto.setEmpresa(c.getInt(0));
                produto.setCodigo(c.getString(0));
                produto.setDescricao(c.getString(1));
                produto.setPreco(c.getDouble(2));
                produto.setUnidade(c.getString(3));
                produto.setSaldo(c.getDouble(4));

                listaProduto.add(produto);
            }while(c.moveToNext());
        }

        db.close();
        return listaProduto;

    }

    public Produto selectProdutoPeloCodigo(Produto produto){

        boolean contemProduto = false;

        SQLiteDatabase db = getReadableDatabase();

        String sqlSelectUmProduto = "Select * from Produto where ProCod = "+produto.getCodigo();

        Cursor c = db.rawQuery(sqlSelectUmProduto, null);

        if(c.moveToFirst()){
            do {
               // produto.setEmpresa(c.getInt(0));
                produto.setCodigo(c.getString(0));
                produto.setDescricao(c.getString(1));
                produto.setPreco(c.getDouble(2));
                produto.setUnidade(c.getString(3));
                produto.setSaldo(c.getDouble(4));

            }while(c.moveToNext());
        }

        db.close();

        return produto;
    }

    public void updatePrecoProduto(Produto produto){

        SQLiteDatabase db = getWritableDatabase();
        String sqlDeletaTodosProdutos = "UPDATE Produto SET ProPreCon = "+produto.getPreco();
        db.execSQL(sqlDeletaTodosProdutos);
        db.close();
    }

}
