package net.programmer.igoodie.serialization.goodiefy;

import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieNull;
import net.programmer.igoodie.util.GoodieUtils;
import net.programmer.igoodie.util.TypeUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListGoodiefier extends DataGoodiefier<GoodieArray> {

    @Override
    public void validateFieldDeclaration(Type fieldType) {
        if (!canGenerateForFieldType(fieldType)) return;
        Type listType = TypeUtilities.getListType(fieldType);
        DataGoodiefier<?> dataGoodifier = GoodieUtils.findDataGoodifier(listType);
        dataGoodifier.validateFieldDeclaration(listType);
    }

    @Override
    public boolean canGenerateForFieldType(Type fieldType) {
        Class<?> fieldClass = TypeUtilities.getBaseClass(fieldType);
        return TypeUtilities.isList(fieldClass);
    }

    @Override
    public boolean canAssignValueToType(Type targetType, Object value) {
        Type listType = TypeUtilities.getListType(targetType);
        DataGoodiefier<?> dataGoodifier = GoodieUtils.findDataGoodifier(listType);

        Class<?> valueClass = value.getClass();

        if (!TypeUtilities.isList(valueClass)) {
            return false;
        }

        List<?> listValue = (List<?>) value;

        for (Object element : listValue) {
            if (element != null && !dataGoodifier.canAssignValueToType(listType, element)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean canGenerateTypeFromGoodie(Type targetType, GoodieElement goodieElement) {
        return goodieElement.isArray();
    }

    @Override
    public GoodieArray auxGoodieElement(GoodieElement goodieElement) {
        return goodieElement.asArray();
    }

    @Override
    public @NotNull Object generateFromGoodie(Type targetType, GoodieArray goodie) {
        Type listType = TypeUtilities.getListType(targetType);
        DataGoodiefier<?> dataGoodifier = GoodieUtils.findDataGoodifier(listType);

        List<Object> list = new ArrayList<>();

        for (GoodieElement goodieElement : goodie) {
            Object value = goodieElement.isNull() ? null : generateFromGoodiefier(dataGoodifier, listType, goodieElement);
            list.add(value);
        }

        return list;
    }

    private @NotNull <G extends GoodieElement> Object generateFromGoodiefier(DataGoodiefier<G> goodiefier, Type type, GoodieElement goodieElement) {
        G goodie = goodiefier.auxGoodieElement(goodieElement);
        return goodiefier.generateFromGoodie(type, goodie);
    }

    @Override
    public @NotNull GoodieArray generateDefaultGoodie(Type targetType) {
        return new GoodieArray();
    }

    @Override
    public @NotNull GoodieArray serializeValueToGoodie(Object value) {
        GoodieArray goodieArray = new GoodieArray();
        List<?> listValue = (List<?>) value;

        for (Object element : listValue) {
            if (element == null) {
                goodieArray.add(GoodieNull.INSTANCE);
            } else {
                Class<?> elementClass = element.getClass();
                DataGoodiefier<?> dataGoodifier = GoodieUtils.findDataGoodifier(elementClass);
                goodieArray.add(dataGoodifier.serializeValueToGoodie(element));
            }
        }

        return goodieArray;
    }

}
