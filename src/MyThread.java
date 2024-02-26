public class MyThread extends Thread {

  private final String threadName;

  public MyThread(String threadName) {
    super(threadName);
    this.threadName = threadName;
  }

  @Override
  public void run() {
    for (int i = 0; i < 500; i++) {
      System.out.println(threadName + " " + Thread.currentThread().getName() + " " + i);
    }
  }
}
