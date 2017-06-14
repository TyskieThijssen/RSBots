package scripts.BarbFishNCook;

import org.powerbot.script.Tile;

/**
 * Created by Thijs on 12-6-2017.
 */
public class MyConstants {
    /**
     * Fishing spot ID's
     */
    public static final int[] FISHING_SPOT_IDS = {1526};

    /**
     * Fish ID's
     */
    public static final int RAW_SALMON_ID = 331;
    public static final int COOKED_SALMON_ID = 329;
    public static final int RAW_TROUT_ID = 335;
    public static final int COOKED_TROUT_ID = 333;

    public static final int[] RAW_FISH_IDS = {RAW_SALMON_ID, RAW_TROUT_ID};
    public static final int[] COOKED_FISH_IDS = {COOKED_SALMON_ID, COOKED_TROUT_ID};

    /**
     * Fire ID's
     */
    public static final int FIRE_ID = 26185;

    /**
     * Fishing Supplies ID's
     */
    public static final int[] FISHING_SUPPLIES_IDS = {309, 314};

    /**
     * Paths
     */
    //fire to bank 1
    public static final Tile[] FIRE_TO_BANK = {new Tile(3105, 3433, 0), new Tile(3102, 3437, 0), new Tile(3097, 3441, 0), new Tile(3093, 3443, 0), new Tile(3091, 3447, 0), new Tile(3093, 3451, 0), new Tile(3093, 3456, 0), new Tile(3090, 3459, 0), new Tile(3088, 3463, 0), new Tile(3092, 3465, 0), new Tile(3096, 3465, 0), new Tile(3098, 3469, 0), new Tile(3098, 3473, 0), new Tile(3098, 3477, 0), new Tile(3098, 3481, 0), new Tile(3094, 3483, 0), new Tile(3091, 3486, 0), new Tile(3090, 3490, 0), new Tile(3094, 3491, 0)};

    //fishing spot to bank
    public static final Tile[] FISHING_TO_BANK = {new Tile(3109, 3433, 0), new Tile(3106, 3433, 0), new Tile(3103, 3433, 0), new Tile(3100, 3434, 0), new Tile(3097, 3436, 0), new Tile(3094, 3439, 0), new Tile(3094, 3442, 0), new Tile(3091, 3445, 0), new Tile(3091, 3448, 0), new Tile(3093, 3451, 0), new Tile(3093, 3454, 0), new Tile(3092, 3457, 0), new Tile(3089, 3460, 0), new Tile(3088, 3463, 0), new Tile(3091, 3465, 0), new Tile(3094, 3465, 0), new Tile(3097, 3465, 0), new Tile(3098, 3468, 0), new Tile(3098, 3471, 0), new Tile(3098, 3474, 0), new Tile(3098, 3477, 0), new Tile(3098, 3480, 0), new Tile(3098, 3483, 0), new Tile(3095, 3483, 0), new Tile(3092, 3485, 0), new Tile(3090, 3488, 0), new Tile(3093, 3490, 0)};

}
