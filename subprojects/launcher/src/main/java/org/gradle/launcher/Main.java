/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.launcher;

import org.gradle.launcher.bootstrap.EntryPoint;
import org.gradle.launcher.bootstrap.ExecutionListener;
import org.gradle.launcher.cli.CommandLineActionFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Arrays;
import java.util.List;

/**
 * The main command-line entry-point for Gradle.
 */
public class Main extends EntryPoint {
    public static void main(String[] args) {
        assertXmx();
        new Main().run(args);
    }

    protected void doAction(String[] args, ExecutionListener listener) {
        createActionFactory().convert(Arrays.asList(args)).execute(listener);
    }

    CommandLineActionFactory createActionFactory() {
        return new CommandLineActionFactory();
    }

    private static void assertXmx() {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        for (String arg :arguments) {
            if (arg.startsWith("-Xmx")) {
                return;
            }
        }
        throw new RuntimeException("-Xmx no defined for: "
            + Main.class.getSimpleName()
            + "\n" + runtimeMxBean.getInputArguments());
    }
}
