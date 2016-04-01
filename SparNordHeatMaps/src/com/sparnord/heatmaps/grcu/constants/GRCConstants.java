package com.sparnord.heatmaps.grcu.constants;

/**
 * All Internal Control report constants : MC_ : prefix for Metaclasses MAE_ :
 * prefix for Meta Association End MA_ : prefix for MetaAttribute QUERY_ :
 * prefix for Queries
 * @author Jamal
 * @since HOPEX 1.1
 */
public class GRCConstants {

  // Code templates
  public static final String CT_EVALUATED                                  = "~B3xt)7EiFnJ8[Evaluated]";
  public static final String CT_NOT_EVALUATED                              = "~e3xtB8EiFnK8[Not Evaluated]";
  public static final String CT_NO_DATA                                    = "~l8QzTVVlFbuB[No Data]";
  public static final String CT_NUMBER_OF_CONTROLS                         = "~TWuV7i9oFfmK[Number of controls]";
  public static final String CT_TOTAL_NUMBER_OF_CONTROLS                   = "~vGipWXbnFjvF[Total number of controls]";
  public static final String CT_CONTROL_PER_OBJECTIVE                      = "~FHip)ionFv)F[Control Per Objective]";
  public static final String CT_CONTROL_PER_PROCESS                        = "~zYuVVW9oFPfK[Control per Process]";
  public static final String CT_CONTROL_PER_ACCOUNT                        = "~8iqmamUBHflS[Control per Account]";
  public static final String CT_CONTROL_PER_CONTROL_TYPE                   = "~wXuV1Z9oF5hK[Control per Control Type]";
  public static final String CT_CONTROL_PER_ORG_UNIT                       = "~lWuVPb9oFriK[Control per Org-Unit]";
  public static final String CT_CONTROL_PER_SUB_ENTITY                     = "~GIAwz4UpFTsO[Controls per Sub Entity]";
  public static final String CT_CONTROL_PER_SUB_PROCESS                    = "~3r6BTtYHGfAO[Controls per Sub Process]";
  public static final String CT_CONTROL_PER_ENTITY                         = "~ds6BTuYHGrCO[Controls per Entity]";

  // MetaAttributes IDs
  public static final String MA_CREATION_DATE                              = "~510000000L00[Creation Date]";
  public static final String MA_SHORT_NAME                                 = "~Z20000000D60[Short Name]";

  // Queries
  public static final String QUERY_GET_PARENT_ORGPROCESS_FROM_ORG_PROCESS  = "~TVi()Z0aGjX6[RCSA Get Parent orgProcess from OrgProcess]";
  public static final String QUERY_GET_PARENT_BUPROCESS_FROM_ORG_PROCESS   = "~4Si(Fd0aG5d6[RCSA GET_PARENT_BUPROCESS_FROM_ORG_PROCESS]";
  public static final String QUERY_GET_PARENT_ORGUNIT_FROM_ORGUNIT         = "~ZMI(wg)aGD(2[RCSA Get parent orgUnit from Org Unit]";
  public static final String QUERY_GET_PARENT_CONTROLTYPE_FROM_CONTROLTYPE = "~2KI(FZ0bGjX3[RCSA Get parent control type from control type]";
  public static final String QUERY_GET_PARENT_BUPROCESS_FROM_BU_PROCESS    = "~oSi(3R0aGzQ6[RCSA get parent buProcess from buProcess]";

  //Others
  public static final String DATA_LABEL_PERCENT                            = "{percent|0}%";
  public static final String MAV_CAMPAIGNTYPE_WITHTEMPLATE                 = "T";
  public static final String MAV_CAMPAIGNTYPE_EXECUTION                    = "E";
  public static final String MAV_CAMPAIGNTYPE_WITHOUTTEMPLATE              = "W";
  public static final String MAV_ACTION_PLAN_CLOSED                        = "CL";
  public static final String MAV_ACTION_PLAN_COMPLTED                      = "CM";
  public static final String MAV_CONTROL_LEVEL_PASS                        = "~UB6SAM7CHrs7[Control Level (CI) - Pass]";
  public static final String MAV_CONTROL_EXECUTION_OK                      = "~nWUFy24JHrc1[Control Execution Value (IC) - OK]";
  public static final String ENTITY_TYPE_COMPANY                           = "C";
  public static final String HEATMAP_DRILLDOWN_METAWIZARD                  = "~lWgv2I0QIL0N";
  public static final String EXCEL_CHECKED                                 = "X";
  public static final String CHECK_IMAGE                                   = "~XRNWz9ONHPzP[check]";
  public static final String CONTEX_IMAGE_EVALUATION_LOWER_THAN_50         = "~1Ju6rk1Y5De0[SQUARE_R4-4]";
  public static final String CONTEX_IMAGE_EVALUATION_ABOVE_50              = "~I9DvtYWF5bL0[SQUARE_O2-1]";
  public static final String CONTEX_IMAGE_EVALUATION_ABOVE_60              = "~9Vxe)XQ2EXNU[SQUARE_Y2]";
  public static final String CONTEX_IMAGE_EVALUATION_ABOVE_75              = "~iOXss2EF5L20[SQUARE_G2-1]";
  public static final String CONTEX_IMAGE_EVALUATION_ABOVE_90              = "~w6RYcDvF55W0[SQUARE_G4]";
  public static final String EVALUATED                                     = "Evaluated";
  public static final String NOT_EVALUATED                                 = "Not Evaluated";
  public static final String WITH_CONTROL                                  = "With Control";
  public static final String WITHOUT_CONTROL                               = "Without Control";
  public static final String NUMBER_OF_EVALUATED_INSTANCES                 = "Number of Eval Instances";
  public static final String NUMBER_OF_NOT_EVALUATED_INSTANCES             = "Number of Not Eval Instances";

  //Assessed Characteristic
  public static final String AC_AVG_PERCENT_PASS_CONTROL_LEVEL             = "~pY9sinDCHvFV[Average percentage of Pass Contol Level]";
  public static final String AC_CONTROL_LEVEL                              = "~S96SHU7CH5E8[Control Level]";
  public static final String AC_OK_KO                                      = "~DXUFx14JHjS1[OK/KO]";
  public static final String AC_AVG_PERCENT_OK_CONTROL_LEVEL               = "~9YUFRc4JHHz3[Average Percentage of OK Control Level]";

  //Assessed Characteristic
  public static final String AC_ERM_MAX_IMPACT                             = "~IIpgrnQzF5VN[ERM Max Impact]";
  public static final String AC_ERM_MAX_LIKELIHOUD                         = "~VGpgPpQzFbXN[ERM Max Likelihood]";
  public static final String AC_ERM_MAX_INHERENT_RISK                      = "~BIpgTqQzF1aN[ERM Max Inherent Risk]";
  public static final String AC_ERM_MAX_CONTROL_LEVEL                      = "~RRzDJmJ)FfVE[ERM Max Mitigation Level]";
  public static final String EXCEL_INCREASING                              = "+";
  public static final String EXCEL_DECREASING                              = "-";
  public static final String EXCEL_STABLE                                  = "0";
  public static final String INCREASING_ARROW                              = "~Ke2gssw6GLpF[increasing]";
  public static final String DECRESAING_ARROW                              = "~Df2gItw6G9rF[decreasing]";
  public static final String STABLE_ARROW                                  = "~6h2gBsw6G9nF[stable]";

  // Internal Value
  public static final String IV_ControlStatus_1                            = "CL";
  public static final String IV_ControlStatus_2                            = "CR";
  public static final String IV_ControlStatus_3                            = "R";
  public static final String IV_ControlStatus_4                            = "S";
  public static final String IV_ControlStatus_5                            = "V";
  public static final String IV_SIGNATORY_TYPE_ASSESSOR                    = "A";
  public static final String IV_QesSTATUS_TO_BE_VALIDATED                  = "V";                                                               //Questionnaire Status
  public static final String IV_QesSTATUS_TO_BE_COMPLETED                  = "T";                                                               //Questionnaire Status
  public static final String IV_QesSTATUS_CLOSED                           = "C";
  public static final String IV_QesSTATUS_AUTOMATICLY_CLOSED               = "AC";
  //internal Value action plan status
  public static final String IV_ACTION_PLAN_CLOSED                         = "CL";
  public static final String IV_ACTION_PLAN_CANCELLED                      = "CA";
  public static final String IV_ACTION_PLAN_IN_PROGRESS                    = "IP";
  public static final String IV_ACTION_PLAN_TO_BE_CLOSED                   = "AC";
  public static final String IV_ACTION_PLAN_TO_BE_STARTED                  = "AO";
  public static final String IV_ACTION_PLAN_TO_BE_SUBMITTED                = "D";
  public static final String IV_ACTION_PLAN_TO_BE_VALIDATED                = "AV";
  // Assessed Characteristic
  public static final String AC_ERM_IMPACT                                 = "~Dwj3HF1zFf2K[ERM Impact]";
  public static final String AC_ERM_LIKELIHOUD                             = "~Auj3rG1zF94K[ERM Likelihood]";
  public static final String AC_ERM_INHERENT_RISK                          = "~)vj3BI1zF56K[ERM Inherent Risk] ";
  public static final String AC_ERM_CONTROL_LEVEL                          = "~9wj3nO1zFDCK[ERM Control Level]";
  public static final String AC_NET_RISK                                   = "~Muj3)J1zFH8K[Net Risk]";
  //-------------------------------------------------------------------------------------------------------------
  // Macro
  public static final String MACRO_ABSOLUTE_RISK_MAP                       = "~nSUX5UeNDbRL[AbsoluteRiskMapMacro]";
  public static final String MACRO_AGGREGATION                             = "~naQ2GfGSHrWL[ERM - Call Aggregation]";
  //Questionnaire Status
  public static final String IV_ASSESSMENT_SESSION_STATUS_CLOSED           = "CL";
  //---------------------------------------------------------------------------------------------------
  //MetaAttribute Value
  public static final String MAV_VALIDATED_CONTROL                         = "~Rh(315XrF5aA[Validated Control]";
  public static final String MAV_SUBMITTED_CONTROL                         = "~fg(3t4XrFbXA[Submitted Control]";
  public static final String MAV_REJECTED_CONTROL                          = "~6e(345XrFbcA[Rejected Control]";
  public static final String MAV_CREATED_CONTROL                           = "~yf(3q4XrF1VA[Created Control]";
  public static final String MAV_CLOSED_CONTROL                            = "~me(365XrF5fA[Closed Control]";
  public static final String MAV_SIGNATORY_TYPE_ASSESSOR                   = "~ovbAp1ptEPAT[Signatory Type - Assessor]";
  public static final String MAV_Mode_Direct                               = "1";
  public static final String MAV_Mode_Standard                             = "2";
  public static final String MAV_AssessmentMode_ExpertAssessment           = "E";
  //-------------------------------------------------------------------------------------------------
  /*Others*/
  public static final String MEGA_FORMAT                                   = "yyyy/MM/dd";
  public static final String IV_ASSESSMENT_SESSION_STATUS_ASClosed         = "CL";
  /*Assessed characteritics*/
  public static final String AC_AVG_COMPLIANCE_CONTROL_LEVEL               = "~G2x(oRn9ILpB[Compliance control Level Result]";
  public static final String AC_COMPLIANCE_CONTROL_LEVEL                   = "~G2x(oRn9ILpB[Compliance control Level]";
  public static final String AC_AVG_NET_RISK                               = "~Muj3)J1zFH8K[Net Risk]";
  public static final String AC_INHERENT_RISK                              = "~)vj3BI1zF56K[Inherent Risk]";
  public static final String AC_AVG_INHERENT_RISK                          = "~yIpgyqQzF9bN[ERM Avg Inherent Risk]";
  public static final String AC_AVG_DESIGN                                 = "~MndpynTEI9(H[Avg Design Compliance Control Level]";
  public static final String AC_AVG_EFFECTIVENESS                          = "~vndplqTEI15I[Avg Direct Effectiveness Compliance Control level]";
  public static final String AC_AVG_INDIRECT_TESTING                       = "~DmdpRrTEIXBI[Avg Indirect Testing Compliance Control level]";
  public static final String AC_AVG_IMPACT                                 = "~GJpgVoQzFHWN[ERM Avg Impact]";
  public static final String AC_AVG_LIKELIHOOD                             = "~DHpgrpQzFjYN[ERM Avg Likelihood]";
  public static final String AC_AVG_VELOCITY                               = "~b1NHM6tsKLKD[Avg Velocity]";
  public static final String AC_AVG_WEIGHTED_INHERENT_RISK                 = "~52NHg8tsK1TD[Avg Weighted Inherent Risk]";

}

