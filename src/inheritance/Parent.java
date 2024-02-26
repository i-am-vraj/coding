package inheritance;

public abstract class Parent {

  public void method() {
    System.out.println("Parent");
  }

  public static void main(String[] args) {
    Parent parent = new Child();
    parent.method();
    parent.methodA();
  }

  private void methodA() {
    System.out.println("private method in abstract class");
  }


}
