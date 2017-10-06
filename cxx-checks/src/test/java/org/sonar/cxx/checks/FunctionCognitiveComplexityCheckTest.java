package org.sonar.cxx.checks;

import org.junit.Test;
import org.sonar.cxx.CxxAstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

import java.io.IOException;

public class FunctionCognitiveComplexityCheckTest {
  @Test
  public void check() throws IOException {
    FunctionCognitiveComplexityCheck check = new FunctionCognitiveComplexityCheck();
    check.setMax(5);
    CxxFileTester tester = CxxFileTesterHelper.CreateCxxFileTester("src/test/resources/checks/FunctionCognitiveComplexity.cc", ".");
    SourceFile file = CxxAstScanner.scanSingleFile(tester.cxxFile, tester.sensorContext, CxxFileTesterHelper.mockCxxLanguage(), check);

    CheckMessagesVerifier.verify(file.getCheckMessages())
            .noMore();
  }
}
