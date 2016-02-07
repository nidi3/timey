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

/**
 *
 */
public class GridBagConstraintsBuilder {
    private int gridx = -1;
    private int gridy = -1;
    private double weightx = -1;
    private double weighty = -1;
    private int ipadx = -1;
    private int ipady = -1;

    private int fill = -1;
    private Insets insets;
    private int anchor = -1;

    public static GridBagConstraintsBuilder gbc() {
        return new GridBagConstraintsBuilder();
    }

    public static GridBagConstraintsBuilder gbc(int gridx, int gridy) {
        return gbc().gridx(gridx).gridy(gridy);
    }

    public GridBagConstraintsBuilder gridx(int gridx) {
        this.gridx = gridx;
        return this;
    }

    public GridBagConstraintsBuilder gridy(int gridy) {
        this.gridy = gridy;
        return this;
    }

    public GridBagConstraintsBuilder weightx(double weightx) {
        this.weightx = weightx;
        return this;
    }

    public GridBagConstraintsBuilder weighty(double weighty) {
        this.weighty = weighty;
        return this;
    }

    public GridBagConstraintsBuilder ipadx(int padx) {
        this.ipadx = padx;
        return this;
    }

    public GridBagConstraintsBuilder ipady(int pady) {
        this.ipady = pady;
        return this;
    }

    public GridBagConstraintsBuilder fill(int fill) {
        this.fill = fill;
        return this;
    }

    public GridBagConstraintsBuilder insets(Insets insets) {
        this.insets = insets;
        return this;
    }

    public GridBagConstraintsBuilder anchor(int anchor) {
        this.anchor = anchor;
        return this;
    }

    public GridBagConstraints build() {
        final GridBagConstraints c = new GridBagConstraints();
        if (gridx >= 0) {
            c.gridx = gridx;
        }
        if (gridy >= 0) {
            c.gridy = gridy;
        }
        if (weightx >= 0) {
            c.weightx = weightx;
        }
        if (weighty >= 0) {
            c.weighty = weighty;
        }
        if (fill >= 0) {
            c.fill = fill;
        }
        if (ipadx >= 0) {
            c.ipadx = ipadx;
        }
        if (ipady >= 0) {
            c.ipady = ipady;
        }
        if (insets != null) {
            c.insets = insets;
        }
        if (anchor >= 0) {
            c.anchor = anchor;
        }
        return c;
    }
}
