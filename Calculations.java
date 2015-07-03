package com.example.calculations;  // Rename for your package name

/**
 * Created by Jon on 7/16/2014.
 */
public class Calculations {

    private static String TAG = "Calculations Class";

    private Float percent_ByVolume;

    /*
     *  Value manipulation / formatting
     */

    /**
     * Removes trailing zero from a String representation of a float value
     * if the values after the decimal is a zero.  Will result in accuracy
     * loss in the value if second decimal value is not zero (e.g. "1.02"
     * will be converted to "1")
     * @param value  String representation of a float value
     * @return       String representation of float value without decimal
     */
    public String removeTrailingZero(String value) {
        int decimal = value.indexOf('.');

        if (value.charAt(decimal+1) == '0') {
            value = value.substring(0,decimal);
        }
        return value;
    }

    /*
     *   Unit Conversions
     */

    /**
     * Converts temperature in Fahrenheit to Celsius
     * @param tempF  temperature in Celsius
     * @return       temperature in Celsius
     */
    public float convertFtoC(float tempF) {
        Float tempC;

        tempC = 5f / 9f * (tempF - 32f);
        return tempC;
    }

    /**
     * Converts temperature in Celsius to Fahrenheit
     * @param tempC  temperature in Celsius
     * @return       temperature in Celsius
     */
    public float convertCtoF(float tempC) {
        Float tempF;

        tempF = (tempC * 9f / 5f) + 32f;
        return tempF;
    }

    /**
     * Converts specific gravity (SG) value to degrees Plato (P)
     * @param sg  value in specific gravity
     * @return    value in degrees Plato
     */
    public float convertSGtoPlato(Float sg) {
        // Old conversion from my spreadsheet
//        Float plato = 224.38f * (sg * sg * sg) - 921.36f * (sg * sg) + 1430.8f * sg - 733.89f;

        // From: http://hbd.org/ensmingr/
        Float plato = (-463.37f) + (668.72f * sg) - (205.35f * (sg * sg));
        return plato;
    }

    /**
     * Converts degrees Plato (P) value to specific gravity (SG)
     * @param plato  value in Plato
     * @return       value in specific gravity
     */
    public float convertPlatoToSG(Float plato) {

        // From my spreadsheet
        Float sg = (0.00002f * (plato * plato)) + (0.0037f * plato) + 1.0007f;
        return sg;
    }

    /**
     * Converts US gallons (gal) to liters (L)
     * @param gal  value in gal
     * @return     value in L
     */
    public float convertGalToLiter(float gal) {
        float liter = 3.78541178f * gal;
        return liter;
    }

    /**
     * Converts liters to US gallons (gal)
     * @param liter  value in L
     * @return       value in gal
     */
    public float convertLiterToGal(float liter) {
        float gal = liter / 3.78541178f;
        return gal;
    }

    /**
     * Converts US ounces (oz, weight) to grams (g)
     * @param ounce  value in oz
     * @return       value in g
     */
    public float convertOuncesToGrams(float ounce) {
        float gram = ounce * 28.3495f;
        return gram;
    }

    /**
     * Converts grams (g) to US ounces (oz, weight)
     * @param gram  value in g
     * @return      value in oz
     */
    public float convertGramsToOunces(float gram) {
        float ounce = 0.0352739619f * gram;
        return ounce;
    }


    /*
     *  Calculators
     */

    /**
     * Calculates the percent alcohol by volume (%ABV) of beer
     * @param OG_SG  original gravity of beer in SG units
     * @param FG_SG  final gravity of beer in SG units
     * @return       %ABV as a percentage (%)
     */
    public float calcPercentAlcoholByVol(Float OG_SG, Float FG_SG) {
        Float deltaSG;

        deltaSG = OG_SG - FG_SG;

        percent_ByVolume = (1.05f / 0.79f) * (deltaSG / FG_SG) * 100f;

        return percent_ByVolume;

    }

    /**
     * Calculates the percent alcohol by weight (%ABW) of beer
     * @param OG_SG  original gravity of beer in SG units
     * @param FG_SG  final gravity of beer in SG units
     * @return       %ABW as a percentage (%)
     */
    public float calcPercentAlcoholByWt(Float OG_SG, Float FG_SG) {
        Float percent_ByWeight;

        if (percent_ByVolume == null) {
            percent_ByVolume = calcPercentAlcoholByVol(OG_SG, FG_SG);
        }
        percent_ByWeight = percent_ByVolume / 1.267f;

        return percent_ByWeight;
    }

    /**
     * Real Extract (RE) calculation of beer.  RE represents the conversion
     * of sugars to ethanol and carbon dioxide during the fermentation process.
     * @param OG_P  original gravity of beer in degrees Plato
     * @param FG_P  final gravity of beer in degrees Plato
     * @return      RE of beer in degrees Plato
     */
    public Float calcRealExtract(Float OG_P, Float FG_P) {
        Float realExtract_P;

        realExtract_P = 0.188f * OG_P + 0.8192f * FG_P;

        return realExtract_P;

    }

    /**
     * Percent Apparent Attenuation (AA) calculation. AA is a representation
     * of the amount of fermentation that occurred as observed by direct hydrometer
     * readings of the beer before and after fermentation.  AA is a characteristic
     * of the yeast strain and is often reported by the yeast manufacturer as either
     * a percentage or in terms of high, medium, and low, where high indicates a
     * higher percentage (thus more fermentation and less residual sugar in the
     * final beer product) and low indicates a lower percentage (thus less fermentation
     * and more residual sugar in the final beer resulting in a fuller body).
     * @param OG_P  original gravity of beer in degrees Plato
     * @param FG_P  final gravity of beer in degrees Plato
     * @return      AA as a percentage (%)
     */
    public Float calcApparentAttenuation(Float OG_P, Float FG_P) {
        Float percentApparentAttenuation;

        percentApparentAttenuation = (1 - (FG_P / OG_P))*100f;

        return percentApparentAttenuation;

    }

    /**
     * Percent Real Attenuation (RA) calculation.  RA is a more accurate representation
     * of fermentation than apparent attenuation (AA) because its calculation only
     * accounts for the fermented sugars and not the presence of alcohol (ethanol)
     * which, having a lower specific gravity than water, skews the attenuation result.
     * Generally, AA is what brewers and yeast manufacturers report as it is directly
     * measurable by the brewer with a hydrometer.
     * @param mOG_P          original gravity of beer in degrees Plato
     * @param realExtract_P  real extract of beer in degrees Plato
     * @return               RA of beer as a percentage (%)
     */
    public Float calcRealAttenuation(Float mOG_P, Float realExtract_P) {
        Float percentRealAttenuation;

        percentRealAttenuation = (1 - (realExtract_P / mOG_P))*100f;

        return percentRealAttenuation;
    }

    /**
     * Calories estimation in 12 US fluid ounces (fl oz) of beer.
     * @param mPercent_ByWeight  percent alcohol by weight (%ABW) as percentage (%)
     * @param mRealExtract_P     real extract of beer in degrees Plato
     * @param mFG_SG             final gravity of beer in specific gravity
     * @return                   estimated calories in 12 fl oz of beer
     */
    public Float calcCalories(Float mPercent_ByWeight, Float mRealExtract_P, Float mFG_SG) {
        Float calories;

        calories = ((6.9f * mPercent_ByWeight) + 4.0f * (mRealExtract_P - 0.1f)) * mFG_SG * 3.55f;

        return calories;
    }

    /**
     * Strike Water Temperature calculation in Fahrenheit (F).
     * @param waterRatio      grain to water ratio of mash as decimal
     * @param grainTemp       temperature of grain (F) before striking
     * @param mashTargetTemp  desired mash rest temperature (F)
     * @return                strike water temperature (F)
     */
    public float calcStrikeTemp(float waterRatio, float grainTemp, float mashTargetTemp) {

        Float strikeTemp;
        Float conThermo = 0.2f; // 0.4 if liters/kilograms

        // Strike Water Temp in F
        strikeTemp = (conThermo / waterRatio) * (mashTargetTemp - grainTemp) + mashTargetTemp;

        return strikeTemp;
    }

    /**
     * Total mash volume in US gallons (gal) calculation including grain and water.
     * Constant value '1.32' used in calculation is technically determined by your
     * grain's grind.  Future version will allow for this constant to be set by user.
     * @param totGrain  weight of grain bill used for mash in US pounds (lbs)
     * @param totWater  volume of mash water (gal) used for mash
     * @return          total mash volume (gal)
     */
    public float calcMashTotalVol(float totGrain, float totWater) {
        float mashTotalVol;

        // Total mash vol in Gal
        mashTotalVol = ((totGrain * 1.32f) + (totWater - totGrain)) / 4;

        return mashTotalVol;
    }

    /**
     * Temperature correction estimation for hydrometers reading for a standard
     * calibrated brewing hydrometer in specific gravity.
     * Sources:  http://hbd.org/brewery/library/HydromCorr0992.html
     *           http://merrycuss.com/calc/sgcorrection.html
     * @param wortTemp    temperature of wort in Fahrenheit (F)
     * @param measuredSG  measured specific gravity (SG) of wort
     * @return            corrected SG of wort
     */
    public float calcHydrometerAdjustment(float wortTemp, float measuredSG) {
        float conversion, correctedSG;

        conversion = (1.313454f - (0.132674f * wortTemp) + (0.002057793f * wortTemp * wortTemp) - (0.000002627634f * wortTemp * wortTemp * wortTemp)) / 1000;

        correctedSG = conversion + measuredSG;

        return correctedSG;
    }

    /**
     * Wort Dilution Calculation based on specific gravity (SG).  Used for adjusting specific
     * gravity of wort with either water or wort (e.g. malt extract) of known specific gravity.
     * @param wort1SG   current SG of wort
     * @param wort1Vol  current wort volume in US gallons (gal)
     * @param wort2SG   SG of wort or water used to adjust current wort SG
     * @param wort2Vol  volume (gal) of wort or water used to adjust current wort SG
     * @return          adjusted SG of wort as result of mixing
     */
    public float calcDilutionNewSG(float wort1SG, float wort1Vol, float wort2SG, float wort2Vol) {
        float dilutionWortNewSG;

        dilutionWortNewSG = ((((wort1SG - 1) * wort1Vol) + ((wort2SG - 1) * wort2Vol)) / (wort1Vol + wort2Vol)) + 1;

        return dilutionWortNewSG;
    }

    /**
     * CO2 Volumes Calculation - Forced (e.g. kegging with CO2 tank).  This is based on
     * the "set it and forget it" method of carbonating keg where the regulator PSI setting
     * and temperature of beer will equilibrate to the desired volumes of CO2.  The amount of
     * time required to reach this equilibration depends on multiple factors, but in practice
     * generally takes 5 - 10 days.
     * @param tempF   temperature of beer in Fahrenheit (F)
     * @param volCO2  desired volumes of CO2 in carbonated beer
     * @return        CO2 regulator PSI setting
     */
    public float calcCO2VolForced(float tempF, float volCO2) {
        float co2RegPSI;

        co2RegPSI = -16.6999f - (0.0101059f * tempF) + (0.00116512f * tempF * tempF) + (0.173354f * tempF * volCO2) + (4.24267f * volCO2) - (0.0684226f * volCO2 * volCO2);

        return co2RegPSI;
    }

    /**
     * CO2 Volumes Calculation - Natural (e.g. bottling beers with priming sugar in 12 US
     * fluid ounce bottles).  This is based on the common technique of bottling beer in
     * standard US beer bottles.  Carbonation levels occur from the refermentation of the
     * beer as of a result of adding a highly fermentable "priming" sugar to the beer
     * (e.g. dextrose / corn sugar) and sealing the beer by capping in a bottle.
     * The amount of time required to reach the desired carbonation level depends of multiple
     * factors, but in practice generally takes 10 - 14 days at room temperature.
     *
     * This calculation is based on dextrose / corn sugar.
     * @param tempF            temperature of beer in Fahrenheit (F)
     * @param volCO2           desired volumes of CO2 in beer
     * @param batchSizeLiters  volume of beer in which priming sugar is to be added to in Liters (L)
     * @return                 grams (g) of priming sugar to add to volume of beer to carbonate
     */
    public float calcCO2VolNatural(float tempF, float volCO2, float batchSizeLiters) {
        float currentCO2, primingSugarGrams;

        currentCO2 = 3.0378f - (0.050062f * tempF) + (0.00026555f * tempF * tempF);
        primingSugarGrams = (((volCO2 * 2f) - (currentCO2 * 2f)) * 2f * batchSizeLiters) / 0.91f;

        return primingSugarGrams;
    }

    /**
     * Non-dextrose Priming Sugar Conversion.  This calculation allows the brewer to determine
     * the mass of a non-dextrose priming sugar in US ounces (oz) that is approximately equivalent
     * to dextrose in terms of "fermentability".  Substitute this mass to prime the beer to
     * reach the desired volumes of CO2 equivalent to what dextrose achieve.
     *
     * This calculation allows for a range of different priming sugars to be used based on the
     * mass of dextrose calculated from the {@link this.calcCO2VolNatural(float, float, float)}
     * method.
     *
     * Source: "How to Brew", Palmer (pg 111), 3rd ed.
     * @param massCornSugarOz  mass of corn sugar (oz) required for desired CO2 volumes
     * @param extractYield     extract yield (ppg) of desired priming sugar
     * @param fermentability   percent (%) fermentability of desired priming sugar
     * @return                 mass of desired priming sugar (oz)
     */
    public float convertPrimingSugar (float massCornSugarOz, float extractYield, float fermentability) {
        float massPrimingSugarOz;

        massPrimingSugarOz = (massCornSugarOz * 42f) / (extractYield * fermentability);

        return massPrimingSugarOz;

    }

}