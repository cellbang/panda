package org.malagu.panda.coke.codec;

public class Feistel {
  public static Double roundFunction(Long val) {
    return ((131239 * val + 15534) % 714025) / 714025.0;
  }

  public static Long permutedId(Long id) {
    Long l1 = (id >> 16) & 65535;
    Long r1 = id & 65535;

    for (int i = 0; i < 2; i++) {
      Long l2 = r1;
      Long r2 = l1 ^ (int) (roundFunction(r1) * 65535);
      l1 = l2;
      r1 = r2;
    }
    return ((r1 << 16) + l1);
  }

  public static void main(String[] args) {
    for (int i = 0; i < 100; i++) {
      Long newId = permutedId(Long.valueOf(i));
      Long de = permutedId(newId);

      System.out.println(
          String.format("i=%d, en=%d, de=%d, base64=%s", i, newId, de, Test._10_to_62(newId, 6)));
    }

  }
}
