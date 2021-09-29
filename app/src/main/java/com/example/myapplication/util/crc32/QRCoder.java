/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.myapplication.util.crc32;

/**
 *
 * @author administrator
 */
public class QRCoder {

    public static String make(String siteId, String documentId, byte[] data) {

        OzDSt1106Digest digest = new OzDSt1106Digest("D-A");
        digest.update(data, 0, data.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        String code = siteId + documentId + HexBin.encode(hash);
        Crc32 crc32 = new Crc32();
        crc32.update(HexBin.decode(code));
        long val = crc32.getValue();
        byte[] crcv = new byte[4];
        crcv[3] = (byte) (val & 0xff);
        val = val >> 8;
        crcv[2] = (byte) (val & 0xff);
        val = val >> 8;
        crcv[1] = (byte) (val & 0xff);
        val = val >> 8;
        crcv[0] = (byte) (val & 0xff);
        code = code + HexBin.encode(crcv);
        return code;
    }

    public static String make(String siteId, String documentId, String document) {
        return make(siteId, documentId, document.getBytes());
    }
}
