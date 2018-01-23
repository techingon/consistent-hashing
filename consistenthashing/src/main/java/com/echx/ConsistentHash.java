package com.echx;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Thinking before Coding
 * Created by echov on 2018/1/9.
 */
public class ConsistentHash {

    private final TreeMap<Long, Node> virtualNodes;

    private final int replicaNumber;

    private final int identityHashCode;

    private static ConsistentHash consistentHash;


    public ConsistentHash(List<Node> nodes) {
        this.virtualNodes = new TreeMap<>();
        this.identityHashCode = identityHashCode(nodes);
        this.replicaNumber = 160;
        for (Node node : nodes) {
            for (int i = 0; i < replicaNumber / 4; i++) {
                byte[] digest = md5(node.getPath() + i);
                for (int h = 0; h < 4; h++) {
                    long m = hash(digest, h);
                    virtualNodes.put(m, node);
                }
            }
        }
    }

    private static int identityHashCode(List<Node> nodes){
//        Collections.sort(nodes);
//        StringBuilder sb = new StringBuilder();
//        for (String s: nodes
//             ) {
//            sb.append(s);
//        }
//        return sb.toString().hashCode();
        return System.identityHashCode(nodes);
    }

    public static Node select(Trigger trigger, List<Node> nodes) {
        int _identityHashCode = identityHashCode(nodes);
        if (consistentHash == null || consistentHash.identityHashCode != _identityHashCode) {
            synchronized (ConsistentHash.class) {
                if (consistentHash == null || consistentHash.identityHashCode != _identityHashCode) {
                    consistentHash = new ConsistentHash(nodes);
                }
            }
        }
        return consistentHash.select(trigger);
    }

    public Node select(Trigger trigger) {
        String key = trigger.toString();
        byte[] digest = md5(key);
        Node node = sekectForKey(hash(digest, 0));
        return node;
    }

    private Node sekectForKey(long hash) {
        Node node;
        Long key = hash;
        if (!virtualNodes.containsKey(key)) {
            SortedMap<Long, Node> tailMap = virtualNodes.tailMap(key);
            if (tailMap.isEmpty()) {
                key = virtualNodes.firstKey();
            } else {
                key = tailMap.firstKey();
            }
        }
        node = virtualNodes.get(key);
        return node;
    }

    private long hash(byte[] digest, int number) {
        return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                | (digest[0 + number * 4] & 0xFF))
                & 0xFFFFFFFFL;
    }

    private byte[] md5(String value) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        md5.reset();
        byte[] bytes;
        try {
            bytes = value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        md5.update(bytes);
        return md5.digest();
    }


}
