package com.example.myapplication.util.crc32;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author administrator
 */
public class GOST28147Engine {

    protected static final int BLOCK_SIZE = 8;
    private int[] workingKey = null;
    private boolean forEncryption;

    private byte[] S;

    private final byte[] DSbox_A = {
        0xA, 0x4, 0x5, 0x6, 0x8, 0x1, 0x3, 0x7, 0xD, 0xC, 0xE, 0x0, 0x9, 0x2, 0xB, 0xF,
        0x5, 0xF, 0x4, 0x0, 0x2, 0xD, 0xB, 0x9, 0x1, 0x7, 0x6, 0x3, 0xC, 0xE, 0xA, 0x8,
        0x7, 0xF, 0xC, 0xE, 0x9, 0x4, 0x1, 0x0, 0x3, 0xB, 0x5, 0x2, 0x6, 0xA, 0x8, 0xD,
        0x4, 0xA, 0x7, 0xC, 0x0, 0xF, 0x2, 0x8, 0xE, 0x1, 0x6, 0x5, 0xD, 0xB, 0x9, 0x3,
        0x7, 0x6, 0x4, 0xB, 0x9, 0xC, 0x2, 0xA, 0x1, 0x8, 0x0, 0xE, 0xF, 0xD, 0x3, 0x5,
        0x7, 0x6, 0x2, 0x4, 0xD, 0x9, 0xF, 0x0, 0xA, 0x1, 0x5, 0xB, 0x8, 0xE, 0xC, 0x3,
        0xD, 0xE, 0x4, 0x1, 0x7, 0x0, 0x5, 0xA, 0x3, 0xC, 0x8, 0xF, 0x6, 0x2, 0x9, 0xB,
        0x1, 0x3, 0xA, 0x9, 0x5, 0xB, 0x4, 0xF, 0x8, 0x6, 0x7, 0xE, 0xD, 0x0, 0x2, 0xC
    };

    public GOST28147Engine() {

    }

    public void init(
            boolean forEncryption,
            byte[] sbox, byte[] key) {
        if (sbox != null) {
            this.S = new byte[sbox.length];
            System.arraycopy(sbox, 0, this.S, 0, sbox.length);
            if (key != null) {
                workingKey = generateWorkingKey(forEncryption, key);
            }
        } else if (key != null) {
            this.S = new byte[DSbox_A.length];
            System.arraycopy(DSbox_A, 0, this.S, 0, DSbox_A.length);
            workingKey = generateWorkingKey(forEncryption, key);
        } else {
            throw new IllegalArgumentException("invalid parameter passed to GOST28147 init");
        }
    }

    public String getAlgorithmName() {
        return "GOST28147";
    }

    public int getBlockSize() {
        return BLOCK_SIZE;
    }

    public int processBlock(
            byte[] in,
            int inOff,
            byte[] out,
            int outOff) {
        if (workingKey == null) {
            throw new IllegalStateException("GOST28147 engine not initialised");
        }

        if ((inOff + BLOCK_SIZE) > in.length) {
            throw new IllegalStateException("input buffer too short");
        }

        if ((outOff + BLOCK_SIZE) > out.length) {
            throw new IllegalStateException("output buffer too short");
        }

        GOST28147Func(workingKey, in, inOff, out, outOff);

        return BLOCK_SIZE;
    }

    public void reset() {
    }

    private int[] generateWorkingKey(
            boolean forEncryption,
            byte[] userKey) {
        this.forEncryption = forEncryption;

        if (userKey.length != 32) {
            throw new IllegalArgumentException("Key length invalid. Key needs to be 32 byte - 256 bit!!!");
        }

        int key[] = new int[8];
        for (int i = 0; i != 8; i++) {
            key[i] = bytesToint(userKey, i * 4);
        }

        return key;
    }

    private int GOST28147_mainStep(int n1, int key) {
        int cm = (key + n1); // CM1

        // S-box replacing
        int om = S[0 + ((cm >> (0 * 4)) & 0xF)] << (0 * 4);
        om += S[16 + ((cm >> (1 * 4)) & 0xF)] << (1 * 4);
        om += S[32 + ((cm >> (2 * 4)) & 0xF)] << (2 * 4);
        om += S[48 + ((cm >> (3 * 4)) & 0xF)] << (3 * 4);
        om += S[64 + ((cm >> (4 * 4)) & 0xF)] << (4 * 4);
        om += S[80 + ((cm >> (5 * 4)) & 0xF)] << (5 * 4);
        om += S[96 + ((cm >> (6 * 4)) & 0xF)] << (6 * 4);
        om += S[112 + ((cm >> (7 * 4)) & 0xF)] << (7 * 4);

        return om << 11 | om >>> (32 - 11); // 11-leftshift
    }

    private void GOST28147Func(
            int[] workingKey,
            byte[] in,
            int inOff,
            byte[] out,
            int outOff) {
        int N1, N2, tmp;  //tmp -> for saving N1
        N1 = bytesToint(in, inOff);
        N2 = bytesToint(in, inOff + 4);

        if (this.forEncryption) {
            for (int k = 0; k < 3; k++) // 1-24 steps
            {
                for (int j = 0; j < 8; j++) {
                    tmp = N1;
                    N1 = N2 ^ GOST28147_mainStep(N1, workingKey[j]); // CM2
                    N2 = tmp;
                }
            }
            for (int j = 7; j > 0; j--) // 25-31 steps
            {
                tmp = N1;
                N1 = N2 ^ GOST28147_mainStep(N1, workingKey[j]); // CM2
                N2 = tmp;
            }
        } else //decrypt
        {
            for (int j = 0; j < 8; j++) // 1-8 steps
            {
                tmp = N1;
                N1 = N2 ^ GOST28147_mainStep(N1, workingKey[j]); // CM2
                N2 = tmp;
            }
            for (int k = 0; k < 3; k++) //9-31 steps
            {
                for (int j = 7; j >= 0; j--) {
                    if ((k == 2) && (j == 0)) {
                        break; // break 32 step
                    }
                    tmp = N1;
                    N1 = N2 ^ GOST28147_mainStep(N1, workingKey[j]); // CM2
                    N2 = tmp;
                }
            }
        }

        N2 = N2 ^ GOST28147_mainStep(N1, workingKey[0]);  // 32 step (N1=N1)

        intTobytes(N1, out, outOff);
        intTobytes(N2, out, outOff + 4);
    }

    //array of bytes to type int
    private int bytesToint(
            byte[] in,
            int inOff) {
        return ((in[inOff + 3] << 24) & 0xff000000) + ((in[inOff + 2] << 16) & 0xff0000)
                + ((in[inOff + 1] << 8) & 0xff00) + (in[inOff] & 0xff);
    }

    //int to array of bytes
    private void intTobytes(
            int num,
            byte[] out,
            int outOff) {
        out[outOff + 3] = (byte) (num >>> 24);
        out[outOff + 2] = (byte) (num >>> 16);
        out[outOff + 1] = (byte) (num >>> 8);
        out[outOff] = (byte) num;
    }

    public byte[] getSBox(
            String sBoxName) {
        byte[] sBox = null;

        if (sBoxName.equals("D-A")) {
            sBox = DSbox_A;
        }

        if (sBox == null) {
            throw new IllegalArgumentException("Unknown S-Box - possible types: \"D-A\".");
        }

        byte[] copy = new byte[sBox.length];

        System.arraycopy(sBox, 0, copy, 0, sBox.length);

        return copy;
    }
}
