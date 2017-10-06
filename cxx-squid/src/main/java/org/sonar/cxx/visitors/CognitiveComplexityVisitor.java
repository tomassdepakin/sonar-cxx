package org.sonar.cxx.visitors;

import com.sonar.sslr.api.AstAndTokenVisitor;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.Token;
import org.sonar.cxx.api.CxxKeyword;
import org.sonar.cxx.api.CxxPunctuator;
import org.sonar.cxx.parser.CxxGrammarImpl;
import org.sonar.squidbridge.SquidAstVisitor;

public class CognitiveComplexityVisitor<GRAMMAR extends Grammar>
        extends SquidAstVisitor<GRAMMAR>
        implements AstAndTokenVisitor {

  public static int getFunctionComplexity(AstNode node) {
    getFunctionComplexity(node, true)
  }

  private static int getFunctionComplexity(AstNode node, boolean topLevel) {
    int complexity = 0;
    for (AstNode child: node.getChildren()) {
      complexity += getNodeComplexity(child, 0);
    }
    return complexity;
  }

  private static int getNodeComplexity(AstNode node, int current_nesting) {
    int complexity = 0;

    for (AstNode child: node.getChildren()) {
      // if, switch, ?, for, while, do while, catch increase complexity by current_nesting + 1.
      // Also nesting is increased
      if (child.is(CxxGrammarImpl.selectionStatement)
              || child.is(CxxPunctuator.QUEST)
              || child.is(CxxGrammarImpl.iterationStatement)
              || child.is(CxxKeyword.CATCH)) {
        complexity += current_nesting++;
        complexity++;
      } else if (child.is(CxxGrammarImpl.functionDefinition)) {
        // Nested functions increase only nesting level
        current_nesting++;
      }

      complexity += getNodeComplexity(child, current_nesting);
    }
    return complexity;
  }

  @Override
  public void visitToken(Token token) {
  }
}
