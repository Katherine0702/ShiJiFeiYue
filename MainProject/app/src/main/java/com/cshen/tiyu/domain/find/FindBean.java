package com.cshen.tiyu.domain.find;

public class FindBean {
	 /**
     * addedBy : 1
     * addedByName : 管理员
     * addedTime : {"date":13,"day":3,"hours":16,"minutes":47,"month":8,"nanos":0,"seconds":4,"time":1505292424000,"timezoneOffset":-480,"year":117}
     * icon : http://localhost:8081/upload/20170913/bifen3990c269-e788-4ebf-a225-1c87a26af025.gif
     * id : 4
     * info : 查看赛事实时状态
     * modifiedBy : 1
     * modifiedByName : 管理员
     * modifiedTime : {"date":13,"day":3,"hours":17,"minutes":10,"month":8,"nanos":0,"seconds":38,"time":1505293838000,"timezoneOffset":-480,"year":117}
     * rank : 1
     * status : 1
     * title : 比分直播
     * tkey : BFZB
     * url : http://hbf.58cainiu.com/live
     * useLocal : 0
     */
    private String icon;
    private int id;
    private String info;
    private int rank;
    private int status;
    private String title;
    private String tkey;
    private String url;
    private int useLocal;
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTkey() {
		return tkey;
	}
	public void setTkey(String tkey) {
		this.tkey = tkey;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getUseLocal() {
		return useLocal;
	}
	public void setUseLocal(int useLocal) {
		this.useLocal = useLocal;
	}
    
}
