/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulgame;

import java.awt.Graphics;

/**
 *
 * @author Fauzan
 */
public abstract class GameObject {
    protected int x, y; // posisi x dan y
    protected ID id; // id object
    protected int vel_x; // kecepatan dlm sumbu x
    protected int vel_y; // kecepatan dlm sumbu y
    
    // konstruktor, dimana setiap objek pasti ada atribut x, y, dan id nya
    public GameObject(int x, int y, ID id){
        // set nilai atribut x, y, dan ID nya
        this.x = x;
        this.y = y;
        this.id = id;
    }
    
    // method abstrak untuk menentukan kecepatan
    public abstract void tick();
    // method abstrak untuk menentukan warna dan size dari objek
    public abstract void render(Graphics g);
    
    // method untuk mendapatkan nilai atribut x
    public int getX() {
        return x;
    }
    
    // method untuk mengeset nilai atribut x
    public void setX(int x) {
        this.x = x;
    }
    
    // method untuk mendapatkan nilai atribut y
    public int getY() {
        return y;
    }
    
    // method untuk mengeset nilai atribut y
    public void setY(int y) {
        this.y = y;
    }

    // method untuk mendapatkan nilai atribut ID
    public ID getId() {
        return id;
    }
    
    // method untuk mengeset nilai atribut ID
    public void setId(ID id) {
        this.id = id;
    }
    
    // method untuk mendapatkan nilai atribut kecepatan di sumbu x
    public int getVel_x() {
        return vel_x;
    }
    
    // method untuk mengeset nilai atribut kecepatan di sumbu x
    public void setVel_x(int vel_x) {
        this.vel_x = vel_x;
    }

    // method untuk mendapatkan nilai atribut kecepatan di sumbu y
    public int getVel_y() {
        return vel_y;
    }
    
    // method untuk mengeset nilai atribut kecepatan di sumbu y
    public void setVel_y(int vel_y) {
        this.vel_y = vel_y;
    }
}
