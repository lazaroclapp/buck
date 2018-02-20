/*
 * Copyright 2015-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.rules.macros;

import static org.junit.Assert.assertEquals;

import com.facebook.buck.io.MorePathsForTests;
import com.facebook.buck.jvm.java.JavaLibraryBuilder;
import com.facebook.buck.model.BuildTargetFactory;
//import com.facebook.buck.model.MacroException;
import com.facebook.buck.rules.BuildRuleResolver;
import com.facebook.buck.rules.BuildRule;
import com.facebook.buck.rules.DefaultSourcePathResolver;
import com.facebook.buck.rules.DefaultTargetNodeToBuildRuleTransformer;
import com.facebook.buck.rules.SingleThreadedBuildRuleResolver;
import com.facebook.buck.rules.SourcePathRuleFinder;
import com.facebook.buck.rules.TargetGraph;
import com.facebook.buck.testutil.FakeProjectFilesystem;
import com.google.common.collect.ImmutableSet;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ProcessorpathMacroExpanderTest {

  private static final Path ROOT =
      MorePathsForTests.rootRelativePath(".").normalize().resolve("opt");
  private ProcessorpathMacroExpander expander;
  private FakeProjectFilesystem filesystem;

  @Before
  public void createMacroExpander() {
    this.expander = new ProcessorpathMacroExpander();
    this.filesystem =
        new FakeProjectFilesystem() {
          @Override
          public Path resolve(Path path) {
            return ROOT.resolve(path);
          }
        };
  }

  @Test
  public void shouldIncludeARuleIfNothingIsGiven() throws Exception {
    final BuildRuleResolver buildRuleResolver =
        new SingleThreadedBuildRuleResolver(
            TargetGraph.EMPTY, new DefaultTargetNodeToBuildRuleTransformer());
    BuildRule rule =
        JavaLibraryBuilder.createBuilder(BuildTargetFactory.newInstance("//pecan:pie"))
            .addSrc(Paths.get("Example.java"))  // Force a jar to be created
            .setAnnotationProcessors(ImmutableSet.of("annotation_proc.jar"))
            .build(buildRuleResolver, filesystem);

    String processorpath =
        expander.expand(
            DefaultSourcePathResolver.from(
                new SourcePathRuleFinder(buildRuleResolver)),
            rule);

    assertEquals(rule.getBuildTarget(), processorpath);
  }
/*
  @Test
  public void shouldIncludeTransitiveDependencies() throws MacroException {
    BuildRuleResolver ruleResolver = new BuildRuleResolver();
    BuildRule dep =
        JavaLibraryBuilder.createBuilder(BuildTargetFactory.newInstance("//exciting:dep"))
            .addSrc(Paths.get("Dep.java"))
            .build(ruleResolver);

    BuildRule rule =
        JavaLibraryBuilder.createBuilder(BuildTargetFactory.newInstance("//exciting:target"))
            .addSrc(Paths.get("Other.java"))
            .addDep(dep.getBuildTarget())
            .build(ruleResolver);

    String classpath = expander.expand(new FakeProjectFilesystem(), rule);

    // Alphabetical sorting expected, so "dep" should be before "rule"
    assertEquals(
        dep.getPathToOutputFile() + File.pathSeparator + rule.getPathToOutputFile(),
        classpath);
  }

  @Test(expected = MacroException.class)
  public void shouldThrowAnExceptionWhenRuleToExpandDoesNotHaveAClasspath() throws MacroException {
    BuildRule rule =
        ExportFileBuilder.newExportFileBuilder(BuildTargetFactory.newInstance("//cheese:peas"))
            .setSrc(new FakeSourcePath("some-file.jar"))
            .build(new BuildRuleResolver());

    expander.expand(new FakeProjectFilesystem(), rule);
  }*/
}