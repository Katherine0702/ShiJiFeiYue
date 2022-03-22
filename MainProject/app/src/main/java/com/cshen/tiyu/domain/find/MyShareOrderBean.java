package com.cshen.tiyu.domain.find;

import java.util.ArrayList;

public class MyShareOrderBean {
	
	 /**
     * processId : 0
     * thesunlist : [{"addTime":"2017-09-15 12:17:52","content":"规格聚聚v","dianzan":0,"imageurl":"http://192.168.0.127:8090/upload/20170912/02eb9ad5-016c-4d5a-8b7c-8bd744c37d1e-1.png;http://192.168.0.127:8090/upload/20170912/02eb9ad5-016c-4d5a-8b7c-8bd744c37d1e-1.png","look_status":0,"theSunId":"21","userImg":"","userNama":"yx8","viewCount":0},{"addTime":"2017-09-15 12:17:50","content":"规格聚聚v","dianzan":0,"imageurl":"http://192.168.0.127:8090/upload/20170912/02eb9ad5-016c-4d5a-8b7c-8bd744c37d1e-1.png","look_status":0,"theSunId":"20","userImg":"","userNama":"yx8","viewCount":0},{"addTime":"2017-09-15 12:17:48","content":"规格聚聚v","dianzan":0,"imageurl":"http://192.168.0.127:8090/upload/20170912/02eb9ad5-016c-4d5a-8b7c-8bd744c37d1e-1.png","look_status":0,"theSunId":"19","userImg":"","userNama":"yx8","viewCount":0},{"addTime":"2017-09-15 12:17:44","content":"规格聚聚v","dianzan":0,"imageurl":"http://192.168.0.127:8090/upload/20170912/02eb9ad5-016c-4d5a-8b7c-8bd744c37d1e-1.png","look_status":0,"theSunId":"18","userImg":"","userNama":"yx8","viewCount":0},{"addTime":"2017-09-15 12:17:38","content":"规格聚聚v","dianzan":0,"imageurl":"http://192.168.0.127:8090/upload/20170912/02eb9ad5-016c-4d5a-8b7c-8bd744c37d1e-1.png","look_status":0,"theSunId":"17","userImg":"","userNama":"yx8","viewCount":0},{"addTime":"2017-09-15 12:17:35","content":"规格聚聚v","dianzan":0,"imageurl":"http://192.168.0.127:8090/upload/20170912/02eb9ad5-016c-4d5a-8b7c-8bd744c37d1e-1.png","look_status":0,"theSunId":"16","userImg":"","userNama":"yx8","viewCount":0},{"addTime":"2017-09-15 12:14:17","content":"规格聚聚v","dianzan":0,"imageurl":"http://192.168.0.127:8090/upload/20170912/02eb9ad5-016c-4d5a-8b7c-8bd744c37d1e-1.png","look_status":0,"theSunId":"13","userImg":"","userNama":"yx8","viewCount":0},{"addTime":"2017-09-15 12:14:17","content":"规格聚聚v","dianzan":0,"imageurl":"http://192.168.0.127:8090/upload/20170912/02eb9ad5-016c-4d5a-8b7c-8bd744c37d1e-1.png","look_status":0,"theSunId":"14","userImg":"","userNama":"yx8","viewCount":0},{"addTime":"2017-09-15 12:14:17","content":"规格聚聚v","dianzan":0,"imageurl":"http://192.168.0.127:8090/upload/20170912/02eb9ad5-016c-4d5a-8b7c-8bd744c37d1e-1.png","look_status":0,"theSunId":"15","userImg":"","userNama":"yx8","viewCount":0},{"addTime":"2017-09-15 11:51:27","content":"你奖学金就放假好烦好烦就发还发好","dianzan":0,"imageurl":"http://192.168.0.127:8090/upload/20170912/02eb9ad5-016c-4d5a-8b7c-8bd744c37d1e-1.png","look_status":0,"theSunId":"12","userImg":"","userNama":"yx8","viewCount":0}]
     */

    private int processId;
    private ArrayList<ThesunlistBean> thesunlist;

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public ArrayList<ThesunlistBean> getThesunlist() {
        return thesunlist;
    }

    public void setThesunlist(ArrayList<ThesunlistBean> thesunlist) {
        this.thesunlist = thesunlist;
    }

    public static class ThesunlistBean {
        /**
         * addTime : 2017-09-15 12:17:52
         * content : 规格聚聚v
         * dianzan : 0  2：未点赞 1：以点赞
         * imageurl : http://192.168.0.127:8090/upload/20170912/02eb9ad5-016c-4d5a-8b7c-8bd744c37d1e-1.png;http://192.168.0.127:8090/upload/20170912/02eb9ad5-016c-4d5a-8b7c-8bd744c37d1e-1.png
         * look_status : 0  审核标志  0:等待审核 1:审核通过  2:审核不通过
         * theSunId : 21
         * userImg :
         * userNama : yx8
         * viewCount : 0 点赞数
         * reason: 审核不通过原因
         * orderRate:本单盈利率
         * authId:
         */

        private String addTime;
        private String content;
        private int dianzan;
        private String imageurl;
        private int look_status;
        private String theSunId;
        private String userImg;
        private String userNama;
        private int viewCount;
        private String reason;
        private String orderRate;
        private String authId;
        
        
        

        public String getAuthId() {
			return authId;
		}

		public void setAuthId(String authId) {
			this.authId = authId;
		}

		public String getOrderRate() {
			return orderRate;
		}

		public void setOrderRate(String orderRate) {
			this.orderRate = orderRate;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

		public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getDianzan() {
            return dianzan;
        }

        public void setDianzan(int dianzan) {
            this.dianzan = dianzan;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public int getLook_status() {
            return look_status;
        }

        public void setLook_status(int look_status) {
            this.look_status = look_status;
        }

        public String getTheSunId() {
            return theSunId;
        }

        public void setTheSunId(String theSunId) {
            this.theSunId = theSunId;
        }

        public String getUserImg() {
            return userImg;
        }

        public void setUserImg(String userImg) {
            this.userImg = userImg;
        }

        public String getUserNama() {
            return userNama;
        }

        public void setUserNama(String userNama) {
            this.userNama = userNama;
        }

        public int getViewCount() {
            return viewCount;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }
    }

}
