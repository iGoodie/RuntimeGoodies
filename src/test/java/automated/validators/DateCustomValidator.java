package automated.validators;

import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieCustomType;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.goodies.serialization.stringify.DateStringifier;

import java.lang.reflect.Field;
import java.util.Date;

public class DateCustomValidator extends GoodieCustomType.Validator<Date> {

    @Override
    public boolean isValidGoodie(GoodieElement goodie) {
        return goodie != null
                && goodie.isPrimitive()
                && goodie.asPrimitive().isString();
    }

    @Override
    public boolean isValidValue(GoodieElement goodie) {
        String value = goodie.asPrimitive().getString();
        try {
            new DateStringifier().objectify(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public GoodieElement fixedGoodie(Object object, Field field, GoodieElement goodie) {
        String value = new DateStringifier().stringify(new Date());
        return GoodiePrimitive.from(value);
    }

}
