package org.malagu.panda.coke.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.malagu.panda.coke.model.Pinyin;
import org.springframework.util.StringUtils;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

public class PinyinUtils {

  public static Collection<Pinyin> toPinyin(String hanzi) {
    if (!StringUtils.hasText(hanzi)) {
      return Collections.emptyList();
    }


    Collection<Collection<String>> pinYinTokenList = new ArrayList<Collection<String>>();
    for (int i = 0; i < hanzi.length(); i++) {
      String[] pinyins = null;
      pinyins = PinyinHelper.convertToPinyinArray(hanzi.charAt(i), PinyinFormat.WITHOUT_TONE);
      if (pinyins == null) {
        continue;
      }
      pinYinTokenList.add(new HashSet<String>(Arrays.asList(pinyins)));
    }

    List<Pinyin> pinyinList = new ArrayList<Pinyin>();

    String best = PinyinHelper.convertToPinyinString(hanzi, "", PinyinFormat.WITHOUT_TONE);
    Pinyin bestPinyin = null;
    for (Collection<String> singlePinYinTokenList : pinYinTokenList) {
      if (pinyinList.size() == 0) {
        for (String pinyin : singlePinYinTokenList) {
          pinyinList.add(new Pinyin(pinyin));
        }
      } else {
        List<Pinyin> newPinYinList = new ArrayList<Pinyin>();
        for (Pinyin r : pinyinList) {
          for (String pinyin : singlePinYinTokenList) {
            newPinYinList.add(r.append(pinyin));
          }
        }
        pinyinList = newPinYinList;
      }
    }
    for (Pinyin pinyin : pinyinList) {
      if (ObjectUtils.equals(best, pinyin.getQuan())) {
        bestPinyin = pinyin;
      }
    }
    if (bestPinyin != null) {
      pinyinList.remove(bestPinyin);
      pinyinList.add(0, bestPinyin);
    }
    return pinyinList;
  }

  public static void main(String[] args) {
    System.out.println(toPinyin("中国人民银行"));
    toPinyin("银行");
  }
}
