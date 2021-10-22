package net.programmer.igoodie.configuration.validation;

import net.programmer.igoodie.RuntimeGoodies;
import net.programmer.igoodie.configuration.validation.logic.ValidatorLogic;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.*;
import net.programmer.igoodie.query.GoodieQuery;
import net.programmer.igoodie.serialization.stringify.DataStringifier;
import net.programmer.igoodie.util.Couple;
import net.programmer.igoodie.util.GoodieTraverser;
import net.programmer.igoodie.util.ReflectionUtilities;
import net.programmer.igoodie.util.TypeUtilities;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

// TODO: Required & Optional annotations (?)
public class GoodieValidator {

    private boolean changesMade;

    public void validateAndFix(Object validateObject, GoodieObject goodieObject) {
        GoodieTraverser goodieTraverser = new GoodieTraverser();

        Set<String> pathsTraversed = new HashSet<>();

        goodieTraverser.traverseGoodies(validateObject, (object, field, goodiePath) -> {
            if (pathsTraversed.contains(goodiePath)) {
                throw new GoodieImplementationException("Goodie path mapped more than once -> " + goodiePath);
            }

            fixByDataStringifier(field, goodieObject, goodiePath);
            fixByValidators(object, field, goodieObject, goodiePath);
            fixMissingValue(object, field, goodieObject, goodiePath);

            pathsTraversed.add(goodiePath);
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
                        GoodieQuery.set(goodieObject, goodiePath, GoodiePrimitive.from(dataStringifier.defaultStringValue()));
                        changesMade = true;
                    }
                }
            }
        }
    }

    public void fixByValidators(Object object, Field field, GoodieObject goodieObject, String goodiePath) {
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
    }

    public void fixMissingValue(Object object, Field field, GoodieObject goodieObject, String goodiePath) {
        GoodieElement query = GoodieQuery.query(goodieObject, goodiePath);
        if (query == null) {
            Object declaredDefault = ReflectionUtilities.getValue(object, field);

            if (declaredDefault != null) {
                GoodieQuery.set(goodieObject, goodiePath, GoodieElement.from(declaredDefault));
                changesMade = true;
                return;
            }

            if (TypeUtilities.isPrimitive(field)) {
                Object defaultValue = TypeUtilities.defaultValue(field.getType());
                if (defaultValue != null) {
                    GoodieQuery.set(goodieObject, goodiePath, GoodiePrimitive.from(defaultValue));
                } else {
                    GoodieQuery.set(goodieObject, goodiePath, new GoodieNull());
                }
            } else if (TypeUtilities.isList(field)) {
                GoodieQuery.set(goodieObject, goodiePath, new GoodieArray());
            } else if (TypeUtilities.isMap(field)) {
                GoodieQuery.set(goodieObject, goodiePath, new GoodieObject());
            } else {
                GoodieQuery.set(goodieObject, goodiePath, new GoodieNull());
            }
            changesMade = true;
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
