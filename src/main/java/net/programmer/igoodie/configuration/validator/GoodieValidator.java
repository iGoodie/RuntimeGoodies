package net.programmer.igoodie.configuration.validator;

import net.programmer.igoodie.RuntimeGoodies;
import net.programmer.igoodie.configuration.validator.logic.ValidatorLogic;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.query.GoodieQuery;
import net.programmer.igoodie.util.Couple;
import net.programmer.igoodie.util.GoodieTraverser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GoodieValidator {

    public boolean validateAndFix(Object validateObject, GoodieObject goodieObject) {
        GoodieTraverser goodieTraverser = new GoodieTraverser();

        AtomicBoolean changesMade = new AtomicBoolean(false);

        goodieTraverser.traverseGoodies(validateObject, (object, field, goodiePath) -> {
            for (Couple<Annotation, ValidatorLogic<Annotation>> couple : getValidators(field)) {
                Annotation annotation = couple.getFirst();
                ValidatorLogic<Annotation> validator = couple.getSecond();

                if (!validator.isValidField(object, field)) {
                    String annotationName = annotation.annotationType().getSimpleName();
                    throw new GoodieImplementationException("Invalid field type for " + annotationName + " validator.", field);
                }

                GoodieElement goodieElement = GoodieQuery.query(goodieObject, goodiePath);

                if (!validator.isValidGoodie(goodieElement) || !validator.isValidValue(annotation, goodieElement)) {
                    Object defaultValue = validator.defaultValue(annotation, object, field, goodieElement);
                    System.out.println(goodiePath + " was invalid. Gonna be replaced by value => " + defaultValue);
                    changesMade.set(true);
                }
            }
        });

        return changesMade.get();
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
