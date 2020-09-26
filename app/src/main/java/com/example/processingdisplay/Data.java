package com.example.processingdisplay;

import cn.bmob.v3.BmobObject;

public class Data extends BmobObject {
        private String name;
        private Integer arg1,arg2;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getArg1() {
            return arg1;
        }
        public void setArg1(Integer arg1) {
            this.arg1 = arg1;
        }
        public Integer getArg2() {
            return arg2;
        }
        public void setArg2(int arg2) {
            this.arg2 = arg2;
        }

}
