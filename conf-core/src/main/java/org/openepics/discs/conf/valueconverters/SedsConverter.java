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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonReader;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.epics.util.time.Timestamp;
import org.openepics.discs.conf.ent.values.DblTableValue;
import org.openepics.discs.conf.ent.values.DblValue;
import org.openepics.discs.conf.ent.values.DblVectorValue;
import org.openepics.discs.conf.ent.values.EnumValue;
import org.openepics.discs.conf.ent.values.IntValue;
import org.openepics.discs.conf.ent.values.IntVectorValue;
import org.openepics.discs.conf.ent.values.StrValue;
import org.openepics.discs.conf.ent.values.StrVectorValue;
import org.openepics.discs.conf.ent.values.TimestampValue;
import org.openepics.discs.conf.ent.values.UrlValue;
import org.openepics.discs.conf.ent.values.Value;
import org.openepics.discs.conf.util.Conversion;
import org.openepics.seds.api.datatypes.SedsEnum;
import org.openepics.seds.api.datatypes.SedsScalar;
import org.openepics.seds.api.datatypes.SedsScalarArray;
import org.openepics.seds.api.datatypes.SedsTable;
import org.openepics.seds.api.datatypes.SedsTime;
import org.openepics.seds.api.datatypes.SedsType;
import org.openepics.seds.core.Seds;
import org.openepics.seds.util.ScalarType;

/**
 * A JPA converter to convert between the Value object and the data serialized in the database.
 *
 * @author Miha Vitorovič <miha.vitorovic@cosylab.com>
 *
 */
@Converter(autoApply=true)
public class SedsConverter implements AttributeConverter<Value, String> {

    public SedsConverter() {}

    @Override
    public String convertToDatabaseColumn(Value attribute) {
        if (attribute == null) {
            return null;
        }

        return SedsConverters.convertToDatabaseColumn(attribute);
    }

    @Override
    public Value convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        if (!dbData.startsWith("{")) {
            return new UrlValue(Conversion.toURL(dbData));
        }

        JsonReader reader = Json.createReader(new StringReader(dbData));
        final SedsType seds = Seds.newDBConverter().deserialize(reader.readObject());

        // simple scalars
        if (seds instanceof SedsScalar<?>) {
            return convertFromSedsScalar((SedsScalar<?>) seds, dbData);
        }

        // timestamp
        if (seds instanceof SedsTime) {
            return convertFromSedsTime((SedsTime)seds);
        }

        // Vectors (1-D)
        if (seds instanceof SedsScalarArray<?>) {
            return convertFromSedsScalarArray((SedsScalarArray<?>)seds, dbData);
        }

        // table
        if (seds instanceof SedsTable) {
            return convertFromSedsTable((SedsTable) seds);
        }

        // enum
        if (seds instanceof SedsEnum) {
            return convertFromSedsEnum((SedsEnum) seds);
        }

        throw new IllegalArgumentException("Unable to convert DB data: " + dbData);
    }

    private Value convertFromSedsScalar(SedsScalar<?> sedsScalar, String dbData) {
        final IntValue intValue;

        switch (sedsScalar.getType()) {
        case INTEGER:
            intValue = new IntValue((Integer)sedsScalar.getValue());
            return intValue;
        case NUMBER:
            final Number number = (Number) sedsScalar.getValue();
            if (number instanceof Double) {
                final DblValue dblValue = new DblValue((Double)number);
                return dblValue;
            } else if (number instanceof Integer) {
                intValue = new IntValue((Integer)number);
                return intValue;
            } else {
                throw new IllegalArgumentException("Unable to convert DB data: " + dbData);
            }
        case STRING:
            final StrValue strValue = new StrValue((String)sedsScalar.getValue());
            return strValue;
        default:
            throw new InvalidDataTypeException("Data type not supported: " + sedsScalar.getType().name());
        }
    }

    private Value convertFromSedsTime(SedsTime sedsTime) {
        final Date javaTimestamp = Timestamp.of(sedsTime.getUnixSec(), sedsTime.getNanoSec()).toDate();
        final TimestampValue timestampValue = new TimestampValue(javaTimestamp);
        return timestampValue;
    }

    private Value convertFromSedsScalarArray(SedsScalarArray<?> sedsScalarArray, String dbData) {
        final IntVectorValue intVectorValue;
        final List<Integer> iValues;

        switch (sedsScalarArray.getType()) {
        case INTEGER:
            iValues = new ArrayList<Integer>(Arrays.asList((Integer[])sedsScalarArray.getValueArray()));
            intVectorValue = new IntVectorValue(iValues);
            return intVectorValue;
        case NUMBER:
            final Number[] numbers = (Number[]) sedsScalarArray.getValueArray();
            if (numbers instanceof Double[]) {
                final List<Double> dValues = new ArrayList<Double>(Arrays.asList((Double[])sedsScalarArray.getValueArray()));
                final DblVectorValue dblVecotrValue = new DblVectorValue(dValues);
                return dblVecotrValue;
            } else if (numbers instanceof Integer[]) {
                iValues = new ArrayList<Integer>(Arrays.asList((Integer[])sedsScalarArray.getValueArray()));
                intVectorValue = new IntVectorValue(iValues);
                return intVectorValue;
            } else {
                throw new IllegalArgumentException("Unable to convert DB data: " + dbData);
            }
        case STRING:
            final List<String> sValues = new ArrayList<String>(Arrays.asList((String[])sedsScalarArray.getValueArray()));
            final StrVectorValue strVectorValue = new StrVectorValue(sValues);
            return strVectorValue;
        default:
            throw new InvalidDataTypeException("Data type not supported: " + sedsScalarArray.getType().name());
        }
    }

    private Value convertFromSedsTable(SedsTable sedsTable) {
        List<List<Double>> tableValues = new ArrayList<List<Double>>();
        if (sedsTable.getNumColumns() > 0) {
            for (SedsScalarArray<?> col : sedsTable.getValues()) {
                if (col.getType() != ScalarType.NUMBER) {
                    throw new InvalidDataTypeException("Data type not supported for table: " + col.getType().name());
                }
                final Number[] colValues = (Number[]) col.getValueArray();
                final List<Double> dblColValues = new ArrayList<>(colValues.length);
                for (Number element : colValues) {
                    if (element instanceof Double) {
                        dblColValues.add((Double)element);
                    } else {
                        throw new InvalidDataTypeException("Data type for table not Double.");
                    }
                }
                tableValues.add(dblColValues);
            }
        }
        final DblTableValue dblTableValue = new DblTableValue(tableValues);
        return dblTableValue;
    }

    private Value convertFromSedsEnum(SedsEnum sedsEnum) {
        final EnumValue enumValue = new EnumValue(sedsEnum.getSelected());
        return enumValue;
    }

}
