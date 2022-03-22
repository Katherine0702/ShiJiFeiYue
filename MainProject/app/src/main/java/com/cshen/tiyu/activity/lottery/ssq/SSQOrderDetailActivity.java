package com.cshen.tiyu.activity.lottery.ssq;

import java.util.Arrays;
import java.util.List;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.lottery.OrderDetailActivity;
import com.cshen.tiyu.domain.Scheme;
import com.cshen.tiyu.domain.dltssq.DltCompoundContent;
import com.cshen.tiyu.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class SSQOrderDetailActivity extends OrderDetailActivity {
	private LinearLayout ll_result_number;
	private TextView textview_periodNumber,textview_content,textview_result_title,
	textview_result_n01, textview_result_n02,textview_result_n03,textview_result_n04,
	textview_result_n05,textview_result_n06,textview_result_n07;
	private View view_result;
	@Override
	public void onCreateView() {
		setContentView(R.layout.activity_detail_dlt);
		initView();
	}
	private void initView() {
		ll_result_number=(LinearLayout) findViewById(R.id.ll_result_number);
		textview_periodNumber = (TextView) findViewById(R.id.textview_periodNumber);
		textview_content = (TextView) findViewById(R.id.textview_content);
		textview_result_title = (TextView) findViewById(R.id.textview_result_title);
		textview_result_n01 = (TextView) findViewById(R.id.textview_result_n01);
		textview_result_n02 = (TextView) findViewById(R.id.textview_result_n02);
		textview_result_n03 = (TextView) findViewById(R.id.textview_result_n03);
		textview_result_n04 = (TextView) findViewById(R.id.textview_result_n04);
		textview_result_n05 = (TextView) findViewById(R.id.textview_result_n05);
		textview_result_n06 = (TextView) findViewById(R.id.textview_result_n061);
		textview_result_n06.setVisibility(View.VISIBLE);
		findViewById(R.id.textview_result_n06).setVisibility(View.GONE);
		textview_result_n07 = (TextView) findViewById(R.id.textview_result_n07);
		view_result = findViewById(R.id.view_result);
	}
	@Override
	public void setData(String result,String state){
		textview_periodNumber.setText("第"+ scheme.getPeriodNumber() + "期");
		String contents = getSchemeContent(scheme,result);
		if (contents != null) {
			textview_content.setText(Html.fromHtml(contents));
		}
		if (!TextUtils.isEmpty(result)) {
			String[] resultNumber = result.split(",");
			try {
				textview_result_n01.setText(Util.periodNumberFromat(resultNumber[0]));
				textview_result_n02.setText(Util.periodNumberFromat(resultNumber[1]));
				textview_result_n03.setText(Util.periodNumberFromat(resultNumber[2]));
				textview_result_n04.setText(Util.periodNumberFromat(resultNumber[3]));
				textview_result_n05.setText(Util.periodNumberFromat(resultNumber[4]));
				textview_result_n06.setText(Util.periodNumberFromat(resultNumber[5]));
				textview_result_n07.setText(Util.periodNumberFromat(resultNumber[6]));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			textview_result_title.setVisibility(View.VISIBLE);
			ll_result_number.setVisibility(View.VISIBLE);
			view_result.setVisibility(View.VISIBLE);
		} else {
			textview_result_title.setVisibility(View.GONE);
			ll_result_number.setVisibility(View.GONE);
			view_result.setVisibility(View.GONE);
		}

	}
	private String getSchemeContent(Scheme scheme, String resultNumber) {
		String[] resultNumbers = null;
		if (!TextUtils.isEmpty(resultNumber)) {
			resultNumbers = resultNumber.split(",");
		}
		String content = scheme.getContent();
		StringBuilder showContent = new StringBuilder("");
		if (content != null && content.contains("[")) {
			Gson g = new Gson();
			List<DltCompoundContent> contents = g.fromJson(content,
					new TypeToken<List<DltCompoundContent>>() {
			}.getType());
			if (contents != null && contents.size() > 0) {
				for (int i = 0; i < contents.size(); i++) {
					if (checkContents(contents.get(i).getRedDanList())) {
						showContent.append("<font color='#ec3634'>  (胆) </font>");
						for (int j = 0; j < contents.get(i).getRedDanList().size(); j++) {
							showContent.append(getContentNumberString(contents.get(i).getRedDanList().get(j),
									resultNumbers!=null && resultNumbers.length==7?	Arrays.copyOfRange(resultNumbers, 0, 5):null) + " ");
						}
						showContent.append("|");
					}
					if (checkContents(contents.get(i).getRedList())) {
						for (int j = 0; j < contents.get(i).getRedList().size(); j++) {
							showContent.append(getContentNumberString(contents.get(i).getRedList().get(j),
									resultNumbers!=null && resultNumbers.length==7?	Arrays.copyOfRange(resultNumbers, 0, 5):null) + " ");
						}
					}
					showContent.append(" + ");
					if (checkContents(contents.get(i).getBlueDanList())) {
						for (int j = 0; j < contents.get(i).getBlueDanList().size(); j++) {
							showContent.append(getContentNumberString(contents.get(i).getBlueDanList().get(j),
									resultNumbers!=null && resultNumbers.length==7?	Arrays.copyOfRange(resultNumbers, 5,7):null) + " ");
						}
						showContent.append("|");
					}
					if (checkContents(contents.get(i).getBlueList())) {
						for (int j = 0; j < contents.get(i).getBlueList().size(); j++) {
							showContent.append(getContentNumberString(contents.get(i).getBlueList().get(j),
									resultNumbers!=null && resultNumbers.length==7?	Arrays.copyOfRange(resultNumbers, 5,7):null) + " ");
						}
					}
					if (i!=contents.size()-1) {
						showContent.append("<br/> ");
					}
				}
			}
		} else {
			String[] numberContent = content.split(" ");
			if (numberContent!=null && numberContent.length==7) {
				for (int i = 0; i < numberContent.length; i++) {
					if (i<6) {
						showContent.append(getContentNumberString(numberContent[i],
								resultNumbers!=null && resultNumbers.length==7?	Arrays.copyOfRange(resultNumbers, 0, 6):null) + " ");
					}
					if (i == 6) {
						showContent.append(" + ");
					}
					if (i >= 6) {
						showContent.append(getContentNumberString(numberContent[i],
								resultNumbers!=null && resultNumbers.length==7?	Arrays.copyOfRange(resultNumbers, 6, 7):null) + " ");
					}
				}
			}

		}
		return showContent.toString();
	}

	private boolean checkContents(List<String> contents) {
		if (contents != null && contents.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	private String getContentNumberString(String number, String[] resultNumbers) {
		number = Util.periodNumberFromat(number);
		if (resultNumbers != null && resultNumbers.length > 0) {
			for (int i = 0; i < resultNumbers.length; i++) {
				if (number.equals( Util.periodNumberFromat(resultNumbers[i]))) {
					number = "<font color='#FF3232'>" + number + "</font>";
				}
			}
		}
		return number;
	}

}
