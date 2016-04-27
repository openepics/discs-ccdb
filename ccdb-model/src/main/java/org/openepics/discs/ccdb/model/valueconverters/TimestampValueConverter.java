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
package org.openepics.discs.ccdb.model.valueconverters;

import org.openepics.discs.ccdb.model.values.TimestampValue;
import org.openepics.seds.api.datatypes.SedsTime;

/**
 * @author <a href="mailto:miha.vitorovic@cosylab.com">Miha Vitorovič</a>
 *
 */
public class TimestampValueConverter extends ValueConverter<TimestampValue> {

    @Override
    public Class<TimestampValue> getType() {
        return TimestampValue.class;
    }

    @Override
    public String convertToDatabaseColumn(TimestampValue attribute) {
        final SedsTime sedsTime = SEDS_FACTORY.getFactory().newTime(attribute.getTimestampValue(), null);
        return SEDS_DB_CONVERTER.serialize(sedsTime).toString();
    }
}
