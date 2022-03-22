package com.cshen.tiyu.domain.pay;


public class PayWayConfig implements Comparable<PayWayConfig> {
	private Long id;
	/** 彩种显示标题 */
	private Long payWayConfigId;
	private String title;
	/** 注释 */
	private String context;
	/** 图标 */
	private String icon;
	/** 排序 */
	private Integer position;
	private String payWayStr;
	private int payWayInt;

	private Long temp;

	public PayWayConfig() {
		super();
		temp = 0L;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPayWayConfigId() {
		return payWayConfigId;
	}

	public void setPayWayConfigId(Long payWayConfigId) {
		this.payWayConfigId = payWayConfigId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getPayWayStr() {
		return payWayStr;
	}

	public void setPayWayStr(String payWayStr) {
		this.payWayStr = payWayStr;
	}

	public int getPayWayInt() {
		return payWayInt;
	}

	public void setPayWayInt(int payWayInt) {
		this.payWayInt = payWayInt;
	}

	public Long getTemp() {
		return temp;
	}

	public void setTemp(Long temp) {
		this.temp = temp;
	}

	@Override
	public int compareTo(PayWayConfig another) {

		if (this.temp > another.temp) {
			return -1;
		} else if (this.temp < another.temp) {
			return 1;
		} else {
			if (this.position > another.position) {
				return 1;
			} else if (this.position < another.position) {
				return -1;
			} else {
				return 0;
			}
		}

	}

}
