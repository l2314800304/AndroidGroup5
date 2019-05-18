package com.androidgroup5.onlinecontact.cn;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public final class CNPinyinIndexFactory {
    /**
     * 转换搜索拼音集合, 考虑在子线程中运行
     * @param cnPinyinList
     * @param keyword
     * @return
     */
    public static <T extends CN> ArrayList<CNPinyinIndex<T>> indexList(List<CNPinyin<T>> cnPinyinList, String keyword) {
        ArrayList<CNPinyinIndex<T>> cnPinyinIndexArrayList = new ArrayList<>();
        for (CNPinyin<T> cnPinyin : cnPinyinList) {
            CNPinyinIndex<T> index = index(cnPinyin, keyword);
            if (index != null) {
                cnPinyinIndexArrayList.add(index);
            }
        }
        return cnPinyinIndexArrayList;
    }

    /**
     * 匹配拼音
     * @param cnPinyin
     * @return null代表没有匹配
     */
    public static <T extends CN> CNPinyinIndex<T> index(CNPinyin<T> cnPinyin, String keyword) {
        if (TextUtils.isEmpty(keyword)) return null;
        CNPinyinIndex cnPinyinIndex = matcherChinese(cnPinyin, keyword);
        if (isContainChinese(keyword)) {//包含中文只匹配原字符
            return cnPinyinIndex;
        }
        if (cnPinyinIndex == null) {
            cnPinyinIndex = matcherFirst(cnPinyin, keyword);
            if (cnPinyinIndex == null) {
                cnPinyinIndex = matchersPinyins(cnPinyin, keyword);
            }
        }
        return cnPinyinIndex;
    }

    /**
     * 匹配中文
     * @param cnPinyin
     * @param keyword
     * @return
     */
    static CNPinyinIndex matcherChinese(CNPinyin cnPinyin, String keyword) {
        if (keyword.length() < cnPinyin.data.chinese().length()) {
            Matcher matcher = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE).matcher(cnPinyin.data.chinese());
            if (matcher.find()) {
                return new CNPinyinIndex(cnPinyin, matcher.start(), matcher.end());
            }
        }
        return null;
    }

    /**
     * 匹配首字母
     * @param cnPinyin
     * @param keyword
     * @return
     */
    static CNPinyinIndex matcherFirst(CNPinyin cnPinyin, String keyword) {
        if (keyword.length() <= cnPinyin.pinyins.length) {
            Matcher matcher = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE).matcher(cnPinyin.firstChars);
            if (matcher.find()) {
                return new CNPinyinIndex(cnPinyin, matcher.start(), matcher.end());
            }
        }
        return null;
    }

    /**
     * 所有拼音匹配
     * @param cnPinyin
     * @param keyword
     * @return
     */
    static CNPinyinIndex matchersPinyins(CNPinyin cnPinyin, String keyword) {
        if (keyword.length() > cnPinyin.pinyinsTotalLength) return null;
        int start = -1;
        int end = -1;
        for (int i = 0; i < cnPinyin.pinyins.length; i++) {
            String pat = cnPinyin.pinyins[i];
            if (pat.length() >= keyword.length()) {//首个位置索引
                Matcher matcher = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE).matcher(pat);
                if (matcher.find() && matcher.start() == 0) {
                    start = i;
                    end = i + 1;
                    break;
                }
            } else {
                Matcher matcher = Pattern.compile(pat, Pattern.CASE_INSENSITIVE).matcher(keyword);
                if (matcher.find() && matcher.start() == 0) {//全拼匹配第一个必须在0位置
                    start = i;
                    String left = matcher.replaceFirst("");
                    end = end(cnPinyin.pinyins, left, ++i);
                    break;
                }
            }
        }
        if (start >= 0 && end >= start) {
            return new CNPinyinIndex(cnPinyin, start, end);
        }
        return null;
    }

    /**
     * 根据匹配字符递归查找下一结束位置
     * @param pinyinGroup
     * @param pattern
     * @param index
     * @return -1 匹配失败
     */
    private static int end(String[] pinyinGroup, String pattern, int index) {
        if (index < pinyinGroup.length) {
            String pinyin = pinyinGroup[index];
            if (pinyin.length() >= pattern.length()) {//首个位置索引
                Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(pinyin);
                if (matcher.find() && matcher.start() == 0) {
                    return index + 1;
                }
            } else {
                Matcher matcher = Pattern.compile(pinyin, Pattern.CASE_INSENSITIVE).matcher(pattern);
                if (matcher.find() && matcher.start() == 0) {//全拼匹配第一个必须在0位置
                    String left = matcher.replaceFirst("");
                    return end(pinyinGroup, left, index + 1);
                }
            }
        }
        return -1;
    }

    private static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

}
