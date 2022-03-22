package com.cshen.tiyu.activity.lottery.Fast3;

import java.util.List;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.lottery.OrderDetailActivity;
import com.cshen.tiyu.domain.Scheme;
import com.cshen.tiyu.domain.fast3.Fast3CompoundContent;
import com.cshen.tiyu.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class Fast3OrderDetailActivity extends OrderDetailActivity {
	private LinearLayout ll_result_number;
	private TextView  textview_periodNumber,textview_content,
	textview_result_title,textview_result_n01,textview_result_n02,textview_result_n03;
	private View view_result;
	@Override
	public void onCreateView() {
		setContentView(R.layout.activity_detail_sdel11to5);
		initView();
	}
	private void initView() {
		ll_result_number=(LinearLayout) findViewById(R.id.ll_result_number);
		textview_periodNumber= (TextView) findViewById(R.id.textview_periodNumber);
		textview_content= (TextView) findViewById(R.id.textview_content);
		textview_result_title=(TextView) findViewById(R.id.textview_result_title);
		textview_result_n01= (TextView) findViewById(R.id.textview_result_n01);
		textview_result_n02= (TextView) findViewById(R.id.textview_result_n02);
		textview_result_n03= (TextView) findViewById(R.id.textview_result_n03);
		findViewById(R.id.textview_result_n04).setVisibility(View.INVISIBLE);
		findViewById(R.id.textview_result_n05).setVisibility(View.INVISIBLE);
		view_result=findViewById(R.id.view_result);
	}
	@Override
	public void setData(String result,String state){
		textview_periodNumber.setText("第"+scheme.getPeriodNumber()+"期");
		String contents = getSchemeContent(scheme,result);
		if (contents != null) {
			textview_content.setText(Html.fromHtml(contents));
		}
		if (!TextUtils.isEmpty(result)) {
			String[] resultNumber=result.split(",");
			try {
				textview_result_n01.setText("0"+resultNumber[0]);
				textview_result_n02.setText("0"+resultNumber[1]);
				textview_result_n03.setText("0"+resultNumber[2]);
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
		String typeName=Util.getfast3TypeName(scheme.getBetType());
		String content=scheme.getContent();
		StringBuilder showContent=new StringBuilder("");
		if (content!=null && content.contains("[")) {
			Gson g=new Gson();
			List<Fast3CompoundContent> contents=g.fromJson(content, new TypeToken<List<Fast3CompoundContent>>(){}.getType());
			for (int i = 0; i < contents.size(); i++) {
				if (checkContents(contents.get(i).getBetDanList())) {
					showContent.append(typeName);
					showContent.append(" | ");
					showContent.append("<font color='#ec3634'>  (胆) </font>");
					for (int j = 0; j < contents.get(i).getBetDanList().size(); j++) {
						showContent.append(getContentGroupNumberString(contents.get(i).getBetDanList().get(j),resultNumbers,3)+" ");
					}
					showContent.append("|");
					if (checkContents(contents.get(i).getBetList())) {
						for (int j = 0; j < contents.get(i).getBetList().size(); j++) {
							showContent.append(getContentGroupNumberString(contents.get(i).getBetList().get(j),resultNumbers,3)+" ");
						}
					}
					if (i!=contents.size()-1) {
						showContent.append("<br/> ");
					}
				}else{
					if ("HeZhi".equals(scheme.getBetType()) ) {
						showContent.append(typeName);
						showContent.append(" | ");
						if (checkContents(contents.get(i).getBetList())) {
							int hezhi = 0;
							if(resultNumbers!=null){
								for(int m= 0 ;m<resultNumbers.length;m++){
									hezhi = hezhi+Integer.parseInt(resultNumbers[m]);
								}
							}
							for (int j = 0; j < contents.get(i).getBetList().size(); j++) {
								showContent.append(getContentGroupNumberString(contents.get(i).getBetList().get(j),new String[]{hezhi+""},1)+" ");
							}

						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}else if ("TwoFX".equals(scheme.getBetType()) ) {//二同号
						showContent.append(typeName);
						showContent.append(" | ");
						if (checkContents(contents.get(i).getBetList())) {
							for (int j = 0; j < contents.get(i).getBetList().size(); j++) {							
								String samenumber = contents.get(i).getBetList().get(j).substring(0,1);
								if(resultNumber!=null&&resultNumber.contains(samenumber)){
									if(resultNumber.indexOf(samenumber) == resultNumber.lastIndexOf(samenumber+"") ){					
										showContent.append("<font color='#ec3634'>"+samenumber+"</font>");
										showContent.append(samenumber+" ");											
									}else{
										showContent.append("<font color='#ec3634'>"+contents.get(i).getBetList().get(j)+"</font>"+" ");

									}
								}else{
									showContent.append(contents.get(i).getBetList().get(j)+" ");		
								}
							}
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}else if ("TwoDX".equals(scheme.getBetType()) ) {//二同号
						showContent.append(typeName);
						showContent.append(" | ");
						if (checkContents(contents.get(i).getBetList())) {
							for (int j = 0; j < contents.get(i).getBetList().size(); j++) {
								String samenumber = contents.get(i).getBetList().get(j).substring(0,1);
								if(resultNumber!=null&&resultNumber.contains(samenumber)){
									if(resultNumber.indexOf(samenumber) == resultNumber.lastIndexOf(samenumber) ){					
										showContent.append("<font color='#ec3634'>"+samenumber+"</font>");
										showContent.append(samenumber+" ");											
									}else{
										showContent.append("<font color='#ec3634'>"+contents.get(i).getBetList().get(j)+"</font>"+" ");

									}
								}else{
									showContent.append(contents.get(i).getBetList().get(j)+" ");		
								}
							}
						}
						showContent.append("#");
						if (checkContents(contents.get(i).getDisList())) {
							for (int j = 0; j < contents.get(i).getDisList().size(); j++) {
								showContent.append(getContentGroupNumberString(contents.get(i).getDisList().get(j),resultNumbers,3)+" ");				
							}
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}else if("ThreeLX".equals(scheme.getBetType())){//三连号
						if (resultNumbers!=null && resultNumbers.length == 3) {
							if(Integer.parseInt(resultNumbers[1]) - Integer.parseInt(resultNumbers[0]) ==1&&
									Integer.parseInt(resultNumbers[2]) - Integer.parseInt(resultNumbers[1])==1){
								showContent.append("<font color='#ec3634'>"+typeName+"</font>");
							}else{
								showContent.append(typeName);
							}
						}else{
							showContent.append(typeName);
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}else if("ThreeTX".equals(scheme.getBetType())){//三同号通选
						if (resultNumbers!=null && resultNumbers.length == 3) {
							if(Integer.parseInt(resultNumbers[0]) == Integer.parseInt(resultNumbers[1])&&
									Integer.parseInt(resultNumbers[0])== Integer.parseInt(resultNumbers[2])){
								showContent.append("<font color='#ec3634'>"+typeName+"</font>");
							}else{
								showContent.append(typeName);
							}
						}else{
							showContent.append(typeName);
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}else if ("ThreeDX".equals(scheme.getBetType()) ) {//三同号
						showContent.append(typeName);
						showContent.append(" | ");

						if (checkContents(contents.get(i).getBetList())) {
							for (int j = 0; j < contents.get(i).getBetList().size(); j++) {
								if(resultNumbers!=null&&resultNumbers.length ==3){
									if(Integer.parseInt(resultNumbers[0]) == Integer.parseInt(contents.get(i).getBetList().get(j).substring(0,1))){
										showContent.append("<font color='#ec3634'>"+contents.get(i).getBetList().get(j).substring(0,1)+"</font>");
									}else{
										showContent.append(contents.get(i).getBetList().get(j).substring(0,1));
									}
									if(Integer.parseInt(resultNumbers[1]) == Integer.parseInt(contents.get(i).getBetList().get(j).substring(1,2))){
										showContent.append("<font color='#ec3634'>"+contents.get(i).getBetList().get(j).substring(0,1)+"</font>");
									}else{
										showContent.append(contents.get(i).getBetList().get(j).substring(0,1));
									}
									if(Integer.parseInt(resultNumbers[2]) == Integer.parseInt(contents.get(i).getBetList().get(j).substring(2,3))){
										showContent.append("<font color='#ec3634'>"+contents.get(i).getBetList().get(j).substring(0,1)+"</font>"+" ");
									}else{
										showContent.append(contents.get(i).getBetList().get(j).substring(0,1)+" ");
									}
								}else{
									showContent.append(contents.get(i).getBetList().get(j)+" ");
								}
							}
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}else {
						showContent.append(typeName);
						showContent.append(" | ");
						if (checkContents(contents.get(i).getBetList())) {
							for (int j = 0; j < contents.get(i).getBetList().size(); j++) {
								showContent.append(getContentGroupNumberString(contents.get(i).getBetList().get(j),resultNumbers,3)+" ");
							}
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}
				}
			}
		}
		return showContent.toString();
	}
	private String getContentGroupNumberString(String number,
			String[] resultNumbers, int size) {
		try {
			if (resultNumbers!=null && resultNumbers.length>0) {
				for (int i = 0; i < size; i++) {
					if (number.equals(resultNumbers[i])) {
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
	private boolean checkContents(List<String> contents) {
		if (contents!=null && contents.size()>0) {
			return true;
		}else{
			return false;
		}
	}
}
