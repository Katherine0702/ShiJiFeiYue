package com.cshen.tiyu.activity.lottery.cai115;

import java.util.List;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.lottery.OrderDetailActivity;
import com.cshen.tiyu.domain.Scheme;
import com.cshen.tiyu.domain.cai115.El11to5CompoundContent;
import com.cshen.tiyu.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class EL11TO5OrderDetailActivity extends OrderDetailActivity {
	private LinearLayout ll_result_number;
	private TextView  textview_periodNumber,textview_content,
	textview_result_title,textview_result_n01,textview_result_n02,
	textview_result_n03,textview_result_n04,textview_result_n05;
	private View view_result;

	@Override
	public void onCreateView() {
		setContentView(R.layout.activity_detail_sdel11to5);
		initView();
	}
	private void initView() {
		ll_result_number=(LinearLayout) findViewById(R.id.ll_result_number);
		textview_periodNumber = (TextView) findViewById(R.id.textview_periodNumber);
		textview_content= (TextView) findViewById(R.id.textview_content);
		textview_result_title=(TextView) findViewById(R.id.textview_result_title);
		textview_result_n01= (TextView) findViewById(R.id.textview_result_n01);
		textview_result_n02= (TextView) findViewById(R.id.textview_result_n02);
		textview_result_n03= (TextView) findViewById(R.id.textview_result_n03);
		textview_result_n04= (TextView) findViewById(R.id.textview_result_n04);
		textview_result_n05= (TextView) findViewById(R.id.textview_result_n05);
		view_result=findViewById(R.id.view_result);
	}
	@Override
	public void setData(String result,String state){
		textview_periodNumber.setText("第"+scheme.getPeriodNumber()+"期");
		String contents = getSchemeContent(scheme,result);
		if (contents!=null) {
			textview_content.setText(Html.fromHtml(contents));
		}
		if (!TextUtils.isEmpty(result)) {
			String[] resultNumber=result.split(",");
			try {
				textview_result_n01.setText(resultNumber[0]);
				textview_result_n02.setText(resultNumber[1]);
				textview_result_n03.setText(resultNumber[2]);
				textview_result_n04.setText(resultNumber[3]);
				textview_result_n05.setText(resultNumber[4]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			textview_result_title.setVisibility(View.VISIBLE);
			ll_result_number.setVisibility(View.VISIBLE);
			view_result.setVisibility(View.VISIBLE);
		}else{
			textview_result_title.setVisibility(View.GONE);
			ll_result_number.setVisibility(View.GONE);
			view_result.setVisibility(View.GONE);
		}
	}
	private String getSchemeContent(Scheme scheme, String resultNumber) {
		String [] resultNumbers=null;
		 if (!TextUtils.isEmpty(resultNumber)) {
			resultNumbers=resultNumber.split(",");
		}
		String typeName=Util.get11to5TypeName(scheme.getBetType());
		String content=scheme.getContent();
		StringBuilder showContent=new StringBuilder("");
		if (content!=null && content.contains("[")) {
			Gson g=new Gson();
			List<El11to5CompoundContent> contents=g.fromJson(content, new TypeToken<List<El11to5CompoundContent>>(){}.getType());
			if (contents!=null && contents.size()>0) {
				for (int i = 0; i < contents.size(); i++) {
					showContent.append(typeName).append(" | ");
					if ("ForeTwoDirect".equals(scheme.getBetType()) || "ForeThreeDirect".equals(scheme.getBetType()) ) {
						if (checkContents(contents.get(i).getBet1List())) {
							for (int j = 0; j < contents.get(i).getBet1List().size(); j++) {
								showContent.append(getDirectNumberString(contents.get(i).getBet1List().get(j),resultNumbers,1)+" ");
							}
						}
						if (checkContents(contents.get(i).getBet2List())) {
							showContent.append("| ");
							for (int j = 0; j < contents.get(i).getBet2List().size(); j++) {
								showContent.append(getDirectNumberString(contents.get(i).getBet2List().get(j),resultNumbers,2)+" ");
							}
						}
						if (checkContents(contents.get(i).getBet3List())) {
							showContent.append("| ");
							for (int j = 0; j < contents.get(i).getBet3List().size(); j++) {
								showContent.append(getDirectNumberString(contents.get(i).getBet3List().get(j),resultNumbers,3)+" ");
							}
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}else if("NormalOne".equals(scheme.getBetType())){
						if (checkContents(contents.get(i).getBetDanList())) {
							showContent.append("<font color='#ec3634'>  (胆) </font>");
							for (int j = 0; j < contents.get(i).getBetDanList().size(); j++) {
								showContent.append(getContentGroupNumberString(contents.get(i).getBetDanList().get(j),resultNumbers,1)+" ");
							}
							showContent.append("| ");
						}
						if (checkContents(contents.get(i).getBetList())) {
							for (int j = 0; j < contents.get(i).getBetList().size(); j++) {
								showContent.append(getContentGroupNumberString(contents.get(i).getBetList().get(j),resultNumbers,1)+" ");
							}
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}else if("ForeTwoGroup".equals(scheme.getBetType())){
						if (checkContents(contents.get(i).getBetDanList())) {
							showContent.append("<font color='#ec3634'>  (胆) </font>");
							for (int j = 0; j < contents.get(i).getBetDanList().size(); j++) {
								showContent.append(getContentGroupNumberString(contents.get(i).getBetDanList().get(j),resultNumbers,2)+" ");
							}
							showContent.append("| ");
						}
						if (checkContents(contents.get(i).getBetList())) {
							for (int j = 0; j < contents.get(i).getBetList().size(); j++) {
								showContent.append(getContentGroupNumberString(contents.get(i).getBetList().get(j),resultNumbers,2)+" ");
							}
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}else if("ForeThreeGroup".equals(scheme.getBetType())){
						if (checkContents(contents.get(i).getBetDanList())) {
							showContent.append("<font color='#ec3634'>  (胆) </font>");
							for (int j = 0; j < contents.get(i).getBetDanList().size(); j++) {
								showContent.append(getContentGroupNumberString(contents.get(i).getBetDanList().get(j),resultNumbers,3)+" ");
							}
							showContent.append("| ");
						}
						if (checkContents(contents.get(i).getBetList())) {
							for (int j = 0; j < contents.get(i).getBetList().size(); j++) {
								showContent.append(getContentGroupNumberString(contents.get(i).getBetList().get(j),resultNumbers,3)+" ");
							}
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}else{
						if (checkContents(contents.get(i).getBetDanList())) {
							showContent.append("<font color='#ec3634'>  (胆) </font>");
							for (int j = 0; j < contents.get(i).getBetDanList().size(); j++) {
								showContent.append(getContentNumberString(contents.get(i).getBetDanList().get(j),resultNumbers)+" ");
							}
							showContent.append("| ");
						}
						if (checkContents(contents.get(i).getBetList())) {
							for (int j = 0; j < contents.get(i).getBetList().size(); j++) {
								showContent.append(getContentNumberString(contents.get(i).getBetList().get(j),resultNumbers)+" ");
							}
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}
				}
			}

		}else{
			String[] numberContent=content.split(" ");
			showContent.append(typeName).append(" | ");
			if (numberContent!=null && numberContent.length>0) {
				for (int i = 0; i < numberContent.length; i++) {
					showContent.append(getContentNumberString(numberContent[i],resultNumbers)+" ");
				}
			}

		}
		return showContent.toString();
	}
	private String getContentGroupNumberString(String number,
			String[] resultNumbers, int size) {
		try {
			number=Util.periodNumberFromat(number);
			if (resultNumbers!=null && resultNumbers.length>0) {
				for (int i = 0; i < size; i++) {
					if (number.equals( Util.periodNumberFromat(resultNumbers[i]))) {
						number ="<font color='#ec3634'>"+number+"</font>";
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return number;
	}
	private String getDirectNumberString(String number, String[] resultNumbers,
			int index) {
		try {
			number=Util.periodNumberFromat(number);
			if (resultNumbers!=null && resultNumbers.length>0) {
				if (number.equals( Util.periodNumberFromat(resultNumbers[index-1]))) {
					number ="<font color='#ec3634'>"+number+"</font>";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return number;
	}
	private boolean checkContents(List<String> contents) {
		if (contents!=null && contents.size()>0) {
			return true;
		}else{
			return false;
		}
	}
	private String getContentNumberString(String number, String[] resultNumbers) {
		try {
			number=Util.periodNumberFromat(number);
			if (resultNumbers!=null && resultNumbers.length>0) {
				for (int i = 0; i < resultNumbers.length; i++) {
					if (number.equals( Util.periodNumberFromat(resultNumbers[i]))) {
						number ="<font color='#FF3232'>"+number+"</font>";
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return number;
	}
}
