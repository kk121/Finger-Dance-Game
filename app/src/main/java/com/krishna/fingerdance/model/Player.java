package com.krishna.fingerdance.model;

import com.krishna.fingerdance.view.TileView;

/**
 * Created by krishna on 06/02/17.
 */

public class Player {
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;

    public String name;
    public int row;
    public int column;
    public int score;
    public TileView currentlySelectedTile;

    public Player(String player) {
        name = player;
    }
}
