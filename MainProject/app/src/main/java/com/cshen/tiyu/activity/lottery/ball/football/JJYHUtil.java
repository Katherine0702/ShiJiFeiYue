package com.cshen.tiyu.activity.lottery.ball.football;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.cshen.tiyu.domain.ball.FootBallMatch;
import com.cshen.tiyu.domain.ball.JCZQJJYHTOUZHU;
import com.cshen.tiyu.domain.ball.JJYHEach;
import com.cshen.tiyu.domain.ball.JczqJJYHChoosedScroeBean;
import com.cshen.tiyu.domain.ball.Match;
import com.cshen.tiyu.domain.ball.ScroeBean;
import com.google.common.collect.Lists;

public class JJYHUtil {
	final static int PINGJUNYOUHUA = 1;
	final static int BOREYOUHUA = 2;
	final static int BOLENGYOUHUA = 3;
	private static JJYHUtil jjyhUtil;
	private static java.text.NumberFormat nf ;
	public static JJYHUtil getJJYHUtil() {
		if (jjyhUtil == null) {
			nf  = java.text.NumberFormat.getInstance();   
			nf.setGroupingUsed(false);
			jjyhUtil = new JJYHUtil();
		}
		return jjyhUtil;
	}
	public ArrayList<JJYHEach> getJJYHEachList(int chuanfa,ArrayList<Match> matchs){
		ArrayList<JJYHEach> jjyhList = new ArrayList<JJYHEach>();
		switch(chuanfa){
		case 1://单关
		case 2:
			for(int i = 0;i<matchs.size();i++){
				String teamNameMother = matchs.get(i).getHomeTeamName();//某场比赛选中的名字
				String matchKeyMother = matchs.get(i).getMatchKey();
				ArrayList<ScroeBean> checkedMother = matchs.get(i).getCheckNumber();//某场比赛选中的选项list
				for(int j = 0;j<checkedMother.size();j++){//某个选项

					for(int m = 0;m<matchs.size();m++){//另一场比赛
						if(m>i){
							String teamNameChilld = matchs.get(m).getHomeTeamName();//另一场比赛选中的名字
							String matchKeyChilld = matchs.get(m).getMatchKey();//另一场比赛选中的名字
							ArrayList<ScroeBean> checkedChilld = matchs.get(m).getCheckNumber();//另一场比赛选中的选项list
							for(int n = 0;n<checkedChilld.size();n++){//某个选项

								JJYHEach jjyheach = new JJYHEach();//A//C	
								jjyheach.getMatchKey().add(matchKeyMother);
								jjyheach.getChecks().add(checkedMother.get(j));
								jjyheach.getMatchKey().add(matchKeyChilld);
								jjyheach.getChecks().add(checkedChilld.get(n));
								jjyheach.setNames(
										teamNameMother+"="
												+JCZQUtil.chooseScroeName(checkedMother.get(j).getKey()).replace(";", "")
												+"["+checkedMother.get(j).getValue()+"],"
												+teamNameChilld+"="
												+JCZQUtil.chooseScroeName(checkedChilld.get(n).getKey()).replace(";", "")
												+"["+checkedChilld.get(n).getValue()+"]");


								jjyheach.setJiangjinDouble(in4remove6double5((double)(Math.round(2*
										Double.parseDouble(checkedMother.get(j).getValue())
										*Double.parseDouble(checkedChilld.get(n).getValue())*10000)/10000.0),2));

								jjyhList.add(jjyheach);
							}
						}
					}
				}
			}

			break;
		case 3:
			for(int i = 0;i<matchs.size();i++){
				String teamNameMother = matchs.get(i).getHomeTeamName();//某场比赛选中的名字
				String matchKeyMother = matchs.get(i).getMatchKey();//另一场比赛选中的名字
				ArrayList<ScroeBean> checkedMother = matchs.get(i).getCheckNumber();//某场比赛选中的选项list
				for(int j = 0;j<checkedMother.size();j++){//某个选项

					for(int m = 0;m<matchs.size();m++){//另一场比赛
						if(m>i){
							String teamNameChilld = matchs.get(m).getHomeTeamName();//另一场比赛选中的名字
							String matchKeyChilld = matchs.get(m).getMatchKey();//另一场比赛选中的名字
							ArrayList<ScroeBean> checkedChilld = matchs.get(m).getCheckNumber();//另一场比赛选中的选项list
							for(int n = 0;n<checkedChilld.size();n++){//某个选项

								for(int p = 0;p<matchs.size();p++){//另一场比赛
									if(p>m){
										String teamNameChilld2 = matchs.get(p).getHomeTeamName();//另一场比赛选中的名字
										String matchKeyChilld2 = matchs.get(p).getMatchKey();//另一场比赛选中的名字
										ArrayList<ScroeBean> checkedChilld2 = matchs.get(p).getCheckNumber();//另一场比赛选中的选项list
										for(int q = 0;q<checkedChilld2.size();q++){//某个选项

											JJYHEach jjyheach = new JJYHEach();//A//C	


											jjyheach.getMatchKey().add(matchKeyMother);
											jjyheach.getChecks().add(checkedMother.get(j));
											jjyheach.getMatchKey().add(matchKeyChilld);
											jjyheach.getChecks().add(checkedChilld.get(n));
											jjyheach.getMatchKey().add(matchKeyChilld2);
											jjyheach.getChecks().add(checkedChilld2.get(q));
											JCZQUtil.getJCZQUtil();
											jjyheach.setNames(
													teamNameMother+"="
															+JCZQUtil.chooseScroeName(checkedMother.get(j).getKey()).replace(";", "")
															+"["+checkedMother.get(j).getValue()+"],"
															+teamNameChilld+"="
															+JCZQUtil.chooseScroeName(checkedChilld.get(n).getKey()).replace(";", "")
															+"["+checkedChilld.get(n).getValue()+"],"
															+teamNameChilld2+"="
															+JCZQUtil.chooseScroeName(checkedChilld2.get(q).getKey()).replace(";", "")
															+"["+checkedChilld2.get(q).getValue()+"]");
											jjyheach.setJiangjinDouble(in4remove6double5((double)(Math.round(2*
													Double.parseDouble(checkedMother.get(j).getValue())
													*Double.parseDouble(checkedChilld.get(n).getValue())
													*Double.parseDouble(checkedChilld2.get(q).getValue())*10000)/10000.0),2));
											jjyhList.add(jjyheach);
										}
									}
								}
							}
						}
					}
				}
			}
			break;
		case 4:
			for(int i = 0;i<matchs.size();i++){
				String teamNameMother = matchs.get(i).getHomeTeamName();//某场比赛选中的名字
				String matchKeyMother = matchs.get(i).getMatchKey();//另一场比赛选中的名字
				ArrayList<ScroeBean> checkedMother = matchs.get(i).getCheckNumber();//某场比赛选中的选项list
				for(int j = 0;j<checkedMother.size();j++){//某个选项

					for(int m = 0;m<matchs.size();m++){//另一场比赛
						if(m>i){
							String teamNameChilld = matchs.get(m).getHomeTeamName();//另一场比赛选中的名字
							String matchKeyChilld = matchs.get(m).getMatchKey();//另一场比赛选中的名字
							ArrayList<ScroeBean> checkedChilld = matchs.get(m).getCheckNumber();//另一场比赛选中的选项list
							for(int n = 0;n<checkedChilld.size();n++){//某个选项

								for(int p = 0;p<matchs.size();p++){//另一场比赛
									if(p>m){
										String teamNameChilld2 = matchs.get(p).getHomeTeamName();//另一场比赛选中的名字
										String matchKeyChilld2 = matchs.get(p).getMatchKey();//另一场比赛选中的名字
										ArrayList<ScroeBean> checkedChilld2 = matchs.get(p).getCheckNumber();//另一场比赛选中的选项list
										for(int q = 0;q<checkedChilld2.size();q++){//某个选项

											for(int x = 0;x<matchs.size();x++){//另一场比赛
												if(x>p){
													String teamNameChilld3 = matchs.get(x).getHomeTeamName();//另一场比赛选中的名字
													String matchKeyChilld3 = matchs.get(x).getMatchKey();//另一场比赛选中的名字
													ArrayList<ScroeBean> checkedChilld3 = matchs.get(x).getCheckNumber();//另一场比赛选中的选项list
													for(int y = 0;y<checkedChilld3.size();y++){//某个选项

														JJYHEach jjyheach = new JJYHEach();//A//C	
														jjyheach.getMatchKey().add(matchKeyMother);
														jjyheach.getChecks().add(checkedMother.get(j));
														jjyheach.getMatchKey().add(matchKeyChilld);
														jjyheach.getChecks().add(checkedChilld.get(n));
														jjyheach.getMatchKey().add(matchKeyChilld2);
														jjyheach.getChecks().add(checkedChilld2.get(q));
														jjyheach.getMatchKey().add(matchKeyChilld3);
														jjyheach.getChecks().add(checkedChilld3.get(y));
														JCZQUtil.getJCZQUtil();
														jjyheach.setNames(
																teamNameMother+"="
																		+JCZQUtil.chooseScroeName(checkedMother.get(j).getKey()).replace(";", "")
																		+"["+checkedMother.get(j).getValue()+"],"
																		+teamNameChilld+"="
																		+JCZQUtil.chooseScroeName(checkedChilld.get(n).getKey()).replace(";", "")
																		+"["+checkedChilld.get(n).getValue()+"],"
																		+teamNameChilld2+"="
																		+JCZQUtil.chooseScroeName(checkedChilld2.get(q).getKey()).replace(";", "")
																		+"["+checkedChilld2.get(q).getValue()+"],"
																		+teamNameChilld3+"="
																		+JCZQUtil.chooseScroeName(checkedChilld3.get(y).getKey()).replace(";", "")
																		+"["+checkedChilld3.get(y).getValue()+"]");
														jjyheach.setJiangjinDouble(in4remove6double5((double)(Math.round(2*
																Double.parseDouble(checkedMother.get(j).getValue())
																*Double.parseDouble(checkedChilld.get(n).getValue())
																*Double.parseDouble(checkedChilld2.get(q).getValue())
																*Double.parseDouble(checkedChilld3.get(y).getValue())*10000)/10000.0),2));
														jjyhList.add(jjyheach);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			break;
		case 5:
			for(int i = 0;i<matchs.size();i++){
				String teamNameMother = matchs.get(i).getHomeTeamName();//某场比赛选中的名字
				String matchKeyMother = matchs.get(i).getMatchKey();//另一场比赛选中的名字
				ArrayList<ScroeBean> checkedMother = matchs.get(i).getCheckNumber();//某场比赛选中的选项list
				for(int j = 0;j<checkedMother.size();j++){//某个选项

					for(int m = 0;m<matchs.size();m++){//另一场比赛
						if(m>i){
							String teamNameChilld = matchs.get(m).getHomeTeamName();//另一场比赛选中的名字
							String matchKeyChilld = matchs.get(m).getMatchKey();//另一场比赛选中的名字
							ArrayList<ScroeBean> checkedChilld = matchs.get(m).getCheckNumber();//另一场比赛选中的选项list
							for(int n = 0;n<checkedChilld.size();n++){//某个选项

								for(int p = 0;p<matchs.size();p++){//另一场比赛
									if(p>m){
										String teamNameChilld2 = matchs.get(p).getHomeTeamName();//另一场比赛选中的名字
										String matchKeyChilld2 = matchs.get(p).getMatchKey();//另一场比赛选中的名字
										ArrayList<ScroeBean> checkedChilld2 = matchs.get(p).getCheckNumber();//另一场比赛选中的选项list
										for(int q = 0;q<checkedChilld2.size();q++){//某个选项

											for(int x = 0;x<matchs.size();x++){//另一场比赛
												if(x>p){
													String teamNameChilld3 = matchs.get(x).getHomeTeamName();//另一场比赛选中的名字
													String matchKeyChilld3 = matchs.get(x).getMatchKey();//另一场比赛选中的名字
													ArrayList<ScroeBean> checkedChilld3 = matchs.get(x).getCheckNumber();//另一场比赛选中的选项list
													for(int y = 0;y<checkedChilld3.size();y++){//某个选项

														for(int a = 0;a<matchs.size();a++){//另一场比赛
															if(a>x){
																String teamNameChilld4 = matchs.get(a).getHomeTeamName();//另一场比赛选中的名字
																String matchKeyChilld4 = matchs.get(a).getMatchKey();//另一场比赛选中的名字
																ArrayList<ScroeBean> checkedChilld4 = matchs.get(a).getCheckNumber();//另一场比赛选中的选项list
																for(int b = 0;b<checkedChilld4.size();b++){//某个选项



																	JJYHEach jjyheach = new JJYHEach();//A//C
																	jjyheach.getMatchKey().add(matchKeyMother);
																	jjyheach.getChecks().add(checkedMother.get(j));
																	jjyheach.getMatchKey().add(matchKeyChilld);
																	jjyheach.getChecks().add(checkedChilld.get(n));
																	jjyheach.getMatchKey().add(matchKeyChilld2);
																	jjyheach.getChecks().add(checkedChilld2.get(q));
																	jjyheach.getMatchKey().add(matchKeyChilld3);
																	jjyheach.getChecks().add(checkedChilld3.get(y));
																	jjyheach.getMatchKey().add(matchKeyChilld4);
																	jjyheach.getChecks().add(checkedChilld4.get(b));
																	JCZQUtil.getJCZQUtil();
																	jjyheach.setNames(
																			teamNameMother+"="
																					+JCZQUtil.chooseScroeName(checkedMother.get(j).getKey()).replace(";", "")
																					+"["+checkedMother.get(j).getValue()+"],"
																					+teamNameChilld+"="
																					+JCZQUtil.chooseScroeName(checkedChilld.get(n).getKey()).replace(";", "")
																					+"["+checkedChilld.get(n).getValue()+"],"
																					+teamNameChilld2+"="
																					+JCZQUtil.chooseScroeName(checkedChilld2.get(q).getKey()).replace(";", "")
																					+"["+checkedChilld2.get(q).getValue()+"],"
																					+teamNameChilld3+"="
																					+JCZQUtil.chooseScroeName(checkedChilld3.get(y).getKey()).replace(";", "")
																					+"["+checkedChilld3.get(y).getValue()+"],"
																					+teamNameChilld4+"="
																					+JCZQUtil.chooseScroeName(checkedChilld4.get(b).getKey()).replace(";", "")
																					+"["+checkedChilld4.get(b).getValue()+"]");
																	jjyheach.setJiangjinDouble(in4remove6double5((double)(Math.round(2*
																			Double.parseDouble(checkedMother.get(j).getValue())
																			*Double.parseDouble(checkedChilld.get(n).getValue())
																			*Double.parseDouble(checkedChilld2.get(q).getValue())
																			*Double.parseDouble(checkedChilld3.get(y).getValue())
																			*Double.parseDouble(checkedChilld4.get(b).getValue())*10000)/10000.0),2));
																	jjyhList.add(jjyheach);
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			break;
		case 6:
			for(int i = 0;i<matchs.size();i++){
				String teamNameMother = matchs.get(i).getHomeTeamName();//某场比赛选中的名字
				String matchKeyMother = matchs.get(i).getMatchKey();//另一场比赛选中的名字
				ArrayList<ScroeBean> checkedMother = matchs.get(i).getCheckNumber();//某场比赛选中的选项list
				for(int j = 0;j<checkedMother.size();j++){//某个选项

					for(int m = 0;m<matchs.size();m++){//另一场比赛
						if(m>i){
							String teamNameChilld = matchs.get(m).getHomeTeamName();//另一场比赛选中的名字
							String matchKeyChilld = matchs.get(m).getMatchKey();//另一场比赛选中的名字
							ArrayList<ScroeBean> checkedChilld = matchs.get(m).getCheckNumber();//另一场比赛选中的选项list
							for(int n = 0;n<checkedChilld.size();n++){//某个选项

								for(int p = 0;p<matchs.size();p++){//另一场比赛
									if(p>m){
										String teamNameChilld2 = matchs.get(p).getHomeTeamName();//另一场比赛选中的名字
										String matchKeyChilld2 = matchs.get(p).getMatchKey();//另一场比赛选中的名字
										ArrayList<ScroeBean> checkedChilld2 = matchs.get(p).getCheckNumber();//另一场比赛选中的选项list
										for(int q = 0;q<checkedChilld2.size();q++){//某个选项

											for(int x = 0;x<matchs.size();x++){//另一场比赛
												if(x>p){
													String teamNameChilld3 = matchs.get(x).getHomeTeamName();//另一场比赛选中的名字
													String matchKeyChilld3 = matchs.get(x).getMatchKey();//另一场比赛选中的名字
													ArrayList<ScroeBean> checkedChilld3 = matchs.get(x).getCheckNumber();//另一场比赛选中的选项list
													for(int y = 0;y<checkedChilld3.size();y++){//某个选项

														for(int a = 0;a<matchs.size();a++){//另一场比赛
															if(a>x){
																String teamNameChilld4 = matchs.get(a).getHomeTeamName();//另一场比赛选中的名字
																String matchKeyChilld4 = matchs.get(a).getMatchKey();//另一场比赛选中的名字
																ArrayList<ScroeBean> checkedChilld4 = matchs.get(a).getCheckNumber();//另一场比赛选中的选项list
																for(int b = 0;b<checkedChilld4.size();b++){//某个选项

																	for(int c = 0;c<matchs.size();c++){//另一场比赛
																		if(c>a){
																			String teamNameChilld5 = matchs.get(c).getHomeTeamName();//另一场比赛选中的名字
																			String matchKeyChilld5 = matchs.get(c).getMatchKey();//另一场比赛选中的名字
																			ArrayList<ScroeBean> checkedChilld5 = matchs.get(c).getCheckNumber();//另一场比赛选中的选项list
																			for(int d = 0;d<checkedChilld5.size();d++){//某个选项


																				JJYHEach jjyheach = new JJYHEach();//A//C	
																				jjyheach.getMatchKey().add(matchKeyMother);
																				jjyheach.getChecks().add(checkedMother.get(j));
																				jjyheach.getMatchKey().add(matchKeyChilld);
																				jjyheach.getChecks().add(checkedChilld.get(n));
																				jjyheach.getMatchKey().add(matchKeyChilld2);
																				jjyheach.getChecks().add(checkedChilld2.get(q));
																				jjyheach.getMatchKey().add(matchKeyChilld3);
																				jjyheach.getChecks().add(checkedChilld3.get(y));
																				jjyheach.getMatchKey().add(matchKeyChilld4);
																				jjyheach.getChecks().add(checkedChilld4.get(b));
																				jjyheach.getMatchKey().add(matchKeyChilld5);
																				jjyheach.getChecks().add(checkedChilld5.get(d));		
																				JCZQUtil.getJCZQUtil();
																				jjyheach.setNames(
																						teamNameMother+"="
																								+JCZQUtil.chooseScroeName(checkedMother.get(j).getKey()).replace(";", "")
																								+"["+checkedMother.get(j).getValue()+"],"
																								+teamNameChilld+"="
																								+JCZQUtil.chooseScroeName(checkedChilld.get(n).getKey()).replace(";", "")
																								+"["+checkedChilld.get(n).getValue()+"],"
																								+teamNameChilld2+"="
																								+JCZQUtil.chooseScroeName(checkedChilld2.get(q).getKey()).replace(";", "")
																								+"["+checkedChilld2.get(q).getValue()+"],"
																								+teamNameChilld3+"="
																								+JCZQUtil.chooseScroeName(checkedChilld3.get(y).getKey()).replace(";", "")
																								+"["+checkedChilld3.get(y).getValue()+"],"
																								+teamNameChilld4+"="
																								+JCZQUtil.chooseScroeName(checkedChilld4.get(b).getKey()).replace(";", "")
																								+"["+checkedChilld4.get(b).getValue()+"],"
																								+teamNameChilld5+"="
																								+JCZQUtil.chooseScroeName(checkedChilld5.get(d).getKey()).replace(";", "")
																								+"["+checkedChilld5.get(d).getValue()+"]");
																				jjyheach.setJiangjinDouble(in4remove6double5((double)(Math.round(2*
																						Double.parseDouble(checkedMother.get(j).getValue())
																						*Double.parseDouble(checkedChilld.get(n).getValue())
																						*Double.parseDouble(checkedChilld2.get(q).getValue())
																						*Double.parseDouble(checkedChilld3.get(y).getValue())
																						*Double.parseDouble(checkedChilld4.get(b).getValue())
																						*Double.parseDouble(checkedChilld5.get(d).getValue())*10000)/10000.0),2));
																				jjyhList.add(jjyheach);
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			break;
		case 7:
			for(int i = 0;i<matchs.size();i++){
				String teamNameMother = matchs.get(i).getHomeTeamName();//某场比赛选中的名字
				String matchKeyMother = matchs.get(i).getMatchKey();//另一场比赛选中的名字
				ArrayList<ScroeBean> checkedMother = matchs.get(i).getCheckNumber();//某场比赛选中的选项list
				for(int j = 0;j<checkedMother.size();j++){//某个选项

					for(int m = 0;m<matchs.size();m++){//另一场比赛
						if(m>i){
							String teamNameChilld = matchs.get(m).getHomeTeamName();//另一场比赛选中的名字
							String matchKeyChilld = matchs.get(m).getMatchKey();//另一场比赛选中的名字
							ArrayList<ScroeBean> checkedChilld = matchs.get(m).getCheckNumber();//另一场比赛选中的选项list
							for(int n = 0;n<checkedChilld.size();n++){//某个选项

								for(int p = 0;p<matchs.size();p++){//另一场比赛
									if(p>m){
										String teamNameChilld2 = matchs.get(p).getHomeTeamName();//另一场比赛选中的名字
										String matchKeyChilld2 = matchs.get(p).getMatchKey();//另一场比赛选中的名字
										ArrayList<ScroeBean> checkedChilld2 = matchs.get(p).getCheckNumber();//另一场比赛选中的选项list
										for(int q = 0;q<checkedChilld2.size();q++){//某个选项

											for(int x = 0;x<matchs.size();x++){//另一场比赛
												if(x>p){
													String teamNameChilld3 = matchs.get(x).getHomeTeamName();//另一场比赛选中的名字
													String matchKeyChilld3 = matchs.get(x).getMatchKey();//另一场比赛选中的名字
													ArrayList<ScroeBean> checkedChilld3 = matchs.get(x).getCheckNumber();//另一场比赛选中的选项list
													for(int y = 0;y<checkedChilld3.size();y++){//某个选项

														for(int a = 0;a<matchs.size();a++){//另一场比赛
															if(a>x){
																String teamNameChilld4 = matchs.get(a).getHomeTeamName();//另一场比赛选中的名字
																String matchKeyChilld4 = matchs.get(a).getMatchKey();//另一场比赛选中的名字
																ArrayList<ScroeBean> checkedChilld4 = matchs.get(a).getCheckNumber();//另一场比赛选中的选项list
																for(int b = 0;b<checkedChilld4.size();b++){//某个选项

																	for(int c = 0;c<matchs.size();c++){//另一场比赛
																		if(c>a){
																			String teamNameChilld5 = matchs.get(c).getHomeTeamName();//另一场比赛选中的名字
																			String matchKeyChilld5 = matchs.get(c).getMatchKey();//另一场比赛选中的名字
																			ArrayList<ScroeBean> checkedChilld5 = matchs.get(c).getCheckNumber();//另一场比赛选中的选项list
																			for(int d = 0;d<checkedChilld5.size();d++){//某个选项

																				for(int e = 0;e<matchs.size();e++){//另一场比赛
																					if(e>c){
																						String teamNameChilld6 = matchs.get(e).getHomeTeamName();//另一场比赛选中的名字
																						String matchKeyChilld6 = matchs.get(e).getMatchKey();//另一场比赛选中的名字
																						ArrayList<ScroeBean> checkedChilld6 = matchs.get(e).getCheckNumber();//另一场比赛选中的选项list
																						for(int f = 0;f<checkedChilld6.size();f++){//某个选项

																							JJYHEach jjyheach = new JJYHEach();//A//C	
																							jjyheach.getMatchKey().add(matchKeyMother);
																							jjyheach.getChecks().add(checkedMother.get(j));
																							jjyheach.getMatchKey().add(matchKeyChilld);
																							jjyheach.getChecks().add(checkedChilld.get(n));
																							jjyheach.getMatchKey().add(matchKeyChilld2);
																							jjyheach.getChecks().add(checkedChilld2.get(q));
																							jjyheach.getMatchKey().add(matchKeyChilld3);
																							jjyheach.getChecks().add(checkedChilld3.get(y));
																							jjyheach.getMatchKey().add(matchKeyChilld4);
																							jjyheach.getChecks().add(checkedChilld4.get(b));
																							jjyheach.getMatchKey().add(matchKeyChilld5);
																							jjyheach.getChecks().add(checkedChilld5.get(d));
																							jjyheach.getMatchKey().add(matchKeyChilld6);
																							jjyheach.getChecks().add(checkedChilld6.get(f));	
																							JCZQUtil.getJCZQUtil();
																							jjyheach.setNames(
																									teamNameMother+"="
																											+JCZQUtil.chooseScroeName(checkedMother.get(j).getKey()).replace(";", "")
																											+"["+checkedMother.get(j).getValue()+"],"
																											+teamNameChilld+"="
																											+JCZQUtil.chooseScroeName(checkedChilld.get(n).getKey()).replace(";", "")
																											+"["+checkedChilld.get(n).getValue()+"],"
																											+teamNameChilld2+"="
																											+JCZQUtil.chooseScroeName(checkedChilld2.get(q).getKey()).replace(";", "")
																											+"["+checkedChilld2.get(q).getValue()+"],"
																											+teamNameChilld3+"="
																											+JCZQUtil.chooseScroeName(checkedChilld3.get(y).getKey()).replace(";", "")
																											+"["+checkedChilld3.get(y).getValue()+"],"
																											+teamNameChilld4+"="
																											+JCZQUtil.chooseScroeName(checkedChilld4.get(b).getKey()).replace(";", "")
																											+"["+checkedChilld4.get(b).getValue()+"],"
																											+teamNameChilld5+"="
																											+JCZQUtil.chooseScroeName(checkedChilld5.get(d).getKey()).replace(";", "")
																											+"["+checkedChilld5.get(d).getValue()+"],"
																											+teamNameChilld6+"="
																											+JCZQUtil.chooseScroeName(checkedChilld6.get(f).getKey()).replace(";", "")
																											+"["+checkedChilld6.get(f).getValue()+"]");
																							jjyheach.setJiangjinDouble(in4remove6double5((double)(Math.round(2*
																									Double.parseDouble(checkedMother.get(j).getValue())
																									*Double.parseDouble(checkedChilld.get(n).getValue())
																									*Double.parseDouble(checkedChilld2.get(q).getValue())
																									*Double.parseDouble(checkedChilld3.get(y).getValue())
																									*Double.parseDouble(checkedChilld4.get(b).getValue())
																									*Double.parseDouble(checkedChilld5.get(d).getValue())
																									*Double.parseDouble(checkedChilld6.get(f).getValue())*10000)/10000.0),2));
																							jjyhList.add(jjyheach);
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			break;
		case 8:
			for(int i = 0;i<matchs.size();i++){
				String teamNameMother = matchs.get(i).getHomeTeamName();//某场比赛选中的名字
				String matchKeyMother = matchs.get(i).getMatchKey();//另一场比赛选中的名字
				ArrayList<ScroeBean> checkedMother = matchs.get(i).getCheckNumber();//某场比赛选中的选项list
				for(int j = 0;j<checkedMother.size();j++){//某个选项

					for(int m = 0;m<matchs.size();m++){//另一场比赛
						if(m>i){
							String teamNameChilld = matchs.get(m).getHomeTeamName();//另一场比赛选中的名字
							String matchKeyChilld = matchs.get(m).getMatchKey();//另一场比赛选中的名字
							ArrayList<ScroeBean> checkedChilld = matchs.get(m).getCheckNumber();//另一场比赛选中的选项list
							for(int n = 0;n<checkedChilld.size();n++){//某个选项

								for(int p = 0;p<matchs.size();p++){//另一场比赛
									if(p>m){
										String teamNameChilld2 = matchs.get(p).getHomeTeamName();//另一场比赛选中的名字
										String matchKeyChilld2 = matchs.get(p).getMatchKey();//另一场比赛选中的名字
										ArrayList<ScroeBean> checkedChilld2 = matchs.get(p).getCheckNumber();//另一场比赛选中的选项list
										for(int q = 0;q<checkedChilld2.size();q++){//某个选项

											for(int x = 0;x<matchs.size();x++){//另一场比赛
												if(x>p){
													String teamNameChilld3 = matchs.get(x).getHomeTeamName();//另一场比赛选中的名字
													String matchKeyChilld3 = matchs.get(x).getMatchKey();//另一场比赛选中的名字
													ArrayList<ScroeBean> checkedChilld3 = matchs.get(x).getCheckNumber();//另一场比赛选中的选项list
													for(int y = 0;y<checkedChilld3.size();y++){//某个选项

														for(int a = 0;a<matchs.size();a++){//另一场比赛
															if(a>x){
																String teamNameChilld4 = matchs.get(a).getHomeTeamName();//另一场比赛选中的名字
																String matchKeyChilld4 = matchs.get(a).getMatchKey();//另一场比赛选中的名字
																ArrayList<ScroeBean> checkedChilld4 = matchs.get(a).getCheckNumber();//另一场比赛选中的选项list
																for(int b = 0;b<checkedChilld4.size();b++){//某个选项

																	for(int c = 0;c<matchs.size();c++){//另一场比赛
																		if(c>a){
																			String teamNameChilld5 = matchs.get(c).getHomeTeamName();//另一场比赛选中的名字
																			String matchKeyChilld5 = matchs.get(c).getMatchKey();//另一场比赛选中的名字
																			ArrayList<ScroeBean> checkedChilld5 = matchs.get(c).getCheckNumber();//另一场比赛选中的选项list
																			for(int d = 0;d<checkedChilld5.size();d++){//某个选项

																				for(int e = 0;e<matchs.size();e++){//另一场比赛
																					if(e>c){
																						String teamNameChilld6 = matchs.get(e).getHomeTeamName();//另一场比赛选中的名字
																						String matchKeyChilld6 = matchs.get(e).getMatchKey();//另一场比赛选中的名字
																						ArrayList<ScroeBean> checkedChilld6 = matchs.get(e).getCheckNumber();//另一场比赛选中的选项list
																						for(int f = 0;f<checkedChilld6.size();f++){//某个选项

																							for(int g = 0;g<matchs.size();g++){//另一场比赛
																								if(g>e){
																									String teamNameChilld7 = matchs.get(g).getHomeTeamName();//另一场比赛选中的名字
																									String matchKeyChilld7 = matchs.get(g).getMatchKey();//另一场比赛选中的名字
																									ArrayList<ScroeBean> checkedChilld7 = matchs.get(g).getCheckNumber();//另一场比赛选中的选项list
																									for(int h = 0;h<checkedChilld7.size();h++){//某个选项


																										JJYHEach jjyheach = new JJYHEach();//A//C	

																										jjyheach.getMatchKey().add(matchKeyMother);
																										jjyheach.getChecks().add(checkedMother.get(j));
																										jjyheach.getMatchKey().add(matchKeyChilld);
																										jjyheach.getChecks().add(checkedChilld.get(n));
																										jjyheach.getMatchKey().add(matchKeyChilld2);
																										jjyheach.getChecks().add(checkedChilld2.get(q));
																										jjyheach.getMatchKey().add(matchKeyChilld3);
																										jjyheach.getChecks().add(checkedChilld3.get(y));
																										jjyheach.getMatchKey().add(matchKeyChilld4);
																										jjyheach.getChecks().add(checkedChilld4.get(b));
																										jjyheach.getMatchKey().add(matchKeyChilld5);
																										jjyheach.getChecks().add(checkedChilld5.get(d));
																										jjyheach.getMatchKey().add(matchKeyChilld6);
																										jjyheach.getChecks().add(checkedChilld6.get(f));	
																										jjyheach.getMatchKey().add(matchKeyChilld7);
																										jjyheach.getChecks().add(checkedChilld7.get(h));
																										JCZQUtil.getJCZQUtil();
																										jjyheach.setNames(
																												teamNameMother+"="
																														+JCZQUtil.chooseScroeName(checkedMother.get(j).getKey()).replace(";", "")
																														+"["+checkedMother.get(j).getValue()+"],"
																														+teamNameChilld+"="
																														+JCZQUtil.chooseScroeName(checkedChilld.get(n).getKey()).replace(";", "")
																														+"["+checkedChilld.get(n).getValue()+"],"
																														+teamNameChilld2+"="
																														+JCZQUtil.chooseScroeName(checkedChilld2.get(q).getKey()).replace(";", "")
																														+"["+checkedChilld2.get(q).getValue()+"],"
																														+teamNameChilld3+"="
																														+JCZQUtil.chooseScroeName(checkedChilld3.get(y).getKey()).replace(";", "")
																														+"["+checkedChilld3.get(y).getValue()+"],"
																														+teamNameChilld4+"="
																														+JCZQUtil.chooseScroeName(checkedChilld4.get(b).getKey()).replace(";", "")
																														+"["+checkedChilld4.get(b).getValue()+"],"
																														+teamNameChilld5+"="
																														+JCZQUtil.chooseScroeName(checkedChilld5.get(d).getKey()).replace(";", "")
																														+"["+checkedChilld5.get(d).getValue()+"],"
																														+teamNameChilld6+"="
																														+JCZQUtil.chooseScroeName(checkedChilld6.get(f).getKey()).replace(";", "")
																														+"["+checkedChilld6.get(f).getValue()+"],"
																														+teamNameChilld7+"="
																														+JCZQUtil.chooseScroeName(checkedChilld7.get(h).getKey()).replace(";", "")
																														+"["+checkedChilld7.get(h).getValue()+"]");
																										jjyheach.setJiangjinDouble(in4remove6double5((double)(Math.round(2*
																												Double.parseDouble(checkedMother.get(j).getValue())
																												*Double.parseDouble(checkedChilld.get(n).getValue())
																												*Double.parseDouble(checkedChilld2.get(q).getValue())
																												*Double.parseDouble(checkedChilld3.get(y).getValue())
																												*Double.parseDouble(checkedChilld4.get(b).getValue())
																												*Double.parseDouble(checkedChilld5.get(d).getValue())
																												*Double.parseDouble(checkedChilld6.get(f).getValue())
																												*Double.parseDouble(checkedChilld7.get(h).getValue())*10000)/10000.0),2));
																										jjyhList.add(jjyheach);
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			break;
		}
		return jjyhList;
	}
	public boolean mian(ArrayList<JJYHEach> list,int m,int YOUHUA) {
		boolean noFuShu = true;
		Collections.sort(list, new SortJiangJinDouble());
		if(list.size()==m){
			for(JJYHEach item : list){
				item.setBeishu(1);
			}
			getJiangjinResult(list);
			return  noFuShu;
		}

		int totalMoney = m*2;
		switch(YOUHUA){
		case PINGJUNYOUHUA:
			Double baseSp = list.get(0).getJiangjinDouble();
			Double percentC = 0.0;
			Double percent = 0.0;
			List<Double> percents = Lists.newArrayList();
			for(JJYHEach item : list){
				percent = baseSp/item.getJiangjinDouble();
				percentC+=percent;
				percents.add(percent);
			}			
			Double basePercent = m/percentC;
			int baseMultiple = (int) Math.round(basePercent);
			list.get(0).setBeishu(baseMultiple==0?1:baseMultiple);
			list.get(0).setBeishuDouble(basePercent*percents.get(0));
			int multiple = 0;
			for(int i=1;i<list.size();i++){

				multiple = (int) Math.round(basePercent*percents.get(i));
				list.get(i).setBeishuDouble(basePercent*percents.get(i));
				if(multiple==0){
					list.get(i).setBeishu(1);
				}else{
					list.get(i).setBeishu(multiple);
				}
			}
			Collections.sort(list, new SortBeishuDouble());
			int realMultiple = 0;
			for(JJYHEach item : list){
				realMultiple+=item.getBeishu();
			}
			if(m!=realMultiple){
				list =this.has0State(list, realMultiple-m);
			}
			Collections.sort(list, new SortBeishuDouble());	
			break;
		case BOREYOUHUA:
			ArrayList<Integer> beishu = new ArrayList<Integer>();
			for(int i=list.size()-1;i>0;i--){
				Double cost = list.get(i).getJiangjinDouble();
				int multiple_bs = (int)Math.ceil(totalMoney/cost);
				if(multiple_bs<=0){
					noFuShu = false;
					break;
				}
				beishu.add(multiple_bs);
				m -= multiple_bs;
			}

			if(m<=0){
				noFuShu = false;
			}else{
				beishu.add(m);
			}
			if(noFuShu&&list.size() == beishu.size() ){
				for(int i =0;i<list.size();i++){
					list.get(i).setBeishu(beishu.get(list.size()-i-1));
				}
			}
			beishu.clear();
			break;
		case BOLENGYOUHUA:
			ArrayList<Integer> beishu2 = new ArrayList<Integer>();
			for(int i=0;i<list.size()-1;i++){
				Double cost = list.get(i).getJiangjinDouble();
				int multiple_bs = (int)Math.ceil(totalMoney/cost);
				if(multiple_bs<=0){
					noFuShu = false;
					break;
				}
				beishu2.add(multiple_bs);
				m -= multiple_bs;
			}
			if(m<=0){
				noFuShu = false;
			}else{
				beishu2.add(m);
			}
			if(noFuShu&&list.size() == beishu2.size() ){
				for(int i =0;i<list.size();i++){
					list.get(i).setBeishu(beishu2.get(i));
				}
			}
			beishu2.clear();
			break;
		}
		getJiangjinResult(list);
		return noFuShu;
	}
	public ArrayList<JJYHEach> getJiangjinResult(ArrayList<JJYHEach> list){
		for(JJYHEach item : list){
			item.setJiangjinEach(JJYHUtil.getJJYHUtil().in4remove6double5(item.getJiangjinDouble(), 2));
		}	
		return list;
	}
	/**
	 * 
	 * resultInfoArr[0] = contentItems;//方案内容数组集合
	resultInfoArr[1] = 0;//倍数(根据总投注金额动态优化)
	resultInfoArr[2] = bonus;//计算单注的赔率（即奖金）
	resultInfoArr[3] = TextUtils.join(valueArr,",");
	resultInfoArr[4] = TextUtils.join(playTypeArr,",");

	 */

	private ArrayList<JJYHEach> has0State(ArrayList<JJYHEach> resultList,int chazhi){
		int total = resultList.size();

		/**
		 * 可根据优化后的注数来定
		 * 如果注数大则将其设置小，否则可设置大些
		 * 根据实际情况来做调整
		 */
		int maxAdjustNum = 10;//精细调整的倍投数(大-精)
		if(total>5000){
			maxAdjustNum = 0;
		}else if(total>3000){
			maxAdjustNum = 3;
		}else if(total>2000){
			maxAdjustNum = 5;
		}

		int operIndex = 0;
		int operNum = 0;
		if(chazhi>0){
			operNum = -1;
			if(chazhi>maxAdjustNum){
				int dimOperNum = chazhi-maxAdjustNum;
				for(int i=0;i<dimOperNum;i++){
					if(resultList.get(i).getBeishu()>1){
						resultList.get(i).setBeishu(resultList.get(i).getBeishu()+operNum);
						chazhi--;
					}
				}
			}
			if(chazhi!=0){
				ArrayList<JJYHEach> compareList = new ArrayList<JJYHEach>();
				for(int i=0;i<total;i++){
					JJYHEach item = resultList.get(i);
					if(item.getBeishu()>1){
						compareList.add(item);
					}	
				}
				operIndex = findOperIndex(operNum,compareList);
				compareList.get(operIndex).setBeishu(compareList.get(operIndex).getBeishu()+operNum);
				chazhi=chazhi+operNum;
			}			
		}else{
			operNum = 1;
			if(chazhi+maxAdjustNum<0){
				int dimOperNum = Math.abs(chazhi+maxAdjustNum);
				for(int i=0;i<dimOperNum;i++){
					resultList.get(i).setBeishu(resultList.get(i).getBeishu()+operNum);
					chazhi++;
				}
			}
			if(chazhi!=0){
				operIndex = findOperIndex(operNum,resultList);
				resultList.get(operIndex).setBeishu(resultList.get(operIndex).getBeishu()+operNum);
				chazhi=chazhi+operNum;
			}
		}

		if(chazhi!=0){
			has0State(resultList,chazhi);
		}
		return resultList;
	}
	private int findOperIndex(int operNum,ArrayList<JJYHEach> compareList){
		int operIndex = 0;//操作的下标
		int total = compareList.size();
		List<Double[]> index_Sums = Lists.newArrayList();
		Double[] sums = null;
		for(int i=0;i<total;i++){
			sums = new Double[total];
			int j=0;
			for(JJYHEach item : compareList){
				if(i==j){
					sums[j] = ((Integer)item.getBeishu()+operNum)*(Double)item.getJiangjinDouble();
				}else{
					sums[j] = (Integer)item.getBeishu()*(Double)item.getJiangjinDouble();
				}
				j++;
			}
			index_Sums.add(sums);
		}
		//对计算结果查差值最小的就是需要填补的位置
		double mixNum = 0;
		double currNum = 0;
		for(int i=0;i<index_Sums.size();i++){				
			currNum = this.getMaxMin(index_Sums.get(i));
			if (mixNum==0){
				mixNum = currNum;
			}	
			if(mixNum >= currNum){ 
				mixNum = currNum;
				operIndex=i;
			}
		}
		return operIndex;
	}
	/**
	 * 获取数组的最大的差值
	 * @param nums
	 * @return
	 */
	private double getMaxMin(Double... nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		double max = nums[0];
		double min = nums[0];
		for (double d : nums) {
			if (d - max > 10e-6) {
				max = d;
			}
			if (min - d > 10e-6) {
				min = d;
			}
		}
		return (max - min);
	}
	class SortBeishuDouble implements Comparator {

		@Override
		public int compare(Object o1, Object o2) {
			// TODO Auto-generated method stub
			double s1 = ((JJYHEach)o1).getBeishuDouble();
			double s2 = ((JJYHEach)o2).getBeishuDouble();
			if (s1 > s2)
				return -1;
			return 1;
		}
	}class SortJiangJinDouble implements Comparator {

		@Override
		public int compare(Object o1, Object o2) {
			// TODO Auto-generated method stub
			double s1 = ((JJYHEach)o1).getJiangjinDouble();
			double s2 = ((JJYHEach)o2).getJiangjinDouble();
			if (s1 > s2)
				return 1;
			return -1;
		}
	}
	public  double  in4remove6double5(double number, int num) {//数据，保留位数:5前
		String result = "";
		String numberStr = nf.format(number);
		if(numberStr.contains(".")){
			int pos = numberStr.indexOf('.') + num+1;
			try{
				int key = Integer.parseInt(numberStr.substring(pos, pos+1));//保留两位小数时，取得第三位小数
				if (key < 5) {
					result = numberStr.substring(0, pos);
				} else if (key > 5) {
					numberStr = (number + 0.01)+"";
					result = numberStr.substring(0,pos);
				} else {
					if (Integer.parseInt(numberStr.substring(pos-1, pos))%2 == 0) {
						result = numberStr.substring(0, pos);
					} else {
						numberStr = (number + 0.01)+"";
						result = numberStr.substring(0,pos);
					}
				}
			}catch(Exception e){
				result = numberStr;
			}
			return Double.parseDouble(result);
		}else{
			return number;
		}
	}
	public JCZQJJYHTOUZHU getJJYHTOUZHU(ArrayList<Match> matchs,ArrayList<JJYHEach> jjyhList,String min,String max){
		JCZQJJYHTOUZHU jjyhtouzhu = new JCZQJJYHTOUZHU();
		jjyhtouzhu.setBestMinPrize(min);
		jjyhtouzhu.setBestMaxPrize(max);
		jjyhtouzhu.setHh(false);
		jjyhtouzhu.setOptimize(true);
		jjyhtouzhu.setItems(getItem(matchs));

		ArrayList<String> matchkey = getMatchKey(matchs);
		jjyhtouzhu.setMatchkeys(matchkey);

		HashMap<String, ArrayList<String>> jjyheachdata = getJJYHEach(matchs.size(),matchkey,jjyhList);

		jjyhtouzhu.setMultiples(getMultiples(jjyhList));

		ArrayList<String> playtypes = jjyheachdata.get("playTypes");
		jjyhtouzhu.setPlayTypes(playtypes);

		ArrayList<String> contents = jjyheachdata.get("contents");
		StringBuffer contentSB = new StringBuffer();
		for(String cotentbean:contents){
			contentSB.append(cotentbean.replace("[", "").replace("]", "")).append("\r\n");
		}
		jjyhtouzhu.setContent(contentSB.toString());

		return jjyhtouzhu;
	}
	public ArrayList<Integer> getMultiples(ArrayList<JJYHEach> jjyhList){
		ArrayList<Integer> multiples = new ArrayList<Integer>();
		for(JJYHEach jjyh:jjyhList){
			if(jjyh.getBeishu()>0){
				multiples.add(jjyh.getBeishu());
			}
		}
		return multiples;
	}
	public HashMap<String, ArrayList<String>> getJJYHEach(int num,ArrayList<String> matchkeys,ArrayList<JJYHEach> jjyhList){
		HashMap<String, ArrayList<String>> jjyheachdata = new HashMap<String, ArrayList<String>>();
		ArrayList<String> playtypes = new ArrayList<String>();
		ArrayList<String> contents = new ArrayList<String>();
		for(JJYHEach jjyh:jjyhList){
			if(jjyh.getBeishu()>0){
				String[] playTypes = new  String[num];
				String[] conTents = new  String[num];
				for(int i=0;i<num;i++){
					playTypes[i] = "#";
					conTents[i] = "#";
				}
				if(jjyh.getMatchKey().size() == jjyh.getChecks().size()){
					for(int i =0;i<jjyh.getMatchKey().size();i++){
						int index = matchkeys.indexOf(jjyh.getMatchKey().get(i));

						String playType = JCZQUtil.getJCZQUtil().getPlayTypeString(jjyh.getChecks().get(i).getKey());
						playTypes[index] = playType;

						String conTent = chooseScroeValue(jjyh.getChecks().get(i).getKey());
						conTents[index] =conTent;
					}
				}
				String playTypesStr = Arrays.toString(playTypes);
				if(playTypesStr.contains(" ")){
					playtypes.add(playTypesStr.replace(" ", "").replace("[", "").replace("]", ""));
				}
				String conTentsStr = Arrays.toString(conTents);
				if(conTentsStr.contains(" ")){
					contents.add(conTentsStr.replace(" ", ""));
				}	
			}

		}
		jjyheachdata.put("playTypes", playtypes);
		jjyheachdata.put("contents", contents);

		return jjyheachdata;
	}
	public  ArrayList<String> getMatchKey(ArrayList<Match> matchs){
		ArrayList<String> matchkey = new ArrayList<String>();
		for(Match match:matchs){
			matchkey.add(match.getMatchKey());
		}
		return matchkey;
	}
	public  ArrayList<JczqJJYHChoosedScroeBean> getItem(ArrayList<Match> matchs){
		ArrayList<JczqJJYHChoosedScroeBean> item = new ArrayList<JczqJJYHChoosedScroeBean>();
		for(int m = 0;m<matchs.size();m++){
			FootBallMatch match = (FootBallMatch) matchs.get(m);
			HashMap<String, char[]> chooseWanFa = chooseWanFaValue(match.getCheckNumber());
			String SPF = new String(chooseWanFa.get("SPF"));
			String RQSPF = new String(chooseWanFa.get("RQSPF"));
			String JQS = new String(chooseWanFa.get("JQS"));
			String BQC = new String(chooseWanFa.get("BQC"));
			String BF = new String(chooseWanFa.get("BF"));
			if(SPF.contains("1")){
				JczqJJYHChoosedScroeBean jjyhChooseBean = new JczqJJYHChoosedScroeBean();
				jjyhChooseBean.setDan(false);
				jjyhChooseBean.setMatchKey(match.getMatchKey());
				jjyhChooseBean.setPlayType("SPF");
				jjyhChooseBean.setValue(new BigInteger(new StringBuilder(SPF).reverse().toString(), 2).toString());

				jjyhChooseBean.getSps().add(match.getSp().getSPF().getWIN().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getSPF().getDRAW().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getSPF().getLOSE().getValue());
				SPF = "";
				if(jjyhChooseBean.getSps()!=null&&jjyhChooseBean.getSps().size()>0){
					item.add(jjyhChooseBean);
				}
			}
			if(RQSPF.contains("1")){
				JczqJJYHChoosedScroeBean jjyhChooseBean = new JczqJJYHChoosedScroeBean();
				jjyhChooseBean.setDan(false);

				jjyhChooseBean.setMatchKey(match.getMatchKey());
				jjyhChooseBean.setPlayType("RQSPF");
				jjyhChooseBean.setValue(new BigInteger(new StringBuilder(RQSPF).reverse().toString(), 2).toString());	
				jjyhChooseBean.getSps().add(match.getSp().getRQSPF().getWIN().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getRQSPF().getDRAW().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getRQSPF().getLOSE().getValue());
				RQSPF = "";

				if(jjyhChooseBean.getSps()!=null&&jjyhChooseBean.getSps().size()>0){
					item.add(jjyhChooseBean);
				}
			}
			if(JQS.contains("1")){
				JczqJJYHChoosedScroeBean jjyhChooseBean = new JczqJJYHChoosedScroeBean();
				jjyhChooseBean.setDan(false);


				jjyhChooseBean.setMatchKey(match.getMatchKey());
				jjyhChooseBean.setPlayType("JQS");
				jjyhChooseBean.setValue(new BigInteger(new StringBuilder(JQS).reverse().toString(), 2).toString());
				jjyhChooseBean.getSps().add(match.getSp().getJQS().getS0().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getJQS().getS1().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getJQS().getS2().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getJQS().getS3().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getJQS().getS4().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getJQS().getS5().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getJQS().getS6().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getJQS().getS7().getValue());

				JQS = "";

				if(jjyhChooseBean.getSps()!=null&&jjyhChooseBean.getSps().size()>0){
					item.add(jjyhChooseBean);
				}
			}
			if(BQC.contains("1")){
				JczqJJYHChoosedScroeBean jjyhChooseBean = new JczqJJYHChoosedScroeBean();
				jjyhChooseBean.setDan(false);


				jjyhChooseBean.setMatchKey(match.getMatchKey());
				jjyhChooseBean.setPlayType("BQQ");
				jjyhChooseBean.setValue(new BigInteger(new StringBuilder(BQC).reverse().toString(), 2).toString());

				jjyhChooseBean.getSps().add(match.getSp().getBQQ().getWIN_WIN().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBQQ().getWIN_DRAW().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBQQ().getWIN_LOSE().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBQQ().getDRAW_WIN().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBQQ().getDRAW_DRAW().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBQQ().getDRAW_LOSE().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBQQ().getLOSE_WIN().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBQQ().getLOSE_DRAW().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBQQ().getLOSE_LOSE().getValue());

				BQC = "";

				if(jjyhChooseBean.getSps()!=null&&jjyhChooseBean.getSps().size()>0){
					item.add(jjyhChooseBean);
				}
			}
			if(BF.contains("1")){
				JczqJJYHChoosedScroeBean jjyhChooseBean = new JczqJJYHChoosedScroeBean();
				jjyhChooseBean.setDan(false);

				if(jjyhChooseBean.getSps()!=null&&jjyhChooseBean.getSps().size()>0){
					item.add(jjyhChooseBean);
				}
				jjyhChooseBean.setMatchKey(match.getMatchKey());
				jjyhChooseBean.setPlayType("BF");
				jjyhChooseBean.setValue(new BigInteger(new StringBuilder(BF).reverse().toString(), 2).toString());


				jjyhChooseBean.getSps().add(match.getSp().getBF().getWIN10().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getWIN20().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getWIN21().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getWIN30().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getWIN31().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getWIN32().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getWIN40().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getWIN41().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getWIN42().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getWIN50().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getWIN51().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getWIN52().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getWIN_OTHER().getValue());

				jjyhChooseBean.getSps().add(match.getSp().getBF().getDRAW00().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getDRAW11().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getDRAW22().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getDRAW33().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getDRAW_OTHER().getValue());;

				jjyhChooseBean.getSps().add(match.getSp().getBF().getLOSE01().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getLOSE02().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getLOSE12().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getLOSE03().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getLOSE13().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getLOSE23().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getLOSE04().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getLOSE14().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getLOSE24().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getLOSE05().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getLOSE15().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getLOSE25().getValue());
				jjyhChooseBean.getSps().add(match.getSp().getBF().getLOSE_OTHER().getValue());

				BF = "";

				if(jjyhChooseBean.getSps()!=null&&jjyhChooseBean.getSps().size()>0){
					item.add(jjyhChooseBean);
				}
			}
		}
		return item;
	}

	public static HashMap<String, char[]>  chooseWanFaValue(ArrayList<ScroeBean> checkNumber) {
		HashMap<String, char[]> chooseWanFa = new HashMap<String, char[]>();
		char[] spf = new char[]{'0','0','0'};
		char[] rqspf = new char[]{'0','0','0'};
		char[] jqs = new char[]{'0','0','0','0','0','0','0','0'};
		char[] bqc = new char[]{'0','0','0','0','0','0','0','0','0'};
		char[] bf = new char[]{'0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0'};
		for(int i=0;i<checkNumber.size();i++){
			switch (checkNumber.get(i).getKey()) {
			case "WINRQSPF":
				rqspf[0] = '1';
				break;
			case "DRAWRQSPF":
				rqspf[1] = '1';
				break;
			case "LOSERQSPF":
				rqspf[2] = '1';
				break;
			case "WINSPF":
				spf[0] = '1';
				break;
			case "DRAWSPF":
				spf[1] = '1';
				break;
			case "LOSESPF":
				spf[2] = '1';
				break;
			case "S0":
				jqs[0] = '1';
				break;
			case "S1":
				jqs[1] = '1';
				break;
			case "S2":
				jqs[2] = '1';
				break;
			case "S3":
				jqs[3] = '1';
				break;
			case "S4":
				jqs[4] = '1';
				break;
			case "S5":
				jqs[5] = '1';
				break;
			case "S6":
				jqs[6] = '1';
				break;
			case "S7":
				jqs[7] = '1';
				break;
			case "WIN10":
				bf[0] = '1';
				break;
			case "WIN20":
				bf[1] = '1';
				break;
			case "WIN21":
				bf[2] = '1';
				break;
			case "WIN30":
				bf[3] = '1';
				break;
			case "WIN31":
				bf[4] = '1';
				break;
			case "WIN32":
				bf[5] = '1';
				break;
			case "WIN40":
				bf[6] = '1';
				break;
			case "WIN41":
				bf[7] = '1';
				break;
			case "WIN42":
				bf[8] = '1';
				break;
			case "WIN50":
				bf[9] = '1';
				break;
			case "WIN51":
				bf[10] = '1';
				break;
			case "WIN52":
				bf[11] = '1';
				break;
			case "WIN_OTHER":
				bf[12] = '1';
				break;

			case "DRAW00":
				bf[13] = '1';
				break;
			case "DRAW11":
				bf[14] = '1';
				break;
			case "DRAW22":
				bf[15] = '1';
				break;
			case "DRAW33":
				bf[16] = '1';
				break;
			case "DRAW_OTHER":
				bf[17] = '1';
				break;

			case "LOSE01":
				bf[18] = '1';
				break;
			case "LOSE02":
				bf[19] = '1';
				break;
			case "LOSE12":
				bf[20] = '1';
				break;
			case "LOSE03":
				bf[21] = '1';
				break;
			case "LOSE13":
				bf[22] = '1';
				break;
			case "LOSE23":
				bf[23] = '1';
				break;
			case "LOSE04":
				bf[24] = '1';
				break;
			case "LOSE14":
				bf[25] = '1';
				break;
			case "LOSE24":
				bf[26] = '1';
				break;
			case "LOSE05":
				bf[27] = '1';
				break;
			case "LOSE15":
				bf[28] = '1';
				break;
			case "LOSE25":
				bf[29] = '1';
				break;
			case "LOSE_OTHER":
				bf[30] = '1';
				break;
			case "WIN_WIN":
				bqc[0] = '1';
				break;
			case "WIN_DRAW":
				bqc[1] = '1';
				break;
			case "WIN_LOSE":
				bqc[2] = '1';
				break;
			case "DRAW_WIN":
				bqc[3] = '1';
				break;
			case "DRAW_DRAW":
				bqc[4] = '1';
				break;
			case "DRAW_LOSE":
				bqc[5] = '1';
				break;
			case "LOSE_WIN":
				bqc[6] = '1';
				break;
			case "LOSE_DRAW":
				bqc[7] = '1';
				break;
			case "LOSE_LOSE":
				bqc[8] = '1';
				break;
			}
		}
		chooseWanFa.put("SPF", spf);
		chooseWanFa.put("RQSPF", rqspf);
		chooseWanFa.put("JQS", jqs);
		chooseWanFa.put("BQC", bqc);
		chooseWanFa.put("BF", bf);
		return chooseWanFa;
	}
	public static String chooseScroeValue(String scroe) {
		String str = "";
		switch (scroe) {
		case "WINRQSPF":
			str = 0 + "";
			break;
		case "DRAWRQSPF":
			str = 1 + "";
			break;
		case "LOSERQSPF":
			str = 2 + "";
			break;
		case "WINSPF":
			str = 0 + "";
			break;
		case "DRAWSPF":
			str = 1 + "";
			break;
		case "LOSESPF":
			str = 2 + "";
			break;
		case "S0":
			str = 0 + "";
			break;
		case "S1":
			str = 1 + "";
			break;
		case "S2":
			str = 2 + "";
			break;
		case "S3":
			str = 3 + "";
			break;
		case "S4":
			str = 4 + "";
			break;
		case "S5":
			str = 5 + "";
			break;
		case "S6":
			str = 6 + "";
			break;
		case "S7":
			str = 7 + "";
			break;
		case "WIN10":
			str = "0";
			break;
		case "WIN20":
			str = "1";
			break;
		case "WIN21":
			str = "2";
			break;
		case "WIN30":
			str = "3";
			break;
		case "WIN31":
			str = "4";
			break;
		case "WIN32":
			str = "5";
			break;
		case "WIN40":
			str = "6";
			break;
		case "WIN41":
			str = "7";
			break;
		case "WIN42":
			str = "8";
			break;
		case "WIN50":
			str = "9";
			break;
		case "WIN51":
			str = "10";
			break;
		case "WIN52":
			str = "11";
			break;
		case "WIN_OTHER":
			str = "12";
			break;
		case "DRAW00":
			str = "13";
			break;
		case "DRAW11":
			str = "14";
			break;
		case "DRAW22":
			str = "15";
			break;
		case "DRAW33":
			str = "16";
			break;
		case "DRAW_OTHER":
			str = "17";
			break;
		case "LOSE01":
			str = "18";
			break;
		case "LOSE02":
			str = "19";
			break;
		case "LOSE12":
			str = "20";
			break;
		case "LOSE03":
			str = "21";
			break;
		case "LOSE13":
			str = "22";
			break;
		case "LOSE23":
			str = "23";
			break;
		case "LOSE04":
			str = "24";
			break;
		case "LOSE14":
			str = "25";
			break;
		case "LOSE24":
			str = "26";
			break;
		case "LOSE05":
			str = "27";
			break;
		case "LOSE15":
			str = "28";
			break;
		case "LOSE25":
			str = "29";
			break;
		case "LOSE_OTHER":
			str = "30";
			break;
		case "WIN_WIN":
			str = "0";
			break;
		case "WIN_DRAW":
			str = "1";
			break;
		case "WIN_LOSE":
			str = "2";
			break;
		case "DRAW_WIN":
			str = "3";
			break;
		case "DRAW_DRAW":
			str = "4";
			break;
		case "DRAW_LOSE":
			str = "5";
			break;
		case "LOSE_WIN":
			str = "6";
			break;
		case "LOSE_DRAW":
			str = "7";
			break;
		case "LOSE_LOSE":
			str = "8";
			break;
		}
		return str;
	}

}