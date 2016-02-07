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

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoField.*;

/**
 *
 */
public class FilePersister implements Persister {
    private static final String NAME_PROP = "name";
    private static final String COMMENT_PROP = "comment";
    private static final String SETTINGS_FILE = "settings.properties";

    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4).appendLiteral('-')
            .appendValue(MONTH_OF_YEAR, 2).appendLiteral('-')
            .appendValue(DAY_OF_MONTH, 2).appendLiteral(' ')
            .appendValue(HOUR_OF_DAY, 2).appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2).toFormatter();

    private final File dir;
    private final Properties settings;
    private BufferedWriter out;

    public FilePersister() throws IOException {
        dir = new File(System.getProperty("user.home"), "timey");
        dir.mkdirs();
        settings = new Properties();
        try {
            settings.load(new FileInputStream(new File(dir, SETTINGS_FILE)));
        } catch (IOException e) {
            //ignore
        }
        doSetName(getName());
    }

    @Override
    public void writeInterval(LocalDateTime from, LocalDateTime to) {
        try {
            out.write(formatter.format(from) + ";" + formatter.format(to) + ";\"" + getComment() + "\";" + from.until(to, ChronoUnit.MINUTES));
            out.newLine();
            out.flush();
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    @Override
    public String getName() {
        return settings.getProperty(NAME_PROP, "default");
    }

    @Override
    public String getComment() {
        return settings.getProperty(COMMENT_PROP, "");
    }

    @Override
    public void storeSettings(String name, String comment) {
        if (!name.equals(getName())) {
            doSetName(name);
        }
        settings.setProperty(COMMENT_PROP, comment);
        try {
            settings.store(new FileOutputStream(new File(dir, SETTINGS_FILE)), null);
        } catch (IOException e) {
            Logger.error(e);
        }

    }

    private void doSetName(String name) {
        settings.setProperty(NAME_PROP, name);
        close();
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, name + ".csv"), true), "utf-8"));
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    @Override
    public void close() {
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    @Override
    public List<String> getNames() {
        return Stream.of(dir.list((d, name) -> name.endsWith(".csv")))
                .map(n -> n.substring(0, n.length() - 4))
                .collect(Collectors.toList());
    }


}
