package com.cshen.tiyu.activity.lottery.ball.basketball;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.cshen.tiyu.domain.ball.BasketBallMatch;
import com.cshen.tiyu.domain.ball.JCLQJJYHTOUZHU;
import com.cshen.tiyu.domain.ball.JJYHEach;
import com.cshen.tiyu.domain.ball.JclqJJYHChoosedScroeBean;
import com.cshen.tiyu.domain.ball.Match;
import com.cshen.tiyu.domain.ball.ScroeBean;
import com.google.common.collect.Lists;

public class JJYHJCLQUtil {
	final static int PINGJUNYOUHUA = 1;
	final static int BOREYOUHUA = 2;
	final static int BOLENGYOUHUA = 3;
	private static JJYHJCLQUtil jjyhUtil;
	private static java.text.NumberFormat nf ;
	public static JJYHJCLQUtil getJJYHUtil() {
		if (jjyhUtil == null) {
			nf  = java.text.NumberFormat.getInstance();   
			nf.setGroupingUsed(false);
			jjyhUtil = new JJYHJCLQUtil();
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
												+JCLQUtil.chooseScroeName(checkedMother.get(j).getKey()).replace(";", "")
												+"["+checkedMother.get(j).getValue()+"],"
												+teamNameChilld+"="
												+JCLQUtil.chooseScroeName(checkedChilld.get(n).getKey()).replace(";", "")
												+"["+checkedChilld.get(n).getValue()+"]");


								jjyheach.setJiangjinDouble(2*in4remove6double5((double)(Math.round(
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
											JCLQUtil.getJCLQUtil();
											jjyheach.setNames(
													teamNameMother+"="
															+JCLQUtil.chooseScroeName(checkedMother.get(j).getKey()).replace(";", "")
															+"["+checkedMother.get(j).getValue()+"],"
															+teamNameChilld+"="
															+JCLQUtil.chooseScroeName(checkedChilld.get(n).getKey()).replace(";", "")
															+"["+checkedChilld.get(n).getValue()+"],"
															+teamNameChilld2+"="
															+JCLQUtil.chooseScroeName(checkedChilld2.get(q).getKey()).replace(";", "")
															+"["+checkedChilld2.get(q).getValue()+"]");
											jjyheach.setJiangjinDouble(2*in4remove6double5((double)(Math.round(
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
														JCLQUtil.getJCLQUtil();
														jjyheach.setNames(
																teamNameMother+"="
																		+JCLQUtil.chooseScroeName(checkedMother.get(j).getKey()).replace(";", "")
																		+"["+checkedMother.get(j).getValue()+"],"
																		+teamNameChilld+"="
																		+JCLQUtil.chooseScroeName(checkedChilld.get(n).getKey()).replace(";", "")
																		+"["+checkedChilld.get(n).getValue()+"],"
																		+teamNameChilld2+"="
																		+JCLQUtil.chooseScroeName(checkedChilld2.get(q).getKey()).replace(";", "")
																		+"["+checkedChilld2.get(q).getValue()+"],"
																		+teamNameChilld3+"="
																		+JCLQUtil.chooseScroeName(checkedChilld3.get(y).getKey()).replace(";", "")
																		+"["+checkedChilld3.get(y).getValue()+"]");
														jjyheach.setJiangjinDouble(2*in4remove6double5((double)(Math.round(
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
																	JCLQUtil.getJCLQUtil();
																	jjyheach.setNames(
																			teamNameMother+"="
																					+JCLQUtil.chooseScroeName(checkedMother.get(j).getKey()).replace(";", "")
																					+"["+checkedMother.get(j).getValue()+"],"
																					+teamNameChilld+"="
																					+JCLQUtil.chooseScroeName(checkedChilld.get(n).getKey()).replace(";", "")
																					+"["+checkedChilld.get(n).getValue()+"],"
																					+teamNameChilld2+"="
																					+JCLQUtil.chooseScroeName(checkedChilld2.get(q).getKey()).replace(";", "")
																					+"["+checkedChilld2.get(q).getValue()+"],"
																					+teamNameChilld3+"="
																					+JCLQUtil.chooseScroeName(checkedChilld3.get(y).getKey()).replace(";", "")
																					+"["+checkedChilld3.get(y).getValue()+"],"
																					+teamNameChilld4+"="
																					+JCLQUtil.chooseScroeName(checkedChilld4.get(b).getKey()).replace(";", "")
																					+"["+checkedChilld4.get(b).getValue()+"]");
																	jjyheach.setJiangjinDouble(2*in4remove6double5((double)(Math.round(
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
																				JCLQUtil.getJCLQUtil();
																				jjyheach.setNames(
																						teamNameMother+"="
																								+JCLQUtil.chooseScroeName(checkedMother.get(j).getKey()).replace(";", "")
																								+"["+checkedMother.get(j).getValue()+"],"
																								+teamNameChilld+"="
																								+JCLQUtil.chooseScroeName(checkedChilld.get(n).getKey()).replace(";", "")
																								+"["+checkedChilld.get(n).getValue()+"],"
																								+teamNameChilld2+"="
																								+JCLQUtil.chooseScroeName(checkedChilld2.get(q).getKey()).replace(";", "")
																								+"["+checkedChilld2.get(q).getValue()+"],"
																								+teamNameChilld3+"="
																								+JCLQUtil.chooseScroeName(checkedChilld3.get(y).getKey()).replace(";", "")
																								+"["+checkedChilld3.get(y).getValue()+"],"
																								+teamNameChilld4+"="
																								+JCLQUtil.chooseScroeName(checkedChilld4.get(b).getKey()).replace(";", "")
																								+"["+checkedChilld4.get(b).getValue()+"],"
																								+teamNameChilld5+"="
																								+JCLQUtil.chooseScroeName(checkedChilld5.get(d).getKey()).replace(";", "")
																								+"["+checkedChilld5.get(d).getValue()+"]");
																				jjyheach.setJiangjinDouble(2*in4remove6double5((double)(Math.round(
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
																							JCLQUtil.getJCLQUtil();
																							jjyheach.setNames(
																									teamNameMother+"="
																											+JCLQUtil.chooseScroeName(checkedMother.get(j).getKey()).replace(";", "")
																											+"["+checkedMother.get(j).getValue()+"],"
																											+teamNameChilld+"="
																											+JCLQUtil.chooseScroeName(checkedChilld.get(n).getKey()).replace(";", "")
																											+"["+checkedChilld.get(n).getValue()+"],"
																											+teamNameChilld2+"="
																											+JCLQUtil.chooseScroeName(checkedChilld2.get(q).getKey()).replace(";", "")
																											+"["+checkedChilld2.get(q).getValue()+"],"
																											+teamNameChilld3+"="
																											+JCLQUtil.chooseScroeName(checkedChilld3.get(y).getKey()).replace(";", "")
																											+"["+checkedChilld3.get(y).getValue()+"],"
																											+teamNameChilld4+"="
																											+JCLQUtil.chooseScroeName(checkedChilld4.get(b).getKey()).replace(";", "")
																											+"["+checkedChilld4.get(b).getValue()+"],"
																											+teamNameChilld5+"="
																											+JCLQUtil.chooseScroeName(checkedChilld5.get(d).getKey()).replace(";", "")
																											+"["+checkedChilld5.get(d).getValue()+"],"
																											+teamNameChilld6+"="
																											+JCLQUtil.chooseScroeName(checkedChilld6.get(f).getKey()).replace(";", "")
																											+"["+checkedChilld6.get(f).getValue()+"]");
																							jjyheach.setJiangjinDouble(2*in4remove6double5((double)(Math.round(
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
																										JCLQUtil.getJCLQUtil();
																										jjyheach.setNames(
																												teamNameMother+"="
																														+JCLQUtil.chooseScroeName(checkedMother.get(j).getKey()).replace(";", "")
																														+"["+checkedMother.get(j).getValue()+"],"
																														+teamNameChilld+"="
																														+JCLQUtil.chooseScroeName(checkedChilld.get(n).getKey()).replace(";", "")
																														+"["+checkedChilld.get(n).getValue()+"],"
																														+teamNameChilld2+"="
																														+JCLQUtil.chooseScroeName(checkedChilld2.get(q).getKey()).replace(";", "")
																														+"["+checkedChilld2.get(q).getValue()+"],"
																														+teamNameChilld3+"="
																														+JCLQUtil.chooseScroeName(checkedChilld3.get(y).getKey()).replace(";", "")
																														+"["+checkedChilld3.get(y).getValue()+"],"
																														+teamNameChilld4+"="
																														+JCLQUtil.chooseScroeName(checkedChilld4.get(b).getKey()).replace(";", "")
																														+"["+checkedChilld4.get(b).getValue()+"],"
																														+teamNameChilld5+"="
																														+JCLQUtil.chooseScroeName(checkedChilld5.get(d).getKey()).replace(";", "")
																														+"["+checkedChilld5.get(d).getValue()+"],"
																														+teamNameChilld6+"="
																														+JCLQUtil.chooseScroeName(checkedChilld6.get(f).getKey()).replace(";", "")
																														+"["+checkedChilld6.get(f).getValue()+"],"
																														+teamNameChilld7+"="
																														+JCLQUtil.chooseScroeName(checkedChilld7.get(h).getKey()).replace(";", "")
																														+"["+checkedChilld7.get(h).getValue()+"]");
																										jjyheach.setJiangjinDouble(2*in4remove6double5((double)(Math.round(
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
			item.setJiangjinEach(JJYHJCLQUtil.getJJYHUtil().in4remove6double5(item.getJiangjinDouble(), 2));
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
	public JCLQJJYHTOUZHU getJJYHTOUZHU(ArrayList<Match> matchs,ArrayList<JJYHEach> jjyhList,String min,String max){
		JCLQJJYHTOUZHU jjyhtouzhu = new JCLQJJYHTOUZHU();
		jjyhtouzhu.setBestMinPrize(min);
		jjyhtouzhu.setBestMaxPrize(max);

		jjyhtouzhu.setItems(getItem(matchs));
		ArrayList<String> matchkey = getMatchKey(matchs);
		jjyhtouzhu.setMatchkeys(matchkey);
		jjyhtouzhu.setMultiples(getMultiples(jjyhList));
		HashMap<String, ArrayList<String>> jjyheachdata = getJJYHEach(matchs.size(),matchkey,jjyhList);
		ArrayList<String> playtypes = jjyheachdata.get("playTypes");
		jjyhtouzhu.setPlayTypes(playtypes);
		jjyhtouzhu.setOptimize(true);


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

						String playType = JCLQUtil.getJCLQUtil().getPlayTypeString(jjyh.getChecks().get(i).getKey());
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
	public  ArrayList<JclqJJYHChoosedScroeBean> getItem(ArrayList<Match> matchs){
		ArrayList<JclqJJYHChoosedScroeBean> item = new ArrayList<JclqJJYHChoosedScroeBean>();
		for(int m = 0;m<matchs.size();m++){
			BasketBallMatch match = (BasketBallMatch) matchs.get(m);
			HashMap<String, char[]> chooseWanFa = chooseWanFaValue(match.getCheckNumber());
			String SF = new String(chooseWanFa.get("SF"));
			String RF = new String(chooseWanFa.get("RF"));
			String DXF = new String(chooseWanFa.get("DXF"));
			String SFC = new String(chooseWanFa.get("SFC"));
			if(SF.contains("1")){
				JclqJJYHChoosedScroeBean jjyhChooseBean = new JclqJJYHChoosedScroeBean();
				jjyhChooseBean.setDan(false);
				jjyhChooseBean.setMatchKey(match.getMatchKey());
				jjyhChooseBean.setPlayType("SF");
				jjyhChooseBean.setValue(new BigInteger(new StringBuilder(SF).reverse().toString(), 2).toString());

				jjyhChooseBean.getSps().add(match.getMixSp().getSF().getLOSE().getValue());
				jjyhChooseBean.getSps().add(match.getMixSp().getSF().getWIN().getValue());
				SF = "";
				if(jjyhChooseBean.getSps()!=null&&jjyhChooseBean.getSps().size()>0){
					item.add(jjyhChooseBean);
				}
			}
			if(RF.contains("1")){
				JclqJJYHChoosedScroeBean jjyhChooseBean = new JclqJJYHChoosedScroeBean();
				jjyhChooseBean.setDan(false);

				jjyhChooseBean.setMatchKey(match.getMatchKey());
				jjyhChooseBean.setPlayType("RFSF");
				jjyhChooseBean.setValue(new BigInteger(new StringBuilder(RF).reverse().toString(), 2).toString());	
				jjyhChooseBean.getSps().add(match.getMixSp().getRFSF().getSF_LOSE().getValue());
				jjyhChooseBean.getSps().add(match.getMixSp().getRFSF().getSF_WIN().getValue());
				jjyhChooseBean.setReferenceValue(match.getHandicap());
				RF = "";

				if(jjyhChooseBean.getSps()!=null&&jjyhChooseBean.getSps().size()>0){
					item.add(jjyhChooseBean);
				}
			}
			if(DXF.contains("1")){
				JclqJJYHChoosedScroeBean jjyhChooseBean = new JclqJJYHChoosedScroeBean();
				jjyhChooseBean.setDan(false);


				jjyhChooseBean.setMatchKey(match.getMatchKey());
				jjyhChooseBean.setPlayType("DXF");
				jjyhChooseBean.setValue(new BigInteger(new StringBuilder(DXF).reverse().toString(), 2).toString());
				jjyhChooseBean.getSps().add(match.getMixSp().getDXF().getLARGE().getValue());
				jjyhChooseBean.getSps().add(match.getMixSp().getDXF().getLITTLE().getValue());
				jjyhChooseBean.setReferenceValue(match.getTotalScore()+"");
				DXF = "";

				if(jjyhChooseBean.getSps()!=null&&jjyhChooseBean.getSps().size()>0){
					item.add(jjyhChooseBean);
				}
			}
			if(SFC.contains("1")){
				JclqJJYHChoosedScroeBean jjyhChooseBean = new JclqJJYHChoosedScroeBean();
				jjyhChooseBean.setDan(false);


				jjyhChooseBean.setMatchKey(match.getMatchKey());
				jjyhChooseBean.setPlayType("SFC");
				jjyhChooseBean.setValue(new BigInteger(new StringBuilder(SFC).reverse().toString(), 2).toString());

				jjyhChooseBean.getSps().add(match.getMixSp().getSFC().getGUEST1_5().getValue());
				jjyhChooseBean.getSps().add(match.getMixSp().getSFC().getGUEST6_10().getValue());
				jjyhChooseBean.getSps().add(match.getMixSp().getSFC().getGUEST11_15().getValue());
				jjyhChooseBean.getSps().add(match.getMixSp().getSFC().getGUEST16_20().getValue());
				jjyhChooseBean.getSps().add(match.getMixSp().getSFC().getGUEST21_25().getValue());
				jjyhChooseBean.getSps().add(match.getMixSp().getSFC().getGUEST26().getValue());
				jjyhChooseBean.getSps().add(match.getMixSp().getSFC().getHOME1_5().getValue());
				jjyhChooseBean.getSps().add(match.getMixSp().getSFC().getHOME6_10().getValue());
				jjyhChooseBean.getSps().add(match.getMixSp().getSFC().getHOME11_15().getValue());
				jjyhChooseBean.getSps().add(match.getMixSp().getSFC().getHOME16_20().getValue());
				jjyhChooseBean.getSps().add(match.getMixSp().getSFC().getHOME21_25().getValue());
				jjyhChooseBean.getSps().add(match.getMixSp().getSFC().getHOME26().getValue());

				SFC = "";

				if(jjyhChooseBean.getSps()!=null&&jjyhChooseBean.getSps().size()>0){
					item.add(jjyhChooseBean);
				}
			}
		}
		return item;
	}

	public static HashMap<String, char[]>  chooseWanFaValue(ArrayList<ScroeBean> checkNumber) {
		HashMap<String, char[]> chooseWanFa = new HashMap<String, char[]>();
		char[] sf = new char[]{'0','0'};
		char[] rf = new char[]{'0','0'};
		char[] dxf = new char[]{'0','0'};
		char[] sfc = new char[]{'0','0','0','0','0','0','0','0','0','0','0','0'};

		for(int i=0;i<checkNumber.size();i++){
			switch (checkNumber.get(i).getKey()) {

			case "LOSE":
				sf[0] = '1';
				break;
			case "WIN":
				sf[1] = '1';
				break;

			case "SF_LOSE":
				rf[0] = '1';
				break;
			case "SF_WIN":
				rf[1] = '1';
				break;

			case "LARGE":
				dxf[0] = '1';
				break;
			case "LITTLE":
				dxf[1] = '1';
				break;

			case "GUEST1_5":
				sfc[0] = '1';
				break;
			case "GUEST6_10":
				sfc[1] = '1';
				break;
			case "GUEST11_15":
				sfc[2] = '1';
				break;
			case "GUEST16_20":
				sfc[3] = '1';
				break;
			case "GUEST21_25":
				sfc[4] = '1';
				break;
			case "GUEST26":
				sfc[5] = '1';
				break;
			case "HOME1_5":
				sfc[6] = '1';
				break;
			case "HOME6_10":
				sfc[7] = '1';
				break;
			case "HOME11_15":
				sfc[8] = '1';
				break;
			case "HOME16_20":
				sfc[9] = '1';
				break;
			case "HOME21_25":
				sfc[10] = '1';
				break;
			case "HOME26":
				sfc[11] = '1';
			}
		}
		chooseWanFa.put("SF", sf);
		chooseWanFa.put("RF", rf);
		chooseWanFa.put("DXF", dxf);
		chooseWanFa.put("SFC", sfc);
		return chooseWanFa;
	}
	public static String chooseScroeValue(String scroe) {
		String str = "";
		switch (scroe) {
		case "WIN":
			str = 1 + "";
			break;
		case "LOSE":
			str = 0 + "";
			break;

		case "SF_WIN":
			str = 1 + "";
			break;
		case "SF_LOSE":
			str = 0 + "";
			break;

		case "LARGE":
			str = 0 + "";
			break;
		case "LITTLE":
			str = 1 + "";
			break;

		case "GUEST1_5":
			str = 0 + "";
			break;
		case "GUEST6_10":
			str = 1 + "";
			break;
		case "GUEST11_15":
			str = 2 + "";
			break;
		case "GUEST16_20":
			str = 3 + "";
			break;
		case "GUEST21_25":
			str = 4 + "";
			break;
		case "GUEST26":
			str = 5 + "";
			break;
		case "HOME1_5":
			str = 6 + "";
			break;
		case "HOME6_10":
			str = 7 + "";
			break;
		case "HOME11_15":
			str = 8 + "";
			break;
		case "HOME16_20":
			str = 9 + "";
			break;
		case "HOME21_25":
			str = 10 + "";
			break;
		case "HOME26":
			str = 11 + "";
		}
		return str;
	}

}