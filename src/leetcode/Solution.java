package leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class Solution {

  private int tId = 1;
  private Map<String, AccountDTO> transactionMap = new HashMap<>();

  class AccountDTO implements Comparable<AccountDTO> {
    private String accId;
    private int balance;
    private int totalTransactions;
    private int time;
    private String sAccId;

    @Override
    public int compareTo(AccountDTO o) {
      return o.totalTransactions == this.totalTransactions
          ? this.accId.compareTo(o.accId)
          : Integer.compare(o.totalTransactions, this.totalTransactions);
    }

    @Override
    public String toString() {
      return accId + '(' + totalTransactions + ')';
    }

    public AccountDTO(String accId, int balance, int totalTransactions) {
      this.accId = accId;
      this.balance = balance;
      this.totalTransactions = totalTransactions;
    }

    public AccountDTO(String accId, String sAccId, int balance, int time) {
      this.accId = accId;
      this.sAccId = sAccId;
      this.balance = balance;
      this.time = time;
    }

    public String getAccId() {
      return accId;
    }

    public String getsAccId() {
      return sAccId;
    }

    private int getBalance() {
      return this.balance;
    }

    private int getTotalTransactions() {
      return this.totalTransactions;
    }

    public int getTime() {
      return time;
    }

    private void setBalance(int balance) {
      this.balance = balance;
    }

    private void setTotalTransactions(int totalTransactions) {
      this.totalTransactions = totalTransactions;
    }
  }

  String[] solution(String[][] queries) {

    int n = queries.length;

    Map<String, AccountDTO> accBalanceMap = new HashMap<>();
    String[] ans = new String[n];

    for (int i = 0; i < n; i++) {
      String operation = queries[i][0];
      switch (operation) {
        case "CREATE_ACCOUNT":
          createAccount(queries, i, accBalanceMap, ans);
          break;
        case "DEPOSIT":
          addBalance(queries, i, accBalanceMap, ans);
          break;
        case "PAY":
          removeBalance(queries, i, accBalanceMap, ans);
          break;
        case "TOP_ACTIVITY":
          topActivities(queries, i, accBalanceMap, ans);
          break;
        case "TRANSFER":
          transfer(queries, i, accBalanceMap, ans);
          break;
        case "ACCEPT_TRANSFER":
          acceptTransfer(queries, i, accBalanceMap, ans);
          break;
      }
    }

    return ans;
  }

  private void acceptTransfer(
      String[][] queries, int i, Map<String, AccountDTO> accBalanceMap, String[] ans) {
    int time = Integer.parseInt(queries[i][1]);
    String tAccId = queries[i][2];
    String transactionId = queries[i][3];
    if (!transactionMap.containsKey(transactionId)) {
      ans[i] = "false";
    } else {
      AccountDTO accountDTO = transactionMap.get(transactionId);
      if (!accountDTO.getAccId().equals(tAccId)) {
        ans[i] = "false";
      } else {

        if (accountDTO.getTime() > time) {
          AccountDTO dto = accBalanceMap.get(accountDTO.getsAccId());

          int balance = dto.getBalance();
          int finalBalance = balance + accountDTO.getBalance();
          dto.setBalance(finalBalance);

          int totalTransactions = dto.getTotalTransactions();
          int finalTransactions = totalTransactions + accountDTO.getBalance();
          dto.setTotalTransactions(finalTransactions);

          ans[i] = String.valueOf(finalBalance);
        } else {
          AccountDTO dto = accBalanceMap.get(tAccId);

          int balance = dto.getBalance();
          int finalBalance = balance + accountDTO.getBalance();
          dto.setBalance(finalBalance);

          int totalTransactions = dto.getTotalTransactions();
          int finalTransactions = totalTransactions + accountDTO.getBalance();
          dto.setTotalTransactions(finalTransactions);

          ans[i] = String.valueOf(finalBalance);
        }
      }
    }
  }

  private void transfer(
      String[][] queries, int i, Map<String, AccountDTO> accBalanceMap, String[] ans) {
    int time = Integer.parseInt(queries[i][1]) + 86400000;
    String sAccId = queries[i][2];
    String tAccId = queries[i][3];
    int removeBalance = Integer.parseInt(queries[i][4]);

    if (!accBalanceMap.containsKey(sAccId)
        || !accBalanceMap.containsKey(tAccId)
        || Objects.equals(sAccId, tAccId)) {
      ans[i] = "";
    } else {

      AccountDTO accountDTO = accBalanceMap.get(sAccId);

      int balance = accountDTO.getBalance();
      if (balance < removeBalance) {
        ans[i] = "";
      } else {
        int finalBalance = balance - removeBalance;
        accountDTO.setBalance(finalBalance);
        ans[i] = "transfer" + tId++;
        transactionMap.put(ans[i], new AccountDTO(tAccId, sAccId, removeBalance, time));

        int totalTransactions = accountDTO.getTotalTransactions();
        int finalTransactions = totalTransactions + removeBalance;
        accountDTO.setTotalTransactions(finalTransactions);
      }
    }
  }

  private static void topActivities(
      String[][] queries, int i, Map<String, AccountDTO> accBalanceMap, String[] ans) {
    int top = Integer.parseInt(queries[i][2]);
    List<AccountDTO> accountDTOs = accBalanceMap.values().stream().sorted().limit(top).toList();
    StringBuilder sb = new StringBuilder();
    for (int j = 0; j < accountDTOs.size(); j++) {
      sb.append(accountDTOs.get(j).toString());
      if (j < accountDTOs.size() - 1) {
        sb.append(", ");
      }
    }
    ans[i] = sb.toString();
  }

  private static void removeBalance(
      String[][] queries, int i, Map<String, AccountDTO> accBalanceMap, String[] ans) {
    String accId = queries[i][2];
    int removeBalance = Integer.parseInt(queries[i][3]);
    if (!accBalanceMap.containsKey(accId)) {
      ans[i] = "";
    } else {

      AccountDTO accountDTO = accBalanceMap.get(accId);

      int balance = accountDTO.getBalance();
      if (balance < removeBalance) {
        ans[i] = "";
      } else {
        int finalBalance = balance - removeBalance;
        accountDTO.setBalance(finalBalance);
        ans[i] = String.valueOf(finalBalance);

        int totalTransactions = accountDTO.getTotalTransactions();
        int finalTransactions = totalTransactions + removeBalance;
        accountDTO.setTotalTransactions(finalTransactions);
      }
    }
  }

  private static void addBalance(
      String[][] queries, int i, Map<String, AccountDTO> accBalanceMap, String[] ans) {
    String accId = queries[i][2];
    int addBalance = Integer.parseInt(queries[i][3]);
    if (!accBalanceMap.containsKey(accId)) {
      ans[i] = "";
    } else {
      AccountDTO accountDTO = accBalanceMap.get(accId);

      int balance = accountDTO.getBalance();
      int finalBalance = balance + addBalance;
      accountDTO.setBalance(finalBalance);

      int totalTransactions = accountDTO.getTotalTransactions();
      int finalTransactions = totalTransactions + addBalance;
      accountDTO.setTotalTransactions(finalTransactions);

      ans[i] = String.valueOf(finalBalance);
    }
  }

  private void createAccount(
      String[][] queries, int i, Map<String, AccountDTO> accBalanceMap, String[] ans) {
    String accId = queries[i][2];
    if (accBalanceMap.containsKey(accId)) {
      ans[i] = "false";
    } else {
      accBalanceMap.put(accId, new AccountDTO(accId, 0, 0));
      ans[i] = "true";
    }
  }

  public String removeStars(String s) {
    int len = s.length();
    char[] stack = new char[len];
    int top = 0;
    for (char c : s.toCharArray()) {
      if (c == '*') {
        top--;
      } else {
        stack[top++] = c;
      }
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < top; i++) {
      char c = stack[i];
      sb.append(c);
    }
    return sb.toString();
  }

  public int[] asteroidCollision(int[] asteroids) {
    int len = asteroids.length;
    int[] stack = new int[len];
    int top = 0;
    for (int i = 0; i < len; i++) {
      if (asteroids[i] < 0) {
        boolean update = true;
        while (top > 0 && stack[top - 1] >= 0) {
          if (Math.abs(asteroids[i]) > stack[top - 1]) {
            stack[top - 1] = asteroids[i];
            top--;
          } else if (Math.abs(asteroids[i]) == stack[top - 1]) {
            top--;
            update = false;
            break;
          } else {
            update = false;
            break;
          }
        }
        if (update) {
          stack[top++] = asteroids[i];
        }
      } else {
        stack[top++] = asteroids[i];
      }
      System.out.println(Arrays.toString(stack));
      System.out.println(top);
    }
    return Arrays.stream(stack).limit(top).toArray();
  }
}
