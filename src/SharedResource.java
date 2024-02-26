public class SharedResource {
  private volatile int counter = 0;

  public void increment() {
    counter++; // This operation is not atomic
  }

  public int getCounter() {
    return counter;
  }

  public static void main(String[] args) {
    SharedResource sharedResource = new SharedResource();
    Thread t1 = new Thread(getTask(sharedResource, "t1"));
    Thread t2 = new Thread(getTask(sharedResource, "t2"));
    Thread t3 = new Thread(getTask(sharedResource, "t3"));
    Thread t4 = new Thread(getTask(sharedResource, "t4"));
    Thread t5 = new Thread(getTask(sharedResource, "t5"));
    Thread t6 = new Thread(getTask(sharedResource, "t6"));
    t1.start();
    t2.start();
    t3.start();
    t4.start();
    t5.start();
    t6.start();

    try {
      t1.join();
      t2.join();
      t3.join();
      t4.join();
      t5.join();
      t6.join();
    } catch (Exception e) {
      System.out.println(e);
    }
    System.out.println("final value: " + sharedResource.getCounter());
  }

  private static Runnable getTask(SharedResource sharedResource, String name) {
    return () -> {
      for (int i = 0; i < 1000; i++) {
        System.out.println(name + " index: " + i + " " + sharedResource.getCounter());
        sharedResource.increment();
        System.out.println(
            name + " after increment index: " + i + " " + sharedResource.getCounter());
      }
    };
  }
}
