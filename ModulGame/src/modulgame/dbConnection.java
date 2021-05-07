/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulgame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
/**
 *
 * @author Fauzan
 */
// kelas untuk mengatur koneksi ke database
public class dbConnection {
    public static Connection con;
    public static Statement stm;
    public static Menu menu;
//    public static Game game;
    
    public void connect(){//untuk membuka koneksi ke database
        try {
            // konfigurasi database
            String url ="jdbc:mysql://localhost/db_gamepbo";
            String user="root";
            String pass="";
            Class.forName("com.mysql.jdbc.Driver");
            
            con = DriverManager.getConnection(url,user,pass);
            stm = con.createStatement();
            
            System.out.println("koneksi berhasil;");
        } catch (Exception e) {
            // print error jika gagal melakukan koneksi dgn database
            System.err.println("koneksi gagal " +e.getMessage());
        }
    }
    
    // untuk membaca data agar ditampilkan di tabel ketika di main menu
    public DefaultTableModel readTable(){
        DefaultTableModel dataTabel = null;
        try{
            // set nama kolom tabelnya apa saja
            Object[] column = {"No", "Username", "Game Score", "Waktu"};
            // konek ke db
            connect();
            // inisialisasi data tabel
            dataTabel = new DefaultTableModel(null, column);
            // query untuk select data dari db
            String sql = "Select * from highscore ORDER by Score DESC";
            // mengeksekusi query
            ResultSet res = stm.executeQuery(sql);
            
            int no = 1;
            // untuk sebanyak data di db:
            while(res.next()){
                // tampung seluruh hasil row nya
                Object[] hasil = new Object[5];
                hasil[0] = no;
                hasil[1] = res.getString("Username");
                hasil[2] = res.getString("Score");
                hasil[3] = res.getString("Waktu");
                no++;
                // lalu masukkan hasil-hasilnya ke dalam tabel
                dataTabel.addRow(hasil);
            }
        } catch(Exception e){
            System.err.println("Read gagal " +e.getMessage());
        }
        return dataTabel;
    }
    
    // membaca data dari db
    public ResultSet readData(){
        ResultSet res = null;
        try {
            connect();
            String sql = "Select Username, Score from highscore";
            res = stm.executeQuery(sql);
        } catch(Exception e){
            System.err.println("Read gagal " +e.getMessage());
        }
        return res;
    }
    
    // menginsert data ke db
    public void insertData(String username, int score, int totalWaktu){
        try {
            connect();
            String sql = "INSERT INTO highscore(Username, Score, Waktu) VALUES ('"+username+"',"+score+", "+totalWaktu+")";
            stm.executeUpdate(sql);
        } catch(Exception e){
            // print error jika gagal melakukan insert Data
            System.err.println("gagal menambahkan data " +e.getMessage());
        }
    }
    
    // mengupdate data yg sudah ada di db
    public void updateData(String username, int score, int totalWaktu){
        try {
            connect();
            String sql = "UPDATE highscore SET Score="+score+", Waktu="+totalWaktu+" WHERE Username='"+username+"' ";
            stm.executeUpdate(sql);
        } catch(Exception e){
            // print error jika gagal melakukan update Data
            System.err.println("gagal mengubah data " +e.getMessage());
        }
    }
}
