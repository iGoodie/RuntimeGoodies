package net.programmer.igoodie.configuration.validation;

import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieNull;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.query.GoodieQuery;
import net.programmer.igoodie.serialization.goodiefy.FieldGoodiefier;
import net.programmer.igoodie.util.GoodieTraverser;
import net.programmer.igoodie.util.GoodieUtils;
import net.programmer.igoodie.util.ReflectionUtilities;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class GoodieValidator {

    private boolean changesMade = false;
    private final Set<String> fixedPaths = new HashSet<>();

    public boolean changesMade() {
        return changesMade;
    }

    public Set<String> getFixedPaths() {
        return fixedPaths;
    }

    public void validateAndFix(Object root, GoodieObject goodieToFix) {
        GoodieTraverser goodieTraverser = new GoodieTraverser();
        GoodieUtils.disallowCircularDependency(root);

        checkConflictingKeys(root);

        goodieTraverser.traverseGoodieFields(root, true, (object, field, goodiePath) -> {
            GoodieUtils.disallowArrayGoodieFields(field);

            FieldGoodiefier<?> fieldGoodiefier = GoodieUtils.findFieldGoodifier(field);

            checkDeclaredDefault(fieldGoodiefier, object, field);

            GoodieElement goodie = GoodieQuery.query(goodieToFix, goodiePath);

            // Missing value - Initialize with null value
            if (goodie == null) {
                goodie = GoodieQuery.set(goodieToFix, goodiePath, new GoodieNull());
                markChanged(goodiePath);
            }

            // Nullability violation - Replace with default goodie
            if (goodie.isNull() && !GoodieUtils.isFieldNullable(field)) {
                goodie = fixWithDefaultValue(fieldGoodiefier, object, field, goodieToFix, goodiePath);
            }

            // Goodie type mismatch - Replace with default goodie
            if (!goodie.isNull() && !fieldGoodiefier.canGenerateFromGoodie(field, goodie)) {
                goodie = fixWithDefaultValue(fieldGoodiefier, object, field, goodieToFix, goodiePath);
            }

            // TODO: Iterate through GoodieValidators

            if (!goodie.isNull() && fixWithFieldGoodiefier(field, fieldGoodiefier, goodie)) {
                markChanged(goodiePath);
            }
        });
    }

    private void checkConflictingKeys(Object root) {
        Set<String> pathsTraversed = new HashSet<>();
        new GoodieTraverser().traverseGoodieFields(root, true, ((object, field, goodiePath) -> {
            if (pathsTraversed.contains(goodiePath)) {
                throw new GoodieImplementationException("Goodies MUST not have field paths mapped more than once -> " + goodiePath);
            }
            pathsTraversed.add(goodiePath);
        }));
    }

    private void checkDeclaredDefault(FieldGoodiefier<?> fieldGoodiefier, Object object, Field field) {
        Object declaredDefaultValue = ReflectionUtilities.getValue(object, field);

        if (declaredDefaultValue != null) {
            if (!fieldGoodiefier.canAssignValueToField(field, declaredDefaultValue)) {
                throw new GoodieImplementationException("Declared default value CANNOT be assigned to the field.", field);
            }

            // TODO: Iterate through GoodieValidators
        }
    }

    /* ---------------------------- */

    private void markChanged(String goodiePath) {
        fixedPaths.add(goodiePath);
        changesMade = true;
    }

    private <G extends GoodieElement> boolean fixWithFieldGoodiefier(Field field, FieldGoodiefier<G> fieldGoodiefier, GoodieElement goodieElement) {
        G goodie = fieldGoodiefier.auxGoodieElement(goodieElement);
        G fixedGoodie = fieldGoodiefier.fixGoodie(field, goodie);
        return goodie != fixedGoodie;
    }

    private GoodieElement fixWithDefaultValue(FieldGoodiefier<?> fieldGoodiefier, Object object, Field field, GoodieObject goodieToFix, String goodiePath) {
        Object declaredDefaultValue = ReflectionUtilities.getValue(object, field);

        GoodieElement defaultGoodie = declaredDefaultValue != null
                ? GoodieElement.from(declaredDefaultValue)
                : fieldGoodiefier.generateDefaultGoodie(field);

        GoodieElement fixedGoodie = GoodieQuery.set(goodieToFix, goodiePath, defaultGoodie);
        markChanged(goodiePath);
        return fixedGoodie;
    }

}
