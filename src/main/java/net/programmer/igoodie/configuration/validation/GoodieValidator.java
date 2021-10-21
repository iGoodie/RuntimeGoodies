package net.programmer.igoodie.configuration.validation;

import net.programmer.igoodie.RuntimeGoodies;
import net.programmer.igoodie.configuration.validation.logic.ValidatorLogic;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.query.GoodieQuery;
import net.programmer.igoodie.serialization.stringify.DataStringifier;
import net.programmer.igoodie.util.Couple;
import net.programmer.igoodie.util.GoodieTraverser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class GoodieValidator {

    private boolean changesMade;

    public void validateAndFix(Object validateObject, GoodieObject goodieObject) {
        GoodieTraverser goodieTraverser = new GoodieTraverser();

        goodieTraverser.traverseGoodies(validateObject, (object, field, goodiePath) -> {
            fixByDataStringifier(field, goodieObject, goodiePath);

            for (Couple<Annotation, ValidatorLogic<Annotation>> couple : getValidators(field)) {
                Annotation annotation = couple.getFirst();
                ValidatorLogic<Annotation> logic = couple.getSecond();

                try {
                    logic.validateField(annotation, object, field);
                    logic.validateAnnotationArgs(annotation);
                } catch (GoodieImplementationException e) {
                    throw new GoodieImplementationException(e.getMessage(), field);
                }

                GoodieElement goodieElement = GoodieQuery.query(goodieObject, goodiePath);

                if (!logic.isValidGoodie(annotation, goodieElement) || !logic.isValidValue(annotation, goodieElement)) {
                    GoodieElement fixedValue = logic.fixedGoodie(annotation, object, field, goodieElement);
                    GoodieQuery.set(goodieObject, goodiePath, fixedValue);
                    changesMade = true;
                }
            }
        });
    }

    public void fixByDataStringifier(Field field, GoodieObject goodieObject, String goodiePath) {
        DataStringifier<?> dataStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(field.getType());

        if (dataStringifier != null) {
            GoodieElement goodieElement = GoodieQuery.query(goodieObject, goodiePath);
            if (goodieElement != null && goodieElement.isPrimitive()) {
                GoodiePrimitive goodiePrimitive = goodieElement.asPrimitive();
                if (goodiePrimitive.isString()) {
                    try {
                        dataStringifier.objectify(goodiePrimitive.getString());
                    } catch (Exception e) {
                        changesMade = true;
                        GoodieQuery.set(goodieObject, goodiePath, GoodiePrimitive.from(dataStringifier.defaultStringValue()));
                    }
                }
            }
        }
    }

    public boolean changesMade() {
        return changesMade;
    }

    /* ----------------------------------- */

    @SuppressWarnings("unchecked")
    private List<Couple<Annotation, ValidatorLogic<Annotation>>> getValidators(Field field) {
        List<Couple<Annotation, ValidatorLogic<Annotation>>> annotations = new LinkedList<>();
        for (Annotation annotation : field.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            ValidatorLogic<Annotation> validatorLogic = (ValidatorLogic<Annotation>) RuntimeGoodies.VALIDATORS.get(annotationType);
            if (validatorLogic != null) {
                annotations.add(new Couple<>(annotation, validatorLogic));
            }
        }
        return annotations;
    }

}
