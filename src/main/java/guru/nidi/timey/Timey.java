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

import java.awt.*;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

/**
 *
 */
public class Timey {
    public static void main(String[] args) {
        try {
            checkTraySupported();
            checkNoInstanceRunning();
            final Persister persister = new FilePersister();
            final Timer timer = new Timer(persister);
            new SystemTrayUI(timer, persister);
        } catch (Exception e) {
            Logger.exit(10, "Problem starting: " + e.getMessage() + ". Too bad.");
        }
    }

    private static void checkTraySupported() {
        if (!SystemTray.isSupported()) {
            Logger.exit(1, "System tray is not supported. Too bad.");
        }
    }

    private static void checkNoInstanceRunning() throws IOException {
        try {
            new ServerSocket(35791);
        } catch (BindException e) {
           Logger.exit(2);
        }
    }


}
