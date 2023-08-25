package com.neck_flexed.scripts.slayer;

import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;

import static com.neck_flexed.scripts.slayer.barrage.areas.ABYSSAL_DEMON;
import static com.neck_flexed.scripts.slayer.barrage.areas.NECH;

public class areas {
    public static Area Spectres = new Area.Rectangular(new Coordinate(2439, 9797, 0), new Coordinate(2474, 9771, 0));
    public static Area AbyssalDemons = ABYSSAL_DEMON;
    public static Area Nechryael = NECH;
    public static Area BlackDemon = new Area.Rectangular(new Coordinate(1714, 10095, 0), new Coordinate(1725, 10077, 0));
    public static Area Bloodveld = new Area.Rectangular(new Coordinate(1683, 10022, 0), new Coordinate(1698, 10007, 0));
    public static Area Bloodveld_Meiye = new Area.Rectangular(new Coordinate(3584, 9756, 0), new Coordinate(3608, 9728, 0));
    public static Area DustDevil = new Area.Rectangular(new Coordinate(1707, 10037, 0), new Coordinate(1721, 10025, 0));
    public static Area GreaterDemon = new Area.Rectangular(new Coordinate(1682, 10106, 0), new Coordinate(1725, 10092, 0));
    public static Area Hellhound = new Area.Polygonal(
            new Coordinate(1641, 10075, 0),
            new Coordinate(1641, 10073, 0),
            new Coordinate(1642, 10070, 0),
            new Coordinate(1640, 10065, 0),
            new Coordinate(1637, 10065, 0),
            new Coordinate(1636, 10059, 0),
            new Coordinate(1639, 10055, 0),
            new Coordinate(1641, 10055, 0),
            new Coordinate(1651, 10056, 0),
            new Coordinate(1654, 10060, 0),
            new Coordinate(1654, 10067, 0),
            new Coordinate(1644, 10076, 0)
    );
    public static Area FireGiant = new Area.Rectangular(new Coordinate(1469, 9884, 0), new Coordinate(1446, 9917, 0));
    public static Area Dagannoth = new Area.Polygonal(
            new Coordinate(1674, 10006, 0),
            new Coordinate(1667, 10006, 0),
            new Coordinate(1657, 10004, 0),
            new Coordinate(1657, 10000, 0),
            new Coordinate(1653, 9998, 0),
            new Coordinate(1653, 9995, 0),
            new Coordinate(1657, 9994, 0),
            new Coordinate(1660, 9993, 0),
            new Coordinate(1662, 9993, 0),
            new Coordinate(1661, 9989, 0),
            new Coordinate(1663, 9987, 0),
            new Coordinate(1666, 9987, 0),
            new Coordinate(1676, 9992, 0),
            new Coordinate(1681, 9994, 0),
            new Coordinate(1679, 10001, 0)
    );
    public static Area Ankou = new Area.Polygonal(
            new Coordinate(1648, 10000, 0),
            new Coordinate(1647, 10001, 0),
            new Coordinate(1643, 10001, 0),
            new Coordinate(1637, 9999, 0),
            new Coordinate(1633, 9998, 0),
            new Coordinate(1633, 9990, 0),
            new Coordinate(1636, 9988, 0),
            new Coordinate(1639, 9988, 0),
            new Coordinate(1643, 9989, 0),
            new Coordinate(1645, 9991, 0),
            new Coordinate(1647, 9994, 0),
            new Coordinate(1649, 9994, 0),
            new Coordinate(1650, 9995, 0),
            new Coordinate(1651, 9995, 0),
            new Coordinate(1652, 9998, 0),
            new Coordinate(1650, 10000, 0)
    );

    public static Area Banshees = new Area.Rectangular(new Coordinate(3429, 3563, 0), new Coordinate(3454, 3531, 0));
    public static Area Bats = new Area.Polygonal(
            new Coordinate(3334, 3485, 0),
            new Coordinate(3334, 3471, 0),
            new Coordinate(3347, 3470, 0),
            new Coordinate(3357, 3481, 0),
            new Coordinate(3378, 3471, 0),
            new Coordinate(3375, 3478, 0),
            new Coordinate(3372, 3490, 0),
            new Coordinate(3363, 3496, 0),
            new Coordinate(3347, 3499, 0)
    );
    public static Area Bears = new Area.Rectangular(new Coordinate(2688, 3319, 0), new Coordinate(2716, 3354, 0));
    public static Area Birds = new Area.Rectangular(new Coordinate(3163, 3362, 0), new Coordinate(3184, 3351, 0));
    public static Area CaveBugs = new Area.Rectangular(new Coordinate(2709, 5243, 0), new Coordinate(2719, 5223, 0));
    public static Area CaveCrawlers = new Area.Rectangular(new Coordinate(2780, 10005, 0), new Coordinate(2809, 9989, 0));
    public static Area CaveSlime = new Area.Rectangular(new Coordinate(2722, 5239, 0), new Coordinate(2736, 5231, 0));
    public static Area Cows = new Area.Rectangular(new Coordinate(3247, 3291, 0), new Coordinate(3265, 3255, 0));
    public static Area CrawlingHands = new Area.Polygonal(
            new Coordinate(3406, 3575, 0),
            new Coordinate(3409, 3578, 0),
            new Coordinate(3412, 3578, 0),
            new Coordinate(3415, 3576, 0),
            new Coordinate(3419, 3576, 0),
            new Coordinate(3420, 3577, 0),
            new Coordinate(3423, 3577, 0),
            new Coordinate(3425, 3576, 0),
            new Coordinate(3425, 3562, 0),
            new Coordinate(3416, 3562, 0),
            new Coordinate(3416, 3568, 0),
            new Coordinate(3409, 3568, 0),
            new Coordinate(3406, 3571, 0)
    );
    public static Area Dogs = new Area.Polygonal(
            new Coordinate(3377, 2955, 0),
            new Coordinate(3365, 2947, 0),
            new Coordinate(3359, 2922, 0),
            new Coordinate(3340, 2921, 0),
            new Coordinate(3342, 2880, 0),
            new Coordinate(3392, 2881, 0),
            new Coordinate(3391, 2955, 0)
    );
    public static Area Dwarves = new Area.Rectangular(new Coordinate(2859, 9882, 0), new Coordinate(2871, 9869, 0));
    public static Area Ghosts = new Area.Rectangular(new Coordinate(1684, 10068, 0), new Coordinate(1701, 10057, 0));
    public static Area Goblins = new Area.Rectangular(new Coordinate(3138, 3310, 0), new Coordinate(3152, 3294, 0));
    public static Area Icefiends = new Area.Rectangular(new Coordinate(2998, 3487, 0), new Coordinate(3017, 3466, 0));
    public static Area Kalphite_Soldier = new Area.Rectangular(new Coordinate(3296, 9543, 0), new Coordinate(3320, 9514, 0));
    public static Area Kalphites = new Area.Polygonal(
            new Coordinate(3314, 9500, 0),
            new Coordinate(3315, 9504, 0),
            new Coordinate(3318, 9515, 0),
            new Coordinate(3328, 9515, 0),
            new Coordinate(3336, 9507, 0),
            new Coordinate(3335, 9500, 0),
            new Coordinate(3328, 9493, 0),
            new Coordinate(3323, 9490, 0),
            new Coordinate(3315, 9494, 0)
    );
    public static Area Lizards = new Area.Polygonal(
            new Coordinate(3382, 3081, 0),
            new Coordinate(3376, 3065, 0),
            new Coordinate(3392, 3036, 0),
            new Coordinate(3371, 3016, 0),
            new Coordinate(3375, 3009, 0),
            new Coordinate(3434, 3008, 0),
            new Coordinate(3430, 3081, 0)
    );
    public static Area Minotaurs = new Area.Polygonal(
            new Coordinate(1870, 5217, 0),
            new Coordinate(1870, 5218, 0),
            new Coordinate(1873, 5223, 0),
            new Coordinate(1877, 5222, 0),
            new Coordinate(1880, 5222, 0),
            new Coordinate(1883, 5221, 0),
            new Coordinate(1883, 5215, 0),
            new Coordinate(1876, 5208, 0),
            new Coordinate(1873, 5208, 0)
    );
    public static Area Monkeys = new Area.Rectangular(new Coordinate(2786, 3006, 0), new Coordinate(2808, 2987, 0));
    public static Area Rats = new Area.Rectangular(new Coordinate(3228, 9872, 0), new Coordinate(3252, 9862, 0));
    public static Area Scorpions = new Area.Rectangular(new Coordinate(3295, 3314, 0), new Coordinate(3304, 3276, 0));
    public static Area Skeletons = new Area.Rectangular(new Coordinate(3358, 9772, 0), new Coordinate(3391, 9735, 0));
    public static Area Sourhogs = new Area.Rectangular(new Coordinate(3150, 9704, 0), new Coordinate(3184, 9670, 0));
    public static Area Spiders = new Area.Rectangular(new Coordinate(3153, 3260, 0), new Coordinate(3180, 3235, 0));
    public static Area Wolves = new Area.Rectangular(new Coordinate(2835, 3509, 0), new Coordinate(2862, 3488, 0));
    public static Area Zombies = new Area.Rectangular(new Coordinate(3609, 3531, 0), new Coordinate(3633, 3524, 0));
    public static Area Kalphites_Lair = new Area.Rectangular(new Coordinate(3479, 9532, 2), new Coordinate(3516, 9507, 2));
    public static Area Kurask = new Area.Rectangular(new Coordinate(2688, 10008, 0), new Coordinate(2708, 9990, 0));
    public static Area Suqah = new Area.Rectangular(new Coordinate(2108, 3951, 0), new Coordinate(2142, 3930, 0));
    public static Area IceTroll = new Area.Rectangular(new Coordinate(2389, 3875, 0), new Coordinate(2422, 3846, 0));
    public static Area Troll = new Area.Rectangular(new Coordinate(1221, 3534, 0), new Coordinate(1255, 3505, 0));
    public static Area Turoth = new Area.Rectangular(new Coordinate(2710, 10015, 0), new Coordinate(2733, 9988, 0));
    public static Area BabyBlackDragon = new Area.Rectangular(new Coordinate(2866, 9823, 1), new Coordinate(2851, 9830, 1));
    public static Area BabyBlueDragon = new Area.Polygonal(
            new Coordinate(2890, 9777, 0),
            new Coordinate(2900, 9777, 0),
            new Coordinate(2907, 9763, 0),
            new Coordinate(2892, 9759, 0),
            new Coordinate(2882, 9769, 0)
    );
    public static Area Gargoyle = new Area.Rectangular(new Coordinate(3421, 9954, 3), new Coordinate(3450, 9924, 3));
    public static Area AdamantDragons = new Area.Rectangular(new Coordinate(1537, 5087, 0), new Coordinate(1561, 5062, 0));
    public static Area RuneDragons = new Area.Rectangular(new Coordinate(1574, 5086, 0), new Coordinate(1597, 5062, 0));
    public static Area SteelDragons = new Area.Rectangular(new Coordinate(1600, 10062, 0), new Coordinate(1614, 10047, 0));
    public static Area IronDragons = new Area.Rectangular(new Coordinate(1657, 10096, 0), new Coordinate(1671, 10078, 0));
    public static Area FossilWyverns = new Area.Polygonal(
            new Coordinate(3596, 10276, 0),
            new Coordinate(3612, 10292, 0),
            new Coordinate(3626, 10278, 0),
            new Coordinate(3625, 10257, 0),
            new Coordinate(3600, 10262, 0)
    );
    public static Area SmokeDevil = new Area.Rectangular(new Coordinate(2382, 9461, 0), new Coordinate(2422, 9423, 0));
    public static Area SkeletalWyverns = new Area.Rectangular(new Coordinate(3022, 9559, 0), new Coordinate(3041, 9536, 0));
    public static Area Wyrm = new Area.Rectangular(new Coordinate(1254, 10203, 0), new Coordinate(1289, 10174, 0));
    public static Area Drake = new Area.Rectangular(new Coordinate(1292, 10260, 1), new Coordinate(1364, 10217, 1));
    public static Area DarkBeast = new Area.Rectangular(new Coordinate(1984, 4668, 0), new Coordinate(2016, 4631, 0));
    public static Area Lizardmen = new Area.Rectangular(new Coordinate(1287, 10101, 0), new Coordinate(1296, 10093, 0));
    public static Area Dagannoth_Lighthouse = new Area.Rectangular(new Coordinate(2495, 10040, 0), new Coordinate(2544, 10007, 0));
    public static Area CaveHorrors = new Area.Rectangular(new Coordinate(3714, 9469, 0), new Coordinate(3774, 9345, 0));
    public static Area Basilisk = new Area.Rectangular(new Coordinate(2733, 10023, 0), new Coordinate(2751, 9989, 0));
    public static Area BasiliskKnight = new Area.Rectangular(new Coordinate(2444, 10405, 0), new Coordinate(2398, 10370, 0));
    public static Area MithrilDragon = new Area.Rectangular(new Coordinate(1749, 5360, 1), new Coordinate(1789, 5323, 1));
    public static Area Waterfiend = new Area.Rectangular(new Coordinate(2279, 9997, 0), new Coordinate(2289, 9986, 0));
    public static Area Vampyre = new Area.Rectangular(new Coordinate(3585, 3366, 0), new Coordinate(3617, 3329, 0));
    public static Area Zygomite = new Area.Rectangular(new Coordinate(2407, 4476, 0), new Coordinate(2421, 4457, 0));
    public static Area Kurask_Iowerth = new Area.Polygonal(
            new Coordinate(3195, 12362, 0),
            new Coordinate(3198, 12386, 0),
            new Coordinate(3240, 12379, 0),
            new Coordinate(3246, 12398, 0),
            new Coordinate(3268, 12396, 0),
            new Coordinate(3259, 12340, 0)
    );
    public static Area Kraken = new Area.Rectangular(new Coordinate(2240, 10047, 0), new Coordinate(2303, 9984, 0));
    public static Area Elves_Llyetya = new Area.Rectangular(new Coordinate(2316, 3193, 0), new Coordinate(2358, 3147, 0));
}
