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
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import static guru.nidi.timey.GridBagConstraintsBuilder.gbc;
import static java.awt.GridBagConstraints.*;

/**
 *
 */
public class SystemTrayUI {
    private final Timer timer;
    private final Persister persister;
    private final JComboBox<String> names = new JComboBox<>();
    private final JTextArea comment = new JTextArea();

    public SystemTrayUI(Timer timer, Persister persister) throws AWTException {
        this.timer = timer;
        this.persister = persister;
        final SystemTray tray = SystemTray.getSystemTray();
        tray.add(createStateIcon());
        tray.add(createConfigIcon(createConfigFrame()));
    }

    private TrayIcon createStateIcon() {
        final TrayIcon stateIcon = new TrayIcon(stateImage());
        stateIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                timer.toggle();
                stateIcon.setImage(stateImage());
            }
        });
        return stateIcon;
    }

    private TrayIcon createConfigIcon(JFrame config) {
        final TrayIcon configIcon = new TrayIcon(getImage("settings.png"));
        configIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                names.setModel(new DefaultComboBoxModel<>(new Vector<>(persister.getNames())));
                names.setSelectedItem(persister.getName());
                comment.setText(persister.getComment());
                config.setVisible(true);
            }
        });
        return configIcon;
    }

    private JFrame createConfigFrame() {
        final JFrame config = new JFrame("Timey");
        config.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        config.setVisible(false);
        config.getRootPane().registerKeyboardAction(e -> config.setVisible(false), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        config.add(createDataPanel(), BorderLayout.CENTER);
        config.add(createButtonPanel(config), BorderLayout.SOUTH);
        config.add(createTopPanel(), BorderLayout.NORTH);
        config.pack();

        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        config.setLocation((screen.width - config.getWidth()) / 2, (screen.height - config.getHeight()) / 2);
        return config;
    }

    private JPanel createTopPanel() {
        final JPanel top = new JPanel();
        top.setLayout(new FlowLayout());

        final JLabel time = new JLabel();
        new Thread(() -> {
            while (true) {
                time.setText(formatRuntime(timer.getRuntime()));
                try {
                    Thread.sleep(990);
                } catch (InterruptedException e1) {
                    //ignore
                }
            }
        }).start();
        top.add(time);
        return top;
    }

    private String formatRuntime(long seconds) {
        return pad(seconds / 3600) + ":" + pad((seconds / 60) % 60) + ":" + pad(seconds % 60);
    }

    private String pad(long value) {
        if (value == 0) {
            return "00";
        }
        if (value < 10) {
            return "0" + value;
        }
        return "" + value;
    }

    private JPanel createButtonPanel(JFrame config) {
        final JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        final JButton ok = new JButton("OK");
        ok.addActionListener(e -> {
            timer.close();
            persister.storeSettings((String) names.getSelectedItem(), comment.getText());
            config.setVisible(false);
        });
        buttons.add(ok);

        final JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> config.setVisible(false));
        buttons.add(cancel);

        return buttons;
    }

    private JPanel createDataPanel() {
        final JPanel data = new JPanel();
        data.setLayout(new GridBagLayout());
        final Insets insets = new Insets(10, 10, 10, 10);

        data.add(new JLabel("Current name:"), gbc(0, 0).weightx(.1).insets(insets).anchor(LINE_END).build());
        names.setEditable(true);
        names.setPreferredSize(new Dimension(200, 20));
        data.add(names, gbc(1, 0).weightx(1).insets(insets).fill(HORIZONTAL).build());
        data.add(new JLabel("Comment:"), gbc(0, 1).insets(insets).anchor(LINE_END).build());
        comment.setPreferredSize(new Dimension(200, 200));
        data.add(comment, gbc(1, 1).insets(insets).fill(BOTH).build());
        return data;
    }

    private Image stateImage() {
        return getImage(timer.isRunning() ? "pause.png" : "start.png");
    }

    private Image getImage(String name) {
        return Toolkit.getDefaultToolkit().getImage(Timey.class.getClassLoader().getResource(name));
    }
}