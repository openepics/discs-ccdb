/*
 * Copyright (c) 2014 European Spallation Source
 * Copyright (c) 2014 Cosylab d.d.
 *
 * This file is part of Controls Configuration Database.
 *
 * Controls Configuration Database is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the License,
 * or any newer version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see https://www.gnu.org/licenses/gpl-2.0.txt
 */
package org.openepics.discs.conf.valueconverters;

import java.util.List;

import org.openepics.discs.conf.ent.values.DblTableValue;
import org.openepics.discs.conf.ent.values.Value;
import org.openepics.seds.api.datatypes.SedsScalarArray;
import org.openepics.seds.api.datatypes.SedsTable;

/**
 * @author Miha Vitorovič <miha.vitorovic@cosylab.com>
 *
 */
public class DblTableValueConverter extends ValueConverter {

    @Override
    public Class<? extends Value> getType() { return DblTableValue.class; }

    @Override
    public String convertToDatabaseColumn(Value attribute) {
        final DblTableValue dblTableValue = (DblTableValue) attribute;

        final SedsScalarArray<?>[] columns = new SedsScalarArray<?>[dblTableValue.getDblTableValue().size()];
        int colIndex = 0;
        for (List<Double> column : dblTableValue.getDblTableValue()) {
            columns[colIndex] = sedsFactory.newScalarArray(column.toArray());
        }
        final SedsTable sedsTable = sedsFactory.newTable(null, columns);
        return sedsDbConverter.serialize(sedsTable).toString();
    }

}
