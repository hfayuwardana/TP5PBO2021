/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulgame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import modulgame.Game.STATE;

/**
 *
 * @author Fauzan
 */
// Kelas untuk menangani inputan dari keyboard
public class KeyInput extends KeyAdapter{
    
    private Handler handler;
    Game game;
    
    // konstruktor
    public KeyInput(Handler handler, Game game){
        this.game = game;
        this.handler = handler;
    }
    
    // method jika menekan key
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        
        // jika game masih berlangsung
        if(game.gameState == STATE.Game){
            for(int i = 0;i<handler.object.size();i++){
                GameObject tempObject = handler.object.get(i);

                if(tempObject.getId() == ID.Player){
                    // jika press tombol W
                    if(key == KeyEvent.VK_W){
                        // set posisi ke atas sebanyak 5 satuan
                        tempObject.setVel_y(-5);
                    }
                    // jika press tombol S
                    if(key == KeyEvent.VK_S){
                        // set posisi ke bawah sebanyak 5 satuan
                        tempObject.setVel_y(+5);
                    }
                    // jika press tombol A
                    if(key == KeyEvent.VK_A){
                        // set posisi ke kiri sebanyak 5 satuan
                        tempObject.setVel_x(-5);
                    }
                    // jika press tombol D
                    if(key == KeyEvent.VK_D){
                        // set posisi ke kanan sebanyak 5 satuan
                        tempObject.setVel_x(+5);
                    }
                }
                if(tempObject.getId() == ID.Player2){
                    // jika press tombol panah atas
                    if(key == KeyEvent.VK_UP){
                        // set posisi ke atas sebanyak 5 satuan
                        tempObject.setVel_y(-5);
                    }
                    // jika press tombol panah bawah
                    if(key == KeyEvent.VK_DOWN){
                        // set posisi ke bawah sebanyak 5 satuan
                        tempObject.setVel_y(+5);
                    }
                    // jika press tombol panah kiri
                    if(key == KeyEvent.VK_LEFT){
                        // set posisi ke kiri sebanyak 5 satuan
                        tempObject.setVel_x(-5);
                    }
                    // jika press tombol panah kanan
                    if(key == KeyEvent.VK_RIGHT){
                        // set posisi ke kanan sebanyak 5 satuan
                        tempObject.setVel_x(+5);
                    }
                }
            }
        }
        
        // jika game over
        if(game.gameState == STATE.GameOver){
            // jika menekan tombol spasi
            if(key == KeyEvent.VK_SPACE){
                game.insert();
                // menampilkan ulang menu
                new Menu().setVisible(true);
                // menutup game
                game.close();
            }
        }
        
        // jika keluar dari game
        if(key == KeyEvent.VK_ESCAPE){
            System.exit(1);
        }   
    }
    
    // method jika melepas key
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        
        for(int i = 0;i<handler.object.size();i++){
            GameObject tempObject = handler.object.get(i);
            
            if(tempObject.getId() == ID.Player){
                // jika press tombol W
                if(key == KeyEvent.VK_W){
                    // set posisi ke atas sebanyak 0 satuan (tidak berpindah lagi)
                    tempObject.setVel_y(0);
                }
                // jika press tombol S
                if(key == KeyEvent.VK_S){
                    // set posisi ke bawah sebanyak 0 satuan (tidak berpindah lagi)
                    tempObject.setVel_y(0);
                }
                // jika press tombol A
                if(key == KeyEvent.VK_A){
                    // set posisi ke kiri sebanyak 0 satuan (tidak berpindah lagi)
                    tempObject.setVel_x(0);
                }
                // jika press tombol D
                if(key == KeyEvent.VK_D){
                    // set posisi ke kanan sebanyak 0 satuan (tidak berpindah lagi)
                    tempObject.setVel_x(0);
                }
            }
            if(tempObject.getId() == ID.Player2){
                // jika press tombol panah atas
                if(key == KeyEvent.VK_UP){
                    // set posisi ke atas sebanyak 0 satuan (tidak berpindah lagi)
                    tempObject.setVel_y(0);
                }
                // jika press tombol panah bawah
                if(key == KeyEvent.VK_DOWN){
                    // set posisi ke bawah sebanyak 0 satuan (tidak berpindah lagi)
                    tempObject.setVel_y(0);
                }
                // jika press tombol panah kiri
                if(key == KeyEvent.VK_LEFT){
                    // set posisi ke kiri sebanyak 0 satuan (tidak berpindah lagi)
                    tempObject.setVel_x(0);
                }
                // jika press tombol panah kanan
                if(key == KeyEvent.VK_RIGHT){
                    // set posisi ke kanan sebanyak 0 satuan (tidak berpindah lagi)
                    tempObject.setVel_x(0);
                }
            }
        }
    }
}
