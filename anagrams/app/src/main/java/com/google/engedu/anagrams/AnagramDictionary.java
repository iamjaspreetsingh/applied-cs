/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    private HashSet<String> wordset = new HashSet<>();
    private ArrayList<String> wordlist = new ArrayList<>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();


    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            String sort_word = sortString(word);
            wordlist.add(word);
            if (sizeToWords.containsKey(word.length()))
                sizeToWords.get(word.length()).add(word);
            else {
                sizeToWords.put(word.length(), new ArrayList<String>());
                sizeToWords.get(word.length()).add(word);
            }
            if (lettersToWord.containsKey(sort_word))
                lettersToWord.get(sort_word).add(word);
            else {
                lettersToWord.put(sort_word, new ArrayList<String>());
                lettersToWord.get(sort_word).add(word);
            }
        }
        wordset.addAll(wordlist);
        Log.e(TAG, "AnagramDictionary: wordset size" + wordset.size(), null);
        Log.e(TAG, "AnagramDictionary: wordmap size" + lettersToWord.size(), null);
        Log.e(TAG, "AnagramDictionary: wordlist size" + wordlist.size(), null);
        Log.e(TAG, "AnagramDictionary: sizetowords size" + sizeToWords.size(), null);
    }

    public boolean isGoodWord(String word, String base) {
        if (wordset.contains(word) && !(word.contains(base)))
            return true;
        return false;

    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String word = sortString(targetWord);

        /*for (String temp : wordlist) {
            if (temp.length() == targetWord.length()) {
                if (word.equals(sortString(temp))) {
                    result.add(temp);
                }
            }
        }*/

        /*if (!lettersToWord.containsKey(word))
        {
            lettersToWord.put(word, new ArrayList<String>());
            lettersToWord.get(word).add(targetWord);
        }*/
        //Log.e(TAG, "getAnagrams: count " + lettersToWord.get(word).size(), null);
        return lettersToWord.get(word);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (char i = 'a'; i <= 'z'; i++) {
            String sort_word = sortString(word + i);
            if (lettersToWord.get(sort_word) != null)
                result.addAll(lettersToWord.get(sort_word));
        }
        for (int i = 0; i < result.size(); i++) {
            String temp = result.get(i);
            if (temp.contains(word))
                result.remove(temp);
        }
        Log.e(TAG, "getAnagramswith one more letter: count " + result.size(), null);
        return result;
    }

    private String sortString(String input) {
        char[] charArray = input.toLowerCase().toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }

    public String pickGoodStarterWord() {
        int n = random.nextInt(sizeToWords.get(wordLength).size() - 1);
        String start;
        int anagram = 0;
        do {
            start = sizeToWords.get(wordLength).get(n);
            anagram = getAnagramsWithOneMoreLetter(start).size();
            n++;
            if (n == sizeToWords.get(wordLength).size())
                n = 0;
        } while (anagram < MIN_NUM_ANAGRAMS);
        if (wordLength < MAX_WORD_LENGTH)
            wordLength++;
        return start;
    }
}
