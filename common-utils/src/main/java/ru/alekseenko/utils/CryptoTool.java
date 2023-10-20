package ru.alekseenko.utils;

import org.hashids.Hashids;

public class CryptoTool {
    private final Hashids hashids;
    private static final int MINIMAL_HASH_LENGTH = 10;

    public CryptoTool(String salt) {

        this.hashids = new Hashids(salt, MINIMAL_HASH_LENGTH);
    }

    public String hashOf(Long value) {
        return hashids.encode(value);
    }

    public Long idOf(String value) {
        long[] res = hashids.decode(value);
        if (res != null && res.length > 0) {
            return res[0];
        }
        return null;
    }
}
