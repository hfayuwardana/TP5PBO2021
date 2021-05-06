/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulgame;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Fauzan
 */
// kelas Items
public class Items extends GameObject {
    // konstruktor Items
    public Items(int x, int y, ID id){
        // menggunakan constructor orangtua
        super(x, y, id);
       
        //speed = 1;
    }

    @Override
    public void tick() {
        // dikosongkan, karena items tidak memiliki pergerakan
    }

    @Override
    public void render(Graphics g) {
        // set warna menjadi kuning
        g.setColor(Color.decode("#f5c542"));
        // set posisi x dan y objek, serta set width dan length nya
        g.fillRect(x, y, 20, 20);
    }
    
}
