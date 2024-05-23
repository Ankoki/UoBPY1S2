package us.byeol.uobpyonestwo.breakout.game.enums;

public enum GameOptions {

    EASY(true, true),
    HARD(false, true),
    TWO_PLAYER_EASY(true, false),
    TWO_PLAYER_HARD(false, false);

    private final boolean easy;
    private final boolean singlePlayer;

    /**
     * Creates a new game option with the associated booleans.
     *
     * @param easy true if easy.
     * @param singlePlayer true if single-player.
     */
    GameOptions(boolean easy, boolean singlePlayer) {
        this.easy = easy;
        this.singlePlayer = singlePlayer;
    }

    /**
     * Whether this game option is easy.
     *
     * @return true if easy.
     */
    public boolean isEasy() {
        return easy;
    }

    /**
     * Whether this game option is single-player.
     *
     * @return true if single-player.
     */
    public boolean isSinglePlayer() {
        return singlePlayer;
    }

}
