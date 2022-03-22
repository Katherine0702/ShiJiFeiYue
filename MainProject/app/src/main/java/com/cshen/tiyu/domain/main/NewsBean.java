package com.cshen.tiyu.domain.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/8/31.
 */

public class NewsBean {

    /**
     * newslist : [{"addTime":"04-27 00:00","author":"","id":"689","imageUrl":"http://218.4.111.38:37091/imgShare/file/other/201704271401260342772.jpg","isTop":1,"title":"昆山局强化农贸市场食品安全昆山局强化农贸市场食品安全昆山局强化农贸市场食品安全","viewCount":58},{"addTime":"04-30 21:45","author":"","id":"290","imageUrl":"http://218.4.111.38:37091/imgShare/file/other/201604302145066019665.png","isTop":1,"title":"集体用餐配送单位抽检信息公告、\u201c集体用餐配\u201c送单位抽检信息公告","viewCount":3},{"addTime":"04-22 16:10","author":"","id":"268","imageUrl":"http://218.4.111.38:37091/imgShare/file/other/201604221609214016935.jpg","isTop":1,"title":"姑苏区召开春季学校食堂食品安全检查通报会","viewCount":14}]
     * processId : 0
     * errMsg :
     */

    private String processId;
    private String errMsg;
    private ArrayList<NewslistBean> newslist;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public ArrayList<NewslistBean> getNewslist() {
        return newslist;
    }

    public void setNewslist(ArrayList<NewslistBean> newslist) {
        this.newslist = newslist;
    }

    @Table(name = "NewslistBean")
    public static class NewslistBean implements Serializable{
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
         * addTime : 04-27 00:00
         * author :
         * id : 689
         * imageUrl : http://218.4.111.38:37091/imgShare/file/other/201704271401260342772.jpg
         * isTop : 1
         * title : 昆山局强化农贸市场食品安全昆山局强化农贸市场食品安全昆山局强化农贸市场食品安全
         * viewCount : 58
         */
    	@Column(name = "addTime")
        private String addTime;
    	@Column(name = "author")
        private String author;
        @Column(name = "id",isId = true)  
        private String id;
        @Column(name = "imageUrl")
        private String imageUrl;
        @Column(name = "isTop")
        private int isTop;
        @Column(name = "title")
        private String title;
        @Column(name = "viewCount")
        private int viewCount;
        @Column(name = "notice")
        private int notice;//0：不展示首頁公告 1:展示首頁公告

        public int getNotice() {
			return notice;
		}

		public void setNotice(int notice) {
			this.notice = notice;
		}

		public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getIsTop() {
            return isTop;
        }

        public void setIsTop(int isTop) {
            this.isTop = isTop;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getViewCount() {
            return viewCount;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }
    }
}
