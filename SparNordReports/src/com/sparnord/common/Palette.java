package com.sparnord.common;

import java.awt.Color;

import com.mega.modeling.api.MegaObject;

/**
 * this class regroup all MEGA chosen Colors For IC,ERM AUDIT ... and also all
 * the methods related to colors
 */

public class Palette {
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
  private static final String CAPPUCCINO_DARK                        = "A19467";                  // beige in French
  private static final String CAPPUCCINO_MEDIUM                      = "D1C8A7";
  private static final String CAPPUCCINO_LIGHT                       = "F2EBCD";
  private static final String RED_DARK                               = "C70000";
  private static final String RED_MEDIUM                             = "EB452F";
  private static final String RED_LIGHT                              = "FF7B78";
  private static final String CUSTOM_GRAY                            = "E8E6DE";
  private static final String CUSTOM_BLUE                            = "00457E";
  private static final String BLUE_DARK_MODERATE                     = "366092";
  private static final String WHITE                                  = "ffffff";
  private static final String GRAY                                   = "DCDCDC";

  /**
   * the two colors selected for the binary condition yes_color =
   * "yes,evaluated,riskw with control"
   */
  public static final String  YES_CASE_COLOR                         = Palette.BLUE_MEDIUM;

  /**
   * the two colors selected for the binary condition no_color = "non,not
   * evaluated,risk without control
   */
  public static final String  NO_CASE_COLOR                          = Palette.CAPPUCCINO_MEDIUM;

  /**
   * Color used in Capital Calculation and Back Testing matrix
   */
  public static final String  TABLE_HEADER                           = Palette.BLUE_DARK_MODERATE;

  /**
   * Color used in Capital Calculation to color Capital Allocation Line
   */
  public static final String  TABLE_SUM_LINE                         = Palette.CAPPUCCINO_MEDIUM;

  /**
   * percentage Status for <= 25 % of forms
   */
  public static final String  PERCENTAGE_LOWER_THAN_25               = Palette.RED_MEDIUM;

  /**
   * percentage Status for >25% <= 50 % of forms
   */
  public static final String  PERCENTAGE_LOWER_THAN_50               = Palette.ORANGE_MEDIUM;

  /**
   * percentage Status for >50% <= 75 % of forms
   */
  public static final String  PERCENTAGE_LOWER_THAN_75               = Palette.YELLOW_MEDIUM;

  /**
   * percentage Status for > 75 % of forms
   */
  public static final String  PERCENTAGE_ABOVE_75                    = Palette.GREEN_MEDIUM;

  /**
   * the std color used for all barCharts
   */
  public static final String  BAR_STD_COLOR                          = Palette.BLUE_MEDIUM;

  /**
   * the std color used for no data barchart/pieChart
   */
  public static final String  NO_DATA_COLOR                          = Palette.GRAY_LIGHT;

  /**
   * the std color used for heatMap Title
   */
  public static final String  HEATMAP_TITLE                          = Palette.CUSTOM_GRAY;

  /**
   * the std color used for "total number ..." title
   */
  public static final String  TITLE_COLOR                            = Palette.CUSTOM_BLUE;

  /**
   * the std color used for entity contextualized evaluation <50%
   */
  public static final String  ENTITY_CONTEX_EVALUATION_LOWER_THAN_50 = Palette.RED_MEDIUM;

  /**
   * the std color used for entity contextualized evaluation >=50%
   */
  public static final String  ENTITY_CONTEX_EVALUATION_ABOVE_50      = Palette.ORANGE_MEDIUM;

  /**
   * the std color used for entity contextualized evaluation >=60%
   */
  public static final String  ENTITY_CONTEX_EVALUATION_ABOVE_60      = Palette.YELLOW_MEDIUM;

  /**
   * the std color used for entity contextualized evaluation >=75%
   */
  public static final String  ENTITY_CONTEX_EVALUATION_ABOVE_75      = Palette.GREEN_LIGHT;

  /**
   * the std color used for entity contextualized evaluation >=90%
   */
  public static final String  ENTITY_CONTEX_EVALUATION_ABOVE_90      = Palette.GREEN_DARK;

  /**
   * HOE modification: add colors for planned and effective dates
   */
  public static final String  PLANNED_DATE_COLOR                     = Palette.CAPPUCCINO_MEDIUM;
  public static final String  EFFECTIVE_DATE_COLOR                   = Palette.BLUE_DARK;

  /**
   * the std color used for Table BackGround titles
   */
  public static final String  TABLE_BACKGROUND                       = Palette.CUSTOM_GRAY;

  /**
   * the std color used for Table BackGround titles (Vertical & Horizontal)
   */
  public static final String  GRAY_TABLE_HEADER_BACKGROUND           = Palette.GRAY;

  /**
   * the std color used as standard white
   */
  public static final String  STANDARD_WHITE                         = Palette.WHITE;

  /**
   * Get an hexadecimal color identifier INTEGER from an int. 24 different
   * colors from web-safe colors. Destined for use w/ ChartDirector
   * @param j an integer
   * @param isPie boolean
   * @return the hexadecimal color identifier
   * @isPie true : get the std colors for pieChart, false: get the std colors !
   *        the order is so important
   */
  public static String getHexaColors(final int j, final Boolean isPie) {

    if (isPie) {
      final String colorsForPieChart[] = { Palette.BLUE_MEDIUM, Palette.CAPPUCCINO_MEDIUM, Palette.ORANGE_MEDIUM, Palette.YELLOW_MEDIUM, Palette.RED_MEDIUM, Palette.GRAY_MEDIUM, Palette.PURPLE_MEDIUM, Palette.GREEN_MEDIUM, Palette.BLUE_LIGHT, Palette.CAPPUCCINO_LIGHT, Palette.ORANGE_LIGHT, Palette.YELLOW_LIGHT, Palette.RED_LIGHT, Palette.GRAY_LIGHT, Palette.PURPLE_LIGHT, Palette.GREEN_LIGHT, Palette.BLUE_DARK, Palette.CAPPUCCINO_DARK, Palette.ORANGE_DARK, Palette.YELLOW_DARK, Palette.RED_DARK, Palette.GRAY_DARK, Palette.PURPLE_DARK, Palette.GREEN_DARK };
      return colorsForPieChart[j % (colorsForPieChart.length + 1)];
    }

    final String colorsForBarChart[] = { Palette.BLUE_DARK, Palette.BLUE_MEDIUM, Palette.BLUE_LIGHT, Palette.CAPPUCCINO_DARK, Palette.CAPPUCCINO_MEDIUM, Palette.CAPPUCCINO_LIGHT, Palette.ORANGE_DARK, Palette.ORANGE_MEDIUM, Palette.ORANGE_LIGHT, Palette.YELLOW_DARK, Palette.YELLOW_MEDIUM, Palette.YELLOW_LIGHT, Palette.RED_DARK, Palette.RED_MEDIUM, Palette.RED_LIGHT, Palette.GRAY_DARK, Palette.GRAY_MEDIUM, Palette.GRAY_LIGHT, Palette.PURPLE_DARK, Palette.PURPLE_MEDIUM, Palette.PURPLE_LIGHT, Palette.GREEN_DARK, Palette.GREEN_MEDIUM, Palette.GREEN_LIGHT };
    return colorsForBarChart[j % (colorsForBarChart.length + 1)];

  }

  /**
   * return a set of std colors depending on the number given
   * @param numberOfColors number of colors needed
   * @param isPie true :get the collection of pie std colors ;false :get the std
   *          barchart colors
   * @return Standards colors as String of hexadecimal
   */
  @Deprecated
  public static String getStdColors(final int numberOfColors, final Boolean isPie) {
    return null;
  }

  /**
   * get the std colors for all cases with (yes,no linked,notLinked ...) or for
   * the staked Two Colors bars
   * @param inverse :if true we inverse the order of the color
   * @return Standards colors of YES/NO as String of hexadecimal :
   *         Palette.YES_CASE_COLOR, Palette.NO_CASE_COLOR or
   *         Palette.YES_CASE_COLOR, Palette.NO_CASE_COLOR according to inverse
   *         parameters
   */
  public static String getDefaultBinaryChartsColors(final Boolean inverse) {
    if (inverse) {
      return Palette.YES_CASE_COLOR + "," + Palette.NO_CASE_COLOR;
    }
    return Palette.NO_CASE_COLOR + "," + Palette.YES_CASE_COLOR;

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

      String nextColor = Palette.getHexaColors(colorIdentifier, isPie);

      // we get a random color that not exist in the color list for the pie
      while (NewColors.contains(nextColor)) {
        colorIdentifier++;
        nextColor = Palette.getHexaColors(colorIdentifier, isPie);
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
      String colors = metaValue.getProp(LDCConstants.MA_PARAMETRISATION);
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
      return Palette.ENTITY_CONTEX_EVALUATION_ABOVE_90;
    } else if (value >= 75) {
      return Palette.ENTITY_CONTEX_EVALUATION_ABOVE_75;
    } else if (value >= 60) {
      return Palette.ENTITY_CONTEX_EVALUATION_ABOVE_60;
    } else if (value >= 50) {
      return Palette.ENTITY_CONTEX_EVALUATION_ABOVE_50;
    }
    return Palette.ENTITY_CONTEX_EVALUATION_LOWER_THAN_50;
  }

  public static Color hex2RGB(final String colorStr) {
    return new Color(Integer.valueOf(colorStr.substring(0, 2), 16), Integer.valueOf(colorStr.substring(2, 4), 16), Integer.valueOf(colorStr.substring(4, 6), 16));
  }

}

