package leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class LFUCache {

  class Node {
    private int key = -1;
    private int value;
    private int freq;
    private Node next;
    private Node prev;

    @Override
    public String toString() {
      return "Node{"
          + "key="
          + key
          + ", value="
          + value
          + ", freq="
          + freq
          + ", next="
          + Optional.ofNullable(next).map(next -> next.key).orElse(null)
          + ", prev="
          + Optional.ofNullable(prev).map(prev -> prev.key).orElse(null)
          + '}';
    }
  }

  private final int capacity;
  private Map<Integer, Node> cacheMap = new HashMap<>();

  List<Map.Entry<Node, Node>> linkPointers = new ArrayList<>();

  public LFUCache(int capacity) {
    this.capacity = capacity;
    for (int i = 0; i < 2 * Math.pow(10, 5); i++) {
      Node start = new Node();
      Node end = new Node();
      start.next = end;
      end.prev = start;
      start.prev = null;
      end.next = null;
      linkPointers.add(Map.entry(start, end));
    }
  }

  public int get(int key) {
    return Optional.ofNullable(cacheMap.get(key))
        .map(
            node -> {
              int freq = node.freq;
              removeKey(key);
              insertKeyInFront(key, node.value, freq + 1);
              return node.value;
            })
        .orElse(-1);
  }

  public void put(int key, int value) {
    if (cacheMap.size() == capacity && !cacheMap.containsKey(key)) {
      removeLFUOrLRUKey();
    }

    int freq = 1;
    if (cacheMap.containsKey(key)) {
      freq = cacheMap.get(key).freq + 1;
      removeKey(key);
    }
    insertKeyInFront(key, value, freq);
  }

  private void removeLFUOrLRUKey() {
    for (int i = 0; i < linkPointers.size(); i++) {
      Node start = linkPointers.get(i).getKey();
      if (start.next.key != -1) {
        Node remove = start.next;
        Node next = remove.next;
        start.next = next;
        next.prev = start;
        remove.next = null;
        remove.prev = null;
        cacheMap.remove(remove.key);
        break;
      }
    }
  }

  private void insertKeyInFront(int key, int value, int freq) {

    Node end = linkPointers.get(freq - 1).getValue();

    Node last = end.prev;
    Node add = new Node();
    add.key = key;
    add.value = value;
    add.freq = freq;
    last.next = add;
    add.prev = last;
    add.next = end;
    end.prev = add;
    cacheMap.put(key, add);
  }

  private void removeKey(int key) {
    Node remove = cacheMap.get(key);
    Node next = remove.next;
    Node prev = remove.prev;
    prev.next = next;
    next.prev = prev;
    cacheMap.remove(remove.key);
  }

  public static void main(String[] args) {
    LFUCache cache = new LFUCache(10);
    int[][] inputs = {
      {10, 13}, {3, 17}, {6, 11}, {10, 5}, {9, 10}, {13}, {2, 19}, {2}, {3}, {5, 25}, {8}, {9, 22},
      {5, 5}, {1, 30}, {11}, {9, 12}, {7}, {5}, {8}, {9}, {4, 30}, {9, 3}, {9}, {10}, {10}, {6, 14},
      {3, 1}, {3}, {10, 11}, {8}, {2, 14}, {1}, {5}, {4}, {11, 4}, {12, 24}, {5, 18}, {13}, {7, 23},
      {8}, {12}, {3, 27}, {2, 12}, {5}, {2, 9}, {13, 4}, {8, 18}, {1, 7}, {6}, {9, 29}, {8, 21},
      {5}, {6, 30}, {1, 12}, {10}, {4, 15}, {7, 22}, {11, 26}, {8, 17}, {9, 29}, {5}, {3, 4},
      {11, 30}, {12}, {4, 29}, {3}, {9}, {6}, {3, 4}, {1}, {10}, {3, 29}, {10, 28}, {1, 20},
      {11, 13}, {3}, {3, 12}, {3, 8}, {10, 9}, {3, 26}, {8}, {7}, {5}, {13, 17}, {2, 27}, {11, 15},
      {12}, {9, 19}, {2, 15}, {3, 16}, {1}, {12, 17}, {9, 1}, {6, 19}, {4}, {5}, {5}, {8, 1},
      {11, 7}, {5, 2}, {9, 28}, {1}, {2, 2}, {7, 4}, {4, 22}, {7, 24}, {9, 26}, {13, 28}, {11, 26}
    };

    for (int[] input : inputs) {
      if (input.length == 2) {
        cache.put(input[0], input[1]);
        System.out.print("null,");
      } else {
        System.out.print(cache.get(input[0]) + ",");
      }
    }
  }
}
