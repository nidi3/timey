/*
 * Copyright (C) 2014 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.timey;

import javax.swing.*;
import java.io.*;
import java.time.LocalDateTime;

/**
 *
 */
public class Logger {
    private final static PrintWriter out = createOut();

    private static PrintWriter createOut() {
        final File dir = new File(System.getProperty("user.home"), "timey");
        dir.mkdirs();
        try {
            return new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, "errors.txt"), true), "utf-8"));
        } catch (IOException e) {
            exit(4, e.getMessage());
        }
        return null;
    }

    public static void exit(int code) {
        System.exit(code);
    }

    public static void exit(int code, String message) {
        JOptionPane.showMessageDialog(null, message, "Fatal", JOptionPane.ERROR_MESSAGE);
        System.exit(code);
    }

    public static void error(Exception e) {
        e.printStackTrace();
        out.println();
        out.println("At " + LocalDateTime.now());
        e.printStackTrace(out);
        out.flush();
    }
}
