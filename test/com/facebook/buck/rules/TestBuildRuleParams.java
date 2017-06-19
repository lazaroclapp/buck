/*
 * Copyright 2013-present Facebook, Inc.
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

package com.facebook.buck.rules;

import com.facebook.buck.io.ProjectFilesystem;
import com.facebook.buck.model.BuildTarget;
import com.facebook.buck.model.BuildTargetFactory;
import com.facebook.buck.testutil.FakeProjectFilesystem;
import com.google.common.collect.ImmutableSortedSet;

public class TestBuildRuleParams {

  public static BuildRuleParams create(
      BuildTarget buildTarget, ProjectFilesystem projectFilesystem) {
    return new BuildRuleParams(
        buildTarget,
        () -> ImmutableSortedSet.of(),
        () -> ImmutableSortedSet.of(),
        ImmutableSortedSet.of(),
        projectFilesystem);
  }

  public static BuildRuleParams create(String buildTarget, ProjectFilesystem projectFilesystem) {
    return create(BuildTargetFactory.newInstance(buildTarget), projectFilesystem);
  }

  public static BuildRuleParams create(BuildTarget buildTarget) {
    return create(buildTarget, new FakeProjectFilesystem());
  }

  public static BuildRuleParams create(String buildTarget) {
    return create(buildTarget, new FakeProjectFilesystem());
  }
}
