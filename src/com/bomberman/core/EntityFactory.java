package com.bomberman.core;

import com.bomberman.entities.Bomb;
import com.bomberman.entities.Enemy;
import com.bomberman.entities.GameObject;
import com.bomberman.entities.Wall;

public class EntityFactory {

    private EntityFactory() {

    }
    public static GameObject createWall(int x, int y, boolean hard) {
        return new Wall(x, y, !hard);
    }

    public static Enemy createEnemy(int x, int y) {
        return new Enemy(x, y);
    }
    
    public static Bomb createBomb(int x, int y, int radius) {
        return new Bomb(x, y, radius);
    }
}
