package com.qibill.redis;

public class BloomFilter {
    private byte[] data;

    public BloomFilter(int initSize) {
        this.data = new byte[initSize * 2]; // 默认创建大小 * 2 的空间
    }

    public void add(int key) {
        int location1 = Math.abs(hash1(key) % data.length);
        int location2 = Math.abs(hash2(key) % data.length);
        int location3 = Math.abs(hash3(key) % data.length);

        data[location1] = data[location2] = data[location3] = 1;
    }

    public boolean contains(int key) {
        int location1 = Math.abs(hash1(key) % data.length);
        int location2 = Math.abs(hash2(key) % data.length);
        int location3 = Math.abs(hash3(key) % data.length);

        return data[location1] * data[location2] * data[location3] == 1;
    }

    private int hash1(Integer key) {
        return key.hashCode();
    }

    private int hash2(Integer key) {
        int hashCode = key.hashCode();
        return hashCode ^ (hashCode >>> 3);
    }

    private int hash3(Integer key) {
        int hashCode = key.hashCode();
        return hashCode ^ (hashCode >>> 16);
    }
}
