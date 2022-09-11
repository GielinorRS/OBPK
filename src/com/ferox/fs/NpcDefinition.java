package com.ferox.fs;

import com.ferox.game.GameConstants;
import com.ferox.game.world.entity.mob.npc.pets.Pet;
import com.ferox.io.RSBuffer;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.CustomNpcIdentifiers;
import com.ferox.util.NpcIdentifiers;
import io.netty.buffer.Unpooled;
import nl.bartpelle.dawnguard.DataStore;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import java.util.*;

import static com.ferox.util.CustomNpcIdentifiers.*;
import static com.ferox.util.NpcIdentifiers.*;

/**
 * Created by Bart Pelle on 10/4/2014.
 */
public class NpcDefinition implements Definition {

    public int getOption(String... searchOptions) {
        if (options != null) {
            for (String s : searchOptions) {
                for (int i = 0; i < options.length; i++) {
                    String option = options[i];
                    if (s.equalsIgnoreCase(option))
                        return i + 1;
                }
            }
        }
        return -1;
    }

    public int[] models;
    public String name = null;
    public int size = 1;
    public int idleAnimation = -1;
    public int walkAnimation = -1;
    public int render3 = -1;
    public int render4 = -1;
    public int render5 = -1;
    public int render6 = -1;
    public int render7 = -1;
    short[] recol_s;
    short[] recol_d;
    short[] retex_s;
    short[] retex_d;
    int[] anIntArray2224;
    public boolean mapdot = true;
    public int combatlevel = -1;
    int width = -1;
    int height = -1;
    public boolean render = false;
    int anInt2242 = 0;
    int contrast = 0;
    public int headIcon = -1;
    public int turnValue = -1;
    int varbit = -1;
    public boolean rightclick = true;
    int varp = -1;
    public boolean aBool2227 = true;
    public int[] altForms;
    public boolean ispet = false;
    public int anInt2252 = -1;
    public String[] options = new String[5];
    public Map<Integer, Object> clientScriptData;
    public int id;

    public static void main(String[] args) throws Exception {
        DataStore ds = new DataStore("./data/filestore/");
        System.out.println(discoverNPCAnims(ds, 3727, false));
    }

    private static List<Integer> discoverNPCAnims(DataStore store, int id, boolean debug) {
        NpcDefinition npcdef = new NpcDefinition(id, store.getIndex(2).getContainer(9).getFileData(id, true, true));
        int animId = npcdef.idleAnimation;
        if (debug) System.out.println("Beginning discovery for " + npcdef.name + ".");
        if (debug) System.out.print("Using stand animation to grab kinematic set... ");
        if (debug) System.out.println(animId);
        AnimationDefinition stand = new AnimationDefinition(animId, store.getIndex(2).getContainer(12).getFileData(animId, true, true));
        if (debug) System.out.print("Finding skin set... ");
        int set = stand.skeletonSets[0] >> 16;
        if (debug) System.out.println(set);
        if (debug) System.out.println("Using that set to find related animations...");
        int skin = AnimationSkeletonSet.get(store, set).loadedSkins.keySet().iterator().next();

        if (skin == 0) {
            return new ArrayList<>(0);
        }

        List<Integer> work = new LinkedList<>();
        for (int i = 0; i < 30000; i++) {
            AnimationDefinition a = new AnimationDefinition(i, store.getIndex(2).getContainer(12).getFileData(i, true, true));
            int skel = a.skeletonSets[0] >> 16;
            try {
                AnimationSkeletonSet sett = AnimationSkeletonSet.get(store, skel);
                if (sett.loadedSkins.keySet().contains(skin)) {
                    work.add(i);
                    //System.out.println("Animation #" + i + " uses player kinematic set.");
                }
                //System.out.println(skel);
            } catch (Exception e) {

            }
        }

        if (debug) System.out.println("Found a total of " + work.size() + " animations: " + work);
        return work;
    }

    private static final int[] GWD_ROOM_NPCIDS = new int[]{
        3165, 3163, 3164, 3162,
        2215, 2216, 2217, 2218,
        3129, 3130, 3132, 3131,
        2206, 2207, 2208, 2205
    };

    public boolean gwdRoomNpc;
    public boolean inferno;
    public boolean roomBoss;

    public NpcDefinition(int id, byte[] data) {
        this.id = id;

        if (data != null && data.length > 0)
            decode(new RSBuffer(Unpooled.wrappedBuffer(data)));
        custom();

        gwdRoomNpc = ArrayUtils.contains(GWD_ROOM_NPCIDS, id);
        inferno = id >= 7677 && id <= 7710;
        roomBoss = name != null && ((id >= 2042 && id <= 2044 || inferno) || gwdRoomNpc);
    }

    void decode(RSBuffer buffer) {
        while (true) {
            int op = buffer.readUByte();
            if (op == 0)
                break;
            decode(buffer, op);
        }
    }

    void custom() {
        for (Pet pet : Pet.values()) {
            if (id == pet.npc) {
                ispet = true;
                size = 1;
                break;
            }
        }

        if(id == 7817) {
            name = "Lava beast";
            combatlevel = 435;//exact combat here
            walkAnimation = 7675;
            options = new String[]{null, "Attack", null, null, null, null};
        }
        if(id == 15031) {
            name = "Ice demon";
            combatlevel = 230;//exact combat here
            options = new String[]{null, "Attack", null, null, null, null};
        }
        if (id == 7632) {
            name = "Men in black";
            combatlevel = 80;
            options = new String[]{null, "Attack", null, null, null, null};
            size = 1;
        }
        if (id == NAGINI) {
            name = "Nagini";
        }
        if (id == LORD_VOLDEMORT) {
            name = "Lord voldemort";
            combatlevel = 1433;
        }
        if (id == SKELETON_HELLHOUND_PET) {
            name = "Skeleton hellhound pet";
        }
        if (id == 15021) {
            name = "Grim";
            size = 3;
            combatlevel = 1322;
        }
        if (id == BLOOD_REAPER) {
            name = "Blood Reaper";
        }
        if (id == SNOWBIRD) {
            name = "Snowbird";
        }
        if (id == ELVARG_JR) {
            name = "Baby Elvarg";
        }

        if (id == LAVA_BABY) {
            name = "Lava beast pet";
        }

        if (id == CustomNpcIdentifiers.DERANGED_ARCHAEOLOGIST) {
            name = "Deranged archaeologist";
        }

        if (id == SERENIC) {
            name = "Memory of seren";
        }

        if (id == 15030) {
            name = "Male centaur";
            size = 2;
        }
        if (id == 15032) {
            name = "Female centaur";
            size = 2;
        }
        if (id == 15028) {
            name = "Dementor";
            size = 1;
        }
        if (id == 15020) {
            name = "Aragog";
            size = 4;
        }
        if (id == 15026) {
            name = "Fluffy";
            size = 5;
        }
        if (id == 15034) {
            name = "Hungarian horntail";
            size = 4;
        }
        if (id == 15050) {
            name = "Fenrir greyback";
            size = 1;
        }
        if (id == 15019 || id == 15016) {
            name = "Brutal lava dragon";
            size = 4;
        }
        if (id == 11113) {
            name = "El Fuego";
            size = 4;
        }
        if (id == 9340) {
            name = "Zriawk";
        }
        if (id == 15040) {
            name = "Centaur male";
        }
        if (id == 15042) {
            name = "Centaur female";
        }
        if (id == 9338) {
            name = "Fluffy Jr";
        }
        if (id == 9339) {
            name = "Fenrir greyback Jr";
        }
        if (id == 15044) {
            name = "Dementor";
        }
        if (id == 15000) {
            name = "Founder imp";
        }
        if (id == 15001) {
            name = "Corrupted nechryarch";
            size = 2;
        }
        if (id == 15002) {
            name = "Corrupted nechryarch";
        }
        if (id == 15005) {
            name = "Mini necromancer";
        }
        if (id == 15008) {
            name = "Jaltok-jad";
        }
        if (id == 15017) {
            name = "Baby lava dragon";
        }
        if (id == 10981) {
            name = "Fawkes";
        }
        if(id == 4928){
            name = "Zaweks pet";
        }
        if(id == 4929){
            name = "Mending life bird";
        }
        if (id == 7315) {
            name = "Blood money";
        }
        if (id == 336) {
            name = "Elysian";
        }
        if (id == 16008) {
            name = "Kerberos";
            combatlevel = 318;
            size = 5;
        }
        if (id == 16009) {
            name = "Skorpios";
            combatlevel = 225;
            size = 5;
        }
        if (id == 16010) {
            name = "Arachne";
            combatlevel = 464;
            size = 4;
        }
        if (id == 16011) {
            name = "Artio";
            combatlevel = 470;
            size = 5;
        }
        if (id == 16000) {
            name = "Ancient revenant dark beast";
            combatlevel = 120;
            size = 3;
        }
        if (id == 16001) {
            name = "Ancient revenant ork";
            combatlevel = 105;
            size = 3;
        }
        if (id == 16002) {
            name = "Ancient revenant cyclops";
            combatlevel = 82;
            size = 3;
        }
        if (id == 16003) {
            name = "Ancient revenant dragon";
            combatlevel = 135;
            size = 5;
        }
        if (id == 16004) {
            name = "Ancient revenant knight";
            combatlevel = 126;
            size = 1;
        }
        if (id == 16005) {
            name = "Ancient barrelchest";
            combatlevel = 190;
            size = 3;
        }
        if (id == 16006) {
            name = "Ancient king black dragon";
            combatlevel = 276;
            size = 5;
        }
        if (id == 16007) {
            name = "Ancient chaos elemental";
            combatlevel = 305;
            size = 3;
        }
        if (id == 9330) {
            name = "Ancient king black dragon";
            size = 4;
        }
        if (id == 9331) {
            name = "Ancient chaos elemental";
            size = 3;
        }
        if (id == 9332) {
            name = "Ancient barrelchest";
            size = 5;
        }
        if (id == 7370) {
            name = "Blood firebird";
            size = 1;
        }
        if (id == 13000 || id == 13001) {
            name = "Pure bot";
            size = 1;
            combatlevel = 80;
        }
        if (id == 13002 || id == 13003) {
            name = "F2p bot";
            size = 1;
            combatlevel = 68;
        }

        if (id == 13004) {
            name = "Maxed bot";
            size = 1;
            combatlevel = 126;
        }

        if (id == 13005) {
            name = "Maxed bot";
            size = 1;
            combatlevel = 126;
        }

        if (id == 13006) {
            name = "Archer bot";
            size = 1;
            combatlevel = 90;
        }

        if (id == 13008 || id == 13009) {
            name = "Pure Archer bot";
            size = 1;
            combatlevel = 80;
        }
        if (id == 15035) {
            name = "Kerberos";
        }
        if (id == 15036) {
            name = "Skorpios";
        }
        if (id == 15037) {
            name = "Arachne";
        }
        if (id == 15038) {
            name = "Artio";
        }
        if (id == 9413) {
            name = "Referral Manager";
        }
        if (id == THORODIN_5526) {
            name = "Boss slayer master";
            options = new String[]{"Talk-to", null, "Slayer-Equipment", "Slayer-Rewards", null};
        } else if (id == 3358) {
            name = "Ket'ian";
            combatlevel = 420;
            width *= 2;
            height *= 2;
            size = 2;
        } else if (id == 3329) {
            name = "Sapphires Champion";
            combatlevel = 600;
            width *= 2;
            height *= 2;
            size = 2;
        } else if (id == 3142) {
            name = "Aragog";
            options = new String[]{null, "Attack", null, null, null};
            combatlevel = 1123;
            models = new int[]{28294, 28295};
            width = 190;
            height = 190;
            idleAnimation = 5318;
            size = 4;
            walkAnimation = 5317;
        } else if (id == TWIGGY_OKORN) {
            options = new String[]{"Talk-to", null, "Rewards", "Claim-cape", null};
        } else if (id == FANCY_DAN) {
            name = "Vote Manager";
            options[0] = "Trade";
            options[2] = "Cast-votes";
        } else if (id == WISE_OLD_MAN) {
            name = "Credit Manager";
            options[0] = "Talk-to";
            options[2] = "Open-Shop";
            options[3] = "Claim-purchases";
        } else if (id == SANTA_CLAUS) {
            name = "Santa Claus";
            options[0] = "Talk-to";
        } else if (id == TRADE_REFEREE) {
            name = "RiskZone Point Shop";
            options[0] = "Talk-to";
            options[2] = "Shop";
            options[3] = null;
            options[4] = null;
        } else if (id == EMERALD_BENEDICT) {
            name = "Tournament Point Shop";
            options[0] = "Talk-to";
            options[2] = "Bank";
            options[3] = "Shop";
            options[4] = null;
        } else if (id == SECURITY_GUARD) {
            name = "Security Advisor";
            options[0] = "Check Pin Settings";
        } else if (id == SIGMUND_THE_MERCHANT) {
            options[0] = "Buy-items";
            options[2] = "Sell-items";
            options[3] = "Sets";
            options[4] = null;
        } else if (id == GRAND_EXCHANGE_CLERK) {
            options[0] = "Exchange";
            options[2] = null;
            options[3] = null;
            options[4] = null;
        } else if (id == MAKEOVER_MAGE_1307) {
            options[0] = "Change-looks";
            options[2] = "Title-unlocks";
            options[3] = null;
            options[4] = null;
        } else if (id == FRANK) {
            name = "Shop";
            options[0] = "Untradeable";
        } else if (id == CLAUS_THE_CHEF) {
            name = "Shop";
            options[0] = "Consumable";
        } else if (id == RADIGAD_PONFIT) {
            name = "Ranged Shop";
            options = new String[]{"Weapons", null, "Armour", "Ironman", null};
        } else if (id == TRAIBORN) {
            name = "Magic Shop";
            options = new String[]{"Weapons", null, "Armour", "Ironman", null};
        } else if (id == GUNNJORN) {
            name = "Melee Shop";
            options = new String[]{"Weapons", null, "Armour", "Ironman", null};
        } else if (id == SPICE_SELLER_4579) {
            name = "Shop";
            options[0] = "Misc";
        } else if (id == LISA) {
            name = "Tournament Manager";
            options = new String[]{"Sign-up", null, "Quick-join", "Quick-spectate", null, null, null};
        } else if (id == VANNAKA) {
            name = "Task master";
            options = new String[]{"Talk-to", null, "Progress", null, null};
        } else if (id == 6481) {
            options = new String[]{"Talk-to", null, "Skillcape", null, null, null, null};
        } else if (id == 9413) {
            name = "Referral Manager";
        } else if (id == 306) {
            name = GameConstants.SERVER_NAME + " Guide";
        } else if (id == 3359) {
            combatlevel = 785;
        } else if (id == 3254) {
            options = new String[]{"Talk-to", null, "Trade", null, null};
        } else if (id == 1220) {
            name = "Wampa";
        } else if (id == 6635) {
            name = "Niffler";
        } else if (id == 4927) {
            name = "Fawkes";
        } else if (id == 3343) {
            name = "Prayer & Hitpoints Healer";
            options[0] = "Heal";
        } else if (id == 1221) {
            name = "Zilyana Jr";
        } else if (id == 1222) {
            name = "General Graardor Jr";
        } else if (id == 1223) {
            name = "Kree'arra Jr";
        } else if (id == 1224) {
            name = "K'ril Tsutsaroth Jr";
        } else if (id == 1225) {
            name = "Baby Squirt";
        } else if (id == 1182) {
            name = "Baby Barrelchest";
        } else if (id == 1228) {
            name = "Grim Reaper";
        } else if (id == 1216) {
            name = "Baby Dark Beast";
        } else if (id == 1214) {
            name = "Baby Aragog";
        } else if (id == 1213) {
            name = "Jawa";
        } else if (id == 1217) {
            name = "Dharok the Wretched";
        } else if (id == 6849) {
            name = "Genie";
        } else if (id == 1218) {
            name = "Baby Abyssal Demon";
        } else if (id == 1219) {
            name = "Zombies champion";
            models = new int[]{20949};
            idleAnimation = 5573;
            size = 1;
            walkAnimation = 5582;
            combatlevel = 0;
            mapdot = false;
            width = 63;
            height = 63;
        }

        if (ispet) {
            this.name = this.name + " pet";
        }

        switch (id) {
            case 9425 -> {
                name = "The Nightmare";
                options = new String[]{null, "Attack", null, null, null};
                anInt2242 = 15;
                combatlevel = 814;
                contrast = 100;
                models = new int[]{39182, 41454};
                idleAnimation = 8593;
                render5 = 8593;
                render6 = 8593;
                render7 = 8593;
                size = 5;
                mapdot = true;
                walkAnimation = 8592;
            }
            case 9426 -> {
                name = "The Nightmare";
                options = new String[]{null, "Attack", null, null, null};
                anInt2242 = 15;
                combatlevel = 814;
                contrast = 100;
                models = new int[]{39186, 41454};
                idleAnimation = 8593;
                render5 = 8593;
                render6 = 8593;
                render7 = 8593;
                size = 5;
                mapdot = true;
                walkAnimation = 8592;
            }
            case 9427 -> {
                name = "The Nightmare";
                options = new String[]{null, "Attack", null, null, null};
                anInt2242 = 15;
                combatlevel = 814;
                contrast = 100;
                models = new int[]{39188, 41454};
                idleAnimation = 8593;
                render5 = 8593;
                render6 = 8593;
                render7 = 8593;
                size = 5;
                mapdot = true;
                walkAnimation = 8592;
            }
            case 9428 -> {
                name = "The Nightmare";
                options = new String[]{null, "Attack", null, null, null};
                anInt2242 = 15;
                combatlevel = 814;
                contrast = 100;
                models = new int[]{39196, 41454};
                idleAnimation = 8593;
                render5 = 8593;
                render6 = 8593;
                render7 = 8593;
                size = 5;
                mapdot = true;
                walkAnimation = 8592;
            }
            case 9429 -> {
                name = "The Nightmare";
                options = new String[]{null, "Attack", null, null, null};
                anInt2242 = 15;
                combatlevel = 814;
                contrast = 100;
                models = new int[]{39185, 41454};
                idleAnimation = 8593;
                render5 = 8593;
                render6 = 8593;
                render7 = 8593;
                size = 5;
                mapdot = true;
                walkAnimation = 8592;
            }
            case 9430 -> {
                name = "The Nightmare";
                options = new String[]{null, "Attack", null, null, null};
                anInt2242 = 15;
                combatlevel = 814;
                contrast = 100;
                models = new int[]{39195, 41454};
                idleAnimation = 8593;
                render5 = 8593;
                render6 = 8593;
                render7 = 8593;
                size = 5;
                mapdot = true;
                walkAnimation = 8592;
            }
            case 9431 -> {
                name = "The Nightmare";
                options = new String[]{null, "Attack", null, null, null};
                anInt2242 = 15;
                combatlevel = 814;
                contrast = 100;
                models = new int[]{39208, 41454};
                idleAnimation = 8603;
                render5 = 8603;
                render6 = 8603;
                render7 = 8603;
                size = 5;
                mapdot = true;
                walkAnimation = 8592;
            }
            case 9432 -> {
                name = "The Nightmare";
                anInt2242 = 15;
                combatlevel = 814;
                contrast = 100;
                models = new int[]{39196, 41454};
                idleAnimation = 8613;
                render5 = 8613;
                render6 = 8613;
                render7 = 8613;
                size = 5;
                mapdot = true;
                walkAnimation = 8613;
            }
            case 9433 -> {
                name = "The Nightmare";
                anInt2242 = 15;
                combatlevel = 814;
                contrast = 100;
                models = new int[]{39196, 41454};
                idleAnimation = 8613;
                render5 = 8613;
                render6 = 8613;
                render7 = 8613;
                size = 5;
                mapdot = true;
                walkAnimation = 8613;
            }
        }
    }

    void decode(RSBuffer buffer, int code) {
        if (code == 1) {
            int numModels = buffer.readUByte();
            models = new int[numModels];

            for (int mdl = 0; mdl < numModels; mdl++) {
                models[mdl] = buffer.readUShort();
            }
        } else if (code == 2) {
            name = buffer.readString();
        } else if (code == 12) {
            size = buffer.readUByte();
        } else if (code == 13) {
            idleAnimation = buffer.readUShort();
        } else if (code == 14) {
            walkAnimation = buffer.readUShort();
        } else if (code == 15) {
            render3 = buffer.readUShort();
        } else if (code == 16) {
            render4 = buffer.readUShort();
        } else if (code == 17) {
            walkAnimation = buffer.readUShort();
            render5 = buffer.readUShort();
            render6 = buffer.readUShort();
            render7 = buffer.readUShort();
        } else if(code == 18){
            buffer.readUShort();
        } else if (code >= 30 && code < 35) {
            options[code - 30] = buffer.readString();
            if (options[code - 30].equalsIgnoreCase(null)) {
                options[code - 30] = null;
            }
        } else if (code == 40) {
            int var5 = buffer.readUByte();
            recol_s = new short[var5];
            recol_d = new short[var5];

            for (int var4 = 0; var4 < var5; var4++) {
                recol_s[var4] = (short) buffer.readUShort();
                recol_d[var4] = (short) buffer.readUShort();
            }
        } else if (code == 41) {
            int var5 = buffer.readUByte();
            retex_s = new short[var5];
            retex_d = new short[var5];

            for (int var4 = 0; var4 < var5; var4++) {
                retex_s[var4] = (short) buffer.readUShort();
                retex_d[var4] = (short) buffer.readUShort();
            }
        } else if (code == 60) {
            int var5 = buffer.readUByte();
            anIntArray2224 = new int[var5];

            for (int var4 = 0; var4 < var5; var4++) {
                anIntArray2224[var4] = buffer.readUShort();
            }
        } else if (code == 93) {
            mapdot = false;
        } else if (code == 95) {
            combatlevel = buffer.readUShort();
        } else if (code == 97) {
            width = buffer.readUShort();
        } else if (code == 98) {
            height = buffer.readUShort();
        } else if (code == 99) {
            render = true;
        } else if (code == 100) {
            anInt2242 = buffer.readByte();
        } else if (code == 101) {
            contrast = buffer.readByte();
        } else if (code == 102) {
            headIcon = buffer.readUShort();
        } else if (code == 103) {
            turnValue = buffer.readUShort();
        } else if (code == 106 || code == 118) {
            varbit = buffer.readUShort();
            if (varbit == 65535) {
                varbit = -1;
            }

            varp = buffer.readUShort();
            if (varp == 65535) {
                varp = -1;
            }

            int ending = -1;
            if (code == 118) {
                ending = buffer.readUShort();
                if (ending == 65535) {
                    ending = -1;
                }
            }

            int var5 = buffer.readUByte();
            altForms = new int[var5 + 2];

            for (int var4 = 0; var4 <= var5; var4++) {
                altForms[var4] = buffer.readUShort();
                if (altForms[var4] == 65535) {
                    altForms[var4] = -1;
                }
            }
            altForms[var5 + 1] = ending;
        } else if (code == 107) {
            rightclick = false;
        } else if (code == 109) {
            aBool2227 = false;
        } else if (code == 111) {
            ispet = true;
        } else if (code == 249) {
            int length = buffer.readUByte();
            int index;
            if (clientScriptData == null) {
                index = method32(length);
                clientScriptData = new HashMap<>(index);
            }
            for (index = 0; index < length; index++) {
                boolean stringData = buffer.readUByte() == 1;
                int key = buffer.readTriByte();
                clientScriptData.put(key, stringData ? buffer.readString() : buffer.readInt());
            }
        } else {
            throw new RuntimeException("cannot parse npc definition, missing config code: " + code);
        }
    }

    public static int method32(int var0) {
        --var0;
        var0 |= var0 >>> 1;
        var0 |= var0 >>> 2;
        var0 |= var0 >>> 4;
        var0 |= var0 >>> 8;
        var0 |= var0 >>> 16;
        return var0 + 1;
    }

    public int[] renderpairs() {
        return new int[]{idleAnimation, render7, walkAnimation, render7, render5, render6, walkAnimation};
    }

    public boolean ignoreOccupiedTiles;
    public boolean flightClipping, swimClipping;
}
