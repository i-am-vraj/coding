public class Swapper {
  public static void main(String[] args) {
    String s1 = "s1";
    String s2 = "s1";
    System.out.println(s1.hashCode());
    System.out.println(s2.hashCode());
    System.out.println(System.identityHashCode(s1));
    System.out.println(System.identityHashCode(s2));
    System.out.println(s1 == s2);
    s1 = "s2";
    s2 = "s2";
    System.out.println(s1.hashCode());
    System.out.println(s2.hashCode());
    System.out.println(System.identityHashCode(s1));
    System.out.println(System.identityHashCode(s2));
    System.out.println(s1 == s2);
  }
}
