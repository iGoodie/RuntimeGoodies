package net.programmer.igoodie.goodies.serialization.goodiefy;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.util.ReflectionUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public abstract class DataGoodiefier<G extends GoodieElement> {

    /* ==== { Integrity Validators } ============ */

    public void validateFieldDeclaration(Type fieldType) {}

    public abstract boolean canGenerateForFieldType(Type fieldType);

    public abstract boolean canAssignValueToType(Type targetType, Object value);

    public abstract boolean canGenerateTypeFromGoodie(Type targetType, GoodieElement goodieElement);

    public abstract G auxGoodieElement(GoodieElement goodieElement);

    /* ==== { Value Generators } ================= */

    public abstract @NotNull Object generateFromGoodie(Type targetType, G goodie);

    public abstract @NotNull G generateDefaultGoodie(Type targetType);

    public Object generateDefaultValue(Field field) {
        Class<?> fieldType = field.getType();
        G defaultGoodie = generateDefaultGoodie(fieldType);
        return generateFromGoodie(fieldType, defaultGoodie);
    }

    /* ===={ Goodie Generators } ================= */

    public abstract @NotNull G serializeValueToGoodie(Object value);

    public G serializeValueToGoodie(Object object, Field field) {
        return serializeValueToGoodie(ReflectionUtilities.getValue(object, field));
    }

}
