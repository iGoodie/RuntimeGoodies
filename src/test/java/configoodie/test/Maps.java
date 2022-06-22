package configoodie.test;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Maps extends JsonConfiGoodie {

    enum StateEnum {
        PRISTINE,
        LOADING,
        DONE,
        ERROR
    }

    public static class InvalidMaps extends JsonConfiGoodie {
        @Goodie
        Map<String, Map<Object, Integer>> invalid;
    }

    @Goodie
    Map<String, UUID> uuids;

    @Goodie
    Map<String, Integer> numbers;

    @Goodie
    Map<StateEnum, UUID> enumKeys;

    @Goodie
    Map<UUID, Random> stringifiables;

    @Goodie
    Map<UUID, List<Integer>> intBucket;

    @Goodie
    Map<Random, Map<String, Integer>> complex;

    @Goodie
    Map<Random, Map<String, List<Integer>>> complex2;

    @Test
    public void testMaps() {
        TestUtils.standardConfiGoodieTest(new Maps(), "{" +
                "'numbers':{'A':1, 'B':2}," +
                "'enumKeys': {'PRISTINE':'123e4567-e89b-12d3-a456-426614174000'}," +
                "'intBucket': {'123e4567-e89b-12d3-a456-426614174000': [1, 2, 3]}," +
                "'complex': {0: {'A':null, 'B':2}, 1: {'A':1, 'B':2}}," +
                "'complex2': {0: {'A':[1], 'B':[2,2]}, 1: {'A':[3,3,3], 'B':[4,4,4,4]}}" +
                "}");
    }

    @Test
    public void testInvalidMaps() {
        Assertions.assertThrows(GoodieImplementationException.class,
                () -> TestUtils.standardConfiGoodieTest(new InvalidMaps(), "{}"));
    }

}
