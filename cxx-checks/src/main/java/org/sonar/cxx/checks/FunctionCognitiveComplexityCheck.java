package org.sonar.cxx.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.cxx.visitors.CognitiveComplexityVisitor;
import org.sonar.cxx.parser.CxxGrammarImpl;
import org.sonar.cxx.tag.Tag;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleLinearWithOffsetRemediation;
import org.sonar.squidbridge.checks.SquidCheck;

@Rule(
        key = "FunctionComplexity",
        name = "Functions should not be too complex",
        tags = {Tag.BRAIN_OVERLOAD},
        priority = Priority.MAJOR)
@ActivatedByDefault
@SqaleLinearWithOffsetRemediation(
        coeff = "1min",
        offset = "10min",
        effortToFixDescription = "per complexity point above the threshold")
public class FunctionCognitiveComplexityCheck extends SquidCheck<Grammar> {

  private static final int DEFAULT_MAX = 10;

  @RuleProperty(
          key = "max",
          description = "Maximum complexity allowed",
          defaultValue = "" + DEFAULT_MAX)
  private int max = DEFAULT_MAX;

  @Override
  public void init() {
    subscribeTo(CxxGrammarImpl.functionBody);
  }

  @Override
  public void leaveNode(AstNode node) {
    int complexity = CognitiveComplexityVisitor.getFunctionComplexity(node);
    if (complexity > max) {
      getContext().createLineViolation(this,
              "The Cognitive Complexity of this function is {0,number,integer} which is greater than {1,number,integer} authorized.",
              node,
              complexity,
              max);
    }
  }

  public void setMax(int max) {
    this.max = max;
  }
}
