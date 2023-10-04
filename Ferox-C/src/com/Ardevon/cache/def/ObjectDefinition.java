package com.Ardevon.cache.def;

import com.Ardevon.Client;
import com.Ardevon.ClientConstants;
import com.Ardevon.cache.Archive;
import com.Ardevon.cache.anim.Animation;
import com.Ardevon.cache.config.VariableBits;
import com.Ardevon.cache.def.impl.ObjectManager;
import com.Ardevon.collection.TempCache;
import com.Ardevon.entity.model.Model;
import com.Ardevon.io.Buffer;
import com.Ardevon.net.requester.ResourceProvider;
import com.Ardevon.util.FileUtils;

import java.util.HashMap;
import java.util.Map;

public final class ObjectDefinition {

    private int category;
    private boolean randomAnimStart;
    private Map<Integer, Object> params;
    private String opcode150;

    public static void init(Archive archive) {
        data_buffer = new Buffer(ClientConstants.LOAD_OSRS_DATA_FROM_CACHE_DIR ? FileUtils.read(ClientConstants.DATA_DIR + "/objects/loc.dat") : archive.get("loc.dat"));
        Buffer index_buffer = new Buffer(ClientConstants.LOAD_OSRS_DATA_FROM_CACHE_DIR ? FileUtils.read(ClientConstants.DATA_DIR + "/objects/loc.idx") : archive.get("loc.idx"));
        length = index_buffer.readUShort();

        System.out.printf("Loaded %d objects loading OSRS version %d and SUB version %d%n", length, ClientConstants.OSRS_DATA_VERSION, ClientConstants.OSRS_DATA_SUB_VERSION);

        stream_indices = new int[length];
        int offset = 2;
        for (int index = 0; index < length; index++) {
            stream_indices[index] = offset;
            offset += index_buffer.readUShort();
        }

        cache = new ObjectDefinition[20];

        for (int index = 0; index < 20; index++) {
            cache[index] = new ObjectDefinition();
        }
    }

    private void decode(Buffer buffer) {
        while (true) {
            int opcode = buffer.readUnsignedByte();
            if (opcode == 0)
                break;
            if (opcode == 1) {
                int len = buffer.readUnsignedByte();
                if (len > 0) {
                    if (modelIds == null) {
                        modelTypes = new int[len];
                        modelIds = new int[len];

                        for (int i = 0; i < len; i++) {
                            modelIds[i] = buffer.readUShort();
                            modelTypes[i] = buffer.readUnsignedByte();
                        }
                    } else {
                        buffer.pos += len * 3;
                    }
                }
            } else if (opcode == 2)
                name = buffer.readString();
            else if (opcode == 3)
                description = buffer.readString();
            else if (opcode == 5) {
                int len = buffer.readUnsignedByte();
                if (len > 0) {
                    if (modelIds == null) {
                        modelTypes = null;
                        modelIds = new int[len];

                        for (int i = 0; i < len; i++) {
                            modelIds[i] = buffer.readUShort();
                        }
                    } else {
                        buffer.pos += len * 3;
                    }
                }
            } else if (opcode == 14)
                objectSizeX = buffer.readUnsignedByte();
            else if (opcode == 15)
                objectSizeY = buffer.readUnsignedByte();
            else if (opcode == 17) {
                solid = false;
                impenetrable = false;
            } else if (opcode == 18)
                impenetrable = false;
            else if (opcode == 19)
                isInteractive = buffer.readUnsignedByte();
            else if (opcode == 21)
                contouredGround = true;
            else if (opcode == 22)
                delayShading = true;
            else if (opcode == 23)
                occludes = true;
            else if (opcode == 24) { // Object Animations
                animation = buffer.readUShort();
                if (animation == 65535)
                    animation = -1;
            } else if (opcode == 28)
                decorDisplacement = buffer.readUnsignedByte();
            else if (opcode == 29)
                ambientLighting = buffer.readSignedByte();
            else if (opcode == 39)
                lightDiffusion = buffer.readSignedByte();
            else if (opcode >= 30 && opcode < 35) {
                if (interactions == null)
                    interactions = new String[10];
                interactions[opcode - 30] = buffer.readString();
                if (interactions[opcode - 30].equalsIgnoreCase("hidden"))
                    interactions[opcode - 30] = null;
            } else if (opcode == 40) {
                int len = buffer.readUnsignedByte();
                modifiedModelColors = new int[len];
                originalModelColors = new int[len];
                for (int i = 0; i < len; i++) {
                    modifiedModelColors[i] = buffer.readUShort();
                    originalModelColors[i] = buffer.readUShort();
                }
            } else if (opcode == 41) {
                int len = buffer.readUnsignedByte();
                dst_texture = new short[len];
                src_texture = new short[len];
                for (int i = 0; i < len; i++) {
                    dst_texture[i] = (short) buffer.readUShort();
                    src_texture[i] = (short) buffer.readUShort();
                }
            } else if (opcode == 61) {
                category = buffer.readUShort();
            } else if (opcode == 62)
                inverted = true;
            else if (opcode == 64)
                castsShadow = false;
            else if (opcode == 65)
                scaleX = buffer.readUShort();
            else if (opcode == 66)
                scaleY = buffer.readUShort();
            else if (opcode == 67)
                scaleZ = buffer.readUShort();
            else if (opcode == 68)
                mapscene = buffer.readUShort();
            else if (opcode == 69)
                surroundings = buffer.readUnsignedByte();
            else if (opcode == 70)
                translateX = buffer.readShort();
            else if (opcode == 71)
                translateY = buffer.readShort();
            else if (opcode == 72)
                translateZ = buffer.readShort();
            else if (opcode == 73)
                obstructsGround = true;
            else if (opcode == 74)
                hollow = true;
            else if (opcode == 75)
                supportItems = buffer.readUnsignedByte();
            else if (opcode == 77 || opcode == 92) {
                varbit = buffer.readUShort();

                if (varp == 0xFFFF) {
                    varp = -1;
                }

                varp = buffer.readUShort();

                if (varbit == 0xFFFF) {
                    varbit = -1;
                }

                int value = -1;

                if (opcode == 92) {
                    value = buffer.readUShort();

                    if (value == 0xFFFF) {
                        value = -1;
                    }
                }

                int len = buffer.readUnsignedByte();

                childrenIDs = new int[len + 2];
                for (int i = 0; i <= len; ++i) {
                    childrenIDs[i] = buffer.readUShort();
                    if (childrenIDs[i] == 0xFFFF) {
                        childrenIDs[i] = -1;
                    }
                }
                childrenIDs[len + 1] = value;
            } else if(opcode == 78) {
                ambientSoundId = buffer.readUShort();
                anInt2083 = buffer.readUnsignedByte();
            } else if(opcode == 79) {
                anInt2112 = buffer.readUShort();
                anInt2113 = buffer.readUShort();
                anInt2083 = buffer.readUShort();

                int length = buffer.readUnsignedByte();
                int[] anims = new int[length];

                for (int index = 0; index < length; ++index)
                {
                    anims[index] = buffer.readUShort();
                }
                ambientSoundIds = anims;
            } else if(opcode == 81) {
                buffer.readUnsignedByte();
            } else if (opcode == 82) {
                minimapFunction = buffer.readUShort();//AreaType
            } else if(opcode == 89) {
                randomAnimStart = false;
            } else if (opcode == 94) {
                opcode150 = buffer.readString();
            } else if (opcode == 249) {
                int length = buffer.readUnsignedByte();

                Map<Integer, Object> params = new HashMap<>(length);
                for (int i = 0; i < length; i++)
                {
                    boolean isString = buffer.readUnsignedByte() == 1;
                    int key = buffer.read24Int();
                    Object value;

                    if (isString) {
                        value = buffer.readString();
                        System.out.println(value);
                    } else {
                        value = buffer.readInt();
                    }

                    params.put(key, value);
                }

                this.params = params;
            } else {
                //System.err.printf("Error unrecognised {Objects} opcode: %d%n%n", opcode);
            }
        }
        post_decode();
    }


    public void post_decode() {
        if (isInteractive == -1) {
            isInteractive = 0;
            if (name != null && !name.equalsIgnoreCase("null")) {
                if (modelIds != null && (modelTypes == null || modelTypes[0] == 10))
                    isInteractive = 1;//1

                if (interactions != null)
                    isInteractive = 1;

            }
        }
        if (hollow) {
            solid = false;
            impenetrable = false;
        }
        if (supportItems == -1)
            supportItems = solid ? 1 : 0;

    }

    public static ObjectDefinition get(int id) {
        if (id > stream_indices.length) {
            id = stream_indices.length - 1;
        }

        if (id == 25913)
            id = 15552;

        if (id == 25916 || id == 25926)
            id = 15553;

        if (id == 25917)
            id = 15554;

        for (int index = 0; index < 20; index++) {
            if (cache[index].id == id) {
                return cache[index];
            }
        }

        cache_index = (cache_index + 1) % 20;
        ObjectDefinition def = cache[cache_index];
        data_buffer.pos = stream_indices[id];
        def.id = id;
        def.set_defaults();
        def.decode(data_buffer);

        if (def.id == 29308)//wintertoldt snow storm 1639 // 3997 cheap fix
            def.delayShading = false;

        if (def.id >= 29167 && def.id <= 29225) {
            def.objectSizeX = 1;
            def.solid = false;
            def.interactions = new String[]{"Take", null, null, null, null};
        }
        if (def.id == 14924) {
            def.objectSizeX = 1;
        }

        if (ClientConstants.WILDERNESS_DITCH_DISABLED) {
            if (id == 23271) {
                def.modelIds = null;
                def.isInteractive = 0;
                def.solid = false;
                return def;
            }
        }

        /*if(def.id > 16500) {
            if(def.delayShading == true)
                def.delayShading = false;

        }*/
        ObjectManager.get(id);

        /*if (def.name == null || def.name.equalsIgnoreCase("null"))
            def.name = "weee";

        def.interact_state = 1;*/
        return def;
    }

    public void set_defaults() {
        modelIds = null;
        modelTypes = null;
        name = null;
        description = null;
        modifiedModelColors = null;
        originalModelColors = null;
        dst_texture = null;
        src_texture = null;
        objectSizeX = 1;
        objectSizeY = 1;
        solid = true;
        impenetrable = true;
        isInteractive = -1;
        contouredGround = false;
        delayShading = false;
        occludes = false;
        animation = -1;
        decorDisplacement = 16;
        ambientLighting = 0;
        lightDiffusion = 0;
        interactions = null;
        minimapFunction = -1;
        mapscene = -1;
        inverted = false;
        castsShadow = true;
        scaleX = 128;
        scaleY = 128;
        scaleZ = 128;
        surroundings = 0;
        translateX = 0;
        translateY = 0;
        translateZ = 0;
        obstructsGround = false;
        hollow = false;
        supportItems = -1;
        varbit = -1;
        varp = -1;
        childrenIDs = null;
    }

    public void passive_request_load(ResourceProvider provider) {
        if (modelIds == null)
            return;

        for (int index = 0; index < modelIds.length; index++)
            provider.passive_request(modelIds[index] & 0xffff, 0);

    }

    public Model get_object(int type, int orientation, int cosine_y, int sine_y, int cosine_x, int sine_x, int animation_id) {
        Model model = get_animated_model(type, animation_id, orientation);

        if (model == null)
            return null;

        if (contouredGround || delayShading) {
            model = new Model(contouredGround, delayShading, model);
        }

        if (contouredGround) {
            int height = (cosine_y + sine_y + cosine_x + sine_x) / 4;
            for (int vertex = 0; vertex < model.verticesCount; vertex++) {
                int start_x = model.verticesX[vertex];
                int start_y = model.verticesZ[vertex];
                int y = cosine_y + ((sine_y - cosine_y) * (start_x + 64)) / 128;
                int x = sine_x + ((cosine_x - sine_x) * (start_x + 64)) / 128;
                int undulation_offset = y + ((x - y) * (start_y + 64)) / 128;
                model.verticesY[vertex] += undulation_offset - height;
            }
            model.computeSphericalBounds();
        }
        return model;
    }

    public boolean group_cached(int type) {
        if (modelTypes == null) {
            if (modelIds == null)
                return true;

            if (type != 10)
                return true;

            boolean cached = true;
            for (int index = 0; index < modelIds.length; index++)
                cached &= Model.cached(modelIds[index]);

            return cached;
        }
        for (int index = 0; index < modelTypes.length; index++)
            if (modelTypes[index] == type)
                return Model.cached(modelIds[index]);

        return true;
    }

    public boolean cached() {
        if (modelIds == null)
            return true;

        boolean cached = true;
        for (int model_id : modelIds) cached &= Model.cached(model_id);

        return cached;
    }

    public ObjectDefinition get_configs() {
        int setting_id = -1;
        if (varbit != -1) {
            VariableBits bit = VariableBits.cache[varbit];
            int setting = bit.configId;
            int low = bit.leastSignificantBit;
            int high = bit.mostSignificantBit;
            int mask = Client.BIT_MASKS[high - low];
            setting_id = Client.singleton.settings[setting] >> low & mask;
        } else if (varp != -1)
            setting_id = Client.singleton.settings[varp];

        if (setting_id < 0 || setting_id >= childrenIDs.length || childrenIDs[setting_id] == -1)
            return null;
        else
            return get(childrenIDs[setting_id]);
    }

    public Model get_animated_model(int type, int animation_id, int orientation) {
        Model model = null;
        long key;
        if (modelTypes == null) {
            if (type != 10)
                return null;

            key = (long) ((id << 6) + orientation) + ((long) (animation_id + 1) << 32);
            Model cached = (Model) model_cache.get(key);
            if (cached != null)
                return cached;

            if (modelIds == null)
                return null;

            boolean invert = inverted ^ (orientation > 3);
            int length = modelIds.length;
            for (int index = 0; index < length; index++) {
                int invert_id = modelIds[index];
                if (invert)
                    invert_id += 0x10000;

                model = (Model) animated_model_cache.get(invert_id);
                if (model == null) {
                    model = Model.get(invert_id & 0xffff);
                    if (model == null)
                        return null;

                    if (invert)
                        model.invert();

                    animated_model_cache.put(model, invert_id);
                }
                if (length > 1)
                    models[index] = model;

            }
            if (length > 1)
                model = new Model(length, models, true);//fixes rotating textures on objects

        } else {
            int model_id = -1;
            for (int index = 0; index < modelTypes.length; index++) {
                if (modelTypes[index] != type)
                    continue;

                model_id = index;
                break;
            }
            if (model_id == -1)
                return null;

            key = (long) ((id << 8) + (model_id << 3) + orientation) + ((long) (animation_id + 1) << 32);
            Model cached = (Model) model_cache.get(key);
            if (cached != null)
                return cached;

            model_id = modelIds[model_id];
            boolean invert = inverted ^ (orientation > 3);
            if (invert)
                model_id += 0x10000;

            model = (Model) animated_model_cache.get(model_id);
            if (model == null) {
                model = Model.get(model_id & 0xffff);
                if (model == null)
                    return null;

                if (invert)
                    model.invert();

                animated_model_cache.put(model, model_id);
            }
        }
        boolean scale = scaleX != 128 || scaleY != 128 || scaleZ != 128;
        boolean translate = translateX != 0 || translateY != 0 || translateZ != 0;
        Model animated_model = new Model(modifiedModelColors == null, Animation.validate(animation_id), orientation == 0 && animation_id == -1 && !scale && !translate, src_texture == null, model);
        if (animation_id != -1) {
            animated_model.skin();
            animated_model.interpolate(animation_id);
            animated_model.faceGroups = null;
            animated_model.vertexGroups = null;
        }
        while (orientation-- > 0)
            animated_model.rotate_90();

        if (modifiedModelColors != null) {
            for (int index = 0; index < modifiedModelColors.length; index++)
                animated_model.recolor(modifiedModelColors[index], originalModelColors[index]);

        }
        if (dst_texture != null) {
            for (int index = 0; index < dst_texture.length; index++) {
                animated_model.retexture(dst_texture[index], src_texture[index]);
            }
        }

        if (scale)
            animated_model.scale(scaleX, scaleZ, scaleY);

        if (translate)
            animated_model.translate(translateX, translateY, translateZ);
            animated_model.light(85 + this.ambientLighting, 768 + this.lightDiffusion * 25, -50, -10, -50, !this.delayShading); // LocoPk\
        if (supportItems == 1) {
            animated_model.itemDropHeight = animated_model.model_height;
        }
        animated_model_cache.put(animated_model, key);
        return animated_model;
    }

    public static void release() {
        model_cache = null;
        animated_model_cache = null;
        stream_indices = null;
        cache = null;
        data_buffer = null;
    }

    public ObjectDefinition() {
        id = -1;
    }

    public static int length;
    public static int cache_index;
    public static boolean low_detail = ClientConstants.OBJECT_DEFINITION_LOW_MEMORY;
    public static Buffer data_buffer;
    public static ObjectDefinition[] cache;
    public static int[] stream_indices;
    public static final Model[] models = new Model[4];
    public static TempCache model_cache = new TempCache(500);
    public static TempCache animated_model_cache = new TempCache(30);

    public int id;
    public int objectSizeX;
    public int objectSizeY;
    public int animation;
    public int surroundings;

    public int scaleX;
    public int scaleY;
    public int scaleZ;
    public int translateX;
    public int translateY;
    public int translateZ;
    public int minimapFunction;
    public int mapscene;
    public int isInteractive;
    public int decorDisplacement;//
    public int supportItems;//
    public int varp;
    public int varbit;

    public int[] modelIds;
    public int[] childrenIDs;
    public int[] modelTypes;

    public int[] modifiedModelColors;
    public int[] originalModelColors;

    public short[] src_texture;
    public short[] dst_texture;

    public String name;
    public String description;
    public String[] interactions;

    public int lightDiffusion;
    public byte ambientLighting;

    public boolean inverted;
    public boolean impenetrable;
    public boolean contouredGround;
    public boolean occludes;
    public boolean hollow;
    public boolean solid;
    public boolean castsShadow;
    public boolean delayShading;//
    public boolean obstructsGround;

    /**
     * Later revisions
     */
    int ambientSoundId = 2019882883;
    int anInt2112 = 0;
    int anInt2113 = 0;
    int anInt2083 = 0;
    int[] ambientSoundIds;

}
