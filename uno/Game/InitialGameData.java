package uno.Game;

import java.io.Serializable;

public class InitialGameData implements Serializable
{
    public Game game;

    InitialGameData(Game game)
    {
        this.game = game;
    }
}
