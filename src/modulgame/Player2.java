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
 * @author REPUBLIC OF GAMERS
 */
public class Player2 extends GameObject{
   // konstruktor Player2
    public Player2(int x, int y, ID id){
        // menggunakan constructor orangtua
        super(x, y, id);
        
        //speed = 1;
    }

    @Override
    public void tick() {
        // selalu update posisi koordinat x dan y player sesuai arah pergerakannya
        x += vel_x;
        y += vel_y;
        
        x = Game.clamp(x, 0, Game.WIDTH - 60);
        y = Game.clamp(y, 0, Game.HEIGHT - 80);
    }

    @Override
    public void render(Graphics g) {
        // set warna menjadi biru muda
        g.setColor(Color.decode("#518cc9"));
        // set posisi x dan y objek, serta set width dan length nya
        g.fillRect(x, y, 50, 50);
    }
}
