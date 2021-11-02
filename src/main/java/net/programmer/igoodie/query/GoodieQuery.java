package net.programmer.igoodie.query;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.query.accessor.GoodieQueryAccessor;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

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

    public GoodieElement set(GoodieElement rootElement, GoodieElement value) {
        doForLeaf(rootElement, (accessor, leafElement) -> accessor.setValue(leafElement, value));
        return value;
    }

    public void delete(GoodieElement rootElement) {
        doForLeaf(rootElement, GoodieQueryAccessor::delete);
    }

    private void doForLeaf(GoodieElement rootElement, BiConsumer<GoodieQueryAccessor, GoodieElement> consumer) {
        GoodieElement parent = null;
        GoodieQueryAccessor parentAccessor = null;
        GoodieElement current = rootElement;

        for (GoodieQueryAccessor accessor : accessors) {
            GoodieElement temp = current;

            if (!accessor.canAccess(current)) {
                current = accessor.makeAccessible(parent, parentAccessor, current);
                temp = current;
            }

            if (isLeaf(accessor)) {
                consumer.accept(accessor, current);
            } else {
                current = accessor.accessOrCreate(parent, parentAccessor, current);
            }

            parent = temp;
            parentAccessor = accessor;
        }
    }

    /* --------------------------------- */

    public static GoodieElement query(GoodieElement goodie, String queryString) {
        GoodieQuery goodieQuery = new GoodieQueryParser(queryString).parse();
        return goodieQuery.query(goodie);
    }

    public static GoodieElement set(GoodieElement goodie, String queryString, GoodieElement value) {
        GoodieQuery goodieQuery = new GoodieQueryParser(queryString).parse();
        return goodieQuery.set(goodie, value);
    }

    public static void delete(GoodieElement goodie, String queryString) {
        GoodieQuery goodieQuery = new GoodieQueryParser(queryString).parse();
        goodieQuery.delete(goodie);
    }

}
