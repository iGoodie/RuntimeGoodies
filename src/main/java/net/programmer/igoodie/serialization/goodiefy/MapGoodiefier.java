package net.programmer.igoodie.serialization.goodiefy;

import net.programmer.igoodie.RuntimeGoodies;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.serialization.stringify.DataStringifier;
import net.programmer.igoodie.util.GoodieUtils;
import net.programmer.igoodie.util.TypeUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class MapGoodiefier extends DataGoodiefier<GoodieObject> {

    @Override
    public boolean canGenerateForFieldType(Type fieldType) {
        if (TypeUtilities.getBaseClass(fieldType) == Map.class) {
            Type[] typeArguments = ((ParameterizedType) fieldType).getActualTypeArguments();
            Type keyType = typeArguments[0];
            Type valueType = typeArguments[1];

            DataStringifier<?> keyStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(TypeUtilities.getBaseClass(keyType));
            if (keyType != String.class && keyStringifier == null) {
                throw new GoodieImplementationException("Key type of Maps MUST be either String or a stringifiable type (e.g UUID)", fieldType);
            }

            try {
                GoodieUtils.findDataGoodifier(valueType);
            } catch (GoodieImplementationException e) {
                throw new GoodieImplementationException(e.getCauseMessage(), e, fieldType);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean canAssignValueToType(Type targetType, Object value) {
        return false;
    }

    @Override
    public boolean canGenerateTypeFromGoodie(Type targetType, GoodieElement goodieElement) {
        return false;
    }

    @Override
    public GoodieObject auxGoodieElement(GoodieElement goodieElement) {
        return null;
    }

    @Override
    public @NotNull Object generateFromGoodie(Type targetType, GoodieObject goodie) {
        return null;
    }

    @Override
    public @NotNull GoodieObject generateDefaultGoodie(Type targetType) {
        return null;
    }

    @Override
    public @NotNull GoodieObject serializeValueToGoodie(Object value) {
        return null;
    }

}
