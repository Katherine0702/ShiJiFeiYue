package com.cshen.tiyu.activity.lottery.ball.util;
import com.cshen.tiyu.domain.ball.PassTypeEach;

/**
 * 竞彩足球过关方式
 * 
 */
public class PassType2 {
	public int maxSingleMatchCount = 8;
	public int maxPassMatchCount= 8;
	//PassType2: {},
	public PassTypeEach[] PassTypeArr = new PassTypeEach[40];
	public PassType2() {
		PassTypeArr[0] = new PassTypeEach();
		PassTypeArr[0].setKey("P1");
		PassTypeArr[0].setUnits(1);
		PassTypeArr[0].setMatchCount(1);
		PassTypeArr[0].setPassMatchs(new int[]{1});
		PassTypeArr[0].setText("单关");
		PassTypeArr[0].setValue(1);
		PassTypeArr[0].setPassTypeValue(0);
		
		PassTypeArr[1]= new PassTypeEach();
		PassTypeArr[1].setKey("P2_1");
		PassTypeArr[1].setUnits(1);
		PassTypeArr[1].setMatchCount(2);
		PassTypeArr[1].setPassMatchs(new int[]{2});
		PassTypeArr[1].setText("2串1");
		PassTypeArr[1].setValue(2);
		PassTypeArr[1].setPassTypeValue(1);
		
		PassTypeArr[2] = new PassTypeEach();
		PassTypeArr[2].setKey("P3_1");
		PassTypeArr[2].setUnits(1);
		PassTypeArr[2].setMatchCount(3);
		PassTypeArr[2].setPassMatchs(new int[]{3});
		PassTypeArr[2].setText("3串1");
		PassTypeArr[2].setValue(4);
		PassTypeArr[2].setPassTypeValue(2);
		
		PassTypeArr[3] = new PassTypeEach();
		PassTypeArr[3].setKey("P3_3");
		PassTypeArr[3].setUnits(3);
		PassTypeArr[3].setMatchCount(3);
		PassTypeArr[3].setPassMatchs(new int[]{2});
		PassTypeArr[3].setText("3串3");
		PassTypeArr[3].setValue(8);
		PassTypeArr[3].setPassTypeValue(3);
		
		PassTypeArr[4] = new PassTypeEach();
		PassTypeArr[4].setKey("P3_4");
		PassTypeArr[4].setUnits(4);
		PassTypeArr[4].setMatchCount(3);
		PassTypeArr[4].setPassMatchs(new int[]{2,3});
		PassTypeArr[4].setText("3串4");
		PassTypeArr[4].setValue(16);
		PassTypeArr[4].setPassTypeValue(4);

		PassTypeArr[5] = new PassTypeEach();
		PassTypeArr[5].setKey("P4_1");
		PassTypeArr[5].setUnits(1);
		PassTypeArr[5].setMatchCount(4);
		PassTypeArr[5].setPassMatchs(new int[]{4});
		PassTypeArr[5].setText("4串1");
		PassTypeArr[5].setValue(32);
		PassTypeArr[5].setPassTypeValue(5);

		PassTypeArr[6] = new PassTypeEach();
		PassTypeArr[6].setKey("P4_4");
		PassTypeArr[6].setUnits(4);
		PassTypeArr[6].setMatchCount(4);
		PassTypeArr[6].setPassMatchs(new int[]{3});
		PassTypeArr[6].setText("4串4");
		PassTypeArr[6].setValue(64);
		PassTypeArr[6].setPassTypeValue(6);

		PassTypeArr[7] = new PassTypeEach();
		PassTypeArr[7].setKey("P4_5");
		PassTypeArr[7].setUnits(5);
		PassTypeArr[7].setMatchCount(4);
		PassTypeArr[7].setPassMatchs(new int[]{3,4});
		PassTypeArr[7].setText("4串5");
		PassTypeArr[7].setValue(128);
		PassTypeArr[7].setPassTypeValue(7);

		PassTypeArr[8] = new PassTypeEach();
		PassTypeArr[8].setKey("P4_6");
		PassTypeArr[8].setUnits(6);
		PassTypeArr[8].setMatchCount(4);
		PassTypeArr[8].setPassMatchs(new int[]{2});
		PassTypeArr[8].setText("4串6");
		PassTypeArr[8].setValue(256);
		PassTypeArr[8].setPassTypeValue(8);

		PassTypeArr[9] = new PassTypeEach();
		PassTypeArr[9].setKey("P4_11");
		PassTypeArr[9].setUnits(11);
		PassTypeArr[9].setMatchCount(4);
		PassTypeArr[9].setPassMatchs(new int[]{2,3,4});
		PassTypeArr[9].setText("4串11");
		PassTypeArr[9].setValue(512);
		PassTypeArr[9].setPassTypeValue(9);

		PassTypeArr[10]= new PassTypeEach();
		PassTypeArr[10].setKey("P5_1");
		PassTypeArr[10].setUnits(1);
		PassTypeArr[10].setMatchCount(5);
		PassTypeArr[10].setPassMatchs(new int[]{5});
		PassTypeArr[10].setText("5串1");
		PassTypeArr[10].setValue(1024);
		PassTypeArr[10].setPassTypeValue(10);

		PassTypeArr[11]= new PassTypeEach();
		PassTypeArr[11].setKey("P5_5");
		PassTypeArr[11].setUnits(5);
		PassTypeArr[11].setMatchCount(5);
		PassTypeArr[11].setPassMatchs(new int[]{4});
		PassTypeArr[11].setText("5串5");
		PassTypeArr[11].setValue(2048);
		PassTypeArr[11].setPassTypeValue(11);

		PassTypeArr[12]= new PassTypeEach();
		PassTypeArr[12].setKey("P5_6");
		PassTypeArr[12].setUnits(6);
		PassTypeArr[12].setMatchCount(5);
		PassTypeArr[12].setPassMatchs(new int[]{4,5});
		PassTypeArr[12].setText("5串6");
		PassTypeArr[12].setValue(4096);
		PassTypeArr[12].setPassTypeValue(12);
		

		PassTypeArr[13]= new PassTypeEach();
		PassTypeArr[13].setKey("P5_10");
		PassTypeArr[13].setUnits(10);
		PassTypeArr[13].setMatchCount(5);
		PassTypeArr[13].setPassMatchs(new int[]{2});
		PassTypeArr[13].setText("5串10");
		PassTypeArr[13].setValue(8192);
		PassTypeArr[13].setPassTypeValue(13);
		

		PassTypeArr[14]= new PassTypeEach();
		PassTypeArr[14].setKey("P5_16");
		PassTypeArr[14].setUnits(16);
		PassTypeArr[14].setMatchCount(5);
		PassTypeArr[14].setPassMatchs(new int[]{3,4,5});
		PassTypeArr[14].setText("5串16");
		PassTypeArr[14].setValue(16384);
		PassTypeArr[14].setPassTypeValue(14);
		

		PassTypeArr[15]= new PassTypeEach();
		PassTypeArr[15].setKey("P5_20");
		PassTypeArr[15].setUnits(20);
		PassTypeArr[15].setMatchCount(5);
		PassTypeArr[15].setPassMatchs(new int[]{2,3});
		PassTypeArr[15].setText("5串20");
		PassTypeArr[15].setValue(32768);
		PassTypeArr[15].setPassTypeValue(15);

		PassTypeArr[16]= new PassTypeEach();
		PassTypeArr[16].setKey("P5_26");
		PassTypeArr[16].setUnits(26);
		PassTypeArr[16].setMatchCount(5);
		PassTypeArr[16].setPassMatchs(new int[]{2,3,4,5});
		PassTypeArr[16].setText("5串26");
		PassTypeArr[16].setValue(65536);
		PassTypeArr[16].setPassTypeValue(16);
		

		PassTypeArr[17]= new PassTypeEach();
		PassTypeArr[17].setKey("P6_1");
		PassTypeArr[17].setUnits(1);
		PassTypeArr[17].setMatchCount(6);
		PassTypeArr[17].setPassMatchs(new int[]{6});
		PassTypeArr[17].setText("6串1");
		PassTypeArr[17].setValue(131072);
		PassTypeArr[17].setPassTypeValue(17);

		PassTypeArr[18]= new PassTypeEach();
		PassTypeArr[18].setKey("P6_6");
		PassTypeArr[18].setUnits(6);
		PassTypeArr[18].setMatchCount(6);
		PassTypeArr[18].setPassMatchs(new int[]{5});
		PassTypeArr[18].setText("6串6");
		PassTypeArr[18].setValue(262144);
		PassTypeArr[18].setPassTypeValue(18);
		

		PassTypeArr[19]= new PassTypeEach();
		PassTypeArr[19].setKey("P6_7");
		PassTypeArr[19].setUnits(7);
		PassTypeArr[19].setMatchCount(6);
		PassTypeArr[19].setPassMatchs(new int[]{5,6});
		PassTypeArr[19].setText("6串7");
		PassTypeArr[19].setValue(524288);
		PassTypeArr[19].setPassTypeValue(19);
		

		PassTypeArr[20]= new PassTypeEach();
		PassTypeArr[20].setKey("P6_15");
		PassTypeArr[20].setUnits(15);
		PassTypeArr[20].setMatchCount(6);
		PassTypeArr[20].setPassMatchs(new int[]{2});
		PassTypeArr[20].setText("6串15");
		PassTypeArr[20].setValue(1048576);
		PassTypeArr[20].setPassTypeValue(20);
		
		PassTypeArr[21]= new PassTypeEach();
		PassTypeArr[21].setKey("P6_20");
		PassTypeArr[21].setUnits(20);
		PassTypeArr[21].setMatchCount(6);
		PassTypeArr[21].setPassMatchs(new int[]{3});
		PassTypeArr[21].setText("6串20");
		PassTypeArr[21].setValue(2097152);
		PassTypeArr[21].setPassTypeValue(21);
		
		PassTypeArr[22]= new PassTypeEach();
		PassTypeArr[22].setKey("P6_22");
		PassTypeArr[22].setUnits(22);
		PassTypeArr[22].setMatchCount(6);
		PassTypeArr[22].setPassMatchs(new int[]{4,5,6});
		PassTypeArr[22].setText("6串22");
		PassTypeArr[22].setValue(4194304);
		PassTypeArr[22].setPassTypeValue(22);
		
		PassTypeArr[23]= new PassTypeEach();
		PassTypeArr[23].setKey("P6_35");
		PassTypeArr[23].setUnits(35);
		PassTypeArr[23].setMatchCount(6);
		PassTypeArr[23].setPassMatchs(new int[]{2,3});
		PassTypeArr[23].setText("6串35");
		PassTypeArr[23].setValue(8388608);
		PassTypeArr[23].setPassTypeValue(23);
		
		PassTypeArr[24]= new PassTypeEach();
		PassTypeArr[24].setKey("P6_42");
		PassTypeArr[24].setUnits(42);
		PassTypeArr[24].setMatchCount(6);
		PassTypeArr[24].setPassMatchs(new int[]{3,4,5,6});
		PassTypeArr[24].setText("6串42");
		PassTypeArr[24].setValue(16777216l);
		PassTypeArr[24].setPassTypeValue(24);
		
		PassTypeArr[25]= new PassTypeEach();
		PassTypeArr[25].setKey("P6_50");
		PassTypeArr[25].setUnits(50);
		PassTypeArr[25].setMatchCount(6);
		PassTypeArr[25].setPassMatchs(new int[]{2,3,4});
		PassTypeArr[25].setText("6串50");
		PassTypeArr[25].setValue(33554432l);
		PassTypeArr[25].setPassTypeValue(25);
		
		PassTypeArr[26]= new PassTypeEach();
		PassTypeArr[26].setKey("P6_57");
		PassTypeArr[26].setUnits(57);
		PassTypeArr[26].setMatchCount(6);
		PassTypeArr[26].setPassMatchs(new int[]{2,3,4,5,6});
		PassTypeArr[26].setText("6串57");
		PassTypeArr[26].setValue(67108864l);
		PassTypeArr[26].setPassTypeValue(26);
		
		PassTypeArr[27]= new PassTypeEach();
		PassTypeArr[27].setKey("P7_1");
		PassTypeArr[27].setUnits(1);
		PassTypeArr[27].setMatchCount(7);
		PassTypeArr[27].setPassMatchs(new int[]{7});
		PassTypeArr[27].setText("7串1");
		PassTypeArr[27].setValue(134217728l);
		PassTypeArr[27].setPassTypeValue(27);
		
		PassTypeArr[28]= new PassTypeEach();
		PassTypeArr[28].setKey("P7_7");
		PassTypeArr[28].setUnits(7);
		PassTypeArr[28].setMatchCount(7);
		PassTypeArr[28].setPassMatchs(new int[]{6});
		PassTypeArr[28].setText("7串7");
		PassTypeArr[28].setValue(268435456l);
		PassTypeArr[28].setPassTypeValue(28);
		
		PassTypeArr[29]= new PassTypeEach();
		PassTypeArr[29].setKey("P7_8");
		PassTypeArr[29].setUnits(8);
		PassTypeArr[29].setMatchCount(7);
		PassTypeArr[29].setPassMatchs(new int[]{6,7});
		PassTypeArr[29].setText("7串8");
		PassTypeArr[29].setValue(536870912l);
		PassTypeArr[29].setPassTypeValue(29);
		
		PassTypeArr[30]= new PassTypeEach();
		PassTypeArr[30].setKey("P7_21");
		PassTypeArr[30].setUnits(21);
		PassTypeArr[30].setMatchCount(7);
		PassTypeArr[30].setPassMatchs(new int[]{5});
		PassTypeArr[30].setText("7串21");
		PassTypeArr[30].setValue(1073741824l);
		PassTypeArr[30].setPassTypeValue(30);

		PassTypeArr[31]= new PassTypeEach();
		PassTypeArr[31].setKey("P7_35");
		PassTypeArr[31].setUnits(35);
		PassTypeArr[31].setMatchCount(7);
		PassTypeArr[31].setPassMatchs(new int[]{4});
		PassTypeArr[31].setText("7串35");
		PassTypeArr[31].setValue(2147483648l);
		PassTypeArr[31].setPassTypeValue(31);
		
		PassTypeArr[32]= new PassTypeEach();
		PassTypeArr[32].setKey("P7_120");
		PassTypeArr[32].setUnits(120);
		PassTypeArr[32].setMatchCount(7);
		PassTypeArr[32].setPassMatchs(new int[]{2,3,4,5,6,7});
		PassTypeArr[32].setText("7串120");
		PassTypeArr[32].setValue(4294967296l);
		PassTypeArr[32].setPassTypeValue(32);
		
		PassTypeArr[33]= new PassTypeEach();
		PassTypeArr[33].setKey("P8_1");
		PassTypeArr[33].setUnits(1);
		PassTypeArr[33].setMatchCount(8);
		PassTypeArr[33].setPassMatchs(new int[]{8});
		PassTypeArr[33].setText("8串1");
		PassTypeArr[33].setValue(8589934592l);
		PassTypeArr[33].setPassTypeValue(33);
		
		PassTypeArr[34]= new PassTypeEach();
		PassTypeArr[34].setKey("P8_8");
		PassTypeArr[34].setUnits(8);
		PassTypeArr[34].setMatchCount(8);
		PassTypeArr[34].setPassMatchs(new int[]{7});
		PassTypeArr[34].setText("8串8");
		PassTypeArr[34].setValue(17179869184l);
		PassTypeArr[34].setPassTypeValue(34);
		
		PassTypeArr[35]= new PassTypeEach();
		PassTypeArr[35].setKey("P8_9");
		PassTypeArr[35].setUnits(9);
		PassTypeArr[35].setMatchCount(8);
		PassTypeArr[35].setPassMatchs(new int[]{7,8});
		PassTypeArr[35].setText("8串9");
		PassTypeArr[35].setValue(34359738368l);
		PassTypeArr[35].setPassTypeValue(35);
		
		PassTypeArr[36]= new PassTypeEach();
		PassTypeArr[36].setKey("P8_28");
		PassTypeArr[36].setUnits(28);
		PassTypeArr[36].setMatchCount(8);
		PassTypeArr[36].setPassMatchs(new int[]{6});
		PassTypeArr[36].setText("8串28");
		PassTypeArr[36].setValue(68719476736l);
		PassTypeArr[36].setPassTypeValue(36);
		
		PassTypeArr[37]= new PassTypeEach();
		PassTypeArr[37].setKey("P8_56");
		PassTypeArr[37].setUnits(56);
		PassTypeArr[37].setMatchCount(8);
		PassTypeArr[37].setPassMatchs(new int[]{5});
		PassTypeArr[37].setText("8串56");
		PassTypeArr[37].setValue(137438953472l);
		PassTypeArr[37].setPassTypeValue(37);
		
		PassTypeArr[38]= new PassTypeEach();
		PassTypeArr[38].setKey("P8_70");
		PassTypeArr[38].setUnits(70);
		PassTypeArr[38].setMatchCount(8);
		PassTypeArr[38].setPassMatchs(new int[]{4});
		PassTypeArr[38].setText("8串70");
		PassTypeArr[38].setValue(274877906944l);
		PassTypeArr[38].setPassTypeValue(38);
		
		
		PassTypeArr[39]= new PassTypeEach();
		PassTypeArr[39].setKey("P8_247");
		PassTypeArr[39].setUnits(247);
		PassTypeArr[39].setMatchCount(8);
		PassTypeArr[39].setPassMatchs(new int[]{2, 3, 4, 5, 6, 7, 8});
		PassTypeArr[39].setText("8串247");
		PassTypeArr[39].setValue(549755813888l);
		PassTypeArr[39].setPassTypeValue(39);
	}
}
