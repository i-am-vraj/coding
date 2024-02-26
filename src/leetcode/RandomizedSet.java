package leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

class RandomizedSet {

  private final Map<Integer, Integer> map;
  private final List<Integer> list;
  private final Random random;

  public RandomizedSet() {
    map = new HashMap<>();
    list = new ArrayList<>();
    random = new Random();
  }

  public boolean insert(int val) {
    if (map.containsKey(val)) return false;
    list.add(val);
    map.put(val, list.size() - 1);
    return true;
  }

  public boolean remove(int val) {
    if (!map.containsKey(val)) return false;
    int lastIndex = list.size() - 1;
    int index = map.get(val);
    int lastValue = list.get(lastIndex);
    list.set(index, lastValue);
    map.put(lastValue, index);
    list.remove(lastIndex);
    map.remove(val);
    return true;
  }

  public int getRandom() {
    return list.get(random.nextInt(list.size()));
  }

  public static void main(String[] args) {
    RandomizedSet randomizedSet = new RandomizedSet();
    randomizedSet.insert(0);
    randomizedSet.insert(1);
    randomizedSet.remove(0);
    randomizedSet.insert(2);
    randomizedSet.remove(1);
    randomizedSet.getRandom();
  }
}
