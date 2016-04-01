package com.sparnord.common;


/**
 * * All LDC report constants : MC_ : prefix for Metaclasses MAE_ : prefix for
 * Meta Association End MA_ : prefix for MetaAttribute QUERY_ : prefix for
 * Queries
 */
public class LDCConstants {

  /*MetaClasses*/
  public static final String MC_INCIDENT                                         = "~iKxTxodsHTpP[Incident]";
  public static final String MC_RISK_TYPE                                        = "~7)tbkKS9zar0[Risk Type]";
  public static final String MC_ORGANIZATIONAL_PROCESS                           = "~gsUiU9B5iiR0[Organizational Process]";
  public static final String MC_ORG_UNIT                                         = "~QrUiM9B5iCN0[Org-Unit]";
  public static final String MC_BUSINESS_PROCESS                                 = "~pj)grmQ9pG90[Business Process]";
  public static final String MC_BUSINESS_LINE                                    = "~KGqbmkPPGrEJ[Business Line]";
  public static final String MC_LOSS                                             = "~xMxTDpdsHHxP[Loss]";
  public static final String MC_RECOVERY                                         = "~2KxTMpdsH9)P[Recovery]";
  public static final String MC_RISK                                             = "~W5faeGPxySL0[Risk]";
  public static final String MC_METAATTRIBUTE                                    = "~O20000000Y10[MetaAttribute]";
  public static final String MC_GROSS_INCOME                                     = "~)Z(S6jhKIPQM[Gross Income]";

  /*MetaAttributes*/
  public static final String MA_PARAMETRISATION                                  = "~eIXaJCfyhuJ0[_Parameterization]";
  public static final String MA_EFFECTIVE_DATE                                   = "~FKxTicisHLjR[Effective date]";
  public static final String MA_DECLARATION_DATE                                 = "~IKxThzhsHPoQ[Declaration Date]";
  public static final String MA_NET_LOSS                                         = "~0LxTPrhsHfMQ[Net Loss]";
  public static final String MA_NET_LOSS_LOCAL                                   = "~Pf5XbrgJIvrQ[Net Loss <local>]";
  public static final String MA_SHORT_NAME                                       = "~Z20000000D60[Short Name]";
  public static final String MA_HEX_ID_ABS                                       = "~H20000000550[_HexaIdAbs]";
  public static final String MA_GROSS_LOSS_LOCAL                                 = "~Kg5XBrgJIHhQ[Gross Loss <local>]";
  public static final String MA_GROSS_ACTUAL_LOSS_LOCAL                          = "~Vh5XMrgJIHlQ[Gross Actual Loss <local>]";
  public static final String MA_NET_ACTUAL_LOSS_LOCAL                            = "~Og5XlrgJIDvQ[Net Actual Loss <local>]";
  public static final String MA_RECOVERIES_LOCAL                                 = "~Se5XTrgJIboQ[Recoveries <local>]";
  public static final String MA_RISK_CODE                                        = "~PHawL1394f31[Risk Code]";
  public static final String MA_NET_RISK                                         = "~DJCPGp7wFzzG[Net Risk /ERM]";
  public static final String MA_VALUE_NAME                                       = "~H3l5fU1F3n80[Value Name]";
  public static final String MA_INTERNAL_VALUE                                   = "~L20000000L50[Internal Value]";
  public static final String MA_BEGIN_DATE_GROSS_INCOME                          = "~3W(SIDiKILfM[Begin Date /Gross Income]";
  public static final String MA_END_DATE_GROSS_INCOME                            = "~hX(SiDiKIDkM[End Date /Gross Income]";
  public static final String MA_REVENUE_AMOUNT                                   = "~DZ(S)DiKI5pM[Revenue Amount]";
  public static final String MA_REVENUE_AMOUNT_LOCAL                             = "~3WqroKEXInvM[Revenue Amount <local>]";
  public static final String MA_TSA                                              = "~OWqracDXIrKM[Standardized Approach Ratio <TSA %>]";

  /*MetaAssociationEnd*/
  public static final String MAE_SUB_BUS_LINE                                    = "~TGqb2vPPGr5K[Sub Business Line]";
  public static final String MAE_SUB_ORG_PROC                                    = "~KqUiuEB5iuR3[Component]";
  public static final String MAE_SUB_ORG_UNIT                                    = "~rtUioAB5iyA1[Component]";
  public static final String MAE_SUB_BUS_PROC                                    = "~8l)gvmQ9pKE0[Component]";
  public static final String MAE_SUB_RISK_TYPE                                   = "~K)tblKS9zKs0[Sub-Risk Type]";
  public static final String MAE_GENERIC_INCIDENT                                = "~X5KVHxezHr8S[Generic Incident]";
  public static final String MAE_MACROINCIDENT_INCIDENT                          = "~KNxTxuhsHfXQ[Incident]";
  public static final String MAE_RISK_INCIDENT                                   = "~uiTqb(zsHvcR[Incident]";
  public static final String MAE_RISK_RISKTYPE                                   = "~X)tbmKS9z4t0[Risk Type]";
  public static final String MAE_PICTURE                                         = "~vBwTyQyoEHHC[MetaPicture]";
  public static final String MAE_METAATTRIBUTEVALUE                              = "~(0000000C830[MetaAttributeValue]";
  public static final String MAE_GROSS_INCOME_FROM_BUSINESS_LINE                 = "~aW(ScBiKIDYM[Gross Income]";
  public static final String MAE_RISKTYPE_RISK                                   = "~W)tbmKS9z0t0[Risk]";
  public static final String MAE_ELEMENTATRISK_RISK                              = "~st6GC8KE9b70[Risk]";

  /*Code Templates*/
  public static final String CT_INCIDENT_NAME                                    = "";
  public static final String CT_MONTHS                                           = "~)sBsCjE(HDsH[LDC - Months]";
  public static final String CT_NET_LOSS                                         = "~l0FE4sJ(HLyD[Net loss]";
  public static final String CT_INCIDENTS                                        = "~fcqVK9e(HfcL[Incidents]";
  public static final String CT_NB_OF_INCIDENTS                                  = "~9sBsvcD(HDjH[Nb of incidents]";
  public static final String CT_NO_DATA                                          = "~l8QzTVVlFbuB[No Data]";
  public static final String CT_TOTAL_NET_LOSS_PER_MONTH                         = "~NP1SC3x)HbDB[Total net loss per month]";
  public static final String CT_TOTAL_INCIDENTS                                  = "~2PPZtGw)HX7V[Total number of Incidents]";
  public static final String CT_INCIDENTS_PER_SUB_PROCESS                        = "~jOPZaIw)HPFV[Incidents and net loss by sub-process]";
  public static final String CT_INCIDENTS_PER_SUB_ENTITY                         = "~iOexndF0InFW[Incidents and net loss by sub-entity]";
  public static final String CT_INCIDENTS_PER_SUB_RISK_TYPE                      = "~PTSnfMESIr3G[Incidents and net loss by Sub-Risk Type]";
  public static final String CT_INCIDENTS_PER_SUB_BUSINESS_LINE                  = "~)VSnxNESI59G[Incidents and net loss by Sub-Business Line]";
  public static final String CT_INCIDENTS_PER_PROCESS                            = "~HQexEuF0IvJW[Incidents and net loss by process]";
  public static final String CT_INCIDENTS_PER_ENTITY                             = "~DQPZ8Jw)H5JV[Incidents and net loss by entity]";
  public static final String CT_INCIDENTS_PER_RISK_TYPE                          = "~kRPZkJw)HnMV[Incidents and net loss by risk type]";
  public static final String CT_INCIDENTS_PER_BUSINESS_LINE                      = "~FPPZJKw)HTQV[Incidents and net loss by business line]";
  public static final String CT_INCIDENTS_PER_MONTH                              = "~xQPZjHw)HjBV[Incidents and net Loss per month]";
  public static final String CT_INCIDENT_EVOLUTION_PER_MONTH_RISKTYPE            = "~xIsjAkxSInbE[Incident evolution per month and risk type]";
  public static final String CT_LOSS_EVOLUTION_PER_MONTH_RISKTYPE                = "~fJsj3oxSI1fE[Loss evolution per month and risk type]";
  public static final String CT_TOTAL_LOSS                                       = "~n5BzsjGWIzHM[Total Loss]";
  public static final String CT_MAX_LOSS                                         = "~s7Bz)kGWIjLM[Max Loss]";
  public static final String CT_MIN_LOSS                                         = "~55BzJlGWIPPM[Min Loss]";
  public static final String CT_RISK_CODE                                        = "~bQF)pgHWI9UC[Risk code]";
  public static final String CT_RISK                                             = "~LOF)IhHWI9ZC[Risk]";
  public static final String CT_NET_RISK_LEVEL                                   = "~8QF)yhHWIjdC[Net Risk Level]";
  public static final String CT_GROSS_LOSS                                       = "~iRF)NiHWIDiC[Gross Loss]";
  public static final String CT_GROSS_ACTUAL_LOSS                                = "~FPF)niHWIjmC[Gross Actual Loss]";
  public static final String CT_RECOVERIES                                       = "~xQF)LjHWIDrC[Recoveries]";
  public static final String CT_NET_ACTUAL_LOSS                                  = "~rOF)7kHWIjvC[Net Actual Loss]";
  public static final String CT_AVERAGE_GROSS_INCOME                             = "~wOoYn29XInb5[Average Gross Income]";
  public static final String CT_BIA                                              = "~tPoY139XIbe5[BIA %]";
  public static final String CT_CAPITAL_ALLOCATION                               = "~tQoYK39XIPh5[Capital Allocation]";
  public static final String CT_GROSS_INCOME                                     = "~wRoYV29XIvY5[Gross Income]";
  public static final String CT_TSA                                              = "~TvzT4M9ZIXRM[TSA%]";
  public static final String CT_ITEM                                             = "~owzTXP9ZIvYM[Item]";
  public static final String CT_RISK_LEVEL                                       = "~AnpqwOTZIb(O[Risk Level]";
  public static final String CT_NOT_ASSESSED                                     = "~5opq5PTZIX1P[Not assessed]";
  public static final String CT_COMPARAISON_RISK_LEVEL_NET_LOSS                  = "~Punrr1VZIH6V[Comparaison of risk level and net loss by risk type]";
  public static final String CT_COMPARAISON_RISK_LEVEL_NBRE_INCIDENT             = "~Iwnrw0VZIX1V[Comparaison of risk level and number of incidents by risk type]";
  public static final String CT_COMP_RISKLEVEL_NETLOSS_BL                        = "~5Av5BWnZILsS[Comparaison of risk level and net loss by business line]";
  public static final String CT_COMP_RISKLEVEL_NBRE_INCIDENT_BL                  = "~48v50XnZIzwS[Comparaison of risk level and number of incidents by business line]";
  
  /*Queries*/
  public static final String QUERY_GET_INCIDENTS_FROM_MACRO_INCIDENT             = "~9QPZ84w)HHVU[LDC - Get Incidents from Macro incident]";
  public static final String QUERY_GET_INCIDENTS_FROM_LDC_ELEMENT                = "~uRPZXsv)H1zT[LDC - Get Incidents From LDC element]";
  public static final String QUERY_GET_ELEMENTS_FROM_INCIDENTS                   = "~3QPZe)v)H1HU[LDC - Get elements from incidents]";
  public static final String QUERY_GET_SUB_BUSINESS_LINES                        = "~6QPZj8w)HfiU[LDC - Get Sub Business Lines]";
  public static final String QUERY_GET_PARENTS_BUSINESS_LINES_FROM_BUSINESS_LINE = "~XPPZdCw)HbwU[LDC - Get Parents Business lines from Business line]";
  public static final String QUERY_GET_ROOT_ENTITIES                             = "~u4kGNv7uFLEB[Get Root Entities]";
  public static final String QUERY_GET_ROOT_ORGANIZATIONAL_PROCESSES             = "~hOGKDTbAGztD[Get Parent OrgProcess query]";
  public static final String QUERY_GET_ROOT_BUSINESS_PROCESSES                   = "~bC1aCYPuFvBS[Get Root Business processes]";
  public static final String QUERY_GET_ROOT_RISK_TYPES                           = "~vk87UEopF5jO[Get Root Risk Types]";
  public static final String QUERY_GET_ROOT_BUSINESS_LINES                       = "~agKAjMa8Gb6P[Get Root Business Lines]";
  public static final String QUERY_SUB_ORGUNIT                                   = "~cHXSSwtmFTLN[Get Sub Org Units]";
  public static final String QUERY_SUB_ORGPROCESS                                = "~8HXSRytmFbRN[Get Sub Org Process]";
  public static final String QUERY_SUB_BPROCESS                                  = "~fJXS(ztmFTVN[Get Sub Business Process]";
  public static final String QUERY_GET_ORGPROCESS_FROM_BUPROCESS                 = "~bk7F4qJ3GH0J[Get Organizational Process From Business Process]";
  public static final String QUERY_SUB_RISKTYPE                                  = "~bIXSN)tmFbbN[Get Sub Risk Type]";
  public static final String QUERY_SUB_BUSINESS_LINE                             = "~6QPZj8w)HfiU[LDC - Get Sub Business Lines]";
  public static final String QUERY_BUSINESS_PROCESS_FROM_ORG_UNIT                = "~pkkVVy7sF575[Get Business Process From Org-Unit]";
  public static final String QUERY_ORGANIZATIONAL_PROCESS_FROM_ORG_UNIT          = "~vkkVT(7sFbF5[Get Organizational Process From org-Unit]";
  public static final String QUERY_GET_PARENT_BUPROCESS_FROM_BU_PROCESS          = "~oSi(3R0aGzQ6[RCSA get parent buProcess from buProcess]";
  public static final String QUERY_GET_PARENT_ORGPROCESS_FROM_ORG_PROCESS        = "~TVi()Z0aGjX6[RCSA Get Parent orgProcess from OrgProcess]";
  public static final String QUERY_GET_PARENT_BUPROCESS_FROM_ORG_PROCESS         = "~4Si(Fd0aG5d6[RCSA GET_PARENT_BUPROCESS_FROM_ORG_PROCESS]";
  public static final String QUERY_GET_PARENT_ORGUNIT_FROM_ORGUNIT               = "~ZMI(wg)aGD(2[RCSA Get parent orgUnit from Org Unit]";
  public static final String QUERY_GET_PARENT_RISK_TYPE_FROM_RISK_TYPE           = "~CL3RYU7sFnnJ[Get Parent Risk Type From Risk Type]";
  public static final String QUERY_GET_PARENT_BUSINESS_LINE_FROM_BUSINESS_LINE   = "~XPPZdCw)HbwU[LDC - Get Parents Business lines from Business line]";
  public static final String QUERY_GET_ENTITIES_FROM_BUSINESS_PROCESS            = "~aCCdjxf4Gvh6[Get Org-units From Business Process]";
  public static final String QUERY_GET_ENTITIES_FROM_ORGANIZATIONAL_PROCESS      = "~CCCdH5g4Gvp6[Get Org-Units From Organizational Process]";
  public static final String QUERY_GET_BUSINESS_LINE_FIRST_LEVEL                 = "~cfhP(3fSI9eS[LDC - Get Business Line First Level]";
  public static final String QUERY_GET_RISK_TYPE_FIRST_LEVEL                     = "~4ghPw6fSIDkS[LDC - Get Risk Type First Level]";
  public static final String QUERY_GET_RISKS_FROM_RISK_ELEMENT                   = "~CRF)mZGWIzDC[LDC - Get Risks From Element At Risk]";
  public static final String QUERY_GET_GROSS_INCOME_FROM_BUSINESS_LINE_TREE      = "~8PoYQiDXILx5[LDC - Get Gross Income From Business line and its Children]";
  public static final String PARENT_BuProcess_QUERY                              = "~mRGKwQbAGbjD[Get Parent BuProcess query]";
  public static final String PARENT_org_QUERY                                    = "~zQGKj8bAGTZD[Get parent org-unit query]";
  public static final String QUERY_GET_RISKS_FROM_OWNING_ENTITY                  = "~zQGKj8bAGTZD[Get parent org-unit query]";

  /*Others*/
  public static final String DATA_LABEL_PERCENT                                  = "{percent|0}%";
  public static final String MEGA_FORMAT                                         = "yyyy/MM/dd";

  /*Colors*/
  public static final String V_HEADER_COLOR                                      = "E8E6DE";
}
