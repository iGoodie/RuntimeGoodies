package net.programmer.igoodie.query;

import net.programmer.igoodie.exception.GoodieSyntaxException;
import net.programmer.igoodie.query.accessor.ArrayIndexAccessor;
import net.programmer.igoodie.query.accessor.GoodieQueryAccessor;
import net.programmer.igoodie.query.accessor.ObjectFieldAccessor;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoodieQueryParser {

    private static Pattern FIELD_PATTERN = Pattern.compile("\\.(?<field>\\w+)");
    private static Pattern INDEX_PATTERN = Pattern.compile("\\[(?<index>\\d+)]");

    String query;
    String[] tokens;

    public GoodieQueryParser(String query) {
        this.query = query;

        tokens = query.replaceAll("\\s+", "")
                .replaceAll("(\\.|\\[)", " $1")
                .split(" ");

        if (tokens.length == 0 || !tokens[0].equals("$")) {
            throw new GoodieSyntaxException("Goodie queries MUST start with the following character -> $");
        }
    }

    public String getQuery() {
        return query;
    }

    public String[] getTokens() {
        return tokens;
    }

    public GoodieQuery parse() {
        GoodieQuery goodieQuery = new GoodieQuery();
        List<GoodieQueryAccessor> accessors = new LinkedList<>();

        for (String token : tokens) {
            if (token.contains("$")) {
                if (accessors.size() != 0) {
                    throw new GoodieSyntaxException("Root statement ($) MUST only be at the beginning of the query.");
                }
                continue;
            }

            Matcher fieldMatcher = FIELD_PATTERN.matcher(token);
            if (fieldMatcher.matches()) {
                String fieldName = fieldMatcher.group("field");
                accessors.add(new ObjectFieldAccessor(fieldName));
                continue;
            }

            Matcher indexMatcher = INDEX_PATTERN.matcher(token);
            if (indexMatcher.matches()) {
                int index = Integer.parseInt(indexMatcher.group("index"));
                accessors.add(new ArrayIndexAccessor(index));
                continue;
            }

            throw new GoodieSyntaxException("Unknown query statement -> " + token);
        }

        goodieQuery.accessors = accessors;
        return goodieQuery;
    }

}
