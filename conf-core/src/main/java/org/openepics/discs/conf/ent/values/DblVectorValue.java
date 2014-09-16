/*
 * Copyright (c) 2014 European Spallation Source
 * Copyright (c) 2014 Cosylab d.d.
 *
 * This file is part of Controls Configuration Database.
 * Controls Configuration Database is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or any
 * newer version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see https://www.gnu.org/licenses/gpl-2.0.txt
 */
package org.openepics.discs.conf.ent.values;

import java.util.List;

import com.google.common.base.Preconditions;

/**
 * 1-D vector of double precision values.
 *
 * @author Miha Vitorovič <miha.vitorovic@cosylab.com>
 *
 */
public class DblVectorValue implements Value {
    private static final int MAX_ELEMENTS = 5;

    private final List<Double> dblVectorValue;

    public DblVectorValue(List<Double> dblVectorValue) {
        this.dblVectorValue = Preconditions.checkNotNull(dblVectorValue);
    }

    /**
     * @return the dblVectorValue
     */
    public List<Double> getDblVectorValue() { return dblVectorValue; }

    @Override
    public String toString() {
        final StringBuilder retStr = new StringBuilder();
        final int vectorSize = dblVectorValue.size();
        int rowIndex = 0;
        retStr.append('[');

        for (Double item : dblVectorValue) {
            retStr.append(item);
            rowIndex++;
            if (rowIndex < vectorSize) {
                retStr.append(", ");
            }
            if ((vectorSize > MAX_ELEMENTS) && (rowIndex >= MAX_ELEMENTS - 1)) {
                retStr.append("..., ").append(dblVectorValue.get(vectorSize - 1));
                break;
            }
        }
        retStr.append(']');
        return retStr.toString();
    }
}
