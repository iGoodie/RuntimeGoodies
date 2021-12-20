package example;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieCustomType;
import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieDouble;
import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieInteger;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.lang.reflect.Field;

public class TSPreferencesConfig extends JsonConfiGoodie {

    @Goodie
    MessageDisplay messageDisplay = MessageDisplay.TITLES;

    @Goodie
    IndicatorDisplay indicatorDisplay = IndicatorDisplay.ENABLED;

    @Goodie
    Enability autoStart = Enability.DISABLED;

    @Goodie
    @GoodieDouble(min = 0.0, max = 1.0)
    double notificationVolume;

    @Goodie
    @GoodieCustomType(NotificationPitchValidator.class)
    Double notificationPitch;

    public static class NotificationPitchValidator extends GoodieCustomType.Validator<Double> {
        @Override
        public boolean isValidGoodie(GoodieElement goodie) {
            return goodie.isPrimitive()
                    && goodie.asPrimitive().isNumber();
        }

        @Override
        public boolean isValidValue(GoodieElement goodie) {
            double value = goodie.asPrimitive().getNumber().doubleValue();
            return (value == -1.0) || (0.0 <= value && value <= 1.0);
        }

        @Override
        public GoodieElement fixedGoodie(Object object, Field field, GoodieElement goodie) {
            return GoodieElement.from(1.0D);
        }
    }

    @Goodie
    @GoodieInteger(min = 0)
    int notificationDelay;

    @Goodie
    @GoodieInteger(min = 0)
    int chatGlobalCooldown, chatIndividualCooldown;

    @Goodie
    Enability chatWarnings = Enability.DISABLED;

    public enum IndicatorDisplay {
        DISABLED, CIRCLE_ONLY, ENABLED
    }

    public enum MessageDisplay {
        DISABLED, TITLES, CHAT
    }

    public enum Enability {
        DISABLED, ENABLED
    }

    @Test
    public void test() {
        TestUtils.standardConfiGoodieTest(new TSPreferencesConfig(), "{}");
    }

}
