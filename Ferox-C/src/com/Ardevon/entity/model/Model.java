package com.Ardevon.entity.model;

import com.Ardevon.cache.anim.Animation;
import com.Ardevon.cache.anim.Skins;
import com.Ardevon.draw.Rasterizer2D;
import com.Ardevon.draw.Rasterizer3D;
import com.Ardevon.entity.Renderable;
import com.Ardevon.io.Buffer;
import com.Ardevon.model.texture.TextureCoordinate;
import com.Ardevon.net.requester.Provider;
import com.Ardevon.scene.SceneGraph;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Model extends Renderable {



    private static final Set<Integer> repeatedTextureModels = new HashSet<>();

    static {
        int[] array = {55555, 55556, 55557, 55558, 55559, 55560, 55561, 55562, 55563, 55564, 55565, 55566, 55567, 55568, 55569, 55570, 55571, 55572, 55573, 55574, 55575, 55576, 55577, 55578, 55579, 55580, 55581, 55582, 55583, 55584, 55585, 55586, 55587, 55588, 55589, 55590, 55591, 55592, 55593, 55594, 55595, 55596, 55597, 55598, 55599, 55600, 55601, 55602, 55603, 55604, 55605, 55606, 55608, 55609};
        for (int id : array) {
            addAll(id);
        }
    }

    private static void addAll(int... values) {
        for (int value : values) {
            repeatedTextureModels.add(value);
        }
    }

    public Model(int modelId) {
        byte[] data = aClass21Array1661[modelId].data;
        if (data[data.length - 1] == -3 && data[data.length - 2] == -1) {
            ModelLoader.decodeType3(this, data);
        } else if (data[data.length - 1] == -2 && data[data.length - 2] == -1) {
            ModelLoader.decodeType2(this, data);
        } else if (data[data.length - 1] == -1 && data[data.length - 2] == -1) {
            ModelLoader.decodeType1(this, data);
        } else {
            ModelLoader.decodeOldFormat(this, data);
        }
        repeatTexture = new boolean[trianglesCount];

        if (repeatedTextureModels.contains(modelId)) {
            Arrays.fill(repeatTexture, true);
        }
    }

    public boolean[] repeatTexture;

    private Model(boolean flag) {
        aBoolean1618 = true;
        fits_on_single_square = false;
        if (!flag)
            aBoolean1618 = !aBoolean1618;
    }

    public Model(int length, Model model_segments[], boolean preset) {
        try {
            aBoolean1618 = true;
            fits_on_single_square = false;
            anInt1620++;
            boolean render_type_flag = false;
            boolean priority_flag = false;
            boolean alpha_flag = false;
            boolean muscle_skin_flag = false;
            boolean color_flag = false;
            boolean texture_flag = false;
            boolean coordinate_flag = false;
            verticesCount = 0;
            trianglesCount = 0;
            texturesCount = 0;
            face_priority = -1;
            Model build;
            for (int segment_index = 0; segment_index < length; segment_index++) {
                build = model_segments[segment_index];
                if (build != null) {
                    verticesCount += build.verticesCount;
                    trianglesCount += build.trianglesCount;
                    texturesCount += build.texturesCount;
                    if (build.face_render_priorities != null) {
                        priority_flag = true;
                    } else {
                        if (face_priority == -1)
                            face_priority = build.face_priority;

                        if (face_priority != build.face_priority)
                            priority_flag = true;
                    }
                    render_type_flag |= build.types != null;
                    alpha_flag |= build.alphas != null;
                    muscle_skin_flag |= build.triangleData != null;
                    color_flag |= build.colors != null;
                    texture_flag |= build.materials != null;
                    coordinate_flag |= build.textures != null;
                }
            }
            verticesX = new int[verticesCount];
            verticesY = new int[verticesCount];
            verticesZ = new int[verticesCount];
            vertexData = new int[verticesCount];
            trianglesX = new int[trianglesCount];
            trianglesY = new int[trianglesCount];
            trianglesZ = new int[trianglesCount];
            repeatTexture = new boolean[trianglesCount];
            if (color_flag) {
                colors = new short[trianglesCount];
                repeatTexture = new boolean[trianglesCount];
            }

            if (render_type_flag)
                types = new int[trianglesCount];

            if (priority_flag)
                face_render_priorities = new byte[trianglesCount];

            if (alpha_flag)
                alphas = new int[trianglesCount];

            if (muscle_skin_flag)
                triangleData = new int[trianglesCount];

            if (texture_flag)
                materials = new short[trianglesCount];

            if (coordinate_flag)
                textures = new byte[trianglesCount];

            if (texturesCount > 0) {
                textureTypes = new byte[texturesCount];
                texturesX = new short[texturesCount];
                texturesY = new short[texturesCount];
                texturesZ = new short[texturesCount];
            }

            verticesCount = 0;
            trianglesCount = 0;
            texturesCount = 0;
            for (int segment_index = 0; segment_index < length; segment_index++) {
                build = model_segments[segment_index];
                if (build != null) {
                    for (int face = 0; face < build.trianglesCount; face++) {
                        if (render_type_flag && build.types != null)
                            types[trianglesCount] = build.types[face];

                        if (priority_flag)
                            if (build.face_render_priorities == null)
                                face_render_priorities[trianglesCount] = build.face_priority;
                            else
                                face_render_priorities[trianglesCount] = build.face_render_priorities[face];

                        if (alpha_flag && build.alphas != null)
                            alphas[trianglesCount] = build.alphas[face];

                        if (muscle_skin_flag && build.triangleData != null)
                            triangleData[trianglesCount] = build.triangleData[face];

                        if (texture_flag) {
                            if (build.materials != null)
                                materials[trianglesCount] = build.materials[face];
                            else
                                materials[trianglesCount] = -1;
                        }
                        if (coordinate_flag) {
                            if (build.textures != null && build.textures[face] != -1) {
                                textures[trianglesCount] = (byte) (build.textures[face] + texturesCount);
                            } else {
                                textures[trianglesCount] = -1;
                            }
                        }

                        if (color_flag && build.colors != null)
                            colors[trianglesCount] = build.colors[face];
                        repeatTexture[trianglesCount] = build.repeatTexture[face];

                        trianglesX[trianglesCount] = method465(build, build.trianglesX[face]);
                        trianglesY[trianglesCount] = method465(build, build.trianglesY[face]);
                        trianglesZ[trianglesCount] = method465(build, build.trianglesZ[face]);
                        trianglesCount++;
                    }
                    for (int texture_edge = 0; texture_edge < build.texturesCount; texture_edge++) {
                        byte opcode = textureTypes[texturesCount] = build.textureTypes[texture_edge];
                        if (opcode == 0) {
                            texturesX[texturesCount] = (short) method465(build, build.texturesX[texture_edge]);
                            texturesY[texturesCount] = (short) method465(build, build.texturesY[texture_edge]);
                            texturesZ[texturesCount] = (short) method465(build, build.texturesZ[texture_edge]);
                        }
                        if (opcode >= 1 && opcode <= 3) {
                            texturesX[texturesCount] = build.texturesX[texture_edge];
                            texturesY[texturesCount] = build.texturesY[texture_edge];
                            texturesZ[texturesCount] = build.texturesZ[texture_edge];
                        }
                        if (opcode == 2) {

                        }
                        texturesCount++;
                    }
                    if (!preset) //for models that don't have preset textured_faces
                        texturesCount++;

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Model(Model amodel[]) {
        int i = 2;
        aBoolean1618 = true;
        fits_on_single_square = false;
        anInt1620++;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;
        boolean texture_flag = false;
        boolean coordinate_flag = false;
        verticesCount = 0;
        trianglesCount = 0;
        texturesCount = 0;
        face_priority = -1;
        for (int k = 0; k < i; k++) {
            Model model = amodel[k];
            if (model != null) {
                verticesCount += model.verticesCount;
                trianglesCount += model.trianglesCount;
                texturesCount += model.texturesCount;
                flag1 |= model.types != null;
                if (model.face_render_priorities != null) {
                    flag2 = true;
                } else {
                    if (face_priority == -1)
                        face_priority = model.face_priority;
                    if (face_priority != model.face_priority)
                        flag2 = true;
                }
                flag3 |= model.alphas != null;
                flag4 |= model.colors != null;
                texture_flag |= model.materials != null;
                coordinate_flag |= model.textures != null;
            }
        }

        verticesX = new int[verticesCount];
        verticesY = new int[verticesCount];
        verticesZ = new int[verticesCount];
        trianglesX = new int[trianglesCount];
        trianglesY = new int[trianglesCount];
        trianglesZ = new int[trianglesCount];
        colorsX = new int[trianglesCount];
        colorsY = new int[trianglesCount];
        colorsZ = new int[trianglesCount];
        texturesX = new short[texturesCount];
        texturesY = new short[texturesCount];
        texturesZ = new short[texturesCount];
        if (flag1)
            types = new int[trianglesCount];
        if (flag2)
            face_render_priorities = new byte[trianglesCount];
        if (flag3)
            alphas = new int[trianglesCount];
        if (flag4) {
            colors = new short[trianglesCount];
            repeatTexture = new boolean[trianglesCount];
        }
        if (texture_flag)
            materials = new short[trianglesCount];

        if (coordinate_flag)
            textures = new byte[trianglesCount];
        verticesCount = 0;
        trianglesCount = 0;
        texturesCount = 0;
        int i1 = 0;
        for (int j1 = 0; j1 < i; j1++) {
            Model model_1 = amodel[j1];
            if (model_1 != null) {
                int k1 = verticesCount;
                for (int l1 = 0; l1 < model_1.verticesCount; l1++) {
                    int x = model_1.verticesX[l1];
                    int y = model_1.verticesY[l1];
                    int z = model_1.verticesZ[l1];
                    verticesX[verticesCount] = x;
                    verticesY[verticesCount] = y;
                    verticesZ[verticesCount] = z;
                    ++verticesCount;
                }

                for (int uid = 0; uid < model_1.trianglesCount; uid++) {
                    trianglesX[trianglesCount] = model_1.trianglesX[uid] + k1;
                    trianglesY[trianglesCount] = model_1.trianglesY[uid] + k1;
                    trianglesZ[trianglesCount] = model_1.trianglesZ[uid] + k1;
                    colorsX[trianglesCount] = model_1.colorsX[uid];
                    colorsY[trianglesCount] = model_1.colorsY[uid];
                    colorsZ[trianglesCount] = model_1.colorsZ[uid];
                    if (flag1)
                        if (model_1.types == null) {
                            types[trianglesCount] = 0;
                        } else {
                            int j2 = model_1.types[uid];
                            if ((j2 & 2) == 2)
                                j2 += i1 << 2;
                            types[trianglesCount] = j2;
                        }
                    if (flag2)
                        if (model_1.face_render_priorities == null)
                            face_render_priorities[trianglesCount] = model_1.face_priority;
                        else
                            face_render_priorities[trianglesCount] = model_1.face_render_priorities[uid];
                    if (flag3) {
                        if (model_1.alphas == null)
                            alphas[trianglesCount] = 0;
                        else
                            alphas[trianglesCount] = model_1.alphas[uid];

                    }
                    if (flag4 && model_1.colors != null) {
                        colors[trianglesCount] = model_1.colors[uid];
                        repeatTexture[trianglesCount] = model_1.repeatTexture[uid];
                    }

                    if (texture_flag) {
                        if (model_1.materials != null) {
                            materials[trianglesCount] = model_1.materials[trianglesCount];
                        } else {
                            materials[trianglesCount] = -1;
                        }
                    }

                    if (coordinate_flag) {
                        if (model_1.textures != null && model_1.textures[trianglesCount] != -1)
                            textures[trianglesCount] = (byte) (model_1.textures[trianglesCount] + texturesCount);
                        else
                            textures[trianglesCount] = -1;

                    }

                    trianglesCount++;
                }

                for (int k2 = 0; k2 < model_1.texturesCount; k2++) {
                    texturesX[texturesCount] = (short) (model_1.texturesX[k2] + k1);
                    texturesY[texturesCount] = (short) (model_1.texturesY[k2] + k1);
                    texturesZ[texturesCount] = (short) (model_1.texturesZ[k2] + k1);
                    texturesCount++;
                }

                i1 += model_1.texturesCount;
            }
        }

        calc_diagonals();
    }

    public Model(boolean color_flag, boolean alpha_flag, boolean animated, Model model) {
        this(color_flag, alpha_flag, animated, false, model);
    }

    public Model(boolean color_flag, boolean alpha_flag, boolean animated, boolean texture_flag, Model model) {
        aBoolean1618 = true;
        fits_on_single_square = false;
        anInt1620++;
        verticesCount = model.verticesCount;
        trianglesCount = model.trianglesCount;
        texturesCount = model.texturesCount;
        if (animated) {
            verticesX = model.verticesX;
            verticesY = model.verticesY;
            verticesZ = model.verticesZ;
        } else {
            verticesX = new int[verticesCount];
            verticesY = new int[verticesCount];
            verticesZ = new int[verticesCount];
            for (int point = 0; point < verticesCount; point++) {
                verticesX[point] = model.verticesX[point];
                verticesY[point] = model.verticesY[point];
                verticesZ[point] = model.verticesZ[point];
            }

        }

        if (color_flag) {
            colors = model.colors;
            repeatTexture = model.repeatTexture;
        } else {
            colors = new short[trianglesCount];
            repeatTexture = new boolean[trianglesCount];
            for (int face = 0; face < trianglesCount; face++) {
                colors[face] = model.colors[face];
                repeatTexture[face] = model.repeatTexture[face];
            }

        }

        if(!texture_flag && model.materials != null) {
            materials = new short[trianglesCount];
            for(int face = 0; face < trianglesCount; face++) {
                materials[face] = model.materials[face];
            }
        } else {
            materials = model.materials;
        }

        if (alpha_flag) {
            alphas = model.alphas;
        } else {
            alphas = new int[trianglesCount];
            if (model.alphas == null) {
                for (int l = 0; l < trianglesCount; l++)
                    alphas[l] = 0;

            } else {
                for (int i1 = 0; i1 < trianglesCount; i1++)
                    alphas[i1] = model.alphas[i1];

            }
        }
        vertexData = model.vertexData;
        triangleData = model.triangleData;
        types = model.types;
        trianglesX = model.trianglesX;
        trianglesY = model.trianglesY;
        trianglesZ = model.trianglesZ;
        face_render_priorities = model.face_render_priorities;
        textures = model.textures;
        textureTypes = model.textureTypes;
        face_priority = model.face_priority;
        texturesX = model.texturesX;
        texturesY = model.texturesY;
        texturesZ = model.texturesZ;
    }

    public Model(boolean adjust_elevation, boolean gouraud_shading, Model model) {
        aBoolean1618 = true;
        fits_on_single_square = false;
        anInt1620++;
        verticesCount = model.verticesCount;
        trianglesCount = model.trianglesCount;
        texturesCount = model.texturesCount;
        if (adjust_elevation) {
            verticesY = new int[verticesCount];
            for (int point = 0; point < verticesCount; point++)
                verticesY[point] = model.verticesY[point];

        } else {
            verticesY = model.verticesY;
        }
 //       if (gouraud_shading) {
 //           colorsX = new int[trianglesCount];
 //           colorsY = new int[trianglesCount];
  //          colorsZ = new int[trianglesCount];
  //          for (int face = 0; face < trianglesCount; face++) {
  //              colorsX[face] = model.colorsX[face];
  //              colorsY[face] = model.colorsY[face];
 //               colorsZ[face] = model.colorsZ[face];
 //           }

 //           types = new int[trianglesCount];
  //          if (model.types == null) {
  //              for (int face = 0; face < trianglesCount; face++)
  //                  types[face] = 0;

  //          } else {
   //             for (int face = 0; face < trianglesCount; face++)
   //                 types[face] = model.types[face];

  //          }
    //        super.normals = new VertexNormal[verticesCount];
   //         for (int point = 0; point < verticesCount; point++) {
   //             VertexNormal class33 = super.normals[point] = new VertexNormal();
  //              VertexNormal class33_1 = model.vertexNormals[point];
   //             class33.x = class33_1.x;
   //             class33.y = class33_1.y;
    //            class33.z = class33_1.z;
    //            class33.magnitude = class33_1.magnitude;
     //       }
   //         vertexNormals = model.vertexNormals;

  //      } else {
            colorsX = model.colorsX;
            colorsY = model.colorsY;
            colorsZ = model.colorsZ;
            types = model.types;
    //    }
        verticesX = model.verticesX;
        verticesZ = model.verticesZ;
        trianglesX = model.trianglesX;
        trianglesY = model.trianglesY;
        trianglesZ = model.trianglesZ;
        face_render_priorities = model.face_render_priorities;
        alphas = model.alphas;
        textures = model.textures;
        colors = model.colors;
        repeatTexture = model.repeatTexture;
        materials = model.materials;
        face_priority = model.face_priority;
        textureTypes = model.textureTypes;
        texturesX = model.texturesX;
        texturesY = model.texturesY;
        texturesZ = model.texturesZ;
        super.model_height = model.model_height;
        XYZMag = model.XYZMag;
        diagonal3DAboveOrigin = model.diagonal3DAboveOrigin;
        maxRenderDepth = model.maxRenderDepth;
        minimumXVertex = model.minimumXVertex;
        maximumZVertex = model.maximumZVertex;
        minimumZVertex = model.minimumZVertex;
        maximumXVertex = model.maximumXVertex;
    }

    public static void clear() {
        aClass21Array1661 = null;
        hasAnEdgeToRestrict = null;
        outOfReach = null;
        projected_vertex_y = null;
        projected_vertex_z = null;
        camera_vertex_x = null;
        camera_vertex_y = null;
        camera_vertex_z = null;
        depthListIndices = null;
        faceLists = null;
        anIntArray1673 = null;
        anIntArrayArray1674 = null;
        anIntArray1675 = null;
        anIntArray1676 = null;
        anIntArray1677 = null;
        SINE = null;
        COSINE = null;
        modelIntArray3 = null;
        modelIntArray4 = null;
    }

    public static void method460(byte abyte0[], int j) {
        try {
            if (abyte0 == null) {
                ModelHeader class21 = aClass21Array1661[j] = new ModelHeader();
                class21.vertices = 0;
                class21.faces = 0;
                class21.texture_faces = 0;
                return;
            }
            Buffer stream = new Buffer(abyte0);
            stream.pos = abyte0.length - 18;
            ModelHeader class21_1 = aClass21Array1661[j] = new ModelHeader();
            class21_1.data = abyte0;
            class21_1.vertices = stream.readUShort();
            class21_1.faces = stream.readUShort();
            class21_1.texture_faces = stream.readUByte();
            int k = stream.readUByte();
            int l = stream.readUByte();
            int i1 = stream.readUByte();
            int j1 = stream.readUByte();
            int k1 = stream.readUByte();
            int l1 = stream.readUShort();
            int uid = stream.readUShort();
            int j2 = stream.readUShort();
            int k2 = stream.readUShort();
            int l2 = 0;
            class21_1.vertex_offset = l2;
            l2 += class21_1.vertices;
            class21_1.face_offset = l2;
            l2 += class21_1.faces;
            class21_1.face_pri_offset = l2;
            if (l == 255)
                l2 += class21_1.faces;
            else
                class21_1.face_pri_offset = -l - 1;
            class21_1.muscle_offset = l2;
            if (j1 == 1)
                l2 += class21_1.faces;
            else
                class21_1.muscle_offset = -1;
            class21_1.render_type_offset = l2;
            if (k == 1)
                l2 += class21_1.faces;
            else
                class21_1.muscle_offset = -1;
            class21_1.bones_offset = l2;
            if (k1 == 1)
                l2 += class21_1.vertices;
            else
                class21_1.bones_offset = -1;
            class21_1.alpha_offset = l2;
            if (i1 == 1)
                l2 += class21_1.faces;
            else
                class21_1.alpha_offset = -1;
            class21_1.points_offset = l2;
            l2 += k2;
            class21_1.color_id = l2;
            l2 += class21_1.faces * 2;
            class21_1.texture_id = l2;
            l2 += class21_1.texture_faces * 6;
            class21_1.vertex_x_offset = l2;
            l2 += l1;
            class21_1.vertex_y_offset = l2;
            l2 += uid;
            class21_1.vertex_z_offset = l2;
            l2 += j2;
        } catch (Exception _ex) {
        }
    }

    public static void method459(int id, Provider onDemandFetcherParent) {
        aClass21Array1661 = new ModelHeader[80000]; //TODO: should this be id?
        resourceProvider = onDemandFetcherParent;
    }

    public static void method461(int file) {
        aClass21Array1661[file] = null;
    }

    public static Model get(int file) {
        if (aClass21Array1661 == null)
            return null;

        ModelHeader class21 = aClass21Array1661[file];
        if (class21 == null) {
            resourceProvider.provide(file);
            return null;
        } else {
            return new Model(file);
        }
    }

    public static boolean cached(int file) {
        if (aClass21Array1661 == null)
            return false;
        //This is great debugging, we can see where the file is set to 65535 in the stack trace when doing this.
        //if (file == 65535) throw new RuntimeException();
        ModelHeader class21 = aClass21Array1661[file];
        if (class21 == null) {
            resourceProvider.provide(file);
            return false;
        } else {
            return true;
        }
    }

    public void replace(Model model, boolean alpha_flag) {
        verticesCount = model.verticesCount;
        trianglesCount = model.trianglesCount;
        texturesCount = model.texturesCount;
        if (anIntArray1622.length < verticesCount) {
            anIntArray1622 = new int[verticesCount + 10000];
            anIntArray1623 = new int[verticesCount + 10000];
            anIntArray1624 = new int[verticesCount + 10000];
        }
        verticesX = anIntArray1622;
        verticesY = anIntArray1623;
        verticesZ = anIntArray1624;
        for (int point = 0; point < verticesCount; point++) {
            verticesX[point] = model.verticesX[point];
            verticesY[point] = model.verticesY[point];
            verticesZ[point] = model.verticesZ[point];
        }
        if (alpha_flag) {
            alphas = model.alphas;
        } else {
            if (anIntArray1625.length < trianglesCount)
                anIntArray1625 = new int[trianglesCount + 100];

            alphas = anIntArray1625;
            if (model.alphas == null) {
                for (int face = 0; face < trianglesCount; face++)
                    alphas[face] = 0;

            } else {
                for (int face = 0; face < trianglesCount; face++)
                    alphas[face] = model.alphas[face];

            }
        }
        types = model.types;
        colors = model.colors;
        repeatTexture = model.repeatTexture;
        face_render_priorities = model.face_render_priorities;
        face_priority = model.face_priority;
        faceGroups = model.faceGroups;
        vertexGroups = model.vertexGroups;
        trianglesX = model.trianglesX;
        trianglesY = model.trianglesY;
        trianglesZ = model.trianglesZ;
        colorsX = model.colorsX;
        colorsY = model.colorsY;
        colorsZ = model.colorsZ;
        texturesX = model.texturesX;
        texturesY = model.texturesY;
        texturesZ = model.texturesZ;
        textures = model.textures;
        textureTypes = model.textureTypes;
        materials = model.materials;
    }

    public void convertNPCTexture(int originalId, int targetId) {
        int assigned = 0;
        this.texturesCount = this.trianglesCount;
        texturesX = new short[trianglesCount];
        texturesY = new short[trianglesCount];
        texturesZ = new short[trianglesCount];
        for(int i = 0; i < this.trianglesCount; ++i) { // loops through all the triangle faces
            if (this.colors[i] == originalId) {
                this.colors[i] = (short) targetId; // sets triangleColours[i] to targetId
                this.texturesX[assigned] = (short) this.trianglesX[i]; // pretty much updates
                this.texturesY[assigned] = (short) this.trianglesY[i];
                this.texturesZ[assigned] = (short) this.trianglesZ[i];
                assigned++;
            }
        }
    }

    private final int method465(Model model, int face) {
        int vertex = -1;
        int x = model.verticesX[face];
        int y = model.verticesY[face];
        int z = model.verticesZ[face];
        for (int index = 0; index < verticesCount; index++) {
            if (x != verticesX[index] || y != verticesY[index] || z != verticesZ[index])
                continue;
            vertex = index;
            break;
        }
        if (vertex == -1) {
            verticesX[verticesCount] = x;
            verticesY[verticesCount] = y;
            verticesZ[verticesCount] = z;
            if (model.vertexData != null)
                vertexData[verticesCount] = model.vertexData[face];

            vertex = verticesCount++;
        }
        return vertex;
    }

    public void calc_diagonals() {
        super.model_height = 0;
        XYZMag = 0;
        maximumYVertex = 0;
        for (int i = 0; i < verticesCount; i++) {
            int j = verticesX[i];
            int k = verticesY[i];
            int l = verticesZ[i];
            if (-k > super.model_height)
                super.model_height = -k;
            if (k > maximumYVertex)
                maximumYVertex = k;
            int i1 = j * j + l * l;
            if (i1 > XYZMag)
                XYZMag = i1;
        }
        XYZMag = (int) (Math.sqrt(XYZMag) + 0.98999999999999999D);
        diagonal3DAboveOrigin = (int) (Math.sqrt(XYZMag * XYZMag + super.model_height
            * super.model_height) + 0.98999999999999999D);
        maxRenderDepth = diagonal3DAboveOrigin
            + (int) (Math.sqrt(XYZMag * XYZMag + maximumYVertex
            * maximumYVertex) + 0.98999999999999999D);
    }

    public void computeSphericalBounds() {
        super.model_height = 0;
        maximumYVertex = 0;
        for (int i = 0; i < verticesCount; i++) {
            int j = verticesY[i];
            if (-j > super.model_height)
                super.model_height = -j;
            if (j > maximumYVertex)
                maximumYVertex = j;
        }

        diagonal3DAboveOrigin = (int) (Math.sqrt(XYZMag * XYZMag + super.model_height
            * super.model_height) + 0.98999999999999999D);
        maxRenderDepth = diagonal3DAboveOrigin
            + (int) (Math.sqrt(XYZMag * XYZMag + maximumYVertex
            * maximumYVertex) + 0.98999999999999999D);
    }

    public void calculateVertexData(int i) {
        super.model_height = 0;
        XYZMag = 0;
        maximumYVertex = 0;
        minimumXVertex = 0xf423f;
        maximumXVertex = 0xfff0bdc1;
        maximumZVertex = 0xfffe7961;
        minimumZVertex = 0x1869f;
        for (int j = 0; j < verticesCount; j++) {
            int k = verticesX[j];
            int l = verticesY[j];
            int i1 = verticesZ[j];
            if (k < minimumXVertex)
                minimumXVertex = k;
            if (k > maximumXVertex)
                maximumXVertex = k;
            if (i1 < minimumZVertex)
                minimumZVertex = i1;
            if (i1 > maximumZVertex)
                maximumZVertex = i1;
            if (-l > super.model_height)
                super.model_height = -l;
            if (l > maximumYVertex)
                maximumYVertex = l;
            int j1 = k * k + i1 * i1;
            if (j1 > XYZMag)
                XYZMag = j1;
        }

        XYZMag = (int) Math.sqrt(XYZMag);
        diagonal3DAboveOrigin = (int) Math.sqrt(XYZMag * XYZMag + super.model_height * super.model_height);
        if (i != 21073) {
            return;
        } else {
            maxRenderDepth = diagonal3DAboveOrigin + (int) Math.sqrt(XYZMag * XYZMag + maximumYVertex * maximumYVertex);
            return;
        }
    }

    public void scale2(int i) {
        for (int i1 = 0; i1 < verticesCount; i1++) {
            verticesX[i1] = verticesX[i1] / i;
            verticesY[i1] = verticesY[i1] / i;
            verticesZ[i1] = verticesZ[i1] / i;
        }
    }

    public void skin() {
        if (vertexData != null) {
            int ai[] = new int[256];
            int j = 0;
            for (int l = 0; l < verticesCount; l++) {
                int j1 = vertexData[l];
                ai[j1]++;
                if (j1 > j)
                    j = j1;
            }
            vertexGroups = new int[j + 1][];
            for (int k1 = 0; k1 <= j; k1++) {
                vertexGroups[k1] = new int[ai[k1]];
                ai[k1] = 0;
            }
            for (int j2 = 0; j2 < verticesCount; j2++) {
                int l2 = vertexData[j2];
                vertexGroups[l2][ai[l2]++] = j2;
            }
            vertexData = null;
        }
        if (triangleData != null) {
            int ai1[] = new int[256];
            int k = 0;
            for (int i1 = 0; i1 < trianglesCount; i1++) {
                int l1 = triangleData[i1];
                ai1[l1]++;
                if (l1 > k)
                    k = l1;
            }
            faceGroups = new int[k + 1][];
            for (int uid = 0; uid <= k; uid++) {
                faceGroups[uid] = new int[ai1[uid]];
                ai1[uid] = 0;
            }
            for (int k2 = 0; k2 < trianglesCount; k2++) {
                int i3 = triangleData[k2];
                faceGroups[i3][ai1[i3]++] = k2;
            }
            triangleData = null;
        }
    }

    private void transform(int opcode, int skin[], int x, int y, int z) {
        int length = skin.length;
        if (opcode == 0) {
            int offset = 0;
            xAnimOffset = 0;
            yAnimOffset = 0;
            zAnimOffset = 0;
            for (int skin_index = 0; skin_index < length; skin_index++) {
                int id = skin[skin_index];
                if (id < vertexGroups.length) {
                    int vertex[] = vertexGroups[id];
                    for (int index = 0; index < vertex.length; index++) {
                        int tri = vertex[index];
                        xAnimOffset += verticesX[tri];
                        yAnimOffset += verticesY[tri];
                        zAnimOffset += verticesZ[tri];
                        offset++;
                    }
                }
            }
            if (offset > 0) {
                xAnimOffset = xAnimOffset / offset + x;
                yAnimOffset = yAnimOffset / offset + y;
                zAnimOffset = zAnimOffset / offset + z;
                return;
            } else {
                xAnimOffset = x;
                yAnimOffset = y;
                zAnimOffset = z;
                return;
            }
        }
        if (opcode == 1) {
            for (int skin_index = 0; skin_index < length; skin_index++) {
                int id = skin[skin_index];
                if (id < vertexGroups.length) {
                    int vertex[] = vertexGroups[id];
                    for (int index = 0; index < vertex.length; index++) {
                        int tri = vertex[index];
                        verticesX[tri] += x;
                        verticesY[tri] += y;
                        verticesZ[tri] += z;
                    }
                }
            }
            return;
        }
        if (opcode == 2) {//rotation
            for (int skin_index = 0; skin_index < length; skin_index++) {
                int id = skin[skin_index];
                if (id < vertexGroups.length) {
                    int vertex[] = vertexGroups[id];
                    for (int index = 0; index < vertex.length; index++) {
                        int tri = vertex[index];
                        verticesX[tri] -= xAnimOffset;
                        verticesY[tri] -= yAnimOffset;
                        verticesZ[tri] -= zAnimOffset;
                        //int k6 = (x & 0xff) * 8;
                        //int l6 = (y & 0xff) * 8;
                        //int i7 = (z & 0xff) * 8;
                        if (z != 0) {//if (i7 != 0) {
                            int rot_x = SINE[z];//i7
                            int rot_y = COSINE[z];//i7
                            int rot_z = verticesY[tri] * rot_x + verticesX[tri] * rot_y >> 16;
                            verticesY[tri] = verticesY[tri] * rot_y - verticesX[tri] * rot_x >> 16;
                            verticesX[tri] = rot_z;
                        }
                        if (x != 0) {//if (k6 != 0) {
                            int rot_x = SINE[x];//k6
                            int rot_y = COSINE[x];//k6
                            int rot_z = verticesY[tri] * rot_y - verticesZ[tri] * rot_x >> 16;
                            verticesZ[tri] = verticesY[tri] * rot_x + verticesZ[tri] * rot_y >> 16;
                            verticesY[tri] = rot_z;
                        }
                        if (y != 0) {//if (l6 != 0) {
                            int rot_x = SINE[y];//l6
                            int rot_y = COSINE[y];//l6
                            int rot_z = verticesZ[tri] * rot_x + verticesX[tri] * rot_y >> 16;
                            verticesZ[tri] = verticesZ[tri] * rot_y - verticesX[tri] * rot_x >> 16;
                            verticesX[tri] = rot_z;
                        }
                        verticesX[tri] += xAnimOffset;
                        verticesY[tri] += yAnimOffset;
                        verticesZ[tri] += zAnimOffset;
                    }

                }
            }
            return;
        }
        if (opcode == 3) {
            for (int skin_index = 0; skin_index < length; skin_index++) {
                int id = skin[skin_index];
                if (id < vertexGroups.length) {
                    int vertex[] = vertexGroups[id];
                    for (int index = 0; index < vertex.length; index++) {
                        int tri = vertex[index];
                        verticesX[tri] -= xAnimOffset;
                        verticesY[tri] -= yAnimOffset;
                        verticesZ[tri] -= zAnimOffset;
                        verticesX[tri] = (verticesX[tri] * x) / 128;
                        verticesY[tri] = (verticesY[tri] * y) / 128;
                        verticesZ[tri] = (verticesZ[tri] * z) / 128;
                        verticesX[tri] += xAnimOffset;
                        verticesY[tri] += yAnimOffset;
                        verticesZ[tri] += zAnimOffset;
                    }
                }
            }
            return;
        }
        if (opcode == 5 && faceGroups != null && alphas != null) {
            for (int skin_index = 0; skin_index < length; skin_index++) {
                int id = skin[skin_index];
                if (id < faceGroups.length) {
                    int face[] = faceGroups[id];
                    for (int index = 0; index < face.length; index++) {
                        int tri = face[index];

                        alphas[tri] += x * 8;
                        if (alphas[tri] < 0)
                            alphas[tri] = 0;

                        if (alphas[tri] > 255)
                            alphas[tri] = 255;

                    }
                }
            }
        }
    }


    private void transformSkin(int animationType, int skin[], int x, int y, int z) {

        int i1 = skin.length;
        if (animationType == 0) {
            int j1 = 0;
            xAnimOffset = 0;
            yAnimOffset = 0;
            zAnimOffset = 0;
            for (int k2 = 0; k2 < i1; k2++) {
                int l3 = skin[k2];
                if (l3 < vertexGroups.length) {
                    int ai5[] = vertexGroups[l3];
                    for (int i5 = 0; i5 < ai5.length; i5++) {
                        int j6 = ai5[i5];
                        xAnimOffset += verticesX[j6];
                        yAnimOffset += verticesY[j6];
                        zAnimOffset += verticesZ[j6];
                        j1++;
                    }

                }
            }

            if (j1 > 0) {
                xAnimOffset = (int) (xAnimOffset / j1 + x);
                yAnimOffset = (int) (yAnimOffset / j1 + y);
                zAnimOffset = (int) (zAnimOffset / j1 + z);
                return;
            } else {
                xAnimOffset = (int) x;
                yAnimOffset = (int) y;
                zAnimOffset = (int) z;
                return;
            }
        }
        if (animationType == 1) {
            for (int k1 = 0; k1 < i1; k1++) {
                int l2 = skin[k1];
                if (l2 < vertexGroups.length) {
                    int ai1[] = vertexGroups[l2];
                    for (int i4 = 0; i4 < ai1.length; i4++) {
                        int j5 = ai1[i4];
                        verticesX[j5] += x;
                        verticesY[j5] += y;
                        verticesZ[j5] += z;
                    }

                }
            }

            return;
        }
        if (animationType == 2) {
            for (int l1 = 0; l1 < i1; l1++) {
                int i3 = skin[l1];
                if (i3 < vertexGroups.length) {
                    int auid[] = vertexGroups[i3];
                    for (int j4 = 0; j4 < auid.length; j4++) {
                        int k5 = auid[j4];
                        verticesX[k5] -= xAnimOffset;
                        verticesY[k5] -= yAnimOffset;
                        verticesZ[k5] -= zAnimOffset;
                        int k6 = (x & 0xff) * 8;
                        int l6 = (y & 0xff) * 8;
                        int i7 = (z & 0xff) * 8;
                        if (i7 != 0) {
                            int j7 = SINE[i7];
                            int i8 = COSINE[i7];
                            int l8 = verticesY[k5] * j7 + verticesX[k5] * i8 >> 16;
                            verticesY[k5] = verticesY[k5] * i8 - verticesX[k5] * j7 >> 16;
                            verticesX[k5] = l8;
                        }
                        if (k6 != 0) {
                            int k7 = SINE[k6];
                            int j8 = COSINE[k6];
                            int i9 = verticesY[k5] * j8 - verticesZ[k5] * k7 >> 16;
                            verticesZ[k5] = verticesY[k5] * k7 + verticesZ[k5] * j8 >> 16;
                            verticesY[k5] = i9;
                        }
                        if (l6 != 0) {
                            int l7 = SINE[l6];
                            int k8 = COSINE[l6];
                            int j9 = verticesZ[k5] * l7 + verticesX[k5] * k8 >> 16;
                            verticesZ[k5] = verticesZ[k5] * k8 - verticesX[k5] * l7 >> 16;
                            verticesX[k5] = j9;
                        }
                        verticesX[k5] += xAnimOffset;
                        verticesY[k5] += yAnimOffset;
                        verticesZ[k5] += zAnimOffset;
                    }

                }
            }

            return;
        }
        if (animationType == 3) {
            for (int uid = 0; uid < i1; uid++) {
                int j3 = skin[uid];
                if (j3 < vertexGroups.length) {
                    int ai3[] = vertexGroups[j3];
                    for (int k4 = 0; k4 < ai3.length; k4++) {
                        int l5 = ai3[k4];
                        verticesX[l5] -= xAnimOffset;
                        verticesY[l5] -= yAnimOffset;
                        verticesZ[l5] -= zAnimOffset;
                        verticesX[l5] = (int) ((verticesX[l5] * x) / 128);
                        verticesY[l5] = (int) ((verticesY[l5] * y) / 128);
                        verticesZ[l5] = (int) ((verticesZ[l5] * z) / 128);
                        verticesX[l5] += xAnimOffset;
                        verticesY[l5] += yAnimOffset;
                        verticesZ[l5] += zAnimOffset;
                    }

                }
            }

            return;
        }
        if (animationType == 5 && faceGroups != null && alphas != null) {
            for (int j2 = 0; j2 < i1; j2++) {
                int k3 = skin[j2];
                if (k3 < faceGroups.length) {
                    int ai4[] = faceGroups[k3];
                    for (int l4 = 0; l4 < ai4.length; l4++) {
                        int i6 = ai4[l4];
                        alphas[i6] += x * 8;
                        if (alphas[i6] < 0)
                            alphas[i6] = 0;
                        if (alphas[i6] > 255)
                            alphas[i6] = 255;
                    }

                }
            }

        }
    }

    public void interpolate(int frameId) {
        if (vertexGroups == null)
            return;

        if (frameId == -1)
            return;

        Animation frame = Animation.get(frameId);
        if (frame == null)
            return;

        Skins skin = frame.skins;
        xAnimOffset = 0;
        yAnimOffset = 0;
        zAnimOffset = 0;

        for (int index = 0; index < frame.frames; index++) {
            int pos = frame.translation_modifier[index];
            //Change skin.cache[pos] to skin.cache[2] for funny animations
            transform(skin.opcodes[pos], skin.cache[pos], frame.x_modifier[index], frame.y_modifier[index], frame.z_modifier[index]);

        }

    }


    public void mix(int label[], int idle, int current) {
        if (current == -1)
            return;

        if (label == null || idle == -1) {
            interpolate(current);
            return;
        }
        Animation anim = Animation.get(current);
        if (anim == null)
            return;

        Animation skin = Animation.get(idle);
        if (skin == null) {
            interpolate(current);
            return;
        }
        Skins list = anim.skins;
        xAnimOffset = 0;
        yAnimOffset = 0;
        zAnimOffset = 0;
        int id = 0;
        int table = label[id++];
        for (int index = 0; index < anim.frames; index++) {
            int condition;
            for (condition = anim.translation_modifier[index]; condition > table; table = label[id++])
                ;//empty
            if (condition != table || list.opcodes[condition] == 0)
                transform(list.opcodes[condition], list.cache[condition], anim.x_modifier[index], anim.y_modifier[index], anim.z_modifier[index]);
        }
        xAnimOffset = 0;
        yAnimOffset = 0;
        zAnimOffset = 0;
        id = 0;
        table = label[id++];
        for (int index = 0; index < skin.frames; index++) {
            int condition;
            for (condition = skin.translation_modifier[index]; condition > table; table = label[id++])
                ;//empty
            if (condition == table || list.opcodes[condition] == 0)
                transform(list.opcodes[condition], list.cache[condition], skin.x_modifier[index], skin.y_modifier[index], skin.z_modifier[index]);

        }
    }


    public void rotate_90() {
        for (int point = 0; point < verticesCount; point++) {
            int k = verticesX[point];
            verticesX[point] = verticesZ[point];
            verticesZ[point] = -k;
        }
    }

    public void leanOverX(int i) {
        int k = SINE[i];
        int l = COSINE[i];
        for (int point = 0; point < verticesCount; point++) {
            int j1 = verticesY[point] * l - verticesZ[point] * k >> 16;
            verticesZ[point] = verticesY[point] * k + verticesZ[point] * l >> 16;
            verticesY[point] = j1;
        }
    }

    public void translate(int x, int y, int z) {
        for (int point = 0; point < verticesCount; point++) {
            verticesX[point] += x;
            verticesY[point] += y;
            verticesZ[point] += z;
        }
    }

    public void recolor(int found, int replace) {
        if (colors != null)
            for (int face = 0; face < trianglesCount; face++)
                if (colors[face] == (short) found)
                    colors[face] = (short) replace;
    }

    public void retexture(short found, short replace) {
        if(materials != null) {
            for (int face = 0; face < trianglesCount; face++) {
                if (materials[face] == found) {
                    materials[face] = replace;
                }
            }
        }
    }

    public void color_to_texture(Model model, short src, short dst, boolean debug) {
        if(model.colors != null) {
            if(model != null) {
                //if(debug)
                //    System.out.println("model * " + Arrays.toString(model.color));//use to find the color you want to replace


                if(model.types == null) {
                    model.types = new int[model.trianglesCount];
                    for(int face = 0; face < trianglesCount; face++) {
                        if(model.colors[face] == src)
                            model.types[face] = 2;
                        else
                            model.types[face] = 0;

                    }
                } else {
                    for(int face = 0; face < trianglesCount; face++) {
                        if(model.colors[face] == src)
                            model.types[face] = 2;
                        else
                            model.types[face] = 0;
                    }
                }
                types = model.types;

                if(model.materials == null) {
                    model.materials = new short[model.trianglesCount];
                    for(int face = 0; face < trianglesCount; face++) {
                        if(model.colors[face] == src)
                            model.materials[face] = dst;

                    }
                } else {
                    for(int face = 0; face < trianglesCount; face++) {
                        if(model.colors[face] == src)
                            model.materials[face] = dst;

                    }
                }
                materials = model.materials;


                if(model.textures == null) {
                    model.textures = new byte[model.trianglesCount];
                }

                textureTypes = new byte[model.trianglesCount];
                texturesX = new short[model.trianglesCount];
                texturesY = new short[model.trianglesCount];
                texturesZ = new short[model.trianglesCount];
                for(int face = 0; face < model.trianglesCount; face++) {
                    if(model.colors[face] == src) {
                        model.textures[face] = (byte) 2;
                        model.colors[face] = 127;
                        model.textureTypes[face] = 1;
                        model.texturesCount++;
                    }
                    if(model.colors[face] != 127 && src == -1) {
                        types[face] = 0;
                        materials[face] = -1;
                    }
                }
                for(int face = 0; face < model.texturesCount; face++) {
                    model.texturesX[face] = (short) model.trianglesX[face];
                    model.texturesY[face] = (short) model.trianglesY[face];
                    model.texturesZ[face] = (short) model.trianglesZ[face];
                }
            }
        }
    }

    public void invert() {
        for (int index = 0; index < verticesCount; index++)
            verticesZ[index] = -verticesZ[index];

        for (int face = 0; face < trianglesCount; face++) {
            int l = trianglesX[face];
            trianglesX[face] = trianglesZ[face];
            trianglesZ[face] = l;
        }
    }

    public void scale(int i, int j, int l) {
        for (int index = 0; index < verticesCount; index++) {
            verticesX[index] = (verticesX[index] * i) / 128;
            verticesY[index] = (verticesY[index] * l) / 128;
            verticesZ[index] = (verticesZ[index] * j) / 128;
        }
    }
    public void light2(int i, int j, int k, int l, int i1, boolean flag) {
        light(i, j, k, l, i1, flag, false);
    }


    public void light(int i, int j, int k, int l, int i1, boolean flag, boolean player) {
        int j1 = (int) Math.sqrt(k * k + l * l + i1 * i1);
        int k1 = j * j1 >> 8;
        colorsX = new int[trianglesCount];
        colorsY = new int[trianglesCount];
        colorsZ = new int[trianglesCount];
        if (super.normals == null) {
            super.normals = new VertexNormal[verticesCount];
            for (int index = 0; index < verticesCount; index++)
                super.normals[index] = new VertexNormal();

        }
        for (int face = 0; face < trianglesCount; face++) {
            int j2 = trianglesX[face];
            int l2 = trianglesY[face];
            int i3 = trianglesZ[face];
            int j3 = verticesX[l2] - verticesX[j2];
            int k3 = verticesY[l2] - verticesY[j2];
            int l3 = verticesZ[l2] - verticesZ[j2];
            int i4 = verticesX[i3] - verticesX[j2];
            int j4 = verticesY[i3] - verticesY[j2];
            int k4 = verticesZ[i3] - verticesZ[j2];
            int l4 = k3 * k4 - j4 * l3;
            int i5 = l3 * i4 - k4 * j3;
            int j5;
            for (j5 = j3 * j4 - i4 * k3; l4 > 8192 || i5 > 8192 || j5 > 8192 || l4 < -8192 || i5 < -8192 || j5 < -8192; j5 >>= 1) {
                l4 >>= 1;
                i5 >>= 1;
            }
            int k5 = (int) Math.sqrt(l4 * l4 + i5 * i5 + j5 * j5);
            if (k5 <= 0)
                k5 = 1;

            l4 = (l4 * 256) / k5;
            i5 = (i5 * 256) / k5;
            j5 = (j5 * 256) / k5;

            int texture_id;
            int type;
            if (types != null)
                type = types[face];
            else
                type = 0;

            if (materials == null) {
                texture_id = -1;
            } else {
                texture_id = materials[face];
            }

            if (types == null || (types[face] & 1) == 0) {
                VertexNormal class33_2 = super.normals[j2];
                class33_2.x += l4;
                class33_2.y += i5;
                class33_2.z += j5;
                class33_2.magnitude++;
                class33_2 = super.normals[l2];
                class33_2.x += l4;
                class33_2.y += i5;
                class33_2.z += j5;
                class33_2.magnitude++;
                class33_2 = super.normals[i3];
                class33_2.x += l4;
                class33_2.y += i5;
                class33_2.z += j5;
                class33_2.magnitude++;
            } else {
                if (texture_id != -1) {
                    type = 2;
                }
                int light = i + (k * l4 + l * i5 + i1 * j5) / (k1 + k1 / 2);
                colorsX[face] = method481(colors[face], light, type);
            }
        }
        if (flag) {
            method480(i, k1, k, l, i1, player);
            calc_diagonals();//method466
        } else {
            vertexNormals = new VertexNormal[verticesCount];
            for (int point = 0; point < verticesCount; point++) {
                VertexNormal class33 = super.normals[point];
                VertexNormal class33_1 = vertexNormals[point] = new VertexNormal();
                class33_1.x = class33.x;
                class33_1.y = class33.y;
                class33_1.z = class33.z;
                class33_1.magnitude = class33.magnitude;
            }
            calculateVertexData(21073);
        }
    }
    public void light() {
        if (vertexNormals == null) {
            vertexNormals = new VertexNormal[verticesCount];

            int var1;
            for (var1 = 0; var1 < verticesCount; ++var1) {
                vertexNormals[var1] = new VertexNormal();
            }

            for (var1 = 0; var1 < trianglesCount; ++var1) {
                final int var2 = trianglesX[var1];
                final int var3 = trianglesY[var1];
                final int var4 = trianglesZ[var1];
                final int var5 = verticesX[var3] - verticesX[var2];
                final int var6 = verticesY[var3] - verticesY[var2];
                final int var7 = verticesZ[var3] - verticesZ[var2];
                final int var8 = verticesX[var4] - verticesX[var2];
                final int var9 = verticesY[var4] - verticesY[var2];
                final int var10 = verticesZ[var4] - verticesZ[var2];
                int var11 = var6 * var10 - var9 * var7;
                int var12 = var7 * var8 - var10 * var5;

                int var13;
                for (var13 = var5 * var9 - var8 * var6; var11 > 8192 || var12 > 8192 || var13 > 8192 || var11 < -8192 || var12 < -8192 || var13 < -8192; var13 >>= 1) {
                    var11 >>= 1;
                    var12 >>= 1;
                }

                int var14 = (int)Math.sqrt((double)(var11 * var11 + var12 * var12 + var13 * var13)); // L: 1368
                if (var14 <= 0) {
                    var14 = 1;
                }

                var11 = var11 * 256 / var14;
                var12 = var12 * 256 / var14;
                var13 = var13 * 256 / var14;
                final int var15;
                if (types == null) {
                    var15 = 0;
                } else {
                    var15 = types[var1];
                }

                if (var15 == 0) {
                    VertexNormal var16 = vertexNormals[var2];
                    var16.x += var11;
                    var16.y += var12;
                    var16.z += var13;
                    ++var16.magnitude;
                    var16 = vertexNormals[var3];
                    var16.x += var11;
                    var16.y += var12;
                    var16.z += var13;
                    ++var16.magnitude;
                    var16 = vertexNormals[var4];
                    var16.x += var11;
                    var16.y += var12;
                    var16.z += var13;
                    ++var16.magnitude;
                } else if (var15 == 1) {
                    if (faceNormals == null) {
                        faceNormals = new FaceNormal[trianglesCount];
                    }

                    final FaceNormal var17 = faceNormals[var1] = new FaceNormal();
                    var17.x = var11;
                    var17.y = var12;
                    var17.z = var13;
                }
            }
        }
    }
    public void light(final int ambient, final int contrast, final int x, final int y, final int z, final boolean flag) {

        light();
        final int magnitude = (int) Math.sqrt((double) (x * x + y * y + z * z));
        final int k1 = contrast * magnitude >> 8;
        colorsX = new int[trianglesCount];
        colorsY = new int[trianglesCount];
        colorsZ = new int[trianglesCount];

        for (int var16 = 0; var16 < trianglesCount; ++var16) {
            int var17;
            if (types == null) {
                var17 = 0;
            } else {
                var17 = types[var16];
            }

            final int var18;
            if (alphas == null) {
                var18 = 0;
            } else {
                var18 = alphas[var16];
            }

            final short var12;
            if (materials == null) {
                var12 = -1;
            } else {
                var12 = materials[var16];
            }

            if (var18 == -2) {
                var17 = 3;
            }

            if (var18 == -1) {
                var17 = 2;
            }

            VertexNormal var13;
            int var14;
            final FaceNormal var19;
            if (var12 == -1) {
                if (var17 == 0) {
                    final int var15 = colors[var16];
                    if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesX[var16]] != null) {
                        var13 = vertexNormalsOffsets[trianglesX[var16]];
                    } else {
                        var13 = vertexNormals[trianglesX[var16]];
                    }

                    var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                    colorsX[var16] = method2792(var15, var14);
                    if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesY[var16]] != null) {
                        var13 = vertexNormalsOffsets[trianglesY[var16]];
                    } else {
                        var13 = vertexNormals[trianglesY[var16]];
                    }

                    var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                    colorsY[var16] = method2792(var15, var14);
                    if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesZ[var16]] != null) {
                        var13 = vertexNormalsOffsets[trianglesZ[var16]];
                    } else {
                        var13 = vertexNormals[trianglesZ[var16]];
                    }

                    var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                    colorsZ[var16] = method2792(var15, var14);
                } else if (var17 == 1) {
                    var19 = faceNormals[var16];
                    var14 = (y * var19.y + z * var19.z + x * var19.x) / (k1 / 2 + k1) + ambient;
                    colorsX[var16] = method2792(colors[var16], var14);
                    colorsZ[var16] = -1;
                } else if (var17 == 3) {
                    colorsX[var16] = 128;
                    colorsZ[var16] = -1;
                } else {
                    colorsZ[var16] = -2;
                }
            } else if (var17 == 0) {
                if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesX[var16]] != null) {
                    var13 = vertexNormalsOffsets[trianglesX[var16]];
                } else {
                    var13 = vertexNormals[trianglesX[var16]];
                }

                var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                colorsX[var16] = method2820(var14);
                if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesY[var16]] != null) {
                    var13 = vertexNormalsOffsets[trianglesY[var16]];
                } else {
                    var13 = vertexNormals[trianglesY[var16]];
                }

                var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                colorsY[var16] = method2820(var14);
                if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesZ[var16]] != null) {
                    var13 = vertexNormalsOffsets[trianglesZ[var16]];
                } else {
                    var13 = vertexNormals[trianglesZ[var16]];
                }

                var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                colorsZ[var16] = method2820(var14);
            } else if (var17 == 1) {
                var19 = faceNormals[var16];
                var14 = (y * var19.y + z * var19.z + x * var19.x) / (k1 / 2 + k1) + ambient;
                colorsX[var16] = method2820(var14);
                colorsZ[var16] = -1;
            } else {
                colorsZ[var16] = -2;
            }
        }
        calc_diagonals();
          if (textures == null) {
              calculateVertexData(21073);
          }

    }
    private int method2792(final int var0, int var1) {
        var1 = (var0 & 127) * var1 >> 7;
        if (var1 < 2) {
            var1 = 2;
        } else if (var1 > 126) {
            var1 = 126;
        }

        return (var0 & '\uff80') + var1;
    }
    private int method2820(int var0) {
        if (var0 < 2) {
            var0 = 2;
        } else if (var0 > 126) {
            var0 = 126;
        }

        return var0;
    }
    public final void doShading(int i, int j, int k, int l, int i1) {
        method480(i, j, k, l, i1, false);
    }

    public final void method480(int i, int j, int k, int l, int i1, boolean player) {
        for (int j1 = 0; j1 < trianglesCount; j1++) {
            int k1 = trianglesX[j1];
            int i2 = trianglesY[j1];
            int j2 = trianglesZ[j1];
            int texture_id;
            if(materials == null) {
                texture_id = -1;
            } else {
                texture_id = materials[j1];
                if (player) {
                    if(alphas != null && colors != null) {
                        if(colors[j1] == 0 && face_render_priorities[j1] == 0) {
                            if(types[j1] == 2 && materials[j1] == -1) {
                                alphas[j1] = 255;
                            }
                        }
                    } else if(alphas == null) {
                        if(colors[j1] == 0 && face_render_priorities[j1] == 0) {
                            if(materials[j1] == -1) {
                                alphas = new int[trianglesCount];
                                if(types[j1] == 2) {
                                    alphas[j1] = 255;
                                }
                            }
                        }
                    }
                }
            }

            if (types == null) {
                int type;
                if(texture_id != -1) {
                    type = 2;
                } else {
                    type = 1;
                }
                int hsl = colors[j1] & 0xffff;
                VertexNormal vertex = super.normals[k1];
                int light = i + (k * vertex.x + l * vertex.y + i1 * vertex.z) / (j * vertex.magnitude);
                colorsX[j1] = method481(hsl, light, type);
                vertex = super.normals[i2];
                light = i + (k * vertex.x + l * vertex.y + i1 * vertex.z) / (j * vertex.magnitude);
                colorsY[j1] = method481(hsl, light, type);
                vertex = super.normals[j2];
                light = i + (k * vertex.x + l * vertex.y + i1 * vertex.z) / (j * vertex.magnitude);
                colorsZ[j1] = method481(hsl, light, type);
            } else if ((types[j1] & 1) == 0) {
                int type = types[j1];
                if(texture_id != -1) {
                    type = 2;
                }
                int hsl = colors[j1] & 0xffff;
                VertexNormal vertex = super.normals[k1];
                int light = i + (k * vertex.x + l * vertex.y + i1 * vertex.z) / (j * vertex.magnitude);
                colorsX[j1] = method481(hsl, light, type);
                vertex = super.normals[i2];
                light = i + (k * vertex.x + l * vertex.y + i1 * vertex.z) / (j * vertex.magnitude);
                colorsY[j1] = method481(hsl, light, type);
                vertex = super.normals[j2];
                light = i + (k * vertex.x + l * vertex.y + i1 * vertex.z) / (j * vertex.magnitude);
                colorsZ[j1] = method481(hsl, light, type);
            }
        }

        super.normals = null;
        vertexNormals = null;
        vertexData = null;
        triangleData = null;
        colors = null;
    }

    public static final int method481(int i, int j, int k) {
        if (i == 65535)
            return 0;

        if ((k & 2) == 2) {
            if (j < 0)
                j = 0;
            else if (j > 127)
                j = 127;

            j = 127 - j;
            return j;
        }

        j = j * (i & 0x7f) >> 7;
        if (j < 2)
            j = 2;
        else if (j > 126)
            j = 126;

        return (i & 0xff80) + j;
    }

    //inventory / widget model rendering (render_2D)
    public final void render_2D(int roll, int yaw, int pitch, int start_x, int start_y, int zoom) {
        int depth = 0;
        int center_x = Rasterizer3D.center_x;
        int center_y = Rasterizer3D.center_y;
        int depth_sin = SINE[depth];
        int depth_cos = COSINE[depth];
        int roll_sin = SINE[roll];
        int roll_cos = COSINE[roll];
        int yaw_sin = SINE[yaw];
        int yaw_cos = COSINE[yaw];
        int pitch_sin = SINE[pitch];
        int pitch_cos = COSINE[pitch];
        int position = start_y * pitch_sin + zoom * pitch_cos >> 16;
        for (int index = 0; index < verticesCount; index++) {
            int x = verticesX[index];
            int y = verticesY[index];
            int z = verticesZ[index];
            if (yaw != 0) {
                int rotated_x = y * yaw_sin + x * yaw_cos >> 16;
                y = y * yaw_cos - x * yaw_sin >> 16;
                x = rotated_x;
            }
            if (depth != 0) {
                int rotated_y = y * depth_cos - z * depth_sin >> 16;
                z = y * depth_sin + z * depth_cos >> 16;
                y = rotated_y;
            }
            if (roll != 0) {
                int rotated_z = z * roll_sin + x * roll_cos >> 16;
                z = z * roll_cos - x * roll_sin >> 16;
                x = rotated_z;
            }
            x += start_x;
            y += start_y;
            z += zoom;

            int y_offset = y * pitch_cos - z * pitch_sin >> 16;
            z = y * pitch_sin + z * pitch_cos >> 16;
            y = y_offset;

            projected_vertex_z[index] = z - position;
            projected_vertex_x[index] = center_x + (x << 9) / z;
            projected_vertex_y[index] = center_y + (y << 9) / z;
            if (texturesCount > 0) {
                camera_vertex_x[index] = x;
                camera_vertex_y[index] = y;
                camera_vertex_z[index] = z;
            }

        }
        try {
            method483(false, false,  0);
        } catch (Exception _ex) {
            _ex.printStackTrace();
            System.out.println("Could not rotate and project item!");
        }
    }
    public static final int VIEW_DISTANCE = 3500; //3500 or 4500, 3500 provides better performance.

    @Override
    public final void render_3D(int orientation, int cos_y, int sin_y, int sin_x, int cos_x, int start_x, int start_y, int depth, long uid) {

        int scene_x = depth * cos_x - start_x * sin_x >> 16;
        int scene_y = start_y * cos_y + scene_x * sin_y >> 16;
        int dimension_sin_y = XYZMag * sin_y >> 16;
        int pos = scene_y + dimension_sin_y;
        if (pos <= 50 || scene_y >= VIEW_DISTANCE)
            return;

        int x_rot = depth * sin_x + start_x * cos_x >> 16;
        int obj_x = x_rot - XYZMag << SceneGraph.view_dist;
        if (obj_x / pos >= Rasterizer2D.viewport_center_y)
            return;

        int obj_width = x_rot + XYZMag << SceneGraph.view_dist;
        if (obj_width / pos <= -Rasterizer2D.viewport_center_y)
            return;

        int y_rot = start_y * sin_y - scene_x * cos_y >> 16;
        int dimension_cos_y = XYZMag * cos_y >> 16;
        int obj_height = y_rot + dimension_cos_y << SceneGraph.view_dist;
        if (obj_height / pos <= -Rasterizer2D.viewport_center_x)
            return;


        int offset = dimension_cos_y + (super.model_height * sin_y >> 16);
        int obj_y = y_rot - offset << SceneGraph.view_dist;
        if (obj_y / pos >= Rasterizer2D.viewport_center_x)
            return;


        int size = dimension_sin_y + (super.model_height * cos_y >> 16);
        boolean flag = false;
        if (scene_y - size <= 50)
            flag = true;

        boolean flag1 = false;
        if (uid > 0 && obj_exists) {
            int obj_height_offset = scene_y - offset;
            if (obj_height_offset <= 50)
                obj_height_offset = 50;
            if (x_rot > 0) {
                obj_x /= pos;
                obj_width /= obj_height_offset;
            } else {
                obj_width /= pos;
                obj_x /= obj_height_offset;
            }
            if (y_rot > 0) {
                obj_y /= pos;
                obj_height /= obj_height_offset;
            } else {
                obj_height /= pos;
                obj_y /= obj_height_offset;
            }
            int mouse_x = anInt1685 - Rasterizer3D.center_x;
            int mouse_y = anInt1686 - Rasterizer3D.center_y;
            if (mouse_x > obj_x && mouse_x < obj_width && mouse_y > obj_y && mouse_y < obj_height)
                if (fits_on_single_square)
                    anIntArray1688[anInt1687++] = uid;
                else
                    flag1 = true;
        }
        int center_x = Rasterizer3D.center_x;
        int center_y = Rasterizer3D.center_y;
        int sine_x = 0;
        int cosine_x = 0;
        if (orientation != 0) {
            sine_x = SINE[orientation];
            cosine_x = COSINE[orientation];
        }
        for (int index = 0; index < verticesCount; index++) {
            int raster_x = verticesX[index];
            int raster_y = verticesY[index];
            int raster_z = verticesZ[index];

            if (orientation != 0) {
                int rotated_x = raster_z * sine_x + raster_x * cosine_x >> 16;
                raster_z = raster_z * cosine_x - raster_x * sine_x >> 16;
                raster_x = rotated_x;

            }
            raster_x += start_x;
            raster_y += start_y;
            raster_z += depth;

            int position = raster_z * sin_x + raster_x * cos_x >> 16;
            raster_z = raster_z * cos_x - raster_x * sin_x >> 16;
            raster_x = position;

            position = raster_y * sin_y - raster_z * cos_y >> 16;
            raster_z = raster_y * cos_y + raster_z * sin_y >> 16;
            raster_y = position;


            projected_vertex_z[index] = raster_z - scene_y;
            if (raster_z >= 50) {
                projected_vertex_x[index] = center_x + (raster_x << SceneGraph.view_dist) / raster_z;
                projected_vertex_y[index] = center_y + (raster_y << SceneGraph.view_dist) / raster_z;
            } else {
                projected_vertex_x[index] = -5000;
                flag = true;
            }
            if (flag || texturesCount > 0) {
                camera_vertex_x[index] = raster_x;
                camera_vertex_y[index] = raster_y;
                camera_vertex_z[index] = raster_z;
            }
        }
        try {
            method483(flag, flag1,  uid);
            return;
        } catch (Exception _ex) {
            return;
        }
    }

    private void method483(boolean flag, boolean flag1, long uid) {

        for (int j = 0; j < maxRenderDepth; j++)
            depthListIndices[j] = 0;

        for (int face = 0; face < trianglesCount; face++) {
            if (types == null || types[face] != -1) {
                int a = trianglesX[face];
                int b = trianglesY[face];
                int c = trianglesZ[face];
                int x_a = projected_vertex_x[a];
                int x_b = projected_vertex_x[b];
                int x_c = projected_vertex_x[c];
                if (flag && (x_a == -5000 || x_b == -5000 || x_c == -5000)) {
                    outOfReach[face] = true;
                    int j5 = (projected_vertex_z[a] + projected_vertex_z[b] + projected_vertex_z[c]) / 3 + diagonal3DAboveOrigin;
                    faceLists[j5][depthListIndices[j5]++] = face;
                } else {
                    if (flag1 && entered_clickbox(anInt1685, anInt1686, projected_vertex_y[a], projected_vertex_y[b], projected_vertex_y[c], x_a, x_b, x_c)) {
                        anIntArray1688[anInt1687++] = uid;
                        flag1 = false;
                    }
                    if ((x_a - x_b) * (projected_vertex_y[c] - projected_vertex_y[b]) - (projected_vertex_y[a] - projected_vertex_y[b]) * (x_c - x_b) > 0) {
                        outOfReach[face] = false;
                        if (x_a < 0 || x_b < 0 || x_c < 0 || x_a > Rasterizer2D.center_x || x_b > Rasterizer2D.center_x || x_c > Rasterizer2D.center_x)
                            hasAnEdgeToRestrict[face] = true;
                        else
                            hasAnEdgeToRestrict[face] = false;

                        int k5 = (projected_vertex_z[a] + projected_vertex_z[b] + projected_vertex_z[c]) / 3 + diagonal3DAboveOrigin;
                        faceLists[k5][depthListIndices[k5]++] = face;
                    }
                }
            }
        }
        if (face_render_priorities == null) {
            for (int i1 = maxRenderDepth - 1; i1 >= 0; i1--) {
                int l1 = depthListIndices[i1];
                if (l1 > 0) {
                    int ai[] = faceLists[i1];
                    for (int j3 = 0; j3 < l1; j3++)
                        rasterize(ai[j3]);

                }
            }
            return;
        }
        for (int j1 = 0; j1 < 12; j1++) {
            anIntArray1673[j1] = 0;
            anIntArray1677[j1] = 0;
        }
        for (int i2 = maxRenderDepth - 1; i2 >= 0; i2--) {
            int k2 = depthListIndices[i2];
            if (k2 > 0) {
                int ai1[] = faceLists[i2];
                for (int i4 = 0; i4 < k2; i4++) {
                    int l4 = ai1[i4];
                    byte l5 = face_render_priorities[l4];
                    int j6 = anIntArray1673[l5]++;
                    anIntArrayArray1674[l5][j6] = l4;
                    if (l5 < 10)
                        anIntArray1677[l5] += i2;
                    else if (l5 == 10)
                        anIntArray1675[j6] = i2;
                    else
                        anIntArray1676[j6] = i2;
                }

            }
        }

        int l2 = 0;
        if (anIntArray1673[1] > 0 || anIntArray1673[2] > 0)
            l2 = (anIntArray1677[1] + anIntArray1677[2]) / (anIntArray1673[1] + anIntArray1673[2]);
        int k3 = 0;
        if (anIntArray1673[3] > 0 || anIntArray1673[4] > 0)
            k3 = (anIntArray1677[3] + anIntArray1677[4]) / (anIntArray1673[3] + anIntArray1673[4]);
        int j4 = 0;
        if (anIntArray1673[6] > 0 || anIntArray1673[8] > 0)
            j4 = (anIntArray1677[6] + anIntArray1677[8]) / (anIntArray1673[6] + anIntArray1673[8]);

        int i6 = 0;
        int k6 = anIntArray1673[10];
        int auid[] = anIntArrayArray1674[10];
        int ai3[] = anIntArray1675;
        if (i6 == k6) {
            i6 = 0;
            k6 = anIntArray1673[11];
            auid = anIntArrayArray1674[11];
            ai3 = anIntArray1676;
        }
        int i5;
        if (i6 < k6)
            i5 = ai3[i6];
        else
            i5 = -1000;

        for (int l6 = 0; l6 < 10; l6++) {
            while (l6 == 0 && i5 > l2) {
                rasterize(auid[i6++]);
                if (i6 == k6 && auid != anIntArrayArray1674[11]) {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    auid = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if (i6 < k6)
                    i5 = ai3[i6];
                else
                    i5 = -1000;
            }
            while (l6 == 3 && i5 > k3) {
                rasterize(auid[i6++]);
                if (i6 == k6 && auid != anIntArrayArray1674[11]) {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    auid = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if (i6 < k6)
                    i5 = ai3[i6];
                else
                    i5 = -1000;
            }
            while (l6 == 5 && i5 > j4) {
                rasterize(auid[i6++]);
                if (i6 == k6 && auid != anIntArrayArray1674[11]) {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    auid = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if (i6 < k6)
                    i5 = ai3[i6];
                else
                    i5 = -1000;
            }
            int i7 = anIntArray1673[l6];
            int ai4[] = anIntArrayArray1674[l6];
            for (int j7 = 0; j7 < i7; j7++)
                rasterize(ai4[j7]);

        }
        while (i5 != -1000) {
            rasterize(auid[i6++]);
            if (i6 == k6 && auid != anIntArrayArray1674[11]) {
                i6 = 0;
                auid = anIntArrayArray1674[11];
                k6 = anIntArray1673[11];
                ai3 = anIntArray1676;
            }
            if (i6 < k6)
                i5 = ai3[i6];
            else
                i5 = -1000;
        }
    }

    private final void rasterize(int face) {
        if (outOfReach[face]) {
            method485(face);
            return;
        }
        int j = trianglesX[face];
        int k = trianglesY[face];
        int l = trianglesZ[face];
        Rasterizer3D.testX = hasAnEdgeToRestrict[face];
        if (alphas == null)
            Rasterizer3D.alpha = 0;
        else
            Rasterizer3D.alpha = alphas[face] & 0xff;

        int type;
        if (types == null)
            type = 0;
        else
            type = types[face] & 3;

        if (!Rasterizer3D.forceRepeat) {
            if (repeatTexture == null) {
                Rasterizer3D.repeatTexture = false;
            } else {
                Rasterizer3D.repeatTexture = repeatTexture[face];
            }
        } else {
            Rasterizer3D.repeatTexture = true;
        }

        if (materials != null && materials[face] != -1) {
            int texture_a = j;
            int texture_b = k;
            int texture_c = l;
            if (textures != null && textures[face] != -1) {
                int coordinate = textures[face] & 0xff;
                texture_a = texturesX[coordinate];
                texture_b = texturesY[coordinate];
                texture_c = texturesZ[coordinate];
            }
            if (colorsZ[face] == -1 || type == 3) {
                Rasterizer3D.drawTexturedTriangle(
                    projected_vertex_y[j], projected_vertex_y[k], projected_vertex_y[l],
                    projected_vertex_x[j], projected_vertex_x[k], projected_vertex_x[l],
                    colorsX[face], colorsX[face], colorsX[face],
                    camera_vertex_x[texture_a], camera_vertex_x[texture_b], camera_vertex_x[texture_c],
                    camera_vertex_y[texture_a], camera_vertex_y[texture_b], camera_vertex_y[texture_c],
                    camera_vertex_z[texture_a], camera_vertex_z[texture_b], camera_vertex_z[texture_c],
                    materials[face]);
            } else {
                Rasterizer3D.drawTexturedTriangle(
                    projected_vertex_y[j], projected_vertex_y[k], projected_vertex_y[l],
                    projected_vertex_x[j], projected_vertex_x[k], projected_vertex_x[l],
                    colorsX[face], colorsY[face], colorsZ[face],
                    camera_vertex_x[texture_a], camera_vertex_x[texture_b], camera_vertex_x[texture_c],
                    camera_vertex_y[texture_a], camera_vertex_y[texture_b], camera_vertex_y[texture_c],
                    camera_vertex_z[texture_a], camera_vertex_z[texture_b], camera_vertex_z[texture_c],
                    materials[face]);
            }
        } else {
            if (type == 0) {
                Rasterizer3D.drawShadedTriangle(projected_vertex_y[j], projected_vertex_y[k],
                    projected_vertex_y[l], projected_vertex_x[j], projected_vertex_x[k],
                    projected_vertex_x[l], colorsX[face], colorsY[face], colorsZ[face]);
                return;
            }
            if (type == 1) {
                Rasterizer3D.drawFlatTriangle(projected_vertex_y[j], projected_vertex_y[k], projected_vertex_y[l], projected_vertex_x[j], projected_vertex_x[k], projected_vertex_x[l], modelIntArray3[colorsX[face]]);
                return;
            }
        }
    }

    private final void method485(int i) {
        int j = Rasterizer3D.center_x;
        int k = Rasterizer3D.center_y;
        int l = 0;
        int i1 = trianglesX[i];
        int j1 = trianglesY[i];
        int k1 = trianglesZ[i];
        int l1 = camera_vertex_z[i1];
        int uid = camera_vertex_z[j1];
        int j2 = camera_vertex_z[k1];
        if (l1 >= 50) {
            anIntArray1678[l] = projected_vertex_x[i1];
            anIntArray1679[l] = projected_vertex_y[i1];
            anIntArray1680[l++] = colorsX[i];
        } else {
            int k2 = camera_vertex_x[i1];
            int k3 = camera_vertex_y[i1];
            int k4 = colorsX[i];
            if (j2 >= 50) {
                int k5 = (50 - l1) * modelIntArray4[j2 - l1];
                anIntArray1678[l] = j + (k2 + ((camera_vertex_x[k1] - k2) * k5 >> 16) << SceneGraph.view_dist) / 50;
                anIntArray1679[l] = k + (k3 + ((camera_vertex_y[k1] - k3) * k5 >> 16) << SceneGraph.view_dist) / 50;
                anIntArray1680[l++] = k4 + ((colorsZ[i] - k4) * k5 >> 16);
            }
            if (uid >= 50) {
                int l5 = (50 - l1) * modelIntArray4[uid - l1];
                anIntArray1678[l] = j + (k2 + ((camera_vertex_x[j1] - k2) * l5 >> 16) << SceneGraph.view_dist) / 50;
                anIntArray1679[l] = k + (k3 + ((camera_vertex_y[j1] - k3) * l5 >> 16) << SceneGraph.view_dist) / 50;
                anIntArray1680[l++] = k4 + ((colorsY[i] - k4) * l5 >> 16);
            }
        }
        if (uid >= 50) {
            anIntArray1678[l] = projected_vertex_x[j1];
            anIntArray1679[l] = projected_vertex_y[j1];
            anIntArray1680[l++] = colorsY[i];
        } else {
            int l2 = camera_vertex_x[j1];
            int l3 = camera_vertex_y[j1];
            int l4 = colorsY[i];
            if (l1 >= 50) {
                int i6 = (50 - uid) * modelIntArray4[l1 - uid];
                anIntArray1678[l] = j + (l2 + ((camera_vertex_x[i1] - l2) * i6 >> 16) << SceneGraph.view_dist) / 50;
                anIntArray1679[l] = k + (l3 + ((camera_vertex_y[i1] - l3) * i6 >> 16) << SceneGraph.view_dist) / 50;
                anIntArray1680[l++] = l4 + ((colorsX[i] - l4) * i6 >> 16);
            }
            if (j2 >= 50) {
                int j6 = (50 - uid) * modelIntArray4[j2 - uid];
                anIntArray1678[l] = j + (l2 + ((camera_vertex_x[k1] - l2) * j6 >> 16) << SceneGraph.view_dist) / 50;
                anIntArray1679[l] = k + (l3 + ((camera_vertex_y[k1] - l3) * j6 >> 16) << SceneGraph.view_dist) / 50;
                anIntArray1680[l++] = l4 + ((colorsZ[i] - l4) * j6 >> 16);
            }
        }
        if (j2 >= 50) {
            anIntArray1678[l] = projected_vertex_x[k1];
            anIntArray1679[l] = projected_vertex_y[k1];
            anIntArray1680[l++] = colorsZ[i];
        } else {
            int i3 = camera_vertex_x[k1];
            int i4 = camera_vertex_y[k1];
            int i5 = colorsZ[i];
            if (uid >= 50) {
                int k6 = (50 - j2) * modelIntArray4[uid - j2];
                anIntArray1678[l] = j + (i3 + ((camera_vertex_x[j1] - i3) * k6 >> 16) << SceneGraph.view_dist) / 50;
                anIntArray1679[l] = k + (i4 + ((camera_vertex_y[j1] - i4) * k6 >> 16) << SceneGraph.view_dist) / 50;
                anIntArray1680[l++] = i5 + ((colorsY[i] - i5) * k6 >> 16);
            }
            if (l1 >= 50) {
                int l6 = (50 - j2) * modelIntArray4[l1 - j2];
                anIntArray1678[l] = j + (i3 + ((camera_vertex_x[i1] - i3) * l6 >> 16) << SceneGraph.view_dist) / 50;
                anIntArray1679[l] = k + (i4 + ((camera_vertex_y[i1] - i4) * l6 >> 16) << SceneGraph.view_dist) / 50;
                anIntArray1680[l++] = i5 + ((colorsX[i] - i5) * l6 >> 16);
            }
        }
        int j3 = anIntArray1678[0];
        int j4 = anIntArray1678[1];
        int j5 = anIntArray1678[2];
        int i7 = anIntArray1679[0];
        int j7 = anIntArray1679[1];
        int k7 = anIntArray1679[2];
        if ((j3 - j4) * (k7 - j7) - (i7 - j7) * (j5 - j4) > 0) {
            Rasterizer3D.testX = false;
            int texture_a = i1;
            int texture_b = j1;
            int texture_c = k1;
            if (l == 3) {
                if (j3 < 0 || j4 < 0 || j5 < 0 || j3 > Rasterizer2D.center_x || j4 > Rasterizer2D.center_x || j5 > Rasterizer2D.center_x)
                    Rasterizer3D.testX = true;

                int l7;
                if (types == null)
                    l7 = 0;
                else
                    l7 = types[i] & 3;

                if (materials != null && materials[i] != -1) {
                    if (textures != null && textures[i] != -1) {
                        int coordinate = textures[i] & 0xff;
                        texture_a = texturesX[coordinate];
                        texture_b = texturesY[coordinate];
                        texture_c = texturesZ[coordinate];
                    }
                    if (colorsZ[i] == -1) {
                        Rasterizer3D.drawTexturedTriangle(
                            i7, j7, k7,
                            j3, j4, j5,
                            colorsX[i], colorsX[i], colorsX[i],
                            camera_vertex_x[texture_a], camera_vertex_x[texture_b], camera_vertex_x[texture_c],
                            camera_vertex_y[texture_a], camera_vertex_y[texture_b], camera_vertex_y[texture_c],
                            camera_vertex_z[texture_a], camera_vertex_z[texture_b], camera_vertex_z[texture_c],
                            materials[i]);
                    } else {
                        Rasterizer3D.drawTexturedTriangle(
                            i7, j7, k7,
                            j3, j4, j5,
                            anIntArray1680[0], anIntArray1680[1], anIntArray1680[2],
                            camera_vertex_x[texture_a], camera_vertex_x[texture_b], camera_vertex_x[texture_c],
                            camera_vertex_y[texture_a], camera_vertex_y[texture_b], camera_vertex_y[texture_c],
                            camera_vertex_z[texture_a], camera_vertex_z[texture_b], camera_vertex_z[texture_c],
                            materials[i]);
                    }
                } else {
                    if (l7 == 0)
                        Rasterizer3D.drawShadedTriangle(i7, j7, k7, j3, j4, j5, anIntArray1680[0], anIntArray1680[1], anIntArray1680[2]);

                    else if (l7 == 1)
                        Rasterizer3D.drawFlatTriangle(i7, j7, k7, j3, j4, j5, modelIntArray3[colorsX[i]]);
                }
            }
            if (l == 4) {
                if (j3 < 0 || j4 < 0 || j5 < 0 || j3 > Rasterizer2D.center_x || j4 > Rasterizer2D.center_x || j5 > Rasterizer2D.center_x || anIntArray1678[3] < 0 || anIntArray1678[3] > Rasterizer2D.center_x)
                    Rasterizer3D.testX = true;
                int type;
                if (types == null)
                    type = 0;
                else
                    type = types[i] & 3;

                if (materials != null && materials[i] != -1) {
                    if (textures != null && textures[i] != -1) {
                        int coordinate = textures[i] & 0xff;
                        texture_a = texturesX[coordinate];
                        texture_b = texturesY[coordinate];
                        texture_c = texturesZ[coordinate];
                    }
                    if (colorsZ[i] == -1) {
                        Rasterizer3D.drawTexturedTriangle(
                            i7, j7, k7,
                            j3, j4, j5,
                            colorsX[i], colorsX[i], colorsX[i],
                            camera_vertex_x[texture_a], camera_vertex_x[texture_b], camera_vertex_x[texture_c],
                            camera_vertex_y[texture_a], camera_vertex_y[texture_b], camera_vertex_y[texture_c],
                            camera_vertex_z[texture_a], camera_vertex_z[texture_b], camera_vertex_z[texture_c],
                            materials[i]);
                        Rasterizer3D.drawTexturedTriangle(
                            i7, k7, anIntArray1679[3],
                            j3, j5, anIntArray1678[3],
                            colorsX[i], colorsX[i], colorsX[i],
                            camera_vertex_x[texture_a], camera_vertex_x[texture_b], camera_vertex_x[texture_c],
                            camera_vertex_y[texture_a], camera_vertex_y[texture_b], camera_vertex_y[texture_c],
                            camera_vertex_z[texture_a], camera_vertex_z[texture_b], camera_vertex_z[texture_c],
                            materials[i]);
                    } else {
                        Rasterizer3D.drawTexturedTriangle(
                            i7, j7, k7,
                            j3, j4, j5,
                            anIntArray1680[0], anIntArray1680[1], anIntArray1680[2],
                            camera_vertex_x[texture_a], camera_vertex_x[texture_b], camera_vertex_x[texture_c],
                            camera_vertex_y[texture_a], camera_vertex_y[texture_b], camera_vertex_y[texture_c],
                            camera_vertex_z[texture_a], camera_vertex_z[texture_b], camera_vertex_z[texture_c],
                            materials[i]);
                        Rasterizer3D.drawTexturedTriangle(
                            i7, k7, anIntArray1679[3],
                            j3, j5, anIntArray1678[3],
                            anIntArray1680[0], anIntArray1680[2], anIntArray1680[3],
                            camera_vertex_x[texture_a], camera_vertex_x[texture_b], camera_vertex_x[texture_c],
                            camera_vertex_y[texture_a], camera_vertex_y[texture_b], camera_vertex_y[texture_c],
                            camera_vertex_z[texture_a], camera_vertex_z[texture_b], camera_vertex_z[texture_c],
                            materials[i]);
                        return;
                    }
                } else {
                    if (type == 0) {
                        Rasterizer3D.drawShadedTriangle(i7, j7, k7, j3, j4, j5, anIntArray1680[0], anIntArray1680[1], anIntArray1680[2]);
                        Rasterizer3D.drawShadedTriangle(i7, k7, anIntArray1679[3], j3, j5, anIntArray1678[3], anIntArray1680[0], anIntArray1680[2], anIntArray1680[3]);
                        return;
                    }
                    if (type == 1) {
                        int l8 = modelIntArray3[colorsX[i]];
                        Rasterizer3D.drawFlatTriangle(i7, j7, k7, j3, j4, j5, l8);
                        Rasterizer3D.drawFlatTriangle(i7, k7, anIntArray1679[3], j3, j5, anIntArray1678[3], l8);
                        return;
                    }
                }
            }
        }
    }

    private final boolean entered_clickbox(int mouse_x, int mouse_y, int y_a, int y_b, int y_c, int x_a, int x_b, int x_c) {
        if (mouse_y < y_a && mouse_y < y_b && mouse_y < y_c)
            return false;
        if (mouse_y > y_a && mouse_y > y_b && mouse_y > y_c)
            return false;
        if (mouse_x < x_a && mouse_x < x_b && mouse_x < x_c)
            return false;
        return mouse_x <= x_a || mouse_x <= x_b || mouse_x <= x_c;
    }


    public void method2593(int orientation) { //I believe this is calculateBoundingBox aka calculateExtreme
        //if (this.field1947 == -1) {
        int min_x = 0;
        int min_y = 0;
        int min_z = 0;
        int max_x = 0;
        int max_y = 0;
        int max_z = 0;
        int cos = COSINE[orientation];
        int sin = SINE[orientation];
        //get bounds
        for (int point = 0; point < this.verticesCount; point++) {
            int v_x = Rasterizer3D.calc_vertex_x(this.verticesX[point], this.verticesZ[point], cos, sin);
            int v_y = this.verticesY[point];
            int v_z = Rasterizer3D.calc_vertex_z(this.verticesX[point], this.verticesZ[point], cos, sin);
            if (v_x < min_x) {
                min_x = v_x;
            }
            if (v_x > max_x) {
                max_x = v_x;
            }
            if (v_y < min_y) {
                min_y = v_y;
            }
            if (v_y > max_y) {
                max_y = v_y;
            }
            if (v_z < min_z) {
                min_z = v_z;
            }
            if (v_z > max_z) {
                max_z = v_z;
            }
        }
        //unsure
        this.field1944 = (max_x + min_x) / 2;
        this.field1963 = (max_y + min_y) / 2;
        this.field1946 = (max_z + min_z) / 2;
        this.field1947 = (max_x - min_x + 1) / 2;
        this.field1948 = (max_y - min_y + 1) / 2;
        this.field1924 = (max_z - min_z + 1) / 2;
        if (this.field1947 < 32) {
            this.field1947 = 32;
        }
        if (this.field1924 < 32) {
            this.field1924 = 32;
        }
        if (fits_on_single_square) {
            this.field1947 += 8;
            this.field1924 += 8;
        }
        //}
    }

    public int field1944;
    public int field1947;
    public int field1963;
    public int field1948;
    public int field1946;
    public int field1924;


    public int bufferOffset;
    public int uvBufferOffset;

    public int getBufferOffset() {
        return bufferOffset;
    }

    public void setBufferOffset(int bufferOffset) {
        this.bufferOffset = bufferOffset;
    }

    public int getUvBufferOffset() {
        return uvBufferOffset;
    }

    public void setUvBufferOffset(int uvBufferOffset) {
        this.uvBufferOffset = uvBufferOffset;
    }

    public float[][] getFaceTextureUCoordinates() {
        return new float[][]{};
    }

    public float[][] getFaceTextureVCoordinates() {
        return new float[][]{};
    }

    public void addTextureWithCoordinate(int[] originalIds, int[] targetIds, TextureCoordinate coordinate) {
        texturesCount = originalIds.length;
        texturesX = new short[texturesCount];
        texturesY = new short[texturesCount];
        texturesZ = new short[texturesCount];
        textures = new byte[trianglesCount];
        textureTypes = new byte[texturesCount];
        materials = new short[trianglesCount];
        if (types == null) {
            types = new int[trianglesCount];
        }

        for (int i = 0; i < texturesCount; i++) {
            this.texturesX[i] = (short) coordinate.getA();
            this.texturesY[i] = (short) coordinate.getB();
            this.texturesZ[i] = (short) coordinate.getC();
        }

        /*this.triangle_texture_edge_a[1] = 12;
        this.triangle_texture_edge_b[1] = 4;
        this.triangle_texture_edge_c[1] = 1;*/

        Arrays.fill(textures, (byte) -1);
        for (int i = 0; i < this.trianglesCount; i++) {
            for (int index = 0; index < originalIds.length; index++) {
                if (this.colors[i] == originalIds[index]) {
                    this.textures[i] = (byte) index;
                    this.types[i] = 2;
                    this.materials[i] = (short) targetIds[index];
                }
            }
        }

        for (int i = 0; i < trianglesCount; i++) {
            if (textures[i] == -1) {
                materials[i] = -1;
                types[i] = 0;
            }
        }
    }

    public void completelyRecolor(int j) {
        for (int k = 0; k < trianglesCount; k++) {
            colors[k] = (short) j;
        }
    }

    public void shadingRecolor(int j) {
        j += 100;
        int kcolor = 0;
        for (int k = 0; k < trianglesCount; k++) {
            kcolor = colors[k];
            if (k + j >= 0) {
                colors[k] = (short) (kcolor + j);
            }
        }
    }

    public void shadingRecolor2(int j) {

        for (int k = 0; k < trianglesCount; k++) {
            if (k + j >= 0) {
                colors[k] = (short) (k + j);
            }
        }
    }

    public void shadingRecolor4(int j) {

        for (int k = 0; k < trianglesCount; k++) {
            if (j == 222) {
                System.out.println("k = " + colors[k]);
            }
            if ((colors[k] != 65535) && (k + j >= 0)) {
                colors[k] += j;
            }
        }
    }

    public void shadingRecolor3(int j) {

        for (int k = 0; k < trianglesCount; k++) {
            int lastcolor = 1;
            if ((colors[k] + j >= 10000)
                && (colors[k] + j <= 90000)) {
                colors[k] = (short) (k + j + lastcolor);
            }
            lastcolor++;
        }
    }

    public void modelRecoloring(int i, int j) {
        for (int k = 0; k < trianglesCount; k++) {
            if (colors[k] == i) {
                colors[k] = (short) j;
            }
        }
    }
    public int[][] animayaGroups;
    public int[][] animayaScales;
    public short[] materials;
    public byte[] textures;
    public byte[] textureTypes;
    private boolean aBoolean1618;
    public static int anInt1620;
    public static Model EMPTY_MODEL = new Model(true);
    private static int anIntArray1622[] = new int[2000];
    private static int anIntArray1623[] = new int[2000];
    private static int anIntArray1624[] = new int[2000];
    private static int anIntArray1625[] = new int[2000];
    public int verticesCount;
    public int verticesX[];
    public int verticesY[];
    public int verticesZ[];
    public int trianglesCount;
    public int trianglesX[];
    public int trianglesY[];
    public int trianglesZ[];
    public int colorsX[];
    public int colorsY[];
    public int colorsZ[];
    public int types[];
    public byte face_render_priorities[];
    public int alphas[];
    public short colors[];
    public byte face_priority;
    public int texturesCount;
    public short texturesX[];
    public short texturesY[];
    public short texturesZ[];
    public int minimumXVertex;
    public int maximumXVertex;
    public int maximumZVertex;
    public int minimumZVertex;
    public int XYZMag;
    public int maximumYVertex;
    public int maxRenderDepth;
    public int diagonal3DAboveOrigin;
    public int itemDropHeight;
    public int vertexData[];
    public int triangleData[];
    public int vertexGroups[][];
    public int faceGroups[][];
    public boolean fits_on_single_square;
    public VertexNormal vertexNormals[];
    public VertexNormal[] vertexNormalsOffsets;
    public FaceNormal[] faceNormals;
    public static boolean obj_exists; //obj_exists
    public static int anInt1685;
    public static int anInt1686;
    public static int anInt1687;
    public static long anIntArray1688[] = new long[1000];
    public static int SINE[];
    public static int COSINE[];
    static ModelHeader aClass21Array1661[];
    static Provider resourceProvider;
    static boolean hasAnEdgeToRestrict[] = new boolean[4700];
    static boolean outOfReach[] = new boolean[4700];
    static int projected_vertex_x[] = new int[4700];
    static int projected_vertex_y[] = new int[4700];
    static int projected_vertex_z[] = new int[4700];
    static int camera_vertex_x[] = new int[4700];
    static int camera_vertex_y[] = new int[4700];
    static int camera_vertex_z[] = new int[4700];
    static int depthListIndices[] = new int[1600];
    static int faceLists[][] = new int[1600][512];
    static int anIntArray1673[] = new int[12];
    static int anIntArrayArray1674[][] = new int[12][2000];
    static int anIntArray1675[] = new int[2000];
    static int anIntArray1676[] = new int[2000];
    static int anIntArray1677[] = new int[12];
    static int anIntArray1678[] = new int[10];
    static int anIntArray1679[] = new int[10];
    static int anIntArray1680[] = new int[10];
    static int xAnimOffset;
    static int yAnimOffset;
    static int zAnimOffset;
    static int modelIntArray3[];
    static int modelIntArray4[];

    static {
        SINE = Rasterizer3D.SINE;
        COSINE = Rasterizer3D.COSINE;
        modelIntArray3 = Rasterizer3D.HSL_TO_RGB;
        modelIntArray4 = Rasterizer3D.anIntArray1469;
    }
}
