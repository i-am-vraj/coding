public class DeadlockExample {
  private Object lock1 = new Object();
  private Object lock2 = new Object();

  public void method1() {
    synchronized (lock1) {
      System.out.println("Inside method1");
      // Critical section using lock1
      synchronized (lock2) {
        System.out.println("Inside method1, holding lock2 as well");
        // Critical section using lock2
      }
    }
  }

  public void method2() {
    synchronized (lock2) {
      System.out.println("Inside method2");
      // Critical section using lock2
      synchronized (lock1) {
        System.out.println("Inside method2, holding lock1 as well");
        // Critical section using lock1
      }
    }
  }

  public static void main(String[] args) {
    DeadlockExample deadlockExample = new DeadlockExample();
    // Thread 1 - Invoking method1
    Thread thread1 = new Thread(deadlockExample::method1);

    // Thread 2 - Invoking method2
    Thread thread2 = new Thread(deadlockExample::method2);

    // Start both threads
    thread1.start();
    thread2.start();
  }
}
