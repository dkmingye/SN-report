package com.sparnord.heatmaps.grcu.colors;

import java.awt.Color;

import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAssociationEnd;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAttribut;
import com.sparnord.heatmaps.grcu.constants.GRCMetaClass;

/**
 * this class regroup all MEGA chosen Colors For IC,ERM AUDIT ... and also all
 * the methods related to colors
 * @author ARK
 */

public class GRCColorsUtility {
  private static final String BLUE_DARK                              = "0054B3";
  private static final String BLUE_MEDIUM                            = "247BDE";
  private static final String BLUE_LIGHT                             = "59A7FF";
  private static final String PURPLE_DARK                            = "682B9E";
  private static final String PURPLE_MEDIUM                          = "A65CE6";
  private static final String PURPLE_LIGHT                           = "D199FF";
  private static final String YELLOW_DARK                            = "D99F00";
  private static final String YELLOW_MEDIUM                          = "FFD11A";
  private static final String YELLOW_LIGHT                           = "FFE273";
  private static final String GRAY_DARK                              = "666666";
  private static final String GRAY_MEDIUM                            = "A6A6A6";
  private static final String GRAY_LIGHT                             = "CCCCCC";
  private static final String ORANGE_DARK                            = "E05E00";
  private static final String ORANGE_MEDIUM                          = "FF8A33";
  private static final String ORANGE_LIGHT                           = "FFAB6E";
  private static final String GREEN_DARK                             = "458C39";
  private static final String GREEN_MEDIUM                           = "66CC59";
  private static final String GREEN_LIGHT                            = "B1F071";
  private static final String CAPPUCCINO_DARK                        = "A19467";                          // beige in French
  private static final String CAPPUCCINO_MEDIUM                      = "D1C8A7";
  private static final String CAPPUCCINO_LIGHT                       = "F2EBCD";
  private static final String RED_DARK                               = "C70000";
  private static final String RED_MEDIUM                             = "EB452F";
  private static final String RED_LIGHT                              = "FF7B78";
  private static final String CUSTOM_GRAY                            = "E8E6DE";
  private static final String CUSTOM_BLUE                            = "00457E";

  /**
   * the two colors selected for the binary condition yes_color =
   * "yes,evaluated,riskw with control"
   */
  public static final String  YES_CASE_COLOR                         = GRCColorsUtility.BLUE_MEDIUM;

  /**
   * the two colors selected for the binary condition no_color = "non,not
   * evaluated,risk without control
   */
  public static final String  NO_CASE_COLOR                          = GRCColorsUtility.CAPPUCCINO_MEDIUM;

  /**
   * percentage Status for <= 25 % of forms
   */
  public static final String  PERCENTAGE_LOWER_THAN_25               = GRCColorsUtility.RED_MEDIUM;

  /**
   * percentage Status for >25% <= 50 % of forms
   */
  public static final String  PERCENTAGE_LOWER_THAN_50               = GRCColorsUtility.ORANGE_MEDIUM;

  /**
   * percentage Status for >50% <= 75 % of forms
   */
  public static final String  PERCENTAGE_LOWER_THAN_75               = GRCColorsUtility.YELLOW_MEDIUM;

  /**
   * percentage Status for > 75 % of forms
   */
  public static final String  PERCENTAGE_ABOVE_75                    = GRCColorsUtility.GREEN_MEDIUM;

  /**
   * the std color used for all barCharts
   */
  public static final String  BAR_STD_COLOR                          = GRCColorsUtility.BLUE_MEDIUM;

  /**
   * the std color used for no data barchart/pieChart
   */
  public static final String  NO_DATA_COLOR                          = GRCColorsUtility.GRAY_LIGHT;

  /**
   * the std color used for heatMap Title
   */
  public static final String  HEATMAP_TITLE                          = GRCColorsUtility.CUSTOM_GRAY;

  /**
   * the std color used for "total number ..." title
   */
  public static final String  TITLE_COLOR                            = GRCColorsUtility.CUSTOM_BLUE;

  /**
   * the std color used for entity contextualized evaluation <50%
   */
  public static final String  ENTITY_CONTEX_EVALUATION_LOWER_THAN_50 = GRCColorsUtility.RED_MEDIUM;

  /**
   * the std color used for entity contextualized evaluation >=50%
   */
  public static final String  ENTITY_CONTEX_EVALUATION_ABOVE_50      = GRCColorsUtility.ORANGE_MEDIUM;

  /**
   * the std color used for entity contextualized evaluation >=60%
   */
  public static final String  ENTITY_CONTEX_EVALUATION_ABOVE_60      = GRCColorsUtility.YELLOW_MEDIUM;

  /**
   * the std color used for entity contextualized evaluation >=75%
   */
  public static final String  ENTITY_CONTEX_EVALUATION_ABOVE_75      = GRCColorsUtility.GREEN_LIGHT;

  /**
   * the std color used for entity contextualized evaluation >=90%
   */
  public static final String  ENTITY_CONTEX_EVALUATION_ABOVE_90      = GRCColorsUtility.GREEN_DARK;

  /**
   * HOE modification: add colors for planned and effective dates
   */
  public static final String  PLANNED_DATE_COLOR                     = GRCColorsUtility.CAPPUCCINO_MEDIUM;
  public static final String  EFFECTIVE_DATE_COLOR                   = GRCColorsUtility.BLUE_DARK;

  /**
   * the std color used for Table BackGround titles
   */
  public static final String  TABLE_BACKGROUND                       = GRCColorsUtility.CUSTOM_GRAY;

  /**
   * IC percentage Status for <50 % of forms
   */
  public static final String  IC_PERCENTAGE_LOWER_THAN_50            = GRCColorsUtility.RED_MEDIUM;

  /**
   * IC percentage Status for >= 50 % of forms
   */
  public static final String  IC_PERCENTAGE_ABOVE_50                 = GRCColorsUtility.ORANGE_MEDIUM;

  /**
   * IC percentage Status for >=70 % of forms
   */
  public static final String  IC_PERCENTAGE_ABOVE_70                 = GRCColorsUtility.YELLOW_MEDIUM;

  /**
   * percentage Status for > 90 % of forms
   */
  public static final String  IC_PERCENTAGE_ABOVE_90                 = GRCColorsUtility.GREEN_MEDIUM;

  /**
   * the std colors for the tree titles
   */
  public static final String  TREE_TITLES_BACKGROUND                 = GRCColorsUtility.BLUE_DARK;

  /**
   * Get an hexadecimal color identifier INTEGER from an int. 24 different
   * colors from web-safe colors. Destined for use w/ ChartDirector
   * @param j an integer
   * @return the hexadecimal color identifier
   * @isPie true : get the std colors for pieChart, false: get the std colors !
   *        the order is so important
   */
  public static String getHexaColors(final int j, final Boolean isPie) {

    if (isPie) {
      final String colorsForPieChart[] = { GRCColorsUtility.BLUE_MEDIUM, GRCColorsUtility.CAPPUCCINO_MEDIUM, GRCColorsUtility.ORANGE_MEDIUM, GRCColorsUtility.YELLOW_MEDIUM, GRCColorsUtility.RED_MEDIUM, GRCColorsUtility.GRAY_MEDIUM, GRCColorsUtility.PURPLE_MEDIUM, GRCColorsUtility.GREEN_MEDIUM, GRCColorsUtility.BLUE_LIGHT, GRCColorsUtility.CAPPUCCINO_LIGHT, GRCColorsUtility.ORANGE_LIGHT, GRCColorsUtility.YELLOW_LIGHT, GRCColorsUtility.RED_LIGHT, GRCColorsUtility.GRAY_LIGHT, GRCColorsUtility.PURPLE_LIGHT, GRCColorsUtility.GREEN_LIGHT, GRCColorsUtility.BLUE_DARK, GRCColorsUtility.CAPPUCCINO_DARK, GRCColorsUtility.ORANGE_DARK, GRCColorsUtility.YELLOW_DARK, GRCColorsUtility.RED_DARK, GRCColorsUtility.GRAY_DARK, GRCColorsUtility.PURPLE_DARK, GRCColorsUtility.GREEN_DARK };
      return colorsForPieChart[j % (colorsForPieChart.length + 1)];
    }

    final String colorsForBarChart[] = { GRCColorsUtility.BLUE_DARK, GRCColorsUtility.BLUE_MEDIUM, GRCColorsUtility.BLUE_LIGHT, GRCColorsUtility.CAPPUCCINO_DARK, GRCColorsUtility.CAPPUCCINO_MEDIUM, GRCColorsUtility.CAPPUCCINO_LIGHT, GRCColorsUtility.ORANGE_DARK, GRCColorsUtility.ORANGE_MEDIUM, GRCColorsUtility.ORANGE_LIGHT, GRCColorsUtility.YELLOW_DARK, GRCColorsUtility.YELLOW_MEDIUM, GRCColorsUtility.YELLOW_LIGHT, GRCColorsUtility.RED_DARK, GRCColorsUtility.RED_MEDIUM, GRCColorsUtility.RED_LIGHT, GRCColorsUtility.GRAY_DARK, GRCColorsUtility.GRAY_MEDIUM, GRCColorsUtility.GRAY_LIGHT, GRCColorsUtility.PURPLE_DARK, GRCColorsUtility.PURPLE_MEDIUM, GRCColorsUtility.PURPLE_LIGHT, GRCColorsUtility.GREEN_DARK, GRCColorsUtility.GREEN_MEDIUM, GRCColorsUtility.GREEN_LIGHT };
    return colorsForBarChart[j % (colorsForBarChart.length + 1)];

  }

  /**
   * return a set of std colors depending on the number given
   * @param numberOfColors number of colors needed
   * @param isPie true :get the collection of pie std colors ;false :get the std
   *          barchart colors
   * @return
   */
  @Deprecated
  public static String getStdColors(final int numberOfColors, final Boolean isPie) {
    return null;
  }

  /**
   * get the std colors for all cases with (yes,no linked,notLinked ...) or for
   * the staked Two Colors bars
   * @param inverse :if true we inverse the order of the color
   * @return
   */
  public static String getDefaultBinaryChartsColors(final Boolean inverse) {
    if (inverse) {
      return GRCColorsUtility.YES_CASE_COLOR + "," + GRCColorsUtility.NO_CASE_COLOR;
    }
    return GRCColorsUtility.NO_CASE_COLOR + "," + GRCColorsUtility.YES_CASE_COLOR;

  }

  /**
   * @param colors existing colors
   * @param numberOfColors the number of colors to be add
   * @param isPie check if it's pie to choose the right set of colors
   * @return add a set of colors to an existing set of colors
   */
  public static String getDefaultColor(final String colors, final Integer numberOfColors, final Boolean isPie) {

    String NewColors = colors;
    int i = 0;
    int colorIdentifier = 0;
    while (i < numberOfColors) {

      String nextColor = GRCColorsUtility.getHexaColors(colorIdentifier, isPie);

      // we get a random color that not exist in the color list for the pie
      while (NewColors.contains(nextColor)) {
        colorIdentifier++;
        nextColor = GRCColorsUtility.getHexaColors(colorIdentifier, isPie);
      }
      NewColors = NewColors + "," + nextColor;
      i++;
    }
    return NewColors;
  }

  public static String Color2Hex(final String[] args) {
    if (args.length != 3) {
      return "FFFFFF";
    }
    int i = Integer.parseInt(args[0]);
    int j = Integer.parseInt(args[1]);
    int k = Integer.parseInt(args[2]);
    Color c = new Color(i, j, k);

    String color = Integer.toHexString(c.getRGB() & 0x00ffffff);

    while (color.length() < 6) {
      color = "0" + color;
    }

    return color;

  }

  public static String[] getRGBfromParam(final MegaObject metaValue) {
    String[] rgb = new String[3];
    try {
      String colors = metaValue.getProp(GRCMetaAttribut.MA_PARAMETRISATION);
      colors = colors.substring(colors.indexOf("(") + 1, colors.indexOf(")"));
      rgb[0] = colors.substring(0, colors.indexOf(","));
      colors = colors.substring(colors.indexOf(",") + 1);
      rgb[1] = colors.substring(0, colors.indexOf(","));
      colors = colors.substring(colors.indexOf(",") + 1);
      rgb[2] = colors;
    } catch (Exception e) {
      rgb[0] = "255";
      rgb[1] = "255";
      rgb[2] = "255";
    }
    return rgb;
  }

  public static String getPercentagePassCtrlColor(final double value) {

    if (value >= 90) {
      return GRCColorsUtility.ENTITY_CONTEX_EVALUATION_ABOVE_90;
    } else if (value >= 75) {
      return GRCColorsUtility.ENTITY_CONTEX_EVALUATION_ABOVE_75;
    } else if (value >= 60) {
      return GRCColorsUtility.ENTITY_CONTEX_EVALUATION_ABOVE_60;
    } else if (value >= 50) {
      return GRCColorsUtility.ENTITY_CONTEX_EVALUATION_ABOVE_50;
    }
    return GRCColorsUtility.ENTITY_CONTEX_EVALUATION_LOWER_THAN_50;
  }

  public static Color hex2RGB(final String colorStr) {
    return new Color(Integer.valueOf(colorStr.substring(0, 2), 16), Integer.valueOf(colorStr.substring(2, 4), 16), Integer.valueOf(colorStr.substring(4, 6), 16));
  }

  public static String getMetaAttributeColors(final MegaRoot root, final String metaAttribute, final Boolean isPie) {

    String colors = "";
    MegaCollection mcMetaAttributes = root.getCollection(GRCMetaClass.MC_METAATTRIBUTE);
    MegaObject moMetaAttribute = mcMetaAttributes.get(metaAttribute);
    mcMetaAttributes.release();

    if (moMetaAttribute.getID() != null) {
      MegaCollection mcMetaAttributesValues = moMetaAttribute.getCollection(GRCMetaAssociationEnd.MAE_META_ATTRIBUTE_VALUE, "order", GRCMetaAttribut.MA_INTERNAL_VALUE);
      for (MegaObject metaAttributeValue : mcMetaAttributesValues) {

        //this treatment is just for escaping the character "," in the end of the string
        if (colors.isEmpty()) {
          colors += GRCColorsUtility.Color2Hex(GRCColorsUtility.getRGBfromParam(metaAttributeValue));
        } else {
          colors += "," + GRCColorsUtility.Color2Hex(GRCColorsUtility.getRGBfromParam(metaAttributeValue));
        }

        metaAttributeValue.release();
      }

      mcMetaAttributesValues.release();
    }

    moMetaAttribute.release();
    //we get an std color for the metaAttributeValue that doesn't have one
    colors = GRCColorsUtility.replaceWhiteColors(colors, isPie);

    return colors;
  }

  /**
   * ARK replace all white colors with std colors
   * @param colors
   * @param numberOfcolors
   * @param isPie true:we use std colors for pie ; false:we use std colors for
   *          barChart
   * @return
   */
  public static String replaceWhiteColors(final String colors, final Boolean isPie) {

    String colorsForPie = colors;
    int colorIdentifier = 0;

    if (colorsForPie.contains("ffffff") || (colorsForPie.length() == 0)) {

      while (colorsForPie.contains("ffffff")) {
        String nextColor = GRCColorsUtility.getHexaColors(colorIdentifier, isPie);

        // we get a random color that not exist in the color list for the pie
        while (colorsForPie.contains(nextColor)) {
          colorIdentifier++;
          nextColor = GRCColorsUtility.getHexaColors(colorIdentifier, isPie);
        }
        colorsForPie = colorsForPie.replaceFirst("ffffff", nextColor);
      }
    }

    return colorsForPie;
  }

  public static String getColorOfMetattributeVal(final MegaObject mgMetaVal) {
    String color = "";
    int intCol = (int) mgMetaVal.getProp(GRCMetaAttribut.MA_RGB_COLOR, "internal");
    color = GRCColorsUtility.getHexColor(intCol);
    if (color.equals("")) {
      color = GRCColorsUtility.Color2Hex(GRCColorsUtility.getRGBfromParam(mgMetaVal));
    }
    return color;
  }

  private static String getHexColor(final int intColor) {
    String color = "";
    color = String.format("%06X", (0xFFFFFF & intColor));
    while (color.length() < 6) {
      color = color + "0";
    }
    //conversion from bb,gg,rr (windows) to rr,gg,bb (html)
    color = color.substring(4) + color.substring(2, 4) + color.substring(0, 2);
    return color;
  }

}

