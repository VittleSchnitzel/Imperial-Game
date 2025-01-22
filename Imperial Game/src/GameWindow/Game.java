package GameWindow;

import Map.*;
import GameWindow.GameWindow;
import Entity.*;

/**
 * General, overarching class that responsible for the running of the game
 * other processes, like map generation and graphics, are kept in lower
 * level classes
 */
public class Game {
    public Map map;
    public GameWindow gameWindow;
    Paiods<Agent> agents;
    Paiods<Structure> structures;
    Paiods<Resource> resources;
    Paiods<Projectile> projectiles;

    public Game(Crd mapSize) {
        map = new Map(mapSize);
        map.test();

    }


}
