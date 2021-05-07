/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulgame;

import java.awt.Graphics;
import java.util.LinkedList;

/**
 *
 * @author Fauzan
 */
public class Handler {
    LinkedList<GameObject> object = new LinkedList<GameObject>();
    
    public void tick(){
        for(int i=0;i<object.size(); i++){
            GameObject tempObject = object.get(i);
            
            tempObject.tick();
        }
    }
    
    public void render(Graphics g){
        for(int i=0;i<object.size(); i++){
            GameObject tempObject = object.get(i);
            
            tempObject.render(g);
        }
    }
    
    // method untuk menambahkan game object
    public void addObject(GameObject object){
        this.object.add(object);
    }
    
    // method untuk menghapus game object
    public void removeObject(GameObject object){
        this.object.remove(object);
    }
}