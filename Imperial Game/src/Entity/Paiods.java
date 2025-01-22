package Entity;

import Map.Crd;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

// Position And Id Organisation Data Structure
// Uses two structures to allow for searching by both position
// and id. The grid hashmap collects ids together by position
// and the directory sorts by id.
public class Paiods<E>{
    private final HashMap<Integer, ArrayList> grid;
    private final HashMap<Integer, E> directory;
    // Each grid square is 10 by 10 units
    public final static int SQ_SIZE = 10;
    public int sizeX;
    public int sizeY;

    // Helper functions that allow the Paiod to directly get
    // position and id from the E class - optional
    private Function<E, Integer> getId;
    private Function<E, Crd> getPos;

    public Paiods(int dimensionX, int dimensionY) {
        sizeX = (int) Math.ceil((1.0*dimensionX)/SQ_SIZE);
        sizeY = (int) Math.ceil((1.0*dimensionY)/SQ_SIZE);
        grid = new HashMap<Integer, ArrayList>();
        directory = new HashMap<Integer, E>();
    }

    public void setGetId(Function<E,Integer> helper) {
        getId = helper;
    }

    public void setGetPos(Function<E, Crd> helper) {
        getPos = helper;
    }

    // return Entries from the directory with the given id
    public E findId(int id) {
        return directory.get(id);
    }

    // return Entries from the directory in a given position
    public ArrayList<E> findPos(int x, int y) {
        ArrayList<Integer> ids = grid.get(conv(x,y));
        ArrayList<E> entries = new ArrayList<E>();
        for (Integer i: ids) entries.add(findId(i));
        return entries;
    }

    // return Ids from the grid in a given position
    public ArrayList<Integer> getPos(int x, int y) {
        return grid.get(conv(x,y));
    }

    // Add a new entry to the grid
    // updates the grid and the directory
    public void add(E entry, int id, int x, int y) {
        addtoGrid(id, x, y);
        directory.put(id, entry);
    }

    public void add(E entry) {
        Crd pos = getPos.apply(entry);
        int id = getId.apply(entry);
        add(entry, id, pos.x(), pos.y());
    }

    // Remove an entry from the Paiod
    public void remove(int id, int x, int y) {
        removetoGrid(id, x, y);
        directory.remove(id);
    }

    public void remove(E entry) {
        Crd pos = getPos.apply(entry);
        int id = getId.apply(entry);
        remove(id, pos.x(), pos.y());
    }

    // Safely adds an id to the grid
    private void addtoGrid(int id, int x, int y) {
        if (getPos(x,y) == null) grid.put(conv(x,y), new ArrayList<Integer>());
        getPos(x,y).add(id);
    }

    // Safely removes an id from the grid
    private void removetoGrid(int id, int x, int y) {
        getPos(x,y).remove(id);
        if (getPos(x, y).isEmpty()) grid.remove(conv(x,y));
    }

    // Converts a coordinate to its associated square
    public int conv(int x, int y) {
        int sqX = x/SQ_SIZE;
        int sqY = y/SQ_SIZE;
        return sqX*sizeY + sqY;
    }

}
