package online.paescape.scene.tile;

import net.runelite.rs.api.RSSceneTileModel;

public final class ShapedTile implements RSSceneTileModel {

    public static final int[] anIntArray688 = new int[6];
    public static final int[] anIntArray689 = new int[6];
    public static final int[] anIntArray690 = new int[6];
    public static final int[] anIntArray691 = new int[6];
    public static final int[] anIntArray692 = new int[6];
    public static final int[] depthPoint = new int[6];
    private static final int[][] anIntArrayArray696 = {{1, 3, 5, 7},
            {1, 3, 5, 7}, {1, 3, 5, 7}, {1, 3, 5, 7, 6},
            {1, 3, 5, 7, 6}, {1, 3, 5, 7, 6}, {1, 3, 5, 7, 6},
            {1, 3, 5, 7, 2, 6}, {1, 3, 5, 7, 2, 8}, {1, 3, 5, 7, 2, 8},
            {1, 3, 5, 7, 11, 12}, {1, 3, 5, 7, 11, 12},
            {1, 3, 5, 7, 13, 14}};
    private static final int[][] anIntArrayArray697 = {
            {0, 1, 2, 3, 0, 0, 1, 3},
            {1, 1, 2, 3, 1, 0, 1, 3},
            {0, 1, 2, 3, 1, 0, 1, 3},
            {0, 0, 1, 2, 0, 0, 2, 4, 1, 0, 4, 3},
            {0, 0, 1, 4, 0, 0, 4, 3, 1, 1, 2, 4},
            {0, 0, 4, 3, 1, 0, 1, 2, 1, 0, 2, 4},
            {0, 1, 2, 4, 1, 0, 1, 4, 1, 0, 4, 3},
            {0, 4, 1, 2, 0, 4, 2, 5, 1, 0, 4, 5, 1, 0, 5, 3},
            {0, 4, 1, 2, 0, 4, 2, 3, 0, 4, 3, 5, 1, 0, 4, 5},
            {0, 0, 4, 5, 1, 4, 1, 2, 1, 4, 2, 3, 1, 4, 3, 5},
            {0, 0, 1, 5, 0, 1, 4, 5, 0, 1, 2, 4, 1, 0, 5, 3, 1, 5, 4, 3, 1, 4,
                    2, 3},
            {1, 0, 1, 5, 1, 1, 4, 5, 1, 1, 2, 4, 0, 0, 5, 3, 0, 5, 4, 3, 0, 4,
                    2, 3},
            {1, 0, 5, 4, 1, 0, 1, 5, 0, 0, 4, 3, 0, 4, 5, 3, 0, 5, 2, 3, 0, 1,
                    2, 5}};
    public final int[] origVertexX;
    public final int[] origVertexY;
    public final int[] origVertexZ;
    public final int[] triangleHslA;
    public final int[] triangleHslB;
    public final int[] triangleHslC;
    public final int[] triangleA;
    public final int[] triangleB;
    public final int[] triangleC;
    public final boolean aBoolean683;
    public final int shape;
    public final int rotation;
    public final int colourRGB;
    public final int colourRGBA;
    public int[] triangleTexture;
    public int color61;
    public int color71;
    public int color81;
    public int color91;
    public int color62;
    public int color72;
    public int color82;
    public int color92;
    public boolean textured;

    public ShapedTile(int i, int j, int k, int l, int overlaytex, int underlaytex,
                      int j1, int k1, int l1, int i2, int j2, int k2, int l2, int i3,
                      int j3, int k3, int l3, int i4, int k4, int l4, boolean tex) {
        color61 = l1;
        color71 = i4;
        color81 = j2;
        color91 = k;
        color62 = j;
        color72 = l3;
        color82 = j1;
        color92 = k3;
        textured = tex;
        aBoolean683 = !(i3 != l2 || i3 != l || i3 != k2);
        shape = j3;
        rotation = k1;
        colourRGB = i2;
        colourRGBA = l4;
        char c = '\200';
        int i5 = c / 2;
        int j5 = c / 4;
        int k5 = c * 3 / 4;
        int[] ai = anIntArrayArray696[j3];
        int l5 = ai.length;
        origVertexX = new int[l5];
        origVertexY = new int[l5];
        origVertexZ = new int[l5];
        int[] ai1 = new int[l5];
        int[] ai2 = new int[l5];
        int i6 = k4 * c;
        int j6 = i * c;
        for (int k6 = 0; k6 < l5; k6++) {
            int l6 = ai[k6];
            if ((l6 & 1) == 0 && l6 <= 8) {
                l6 = (l6 - k1 - k1 - 1 & 7) + 1;
            }
            if (l6 > 8 && l6 <= 12) {
                l6 = (l6 - 9 - k1 & 3) + 9;
            }
            if (l6 > 12 && l6 <= 16) {
                l6 = (l6 - 13 - k1 & 3) + 13;
            }
            int i7;
            int k7;
            int i8;
            int k8;
            int j9;
            if (l6 == 1) {
                i7 = i6;
                k7 = j6;
                i8 = i3;
                k8 = l1;
                j9 = j;
            } else if (l6 == 2) {
                i7 = i6 + i5;
                k7 = j6;
                i8 = i3 + l2 >> 1;
                k8 = l1 + i4 >> 1;
                j9 = j + l3 >> 1;
            } else if (l6 == 3) {
                i7 = i6 + c;
                k7 = j6;
                i8 = l2;
                k8 = i4;
                j9 = l3;
            } else if (l6 == 4) {
                i7 = i6 + c;
                k7 = j6 + i5;
                i8 = l2 + l >> 1;
                k8 = i4 + j2 >> 1;
                j9 = l3 + j1 >> 1;
            } else if (l6 == 5) {
                i7 = i6 + c;
                k7 = j6 + c;
                i8 = l;
                k8 = j2;
                j9 = j1;
            } else if (l6 == 6) {
                i7 = i6 + i5;
                k7 = j6 + c;
                i8 = l + k2 >> 1;
                k8 = j2 + k >> 1;
                j9 = j1 + k3 >> 1;
            } else if (l6 == 7) {
                i7 = i6;
                k7 = j6 + c;
                i8 = k2;
                k8 = k;
                j9 = k3;
            } else if (l6 == 8) {
                i7 = i6;
                k7 = j6 + i5;
                i8 = k2 + i3 >> 1;
                k8 = k + l1 >> 1;
                j9 = k3 + j >> 1;
            } else if (l6 == 9) {
                i7 = i6 + i5;
                k7 = j6 + j5;
                i8 = i3 + l2 >> 1;
                k8 = l1 + i4 >> 1;
                j9 = j + l3 >> 1;
            } else if (l6 == 10) {
                i7 = i6 + k5;
                k7 = j6 + i5;
                i8 = l2 + l >> 1;
                k8 = i4 + j2 >> 1;
                j9 = l3 + j1 >> 1;
            } else if (l6 == 11) {
                i7 = i6 + i5;
                k7 = j6 + k5;
                i8 = l + k2 >> 1;
                k8 = j2 + k >> 1;
                j9 = j1 + k3 >> 1;
            } else if (l6 == 12) {
                i7 = i6 + j5;
                k7 = j6 + i5;
                i8 = k2 + i3 >> 1;
                k8 = k + l1 >> 1;
                j9 = k3 + j >> 1;
            } else if (l6 == 13) {
                i7 = i6 + j5;
                k7 = j6 + j5;
                i8 = i3;
                k8 = l1;
                j9 = j;
            } else if (l6 == 14) {
                i7 = i6 + k5;
                k7 = j6 + j5;
                i8 = l2;
                k8 = i4;
                j9 = l3;
            } else if (l6 == 15) {
                i7 = i6 + k5;
                k7 = j6 + k5;
                i8 = l;
                k8 = j2;
                j9 = j1;
            } else {
                i7 = i6 + j5;
                k7 = j6 + k5;
                i8 = k2;
                k8 = k;
                j9 = k3;
            }
            origVertexX[k6] = i7;
            origVertexY[k6] = i8;
            origVertexZ[k6] = k7;
            ai1[k6] = k8;
            ai2[k6] = j9;
        }

        int[] ai3 = anIntArrayArray697[j3];
        int j7 = ai3.length / 4;
        triangleA = new int[j7];
        triangleB = new int[j7];
        triangleC = new int[j7];
        triangleHslA = new int[j7];
        triangleHslB = new int[j7];
        triangleHslC = new int[j7];
        if (overlaytex != -1 || underlaytex != -1) {
            triangleTexture = new int[j7];
        }
        int l7 = 0;
        for (int j8 = 0; j8 < j7; j8++) {
            int l8 = ai3[l7];
            int k9 = ai3[l7 + 1];
            int i10 = ai3[l7 + 2];
            int k10 = ai3[l7 + 3];
            l7 += 4;
            if (k9 < 4) {
                k9 = k9 - k1 & 3;
            }
            if (i10 < 4) {
                i10 = i10 - k1 & 3;
            }
            if (k10 < 4) {
                k10 = k10 - k1 & 3;
            }
            triangleA[j8] = k9;
            triangleB[j8] = i10;
            triangleC[j8] = k10;
            if (l8 == 0) {
                triangleHslA[j8] = ai1[k9];
                triangleHslB[j8] = ai1[i10];
                triangleHslC[j8] = ai1[k10];
                if (triangleTexture != null) {
                    triangleTexture[j8] = underlaytex;
                }
            } else {
                triangleHslA[j8] = ai2[k9];
                triangleHslB[j8] = ai2[i10];
                triangleHslC[j8] = ai2[k10];
                if (triangleTexture != null) {
                    triangleTexture[j8] = overlaytex;
                }
            }
        }

        int i9 = i3;
        int l9 = l2;
        if (l2 < i9) {
            i9 = l2;
        }
        if (l2 > l9) {
            l9 = l2;
        }
        if (l < i9) {
            i9 = l;
        }
        if (l > l9) {
            l9 = l;
        }
        if (k2 < i9) {
            i9 = k2;
        }
        if (k2 > l9) {
            l9 = k2;
        }
        i9 /= 14;
        l9 /= 14;
    }

    private int bufferOffset = -1;

    public void setBufferOffset(int offset) {
        bufferOffset = offset;
    }

    private int uVBufferOffset = -1;

    public void setUvBufferOffset(int offset) {
        uVBufferOffset = offset;
    }

    private int bufferLength = -1;

    public void setBufferLen(int length) {
        bufferLength = length;
    }

    @Override
    public int getUnderlaySwColor() {
        return color61;
    }

    @Override
    public void setUnderlaySwColor(int color) {
        color61 = color;
    }

    @Override
    public int getUnderlaySeColor() {
        return color91;
    }

    @Override
    public void setUnderlaySeColor(int color) {
        this.color91 = color;
    }

    @Override
    public int getUnderlayNeColor() {
        return color81;
    }

    @Override
    public void setUnderlayNeColor(int color) {
        this.color81 = color;
    }

    @Override
    public int getUnderlayNwColor() {
        return color71;
    }

    @Override
    public void setUnderlayNwColor(int color) {
        color71 = color;
    }

    @Override
    public int getOverlaySwColor() {
        return color62;
    }

    @Override
    public void setOverlaySwColor(int color) {
        color62 = color;
    }

    @Override
    public int getOverlaySeColor() {
        return color92;
    }

    @Override
    public void setOverlaySeColor(int color) {
        color92 = color;
    }

    @Override
    public int getOverlayNeColor() {
        return color82;
    }

    @Override
    public void setOverlayNeColor(int color) {
        color82 = color;
    }

    @Override
    public int getOverlayNwColor() {
        return color72;
    }

    @Override
    public void setOverlayNwColor(int color) {
        color72 = color;
    }

    public int getBufferLen() {
        return bufferLength;
    }

    public int getBufferOffset() {
        return bufferOffset;
    }

    public int getUvBufferOffset() {
        return uVBufferOffset;
    }

    @Override
    public int getModelUnderlay() {
        return colourRGB;
    }

    @Override
    public int getModelOverlay() {
        return colourRGBA;
    }

    @Override
    public int getShape() {
        return shape;
    }

    @Override
    public int[] getFaceX() {
        return triangleA;
    }

    @Override
    public int[] getFaceY() {
        return triangleB;
    }

    @Override
    public int[] getFaceZ() {
        return triangleC;
    }

    @Override
    public int[] getVertexX() {
        return this.origVertexX;
    }

    @Override
    public int[] getVertexY() {
        return origVertexY;
    }

    @Override
    public int[] getVertexZ() {
        return origVertexZ;
    }

    @Override
    public int[] getTriangleColorA() {
        return triangleHslA;
    }

    @Override
    public int[] getTriangleColorB() {
        return triangleHslB;
    }

    @Override
    public int[] getTriangleColorC() {
        return triangleHslC;
    }

    @Override
    public int[] getTriangleTextureId() {
        return triangleTexture;
    }

    @Override
    public boolean getIsFlat() {
        return false;
    }

    @Override
    public int getRotation() {
        return rotation;
    }

}
