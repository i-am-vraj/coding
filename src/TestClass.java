import java.util.ArrayList;
import java.util.List;

public class TestClass {

  public static void main(String[] args) {
    List<String> list = new ArrayList<>();
    list.add("a");
    list.add("b");
    list.add("c");
    list.add("d");
    char ans =
        list.stream()
            .map(
                val -> {
                  System.out.println(val);
                  return val.charAt(0);
                })
            .findFirst()
            .orElse('n');
    System.out.println(ans);
  }
}
