package com.ferox.fs;

import com.ferox.game.GameConstants;
import com.ferox.io.RSBuffer;
import io.netty.buffer.Unpooled;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bart Pelle on 10/4/2014.
 */
public class ObjectDefinition implements Definition {

    public String name = "null";
    public int[] modeltypes;
    public int[] models;
    public int sizeX = 1;
    public int sizeY = 1;
    public int clipType = 2;
    public boolean tall = true; // formerly projectileClipped
    public int anInt2292 = -1;
    public int anInt2296 = -1;
    public boolean aBool2279 = false;
    public boolean aBool2280 = false;
    public int anInt2281 = -1;
    public int anInt2291 = -1;
    public int anInt2283 = 0;
    public int anInt2285 = 0;
    public short[] recol_s;
    public short[] recol_d;
    public short[] retex_s;
    public short[] retex_d;
    public int anInt2286 = -1;
    public boolean vflip = false;
    public boolean aBool2284 = true;
    public int op65Render0x1 = -1;
    public int op66Render0x2 = -1;
    public int op67Render0x4 = -1;
    public int anInt2287 = -1;
    public int anInt2307 = 0;
    public int anInt2294 = 0;
    public int anInt2295 = 0;
    public boolean aBool2264 = false;
    public boolean unclipped = false;
    public int anInt2298 = -1;
    public int varbit = -1;
    public int anInt2302 = -1;
    public int anInt2303 = 0;
    public int varp = -1;
    public int anInt2304 = 0;
    public int anInt2290 = 0;
    public int cflag = 0;
    public int[] anIntArray2306;
    public int[] to_objs;
    public String[] options = new String[5];
    public Map<Integer, Object> clientScriptData;

    public int id;
    private int anInt2167;

    public ObjectDefinition(int id, byte[] data) {
        this.id = id;

        if (data != null && data.length > 0)
            decode(new RSBuffer(Unpooled.wrappedBuffer(data)));
    }

    void decode(RSBuffer buffer) {
        while (true) {
            int op = buffer.readUByte();
            if (op == 0)
                break;
            decode(buffer, op);
        }

        if(id == 23311) {
            name = GameConstants.SERVER_NAME+ " Teleporter";
            options = new String[] {"Teleport", null, null, null, null};
        }

        if (id == 7811) {
            name = "Supplies";
            options[1] = "Blood money supplies";
            options[1] = null;
        }

        if (id == 29149) {
            options = new String[]{"Pray-at", "Ancients", "Lunar", "Modern", null};
        }

        if(id == 33020) {
            name = "Forging table";
            options = new String[] {"Forge", null, null, null, null};
        }

        if (id == 29165) {
            name = "Pile Of Coins";
            options[0] = null;
            options[1] = null;
            options[2] = null;
            options[3] = null;
            options[4] = null;
        }

        if(id == 8878) {
            name = "Item dispenser";
            options = new String[] {"Dispense", "Exchange coins", null, null, null};
        }

        if(id == 637) {
            name = "Item cart";
            options = new String[] {"Check cart", "Item list", "Clear cart", null, null};
        }

        if (id == 13291) {
            options = new String[] {"Open", null, null, null, null};
        }

        if (id == 27269) {
            name = "Wilderness key chest";
        }

        if (id == 172) {
            name = "Crystal key chest";
        }

        if (id == 173) {
            name = "Open crystal key chest";
        }

        if (id == 23709) {
            options[0] = "Use";
        }

        if (id == 2156) {
            name = "World Boss Portal";
        }

        if (id == 27780) {
            name = "Scoreboard";
        }

        if (id == 27097) {
            name = "Boss Portal";
            options[0] = "Teleport to";
            options[1] = null;
            options[2] = null;
            options[3] = null;
        }

        if (id == 14986) {
            name = "Key Chest";
        }

        if (id == 13291) {
            name = "Enchanted chest";
        }

        if(id == 11508 || id == 11509) {
            //curtain
            clipType = 0;
        }
    }

    void decode(RSBuffer buffer, int code) {
        if (code == 1) {
            int count = buffer.readUByte();
            if (count > 0) {
                modeltypes = new int[count];
                models = new int[count];

                for (int i = 0; i < count; i++) {
                    models[i] = buffer.readUShort();
                    modeltypes[i] = buffer.readUByte();
                }
            }
        } else if (code == 2) {
            name = buffer.readString();
        } else if (code == 5) {
            int num = buffer.readUByte();
            if (num > 0) {
                modeltypes = null;
                models = new int[num];

                for (int i = 0; i < num; i++) {
                    models[i] = buffer.readUShort();
                }
            }
        } else if (code == 14) {
            sizeX = buffer.readUByte();
        } else if (code == 15) {
            sizeY = buffer.readUByte();
        } else if (code == 17) {
            clipType = 0;
            tall = false;
        } else if (code == 18) {
            tall = false;
        } else if (code == 19) {
            anInt2292 = buffer.readUByte();
        } else if (code == 21) {
            anInt2296 = 0;
        } else if (code == 22) {
            aBool2279 = true;
        } else if (code == 23) {
            aBool2280 = true;
        } else if (code == 24) {
            anInt2281 = buffer.readUShort();
            if (anInt2281 == 65535) {
                anInt2281 = -1;
            }
        } else if (code == 27) {
            clipType = 1;
        } else if (code == 28) {
            anInt2291 = buffer.readUByte();
        } else if (code == 29) {
            anInt2283 = buffer.readByte();
        } else if (code == 39) {
            anInt2285 = buffer.readByte();
        } else if (code >= 30 && code < 35) {
            options[code - 30] = buffer.readString();
            if (options[code - 30].equalsIgnoreCase("null")) {
                options[code - 30] = null;
            }
        } else if (code == 40) {
            int count = buffer.readUByte();
            recol_s = new short[count];
            recol_d = new short[count];

            for (int i = 0; i < count; i++) {
                recol_s[i] = (short) buffer.readUShort();
                recol_d[i] = (short) buffer.readUShort();
            }
        } else if (code == 41) {
            int count = buffer.readUByte();
            retex_s = new short[count];
            retex_d = new short[count];

            for (int i = 0; i < count; i++) {
                retex_s[i] = (short) buffer.readUShort();
                retex_d[i] = (short) buffer.readUShort();
            }
        } else if (code == 61) {
            buffer.readUShort();
        } else if (code == 62) {
            vflip = true;
        } else if (code == 64) {
            aBool2284 = false;
        } else if (code == 65) {
            op65Render0x1 = buffer.readUShort();
        } else if (code == 66) {
            op66Render0x2 = buffer.readUShort();
        } else if (code == 67) {
            op67Render0x4 = buffer.readUShort();
        } else if (code == 68) {
            anInt2287 = buffer.readUShort();
        } else if (code == 69) {
            cflag = buffer.readUByte(); // clip access flag
        } else if (code == 70) {
            anInt2307 = buffer.readShort();
        } else if (code == 71) {
            anInt2294 = buffer.readShort();
        } else if (code == 72) {
            anInt2295 = buffer.readShort();
        } else if (code == 73) {
            aBool2264 = true;
        } else if (code == 74) {
            unclipped = true;
        } else if (code == 75) {
            anInt2298 = buffer.readUByte();
        } else if (code == 77 || code == 92) {
            varbit = buffer.readUShort();
            if (varbit == 65535) {
                varbit = -1;
            }

            varp = buffer.readUShort();
            if (varp == 65535) {
                varp = -1;
            }

            int count2 = -1;
            if (code == 92) {
                count2 = buffer.readUShort();
                if (count2 == 65535) {
                    count2 = -1;
                }
            }

            int count = buffer.readUByte();
            to_objs = new int[2 + count];

            for (int i = 0; i <= count; i++) {
                to_objs[i] = buffer.readUShort();
                if (to_objs[i] == 65535) {
                    to_objs[i] = -1;
                }
            }

            to_objs[1 + count] = count2;
        } else if (code == 78) {
            anInt2302 = buffer.readUShort();
            anInt2303 = buffer.readUByte();
        } else if (code == 79) {
            anInt2304 = buffer.readUShort();
            anInt2290 = buffer.readUShort();
            anInt2303 = buffer.readUByte();
            int count = buffer.readUByte();
            anIntArray2306 = new int[count];

            for (int i = 0; i < count; i++) {
                anIntArray2306[i] = buffer.readUShort();
            }
        } else if (code == 81) {
            anInt2296 = buffer.readUByte();
        } else if (code == 82) {
            this.anInt2167 = buffer.readUShort();
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
            throw new RuntimeException("cannot parse object definition, missing config code: " + code);
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

    public boolean isClippedDecoration() {
        return anInt2292 != 0 || clipType == 1 || aBool2264;
    }

    public String toStringBig() {
        return "ObjectDefinition{" +
            "name='" + name + '\'' +
            ", modeltypes=" + Arrays.toString(modeltypes) +
            ", models=" + Arrays.toString(models) +
            ", sizeX=" + sizeX +
            ", sizeY=" + sizeY +
            ", clipType=" + clipType +
            ", tall=" + tall +
            ", anInt2292=" + anInt2292 +
            ", anInt2296=" + anInt2296 +
            ", aBool2279=" + aBool2279 +
            ", aBool2280=" + aBool2280 +
            ", anInt2281=" + anInt2281 +
            ", anInt2291=" + anInt2291 +
            ", anInt2283=" + anInt2283 +
            ", anInt2285=" + anInt2285 +
            ", recol_s=" + Arrays.toString(recol_s) +
            ", recol_d=" + Arrays.toString(recol_d) +
            ", retex_s=" + Arrays.toString(retex_s) +
            ", retex_d=" + Arrays.toString(retex_d) +
            ", anInt2286=" + anInt2286 +
            ", vflip=" + vflip +
            ", aBool2284=" + aBool2284 +
            ", op65Render0x1=" + op65Render0x1 +
            ", op66Render0x2=" + op66Render0x2 +
            ", op67Render0x4=" + op67Render0x4 +
            ", anInt2287=" + anInt2287 +
            ", anInt2307=" + anInt2307 +
            ", anInt2294=" + anInt2294 +
            ", anInt2295=" + anInt2295 +
            ", aBool2264=" + aBool2264 +
            ", unclipped=" + unclipped +
            ", anInt2298=" + anInt2298 +
            ", varbit=" + varbit +
            ", anInt2302=" + anInt2302 +
            ", anInt2303=" + anInt2303 +
            ", varp=" + varp +
            ", anInt2304=" + anInt2304 +
            ", anInt2290=" + anInt2290 +
            ", cflag=" + cflag +
            ", anIntArray2306=" + Arrays.toString(anIntArray2306) +
            ", to_objs=" + Arrays.toString(to_objs) +
            ", options=" + Arrays.toString(options) +
            ", clientScriptData=" + clientScriptData +
            ", id=" + id +
            ", anInt2167=" + anInt2167 +
            '}';
    }
}
