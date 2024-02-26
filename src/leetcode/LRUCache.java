package leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class LRUCache {

  class Node {
    private int key;
    private int value;
    private Node next;
    private Node prev;

    @Override
    public String toString() {
      return "Node{"
          + "key="
          + key
          + ", value="
          + value
          + ", next="
          + Optional.ofNullable(next).map(next -> next.key).orElse(null)
          + ", prev="
          + Optional.ofNullable(prev).map(prev -> prev.key).orElse(null)
          + '}';
    }
  }

  private final int capacity;
  private Map<Integer, Node> cacheMap = new HashMap<>();

  private Node start = new Node();
  private Node end = new Node();

  public LRUCache(int capacity) {
    this.capacity = capacity;
    start.key = -1;
    end.key = -2;
    start.next = end;
    end.prev = start;
    start.prev = null;
    end.next = null;
  }

  public int get(int key) {
    return Optional.ofNullable(cacheMap.get(key))
        .map(
            node -> {
              removeKey(key);
              insertKeyInFront(key, node.value);
              return node.value;
            })
        .orElse(-1);
  }

  public void put(int key, int value) {
    if (cacheMap.containsKey(key)) {
      removeKey(key);
    }
    insertKeyInFront(key, value);

    if (cacheMap.size() > capacity) {
      removeKeyFromBack();
    }
  }

  private void insertKeyInFront(int key, int value) {
    Node last = end.prev;
    Node add = new Node();
    add.key = key;
    add.value = value;
    last.next = add;
    add.prev = last;
    add.next = end;
    end.prev = add;
    cacheMap.put(key, add);
  }

  private void removeKeyFromBack() {
    Node remove = start.next;
    Node next = remove.next;
    next.prev = start;
    start.next = next;
    remove.next = null;
    remove.prev = null;
    cacheMap.remove(remove.key);
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
    LRUCache obj = new LRUCache(1);
    obj.put(2, 1);
    System.out.println(obj.cacheMap);
    obj.get(2);
    System.out.println(obj.cacheMap);
  }
}
