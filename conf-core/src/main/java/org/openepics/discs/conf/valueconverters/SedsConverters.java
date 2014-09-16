package org.openepics.discs.conf.valueconverters;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.openepics.discs.conf.ent.values.Value;
import org.openepics.discs.conf.util.PropertyDataType;

/**
 * This class initializes a mapping between various Value classes and the implementation of the converter. The class is a
 * singleton so that the converters are converter mapping is initialized at application startup and only once.
 *
 * @author Miha Vitorovič <miha.vitorovic@cosylab.com>
 *
 */
@Singleton
@Startup
public class SedsConverters {

    private static final Logger logger = Logger.getLogger(SedsConverter.class.getCanonicalName());

    private static final Map<Class<? extends Value>, ValueConverter<? extends Value>> converters = new ConcurrentHashMap<>();

    public SedsConverters() {}

    /**
     * Constructs the item. Expects injected iterator of all EntityLogger implementations
     *
     * @param allLoggers CDI will inject all logger types in this constructor parameter
     */
    @Inject
    public SedsConverters(@Any Instance<ValueConverter<? extends Value>> allConverters) {
        int convertersFound = 0;
        for (ValueConverter<? extends Value> converter : allConverters) {
            converters.put(converter.getType(), converter);
            convertersFound++;
        }

        logger.log(Level.INFO, "Loaded " + convertersFound + " data type converters.");
        if (convertersFound != PropertyDataType.values().length) {
            logger.log(Level.SEVERE, "Converter data type implementation number mismatch. Expected: " + PropertyDataType.values().length
                    + ", found: " + convertersFound);
        }
    }

    public static <T extends Value> String convertToDatabaseColumn(T attribute) {
        @SuppressWarnings("unchecked")
        ValueConverter<T> converter = (ValueConverter<T>) converters.get(attribute.getClass());
        if (converter == null) {
            throw new IllegalStateException("Could not find converter for " + attribute.getClass().getName());
        }

        return converter.convertToDatabaseColumn(attribute);
    }
}
