package online.paescape.net;

import online.paescape.collection.Deque;
import online.paescape.collection.QueueNode;

import java.math.BigInteger;

public final class Stream extends QueueNode {

    public static final BigInteger RSA_MODULUS = new BigInteger("141038977654242498796653256463581947707085475448374831324884224283104317501838296020488428503639086635001378639378416098546218003298341019473053164624088381038791532123008519201622098961063764779454144079550558844578144888226959180389428577531353862575582264379889305154355721898818709924743716570464556076517");
    public static final BigInteger RSA_EXPONENT = new BigInteger("65537");
    private static final int[] bitPositionTable = {0, 1, 3, 7, 15, 31, 63,
            127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535,
            0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff,
            0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff,
            0x3fffffff, 0x7fffffff, -1};
    private static final Deque nodeList = new Deque();
    private static int anInt1412;
    public byte[] buffer;
    public int currentOffset;
    public int bitPosition;
    public ISAACRandomGen encryption;

    public Stream() {
    }

    public Stream(byte[] abyte0) {
        buffer = abyte0;
        currentOffset = 0;
    }

    public static Stream create() {
        synchronized (nodeList) {
            Stream stream = null;
            if (anInt1412 > 0) {
                anInt1412--;
                stream = (Stream) nodeList.popHead();
            }
            if (stream != null) {
                stream.currentOffset = 0;
                return stream;
            }
        }
        Stream stream_1 = new Stream();
        stream_1.currentOffset = 0;
        stream_1.buffer = new byte[40000];
        return stream_1;
    }

    public int getMedium() {
        currentOffset += 3;
        return ((buffer[currentOffset - 3] & 0xff) << 16)
                | ((buffer[currentOffset - 2] & 0xff) << 8)
                | (buffer[currentOffset - 1] & 0xff);
    }

    public int readLittleEndian() {
        currentOffset += 4;
        return ((buffer[currentOffset - 4] & 0xFF) << 24) + ((buffer[currentOffset - 3] & 0xFF) << 16) + ((buffer[currentOffset - 2] & 0xFF) << 8) + (buffer[currentOffset - 1] & 0xFF);
    }

    public String readNewString() {
        int i = currentOffset;
        while (buffer[currentOffset++] != 0)
            ;
        return new String(buffer, i, currentOffset - i - 1);
    }

    public void writeIntBigEndian(int i) {
        buffer[currentOffset++] = (byte) (i >> 16);
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) i;
    }

    public void writeInt(int i) {
        buffer[currentOffset++] = (byte) (i >> 24);
        buffer[currentOffset++] = (byte) (i >> 16);
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) i;
    }

    public int readUSmart2() {
        int baseVal = 0;
        int lastVal = 0;
        while ((lastVal = method422()) == 42000) {
            baseVal += 32767;
        }
        return baseVal + lastVal;
    }

    public int readInt() {
        return (readUnsignedByte() << 24) + (readUnsignedByte() << 16)
                + (readUnsignedByte() << 8) + readUnsignedByte();
    }

    public int readShort2() {
        currentOffset += 2;
        int i = ((buffer[currentOffset - 2] & 0xff) << 8)
                + (buffer[currentOffset - 1] & 0xff);
        if (i > 60000)
            i = -65535 + i;
        return i;
    }

    public final int v(int i) {
        currentOffset += 3;
        return (0xff & buffer[currentOffset - 3] << 16)
                + (0xff & buffer[currentOffset - 2] << 8)
                + (0xff & buffer[currentOffset - 1]);
    }

    public void createFrame(int i) {
        buffer[currentOffset++] = (byte) (i + encryption.getNextKey());
    }

    public void writeWordBigEndian(int i) {
        buffer[currentOffset++] = (byte) i;
    }

    public void writeWord(int i) {
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) i;
    }

    public void method400(int i) {
        buffer[currentOffset++] = (byte) i;
        buffer[currentOffset++] = (byte) (i >> 8);
    }

    public void writeDWordBigEndian(int i) {
        buffer[currentOffset++] = (byte) (i >> 16);
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) i;
    }

    public void writeDWord(int i) {
        buffer[currentOffset++] = (byte) (i >> 24);
        buffer[currentOffset++] = (byte) (i >> 16);
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) i;
    }

    public void method403(int j) {
        buffer[currentOffset++] = (byte) j;
        buffer[currentOffset++] = (byte) (j >> 8);
        buffer[currentOffset++] = (byte) (j >> 16);
        buffer[currentOffset++] = (byte) (j >> 24);
    }

    public void writeQWord(long l) {
        try {
            buffer[currentOffset++] = (byte) (int) (l >> 56);
            buffer[currentOffset++] = (byte) (int) (l >> 48);
            buffer[currentOffset++] = (byte) (int) (l >> 40);
            buffer[currentOffset++] = (byte) (int) (l >> 32);
            buffer[currentOffset++] = (byte) (int) (l >> 24);
            buffer[currentOffset++] = (byte) (int) (l >> 16);
            buffer[currentOffset++] = (byte) (int) (l >> 8);
            buffer[currentOffset++] = (byte) (int) l;
        } catch (RuntimeException runtimeexception) {
            System.out.println("14395, " + 5 + ", " + l + ", "
                    + runtimeexception);
            throw new RuntimeException();
        }
    }

    public void writeString(String s) {
        // s.getBytes(0, s.length(), buffer, currentOffset); //deprecated
        System.arraycopy(s.getBytes(), 0, buffer, currentOffset, s.length());
        currentOffset += s.length();
        buffer[currentOffset++] = 10;
    }

    public void writeBytes(byte[] abyte0, int i, int j) {
        for (int k = j; k < j + i; k++)
            buffer[currentOffset++] = abyte0[k];
    }

    public void writeBytes(int i) {
        buffer[currentOffset - i - 1] = (byte) i;
    }

    public int readUByte() {
        return buffer[currentOffset++] & 0xff;
    }

    public byte readByte() {
        return buffer[currentOffset++];
    }

    public void writeByte(int i) {
        buffer[currentOffset++] = (byte) i;
    }

    public int readShort() {
        currentOffset += 2;
        return ((buffer[currentOffset - 2] & 0xff) << 8)
                + (buffer[currentOffset - 1] & 0xff);
    }

    public byte readSignedByte() {
        return buffer[currentOffset++];
    }

    public int readUnsignedByte() {
        return buffer[currentOffset++] & 0xff;
    }

    public int readUnsignedWord() {
        currentOffset += 2;
        return ((buffer[currentOffset - 2] & 0xff) << 8)
                + (buffer[currentOffset - 1] & 0xff);
    }

    public int readSignedWord() {
        currentOffset += 2;
        int i = ((buffer[currentOffset - 2] & 0xff) << 8)
                + (buffer[currentOffset - 1] & 0xff);
        if (i > 32767)
            i -= 0x10000;
        return i;
    }

    public int method434() {
        currentOffset += 2;
        return ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
    }

    public int read3Bytes() {
        currentOffset += 3;
        return ((buffer[currentOffset - 3] & 0xff) << 16)
                + ((buffer[currentOffset - 2] & 0xff) << 8)
                + (buffer[currentOffset - 1] & 0xff);
    }

    public int readDWord() {
        currentOffset += 4;
        return ((buffer[currentOffset - 4] & 0xff) << 24)
                + ((buffer[currentOffset - 3] & 0xff) << 16)
                + ((buffer[currentOffset - 2] & 0xff) << 8)
                + (buffer[currentOffset - 1] & 0xff);
    }

    public long readQWord() {
        long l = (long) readDWord() & 0xffffffffL;
        long l1 = (long) readDWord() & 0xffffffffL;
        return (l << 32) + l1;
    }

    public String readString() {
        int i = currentOffset;
        while (buffer[currentOffset++] != 10)
            ;
        return new String(buffer, i, currentOffset - i - 1);
    }

    public byte[] readBytes() {
        int i = currentOffset;
        while (buffer[currentOffset++] != 10)
            ;
        byte[] abyte0 = new byte[currentOffset - i - 1];
        System.arraycopy(buffer, i, abyte0, i - i, currentOffset - 1 - i);
        return abyte0;
    }

    public void readBytes(int i, int j, byte[] abyte0) {
        for (int l = j; l < j + i; l++)
            abyte0[l] = buffer[currentOffset++];
    }

    public void initBitAccess() {
        bitPosition = currentOffset * 8;
    }

    public int readBits(int i) {
        int k = bitPosition >> 3;
        int l = 8 - (bitPosition & 7);
        int i1 = 0;
        bitPosition += i;
        for (; i > l; l = 8) {
            i1 += (buffer[k++] & bitPositionTable[l]) << i - l;
            i -= l;
        }
        if (i == l)
            i1 += buffer[k] & bitPositionTable[l];
        else
            i1 += buffer[k] >> l - i & bitPositionTable[i];
        return i1;
    }

    public void finishBitAccess() {
        currentOffset = (bitPosition + 7) / 8;
    }

    public int method421() {
        int i = buffer[currentOffset] & 0xff;
        if (i < 128)
            return readUnsignedByte() - 64;
        else
            return readUnsignedWord() - 49152;
    }

    public int method422() {
        int i = buffer[currentOffset] & 0xff;
        if (i < 128)
            return readUnsignedByte();
        else
            return readUnsignedWord() - 32768;
    }

    public void doKeys() {
        int i = currentOffset;
        currentOffset = 0;
        byte[] abyte0 = new byte[i];
        readBytes(i, 0, abyte0);
        BigInteger biginteger2 = new BigInteger(abyte0);
        BigInteger biginteger3 = biginteger2.modPow(RSA_EXPONENT, RSA_MODULUS);
        byte[] abyte1 = biginteger3.toByteArray();
        currentOffset = 0;
        writeWordBigEndian(abyte1.length);
        writeBytes(abyte1, abyte1.length, 0);
    }

    public void writeByteC(int i) {
        buffer[currentOffset++] = (byte) (-i);
    }

    public void method425(int j) {
        buffer[currentOffset++] = (byte) (128 - j);
    }

    public int method426() {
        return buffer[currentOffset++] - 128 & 0xff;
    }

    public int nglb() {
        return -buffer[currentOffset++] & 0xff;
    }

    public int readByteS() {
        return 128 - buffer[currentOffset++] & 0xff;
    }

    public byte method429() {
        return (byte) (-buffer[currentOffset++]);
    }

    public byte method430() {
        return (byte) (128 - buffer[currentOffset++]);
    }

    public void writeUnsignedWordBigEndian(int i) {
        buffer[currentOffset++] = (byte) i;
        buffer[currentOffset++] = (byte) (i >> 8);
    }

    public void writeUnsignedWordA(int j) {
        buffer[currentOffset++] = (byte) (j >> 8);
        buffer[currentOffset++] = (byte) (j + 128);
    }

    public void writeSignedBigEndian(int j) {
        buffer[currentOffset++] = (byte) (j + 128);
        buffer[currentOffset++] = (byte) (j >> 8);
    }

    public int ig2() {
        currentOffset += 2;
        return ((buffer[currentOffset - 1] & 0xff) << 8)
                + (buffer[currentOffset - 2] & 0xff);
    }

    public int readByteA() {
        currentOffset += 2;
        return ((buffer[currentOffset - 2] & 0xff) << 8)
                + (buffer[currentOffset - 1] - 128 & 0xff);
    }

    public int readWordBigEndian() {
        currentOffset += 2;
        return ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] - 128 & 0xff);
    }

    public int method437() {
        currentOffset += 2;
        int j = ((buffer[currentOffset - 1] & 0xff) << 8)
                + (buffer[currentOffset - 2] & 0xff);
        if (j > 32767)
            j -= 0x10000;
        return j;
    }

    public int method438() {
        currentOffset += 2;
        int j = ((buffer[currentOffset - 1] & 0xff) << 8)
                + (buffer[currentOffset - 2] - 128 & 0xff);
        if (j > 32767)
            j -= 0x10000;
        return j;
    }

    public int method439() {
        currentOffset += 4;
        return ((buffer[currentOffset - 2] & 0xff) << 24)
                + ((buffer[currentOffset - 1] & 0xff) << 16)
                + ((buffer[currentOffset - 4] & 0xff) << 8)
                + (buffer[currentOffset - 3] & 0xff);
    }

    public int method440() {
        currentOffset += 4;
        return ((buffer[currentOffset - 3] & 0xff) << 24)
                + ((buffer[currentOffset - 4] & 0xff) << 16)
                + ((buffer[currentOffset - 1] & 0xff) << 8)
                + (buffer[currentOffset - 2] & 0xff);
    }

    public void method441(int i, byte[] abyte0, int j) {
        for (int k = (i + j) - 1; k >= i; k--)
            buffer[currentOffset++] = (byte) (abyte0[k] + 128);

    }

    public void readBytes_reverse(int i, int j, byte[] abyte0) {
        for (int k = (j + i) - 1; k >= j; k--)
            abyte0[k] = buffer[currentOffset++];

    }

    public int method435() {
        currentOffset += 2;
        return ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] - 128 & 0xff);
    }

    public void method431(int i) {
        buffer[currentOffset++] = (byte) i;
        buffer[currentOffset++] = (byte) (i >> 8);
    }

    public int method428() {
        return 128 - buffer[currentOffset++] & 0xff;
    }

    public int method427() {
        return -buffer[currentOffset++] & 0xff;
    }

    public void method433(int j) {
        buffer[currentOffset++] = (byte) (j + 128);
        buffer[currentOffset++] = (byte) (j >> 8);
    }

    public void method432(int i) {
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) (i + 128);
    }

    public int readLEShort() {
        currentOffset += 2;
        return ((buffer[currentOffset - 1] & 0xff) << 8)
                + (buffer[currentOffset - 2] - 128 & 0xff);
    }
}
