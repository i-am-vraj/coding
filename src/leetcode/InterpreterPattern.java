package leetcode;

import java.util.Stack;

public class InterpreterPattern {

  public interface Expression {
    String interpret();
  }

  public static class MultiplicationExpression implements Expression {

    private final Expression times;
    private final Expression repeat;

    @Override
    public String toString() {
      return "MultiplicationExpression{" + "times=" + times + ", repeat=" + repeat + '}';
    }

    public MultiplicationExpression(Expression times, Expression repeat) {
      this.times = times;
      this.repeat = repeat;
    }

    @Override
    public String interpret() {
      return repeat.interpret().repeat(Integer.parseInt(times.interpret()));
    }
  }

  public static class MultiplicationPlaceholder implements Expression {

    @Override
    public String toString() {
      return "MultiplicationPlaceholder";
    }

    @Override
    public String interpret() {
      // this should not be called as this is just a placeholder impl
      throw new RuntimeException();
    }
  }

  public static class AdditionExpression implements Expression {
    private final Expression s1;
    private final Expression s2;

    @Override
    public String toString() {
      return "AdditionExpression{" + "s1=" + s1 + ", s2=" + s2 + '}';
    }

    public AdditionExpression(Expression s1, Expression s2) {
      this.s1 = s1;
      this.s2 = s2;
    }

    @Override
    public String interpret() {
      return s1.interpret() + s2.interpret();
    }
  }

  public static class AdditionPlaceholder implements Expression {

    @Override
    public String toString() {
      return "AdditionPlaceholder";
    }

    @Override
    public String interpret() {
      // this should not be called as this is just a placeholder impl
      throw new RuntimeException();
    }
  }

  public static class StringExpression implements Expression {
    private final String string;

    @Override
    public String toString() {
      return "StringExpression{" + "string='" + string + '\'' + '}';
    }

    public StringExpression(String string) {
      this.string = string;
    }

    @Override
    public String interpret() {
      return this.string;
    }
  }

  public static class NumberExpression implements Expression {
    private final int number;

    @Override
    public String toString() {
      return "NumberExpression{" + "number=" + number + '}';
    }

    public NumberExpression(int number) {
      this.number = number;
    }

    @Override
    public String interpret() {
      return String.valueOf(this.number);
    }
  }

  public static String decodeString(String s) {
    Expression expression = buildExpression(s);
    return expression.interpret();
  }

  public static Expression buildExpression(String s) {

    // 3[c2[4[d]]]ab3[c]
    // 3, mul, [c, add, 2, mul, 4, mul, d], add, a, add, b, add, 3, mul, c
    // cddddddddcddddddddcddddddddabccc

    Stack<Expression> expressions = new Stack<>();
    int curr = 0;
    while (curr < s.length()) {
      if (isNum(s.charAt(curr))) {
        pushAddExpressionIfRequired(s, curr, expressions);
        curr = addNumberExpression(s, curr, expressions);
      } else if (s.charAt(curr) == '[') {
        expressions.push(new MultiplicationPlaceholder());
        curr = addInnerExpression(s, curr, expressions);
      } else if (s.charAt(curr) == ']') {
        curr++;
        expressions.push(evaluateStackExpressions(expressions));
      } else {
        pushAddExpressionIfRequired(s, curr, expressions);
        expressions.push(new StringExpression("" + s.charAt(curr++)));
      }
    }

    return evaluateStackExpressions(expressions);
  }

  private static void pushAddExpressionIfRequired(
      String s, int curr, Stack<Expression> expressions) {
    if (curr > 0 && s.charAt(curr - 1) != '[') {
      expressions.push(new AdditionPlaceholder());
    }
  }

  private static Expression evaluateStackExpressions(Stack<Expression> expressions) {
    Expression left;
    Expression right = null;

    while (expressions.size() > 1) {
      Expression expression = expressions.pop();
      if (expression instanceof StringExpression || expression instanceof NumberExpression) {
        right = expression;
      } else if (expression instanceof MultiplicationPlaceholder) {
        left = expressions.pop();
        expressions.push(
            new StringExpression(
                new MultiplicationExpression(
                        new StringExpression(left.interpret()),
                        new StringExpression(right.interpret()))
                    .interpret()));
      } else if (expression instanceof AdditionPlaceholder) {
        left = expressions.pop();
        expressions.push(
            new StringExpression(
                new AdditionExpression(
                        new StringExpression(left.interpret()),
                        new StringExpression(right.interpret()))
                    .interpret()));
      } else {
        // this should not happen
        // as we are not pushing any other type of expressions in stack
        throw new RuntimeException();
      }
    }

    return expressions.pop();
  }

  private static int addInnerExpression(String s, int curr, Stack<Expression> expressions) {
    int start = curr + 1;
    int done = 0;
    while (true) {
      if (s.charAt(curr) == '[') {
        done++;
      } else if (s.charAt(curr) == ']') {
        done--;
      }
      if (done == 0) {
        expressions.push(buildExpression(s.substring(start, curr)));
        break;
      }
      curr++;
    }
    return curr;
  }

  private static int addNumberExpression(String s, int curr, Stack<Expression> expressions) {
    int start = curr;
    int end = curr;
    curr++;
    while (isNum(s.charAt(curr))) {
      curr++;
      end = curr;
    }
    expressions.push(new NumberExpression(getNumberExpression(s, start, end)));
    return curr;
  }

  private static int getNumberExpression(String s, int start, int end) {
    if (start == end) {
      return s.charAt(start) - 48;
    }
    return Integer.parseInt(s.substring(start, end));
  }

  private static boolean isNum(char c) {
    return c - 48 >= 0 && c - 48 < 10;
  }

  public static void main(String[] args) {
    String s = "3[c2[4[d]]]ab3[c]";
    System.out.println(decodeString(s));
  }
}
