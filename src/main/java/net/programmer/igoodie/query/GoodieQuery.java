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

    /* ------------------------------ */

    public static GoodieElement query(GoodieObject goodieObject, String queryString) {
        GoodieQuery goodieQuery = new GoodieQuery(queryString);
        return goodieQuery.query(goodieObject);
    }

}
