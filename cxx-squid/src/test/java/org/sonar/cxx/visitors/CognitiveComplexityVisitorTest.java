package org.sonar.cxx.visitors;

import com.sonar.sslr.api.AstNode;
import org.junit.Test;
import org.sonar.cxx.parser.ParserBaseTest;


import static org.junit.Assert.assertEquals;

public class CognitiveComplexityVisitorTest extends ParserBaseTest {
  private void testCodeComplexity(String code, int expectedComplexity) {
    AstNode node = p.parse(code);
    int complexity = CognitiveComplexityVisitor.getFunctionComplexity(node);
    assertEquals(expectedComplexity, complexity);
  }

  @Test
  public void testEmptyFunctionComplexity() {
    String code = "int main() {}";
    testCodeComplexity(code, 0);
  }

  @Test
  public void testIfComplexity() {
    String code = "int main() {if (true) {return;}}";
    testCodeComplexity(code, 1); // +1 if
  }

  @Test
  public void testNestedIfComplexity() {
    String code = "int main() {if (true) {if (true) {return;}}}";
    testCodeComplexity(code, 3); // +1 if, +1 nesting, +1 if
  }

  @Test
  public void testSwitchComplexity() {
    String code = "int main() {switch (i) {case 1: return 1; case 2: return 2; default: return 0;}}";
    testCodeComplexity(code, 1); // +1 switch
  }

  @Test
  public void testTernaryComplexity() {
    String code = "int main() {x ? 1 : 0;}";
    testCodeComplexity(code, 1); // +1 if
  }

  @Test
  public void testNestedTernaryComplexity() {
    String code = "int main() {x ? 1 : y ? 1 : 0;}";
    testCodeComplexity(code, 3); // +1 if, +1 nesting, +1 if
  }

  @Test
  public void testForComplexity() {
    String code = "int main() {for (int i=0; i<1; i++) {cout << i;}}";
    testCodeComplexity(code, 1); // +1 for
  }

  @Test
  public void testNestedForComplexity() {
    String code = "int main() {for (int i=0; i<1; i++) {for (int j=0; j<1; j++) {cout << j;}}}";
    testCodeComplexity(code, 3); // +1 for, +1 nesting, +1 for
  }

  @Test
  public void testForNestedIfComplexity() {
    String code = "int main() {for (int i=0; i<1; i++) {if (true) {if (true) {return;}}}}";
    testCodeComplexity(code, 6); // +1 for, +1 nesting, +1 if, +2 nesting, +1 if
  }

  @Test
  public void testTryCatchComplexity() {
    String code = "int main() {try {int i=1;} catch (...) {}}";
    testCodeComplexity(code, 1); // +1 catch
  }

  @Test
  public void testTryMultipleCatchComplexity() {
    String code = "int main() {try {int i=1;} catch (int i) {} catch (...) {}}";
    testCodeComplexity(code, 2); // +1 catch +1 catch
  }

  @Test
  public void testLambdaIfComplexity() {
    String code = "int main() {auto f = []() {if (true) return 42;};}";
    testCodeComplexity(code, 2); // +1 nesting, +1 if
  }

  @Test
  public void testNestedStructIfComplexity() {
    String code = "int main() {struct X {int func() { if (true) return 1;};};}";
    testCodeComplexity(code, 2); // +0 struct, +1 func nesting, +1 if
  }

  @Test
  public void testLogicalOperatorComplexity() {
    String code = "int main() {return a && b;}";
    testCodeComplexity(code, 1); // +1 logical op
  }

  @Test
  public void testSameLogicalOperatorSequenceComplexity() {
    String code = "int main() {return a && b && c;}";
    testCodeComplexity(code, 1); // +1 logical op, +0 same logical op
  }

  @Test
  public void testOtherLogicalOperatorSequenceComplexity() {
    String code = "int main() {return a && b && c || d;}";
    testCodeComplexity(code, 1); // +1 logical op, +0 same logical op, +1 another logical op
  }

}
