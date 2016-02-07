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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 *
 */
public class Timer {
    private final Persister persister;
    private LocalDateTime start;

    public Timer(Persister persister) {
        this.persister = persister;
    }

    public void toggle() {
        if (start == null) {
            start = LocalDateTime.now();
        } else {
            persister.writeInterval(start, LocalDateTime.now());
            start = null;
        }
    }

    public boolean isRunning() {
        return start != null;
    }

    public void close() {
        if (isRunning()) {
            toggle();
        }
        persister.close();
    }

    public long getRuntime() {
        return isRunning() ? start.until(LocalDateTime.now(), ChronoUnit.SECONDS) : 0;
    }
}
