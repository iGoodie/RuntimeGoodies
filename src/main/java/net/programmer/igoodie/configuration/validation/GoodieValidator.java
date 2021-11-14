package net.programmer.igoodie.configuration.validation;

import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieNull;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.query.GoodieQuery;
import net.programmer.igoodie.serialization.goodiefy.FieldGoodiefier;
import net.programmer.igoodie.util.GoodieTraverser;
import net.programmer.igoodie.util.GoodieUtils;
import net.programmer.igoodie.util.TypeUtilities;

import java.lang.reflect.Field;
import java.util.*;

public class GoodieValidator {

    private boolean changesMade = false;

    public boolean changesMade() {
        return changesMade;
    }

    public void validateAndFix(Object root, GoodieObject goodieToFix) {
        GoodieTraverser goodieTraverser = new GoodieTraverser();

        checkConflictingKeys(root);

        // TODO: Test circular field type dept

        goodieTraverser.traverseGoodieFields(root, true, (object, field, goodiePath) -> {
            if (TypeUtilities.isArray(field)) { // Disallow usage of Arrays over Lists
                throw new GoodieImplementationException("Goodie fields MUST not be an array fieldType. Use List<?> fieldType instead.", field);
            }

            // TODO: Validate field's declared default value

            FieldGoodiefier<?> fieldGoodiefier = GoodieUtils.findFieldGoodifier(field);
            GoodieElement goodie = GoodieQuery.query(goodieToFix, goodiePath);

            // Missing value - Initialize with null value
            if (goodie == null) {
                goodie = GoodieQuery.set(goodieToFix, goodiePath, new GoodieNull());
                changesMade = true;
            }

            // Nullability violation - Replace with default goodie
            if (goodie.isNull() && !GoodieUtils.isFieldNullable(field)) {
                // TODO: Either used declared default, or generate fresh
                GoodieElement defaultGoodie = fieldGoodiefier.generateDefaultGoodie(field);
                goodie = GoodieQuery.set(goodieToFix, goodiePath, defaultGoodie);
                changesMade = true;
            }

            // Goodie type mismatch - Replace with default goodie
            if (!fieldGoodiefier.canGenerateFromGoodie(field, goodie)) {
                // TODO: Either used declared default, or generate fresh
                GoodieElement defaultGoodie = fieldGoodiefier.generateDefaultGoodie(field);
                goodie = GoodieQuery.set(goodieToFix, goodiePath, defaultGoodie);
                changesMade = true;
            }

            // TODO: Iterate through GoodieValidators

            fixWithFieldGoodiefier(field, fieldGoodiefier, goodie);
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

    /* ---------------------------- */

    private <G extends GoodieElement> void fixWithFieldGoodiefier(Field field, FieldGoodiefier<G> fieldGoodiefier, GoodieElement goodieElement) {
        G goodie = fieldGoodiefier.auxGoodieElement(goodieElement);
        G fixedGoodie = fieldGoodiefier.fixGoodie(field, goodie);

        if (goodie != fixedGoodie) {
            changesMade = true;
        }
    }

}
