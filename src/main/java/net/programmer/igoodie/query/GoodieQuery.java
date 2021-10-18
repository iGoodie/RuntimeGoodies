package net.programmer.igoodie.query;

import net.programmer.igoodie.exception.GoodieSyntaxException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.query.accessor.ArrayIndexAccessor;
import net.programmer.igoodie.query.accessor.GoodieQueryAccessor;
import net.programmer.igoodie.query.accessor.ObjectFieldAccessor;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

/*
 * $
 * $.field1
 * $.field1.field2
 * $.list[0]
 */
public class GoodieQuery {

    List<GoodieQueryAccessor> accessors = new LinkedList<>();

    public GoodieQuery(String queryString) {
        for (String token : queryString.split("\\.")) {
            if (token.equals("$")) {
                if (!accessors.isEmpty())
                    throw new GoodieSyntaxException("$ must be the very first token.");
                continue;
            }

            if (parseArrayIndexAccessor(token)) continue;

            accessors.add(new ObjectFieldAccessor(token));
        }
    }

    private boolean parseArrayIndexAccessor(String token) {
        Matcher matcher = ArrayIndexAccessor.PATTERN.matcher(token);

        if (!matcher.matches()) return false;

        String arrayName = matcher.group(1);
        int index = Integer.parseInt(matcher.group(2));
        accessors.add(new ArrayIndexAccessor(arrayName, index));
        return true;

    }

    public GoodieElement query(GoodieObject goodieObject) {
        try {
            GoodieElement accessed = goodieObject;
            for (GoodieQueryAccessor accessor : accessors) {
                accessed = accessor.access(accessed);
            }
            return accessed;

        } catch (Exception e) {
            return null;
        }
    }

    public void set(GoodieObject rootObject, GoodieElement value) {
        GoodieElement created = rootObject;

        for (GoodieQueryAccessor accessor : accessors) {
            if (isLeaf(accessor)) {
                accessor.accessOrCreate(created);
                accessor.setValue(created, value);
            } else {
                created = accessor.accessOrCreate(created);
            }
        }
    }

    private boolean isLeaf(GoodieQueryAccessor accessor) {
        return accessors.get(accessors.size() - 1) == accessor;
    }

    /* ------------------------------ */

    public static GoodieElement query(GoodieObject goodieObject, String queryString) {
        GoodieQuery goodieQuery = new GoodieQuery(queryString);
        return goodieQuery.query(goodieObject);
    }

    public static void set(GoodieObject goodieObject, String queryString, GoodieElement value) {
        GoodieQuery goodieQuery = new GoodieQuery(queryString);
        goodieQuery.set(goodieObject, value);
    }

}
