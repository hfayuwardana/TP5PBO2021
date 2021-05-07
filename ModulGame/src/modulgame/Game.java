/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulgame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.Random;
import java.sql.ResultSet;

/**
 *
 * @author Fauzan
 */
public class Game extends Canvas implements Runnable{
    Window window;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    
    private int score = 0;
    
    private int time = 0;
    
    private Thread thread;
    private boolean running = false;
    
    private Handler handler;
    
    private dbConnection conn = new dbConnection();
    private String username;
    
    private int countItem = 0;
    
    private int countTotalWaktu = 10;
    private int countTotalScore = 0;
    
    private int isKenaMusuh = 0;
    private int level = 1;
    
    Random rand;
    
    public enum STATE{
        Game,
        GameOver
    };
    
    public STATE gameState = STATE.Game;
    
    public Game(String username, int lvl){
        // instansiasi window baru untuk membuka window game
        window = new Window(WIDTH, HEIGHT, "Modul praktikum 5", this);
        // instansiasi handler
        handler = new Handler();
        // menambahkan listener dari keyboard
        this.addKeyListener(new KeyInput(handler, this));
        
        // memutar BGM
        playSound("/Daystar - Sugar Cookie.wav");
        
        // instansiasi kelas untuk merandom suatu nilai
        rand = new Random(); //instansiasi kelas 'Random'
        
        // jika level yang dipilih adalah level 1 alias easy
        if(lvl == 1) {
            // set atribut level di kelas Game sebagai 1
            this.level = 1;
            // set waktu awal mulai game sebagai 20
            this.time = 20;
        } 
        // jika level yang dipilih adalah level 1 alias normal
        else if (lvl == 2) {
            // set atribut level di kelas Game sebagai 2
            this.level = 2;
            // set waktu awal mulai game sebagai 10
            this.time = 10;
        } 
        // jika level yang dipilih adalah level 3 alias hard
        else {
            // set atribut level di kelas Game sebagai 3
            this.level = 3;
            // set waktu awal mulai game sebagai 5
            this.time = 5;
        }
        
        // jika game sudah dimulai
        if(gameState == STATE.Game){
            // menentukan batas atas untuk value panjang dan lebar window
            int upperbound_x = WIDTH - 60;
            int upperbound_y = HEIGHT - 80;
            
            // menambahkan objek di dalam game
            // tambah item
            handler.addObject(new Items(rand.nextInt(upperbound_x), rand.nextInt(upperbound_y), ID.Item));
            countItem++;
            // tambah item
            handler.addObject(new Items(rand.nextInt(upperbound_x), rand.nextInt(upperbound_y), ID.Item));
            countItem++;
            
            // tambah player pertama
            handler.addObject(new Player(150, 150, ID.Player));
            // tambah player kedua
            handler.addObject(new Player2(500, 500, ID.Player2));
            
            // tambah 2 buah musuh
            handler.addObject(new Enemy(250, 0, ID.Enemy));
            handler.addObject(new Enemy(0, 250, ID.Enemy));
        }
        // set nilai atribut username sesuai dengan username yang ada pada parameter (yg berasal dari kelas Menu)
        this.username = username;
    }
    
    // method yg dijalankan ketika game dimulai
    public synchronized void start(){
        // membuat thread
        thread = new Thread(this);
        // menjalankan thread
        thread.start();
        // set status running sebagai true
        running = true;
    }
    
    // method yg dijalankan ketika game berhenti
    public synchronized void stop(){
        try{
            // menggabungkan thread
            thread.join();
            // set status running sebagai false
            running = false;
        }catch(Exception e){
            // print error jika terjadi kesalahan
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            
            while(delta >= 1){
                tick();
                delta--;
            }
            if(running){
                render();
                frames++;
            }
            
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                //System.out.println("FPS: " + frames);
                frames = 0;
                // jika status game sudah dimulai
                if(gameState == STATE.Game){
                    // jika waktu belum habis ataupun jika player tidak menyentuh musuh
                    if(time>0 && (isKenaMusuh == 0)){
                        // count down waktu
                        time--;
                    }
                    // jika waktu sudah habis, ataupun jika player menyentuh musuh
                    else{
                        // set status game sebagai game over
                        gameState = STATE.GameOver;
                    }
                }
            }
        }
        stop();
    }
    
    private void tick(){
        handler.tick();
        // jika game sudah dimulai
        if(gameState == STATE.Game){
            // untuk sebanyak objek yg ada di dalam game
            for(int i = 0;i<handler.object.size();i++){
                // menampung objek tertentu
                GameObject tempObject = handler.object.get(i);
                
                // jika objek tertentu tersebut merupakan objek enemy alias musuh
                if(tempObject.getId() == ID.Enemy){
                    // menentukan batas atas untuk value panjang dan lebar window
                    int upperbound_x = WIDTH - 60;
                    int upperbound_y = HEIGHT - 80;
                    
                    // menentukan kecepatan musuh
                    int kec_musuh = 0;
                    // jika levelnya adalah 1 alias easy
                    if(this.level == 1) {
                        // set kecepatan musuh sebagai 2 (alias lebih lambat daripada kecepatan player)
                        kec_musuh = 2;                    
                    }
                    // jika levelnya adalah 2 alias normal
                    else if(this.level == 2) {
                        // set kecepatan musuh sebagai 5 (alias sama dengan kecepatan player)
                        kec_musuh = 5;                      
                    } 
                    // jika levelnya adalah 2 alias hard
                    else {
                        // set kecepatan musuh sebagai 7 (alias lebih cepat daripada kecepatan player)
                        kec_musuh = 7;
                    }
                    
                    // jika musuh menyentuh batas atas layar
                     if(tempObject.getY() == 0) {
                         // musuh berjalan ke bawah
                         tempObject.setVel_y(+kec_musuh);
                     }
                     // jika musuh menyentuh batas bawah layar
                     else if(tempObject.getY() == upperbound_y) {
                         // musuh berjalan ke atas
                         tempObject.setVel_y(-kec_musuh);
                     }

                     // jika musuh menyentuh batas kiri layar
                     if(tempObject.getX() == 0) {
                         // musuh berjalan ke kanan
                         tempObject.setVel_x(+kec_musuh);
                     }
                     // jika musuh menyentuh batas kanan layar
                     else if(tempObject.getX() == upperbound_x) {
                         // musuh berjalan ke kiri
                         tempObject.setVel_x(-kec_musuh);
                     }    
                }
            }
            
            GameObject playerObject = null;
            GameObject player2Object = null;
            // untuk sebanyak objek game, lakukan:
            for(int i=0;i< handler.object.size(); i++){
                // tampung objek player 1 di var playerObject
                if(handler.object.get(i).getId() == ID.Player){
                   playerObject = handler.object.get(i);
                }
                // tampung objek player 2 di var player2Object
                if(handler.object.get(i).getId() == ID.Player2){
                    player2Object = handler.object.get(i);
                }
            }
            // jika player1 dan player2 berhasil diambil nilainya
            if(playerObject != null && playerObject != null){
                // untuk sebanyak objek game, lakukan:
                for(int i=0;i< handler.object.size(); i++){
                    // jika objek gamenya adalah item
                    if(handler.object.get(i).getId() == ID.Item){
                        // jika player memakan item
                        if(checkCollision(playerObject, handler.object.get(i)) || (checkCollision(player2Object, handler.object.get(i)))){
                            // munculkan suara memakan
                            playSound("/Eat.wav");
                            // hapus objek item
                            handler.removeObject(handler.object.get(i));
                            countItem--;
                            
                            // kalau item sudah 0 alias sudah dimakan semua oleh player
                            if(countItem == 0) {
                                // menentukan batas atas untuk value panjang dan lebar window
                                int upperbound_x = WIDTH - 60;
                                int upperbound_y = HEIGHT - 80;
                                // menambahkan 2 buah objek baru di dalam game
                                handler.addObject(new Items(rand.nextInt(upperbound_x), rand.nextInt(upperbound_y), ID.Item));
                                countItem++;
                                handler.addObject(new Items(rand.nextInt(upperbound_x), rand.nextInt(upperbound_y), ID.Item));
                                countItem++;                               
                            }
                            
                            // menambahkan score dengan random integer dengan range 0 sampai 100
                            score = score + rand.nextInt(30);
                            // menambahkan waktu dengan random integer dengan range 0 sampai 10
                            int rand_nextTime = rand.nextInt(10);
                            time = time + rand_nextTime;
                            
                            // menghitung total waktu untuk menentukan score akhir
                            countTotalWaktu = countTotalWaktu + rand_nextTime;
                            break;
                        }
                    }
                    if(handler.object.get(i).getId() == ID.Enemy){
                        // jika player menyentuh musuh
                        if(checkCollisionEnemy(playerObject, handler.object.get(i)) || (checkCollisionEnemy(player2Object, handler.object.get(i)))){
                            // set kondisi isKenaMusuh sebagai 1 atau true
                            this.isKenaMusuh = 1;
                            
                            playSound("/Eat.wav");
                            
                            // hapus objek player1 dan player 2
                            handler.removeObject(playerObject);
                            handler.removeObject(player2Object);
                            break;
                        }
                    }                 
                }
            }
        }
    }
    
    // method untuk mengecek apakah player memakan item
    public static boolean checkCollision(GameObject player, GameObject item){
        // set result sbg false terlebih dahulu
        boolean result = false;
        // set size player dan item
        int sizePlayer = 50;
        int sizeItem = 20;
        
        // menentukan sisi (batas) dari player
        int playerLeft = player.x;                  // nilai sisi kiri
        int playerRight = player.x + sizePlayer;    // nilai sisi kanan
        int playerTop = player.y;                   // nilai sisi atas
        int playerBottom = player.y + sizePlayer;   // nilai sisi bawah
        
        // menentukan sisi (batas) dari item
        int itemLeft = item.x;              // nilai sisi kiri
        int itemRight = item.x + sizeItem;  // nilai sisi kanan
        int itemTop = item.y;               // nilai sisi atas
        int itemBottom = item.y + sizeItem; // nilai sisi bawah
        
        // jika posisinya memakan
        if((playerRight > itemLeft ) &&
        (playerLeft < itemRight) &&
        (itemBottom > playerTop) &&
        (itemTop < playerBottom)
        ){
            // set nilai result sebagai true
            result = true;
        }
        // mengembalikan nilai result
        return result;
    }
    
    // method untuk mengecek apakah player bersentuhan dengan musuh
    public static boolean checkCollisionEnemy(GameObject player, GameObject enemy){
        // set result sbg false terlebih dahulu
        boolean result = false;
        // set size player dan musuh atau enemy
        int sizePlayer = 50;
        int sizeEnemy = 30;
        
        // menentukan sisi (batas) dari player
        int playerLeft = player.x;                  // nilai sisi kiri
        int playerRight = player.x + sizePlayer;    // nilai sisi kanan
        int playerTop = player.y;                   // nilai sisi atas
        int playerBottom = player.y + sizePlayer;   // nilai sisi bawah
        
        // menentukan sisi (batas) dari enemy
        int enemyLeft = enemy.x;              // nilai sisi kiri
        int enemyRight = enemy.x + sizeEnemy;  // nilai sisi kanan
        int enemyTop = enemy.y;               // nilai sisi atas
        int enemyBottom = enemy.y + sizeEnemy; // nilai sisi bawah

        // jika posisinya memakan
        if((playerRight > enemyLeft ) &&
        (playerLeft < enemyRight) &&
        (enemyBottom > playerTop) &&
        (enemyTop < playerBottom)
        ){
            // set nilai result sebagai true
            result = true;
        }
        // mengembalikan nilai result
        return result;
    }
    
    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        
        Graphics g = bs.getDrawGraphics();
        
        // mengeset warna background
        g.setColor(Color.decode("#F1f3f3"));
        // mengeset width dan height dari background
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        // jika game sudah dimulai
        if(gameState ==  STATE.Game){
            handler.render(g);
            
            // mengeset ukuran font
            Font currentFont = g.getFont();
            Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.4F);
            g.setFont(newFont);
            
            // membuat tulisan yang menampilkan nilai score
            g.setColor(Color.BLACK);
            g.drawString("Score: " +Integer.toString(score), 20, 20);
            
            // membuat tulisan yang menampilkan sisa waktu
            g.setColor(Color.BLACK);
            g.drawString("Time: " +Integer.toString(time), WIDTH-120, 20);
        }
        // jika game telah berakhir
        else{
           this.countTotalScore = this.score + this.countTotalWaktu;
            
            // mengeset ukuran font
            Font currentFont = g.getFont();
            Font newFont = currentFont.deriveFont(currentFont.getSize() * 3F);
            g.setFont(newFont);
            // membuat tulisan game over
            g.setColor(Color.BLACK);
            g.drawString("GAME OVER", WIDTH/2 - 120, HEIGHT/2 - 30);
            
            // mengeset ukuran font
            currentFont = g.getFont();
            Font newScoreFont = currentFont.deriveFont(currentFont.getSize() * 0.5F);
            g.setFont(newScoreFont);
            // membuat tulisan score
            g.setColor(Color.BLACK);
            g.drawString("Score: " +Integer.toString(this.countTotalScore), WIDTH/2 - 50, HEIGHT/2 - 10);
            
            // membuat tulisan press space to continue
            g.setColor(Color.BLACK);
            g.drawString("Press Space to Continue", WIDTH/2 - 100, HEIGHT/2 + 30);
        }
        g.dispose();
        bs.show();
    }
    
    public static int clamp(int var, int min, int max){
        if(var >= max){
            return var = max;
        }else if(var <= min){
            return var = min;
        }else{
            return var;
        }
    }
    
    // method untuk menutup windows
    public void close(){
        window.CloseWindow();
    }
    
    // method untuk mengeluarkan suara
    public void playSound(String filename){
        try {
            // Open an audio input stream.
            URL url = this.getClass().getResource(filename);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
           e.printStackTrace();
        } catch (IOException e) {
           e.printStackTrace();
        } catch (LineUnavailableException e) {
           e.printStackTrace();
        }
    }
    
    // method untuk mengisi username ke dalam database
    public void insert(){
        // set doUpdate sebagai 0 atau false terlebih dahulu,
        // yg berarti bahwa tidak akan dilakukan update
        int doUpdate = 0;
        try {
            // read data dari database
            ResultSet rs = conn.readData();
            // sebanyak jumlah data yg ada di database, lakukan:
            while(rs.next()){
                // tampung username user dari database
                String uname = rs.getString(1);
                // tampung score user dari database
                int scr = rs.getInt(2);
                // jika username yg ada di database SAMA dgn username yg baru dimasukkan user melalui input
                if(this.username.equals(uname)) {
                    // jika total score yg baru diperoleh username dalam game lebih besar dri score username tsb yg pernah ada di database
                    if(this.countTotalScore > scr) {
                        // set nilai doUpdate sebagai 1 atau true dilakukan update
                        doUpdate = 1;
                        // kalau sudah ketemu, artinya langsung di-break dari iterasi
                        break;                       
                    }
                    // jika total score yg baru diperoleh username dalam game kurang dari atau sama dgn score username tsb yg pernah ada di database
                    else {
                        // set nilai doUpdate sebagai 2 alias tidak perlu dilakukan update
                        doUpdate = 2;
                        // keluar dari iterasi
                        break;
                    }
                }
            }
            
            // jika perlu dilakukan update
            if(doUpdate == 1) {
                // maka panggil prosedur update data untuk langsung mengupdate data ke database
               conn.updateData(this.username, this.countTotalScore, this.countTotalWaktu); 
            } 
            // jika TIDAK perlu dilakukan update
            else if(doUpdate == 0) {
                // maka panggil prosedur insert data untuk langsung menambahkan data ke database
               conn.insertData(this.username, this.countTotalScore, this.countTotalWaktu);
            }
        } catch(Exception e){
            // print error jika gagal melakukan insert Data
            System.err.println("gagal melakukan insert data " +e.getMessage());
        }
    }
}
