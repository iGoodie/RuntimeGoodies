package net.programmer.igoodie.configuration.validation;

import net.programmer.igoodie.RuntimeGoodies;
import net.programmer.igoodie.configuration.validation.annotation.GoodieNullable;
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

public class GoodieValidator {

    private boolean changesMade;

    public void validateAndFix(Object validateObject, GoodieObject goodieObject) {
        GoodieTraverser goodieTraverser = new GoodieTraverser();

        Set<String> pathsTraversed = new HashSet<>();

        goodieTraverser.traverseGoodieFields(validateObject, (object, field, goodiePath) -> {
            if (pathsTraversed.contains(goodiePath)) {
                throw new GoodieImplementationException("Goodies MUST not have field paths mapped more than once -> " + goodiePath);
            }

            fixByNonNullability(field, goodieObject, goodiePath);
            fixByDataStringifier(field, goodieObject, goodiePath);
            fixByValidators(object, field, goodieObject, goodiePath);
            fixMissingValue(object, field, goodieObject, goodiePath);

            // Remove default values from non-primitive fields once validation is done
            if (!TypeUtilities.isPrimitive(field)) {
                ReflectionUtilities.setValue(object, field, null);
            }

            pathsTraversed.add(goodiePath);
        });
    }

    public void fixByNonNullability(Field field, GoodieObject goodieObject, String goodiePath) {
        GoodieElement query = GoodieQuery.query(goodieObject, goodiePath);
        if (query != null && !isNullable(field) && query.isNull()) {
            GoodieQuery.delete(goodieObject, goodiePath);
        }
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
                        GoodiePrimitive defaultGoodieValue = GoodiePrimitive.from(dataStringifier.defaultStringValue());
                        GoodieQuery.set(goodieObject, goodiePath, defaultGoodieValue);
                        changesMade = true;
                    }
                }

            } else {
                GoodiePrimitive defaultGoodieValue = GoodiePrimitive.from(dataStringifier.defaultStringValue());
                GoodieQuery.set(goodieObject, goodiePath, defaultGoodieValue);
                changesMade = true;
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
        Object declaredDefault = ReflectionUtilities.getValue(object, field);
        if (declaredDefault == null && !isNullable(field)) {
            if (RuntimeGoodies.DATA_STRINGIFIERS.get(field.getType()) == null
                    && field.getType() != Object.class
                    && !TypeUtilities.isGoodie(field)
                    && !TypeUtilities.isPrimitive(field)
                    && !TypeUtilities.isList(field)
                    && !TypeUtilities.isMap(field)) {
                throw new GoodieImplementationException("Non-nullable Goodie fields MUST have a default value declared.", field);
            }
        }

        GoodieElement query = GoodieQuery.query(goodieObject, goodiePath);

        if (query == null) {
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
                    throw new InternalError("How the hack does a primitive not have a default value?");
                }

            } else if (TypeUtilities.isList(field)) {
                GoodieQuery.set(goodieObject, goodiePath, new GoodieArray());

            } else if (TypeUtilities.isMap(field)) {
                GoodieQuery.set(goodieObject, goodiePath, new GoodieObject());

            } else if (field.getType() == GoodieObject.class) {
                GoodieQuery.set(goodieObject, goodiePath, new GoodieObject());

            } else if (field.getType() == GoodieArray.class) {
                GoodieQuery.set(goodieObject, goodiePath, new GoodieArray());

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

    private boolean isNullable(Field field) {
        GoodieNullable annotation = field.getAnnotation(GoodieNullable.class);
        return annotation != null;
    }

}
