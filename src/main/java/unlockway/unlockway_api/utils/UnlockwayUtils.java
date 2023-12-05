package unlockway.unlockway_api.utils;

import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class UnlockwayUtils {

    private static final DecimalFormat decfor = new DecimalFormat("0.00");

    public double generateUserImc(double height, double weight) {
        double imc = weight / (height * height);
        return Double.parseDouble(decfor.format(imc).replace(",", "."));
    }
}
