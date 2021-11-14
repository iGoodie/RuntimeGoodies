package net.programmer.igoodie.serialization.goodiefy;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.util.ReflectionUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public abstract class FieldGoodiefier<G extends GoodieElement> {

    /* ==== { Integrity Validators }============= */

    public abstract boolean canGenerateForField(Field field);

    public abstract boolean canGenerateFromGoodie(Field field, GoodieElement goodieElement);

    public abstract G auxGoodieElement(GoodieElement goodieElement);

    /* ==== { Value Generators }================== */

    public abstract @NotNull Object generateFromGoodie(Field field, G goodie);

    public abstract @NotNull G generateDefaultGoodie(Field field);

    public Object generateDefaultValue(Field field) {
        G defaultGoodie = generateDefaultGoodie(field);
        return generateFromGoodie(field, defaultGoodie);
    }

    /* =========================================== */

    public abstract @NotNull G serializeValueToGoodie(Object value);

    public G serializeValueToGoodie(Object object, Field field) {
        return serializeValueToGoodie(ReflectionUtilities.getValue(object, field));
    }

    /* ==== { Fixers & Sanitizers }=============== */

    public G fixGoodie(Field field, G goodie) {
        return goodie;
    }

    public G sanitizeGoodie(G goodie) {
        return goodie; // By default, a goodie is considered sanitized.
    }

    /* ==== { Utility Methods }================== */

}
