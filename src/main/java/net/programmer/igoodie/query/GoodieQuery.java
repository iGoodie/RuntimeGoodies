package net.programmer.igoodie.query;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.query.accessor.GoodieQueryAccessor;

import java.util.LinkedList;
import java.util.List;

/*
 * $
 * $.field1
 * $.field1.field2
 * $.list[0]
 */
public class GoodieQuery {

    List<GoodieQueryAccessor> accessors = new LinkedList<>();

    GoodieQuery() {}

    public List<GoodieQueryAccessor> getAccessors() {
        return accessors;
    }

    private boolean isLeaf(GoodieQueryAccessor accessor) {
        return accessors.get(accessors.size() - 1) == accessor;
    }

    public GoodieElement query(GoodieElement goodie) {
        try {
            GoodieElement accessed = goodie;
            for (GoodieQueryAccessor accessor : accessors) {
                accessed = accessor.access(accessed);
            }
            return accessed;

        } catch (Exception e) {
            return null;
        }
    }

    public void set(GoodieElement rootElement, GoodieElement value) {
        GoodieElement parent = null;
        GoodieQueryAccessor parentAccessor = null;
        GoodieElement current = rootElement;

        boolean debug = false;

        for (GoodieQueryAccessor accessor : accessors) {
            GoodieElement temp = current;

            if (debug) System.out.print("Executing " + accessor + " ON " + current);

            if (!accessor.canAccess(current)) {
                current = accessor.makeAccessible(parent, parentAccessor, current);
                temp = current;
            }

            if (isLeaf(accessor)) {
                if (debug) System.out.print(" (leaf)");
                accessor.setValue(current, value);
            } else {
                if (debug) System.out.print(" (stem)");
                current = accessor.accessOrCreate(parent, parentAccessor, current);
            }

            if (debug) System.out.println(" = " + current);

            parent = temp;
            parentAccessor = accessor;
        }
    }

    /* --------------------------------- */

    public static GoodieElement query(GoodieElement goodie, String queryString) {
        GoodieQuery goodieQuery = new GoodieQueryParser(queryString).parse();
        return goodieQuery.query(goodie);
    }

    public static void set(GoodieElement goodie, String queryString, GoodieElement value) {
        GoodieQuery goodieQuery = new GoodieQueryParser(queryString).parse();
        goodieQuery.set(goodie, value);
    }

}
