/*
 * Sonar C++ Plugin (Community)
 * Copyright (C) 2010-2017 SonarOpenCommunity
 * http://github.com/SonarOpenCommunity/sonar-cxx
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.cxx.sensors.veraxx;

import org.sonar.cxx.sensors.tests.xunit.CxxXunitSensor;
import org.sonar.cxx.sensors.utils.TestUtils;
import static org.fest.assertions.Assertions.assertThat;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.when;

import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.Settings;
import org.sonar.cxx.CxxLanguage;

public class CxxVeraxxSensorTest {

  private DefaultFileSystem fs;
  private CxxLanguage language;
  private Settings settings;

  @Before
  public void setUp() {
    fs = TestUtils.mockFileSystem();
    settings = new Settings();
    language = TestUtils.mockCxxLanguage();
    when(language.getPluginProperty(CxxVeraxxSensor.REPORT_PATH_KEY)).thenReturn("sonar.cxx." + CxxVeraxxSensor.REPORT_PATH_KEY);
    when(language.IsRecoveryEnabled()).thenReturn(true);
    }

  @Test
  public void shouldReportCorrectViolations() {
    SensorContextTester context = SensorContextTester.create(fs.baseDir());

    settings.setProperty(language.getPluginProperty(CxxVeraxxSensor.REPORT_PATH_KEY), "vera++-reports/vera++-result-*.xml");
    context.setSettings(settings);
    
    CxxVeraxxSensor sensor = new CxxVeraxxSensor(language, settings);
    
    context.fileSystem().add(new DefaultInputFile("ProjectKey", "sources/application/main.cpp").setLanguage("cpp").initMetadata("asd\nasdas\nasda\n"));
    context.fileSystem().add(new DefaultInputFile("ProjectKey", "sources/tests/SAMPLE-test.cpp").setLanguage("cpp").initMetadata("asd\nasdas\nasda\n"));
    context.fileSystem().add(new DefaultInputFile("ProjectKey", "sources/tests/SAMPLE-test.h").setLanguage("cpp").initMetadata("asd\nasdas\nasda\n"));
    context.fileSystem().add(new DefaultInputFile("ProjectKey", "sources/tests/main.cpp").setLanguage("cpp").initMetadata("asd\nasdas\nasda\n"));
    context.fileSystem().add(new DefaultInputFile("ProjectKey", "sources/utils/code_chunks.cpp").setLanguage("cpp").initMetadata("asd\nasdas\nasda\n"));
    context.fileSystem().add(new DefaultInputFile("ProjectKey", "sources/utils/utils.cpp").setLanguage("cpp").initMetadata("asd\nasdas\nasda\n"));
    sensor.execute(context);
    assertThat(context.allIssues()).hasSize(10);
  }
}
