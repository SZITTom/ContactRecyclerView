package com.szittom.contactrecyclerview.model;

import java.util.List;

/**
 * Created by SZITTom on 2016/4/1.
 */
public class CityModel {

    private CityBean City;

    public CityBean getCity() {
        return City;
    }

    public void setCity(CityBean City) {
        this.City = City;
    }

    public static class CityBean {
        /**
         * Name : 北京
         * IsHot : 1
         * ShotPY : BJ
         * simpleSpell: beijin
         * sortLetter:B
         */

        private List<CityListBean> CityList;

        public List<CityListBean> getCityList() {
            return CityList;
        }

        public void setCityList(List<CityListBean> CityList) {
            this.CityList = CityList;
        }

        public static class CityListBean {
            private String Name;
            private String IsHot;
            private String ShotPY;
            private String simpleSpell;
            private String sortLetter;

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public String getIsHot() {
                return IsHot;
            }

            public void setIsHot(String IsHot) {
                this.IsHot = IsHot;
            }

            public String getShotPY() {
                return ShotPY;
            }

            public void setShotPY(String ShotPY) {
                this.ShotPY = ShotPY;
            }

            public String getSimpleSpell() {
                return simpleSpell;
            }

            public void setSimpleSpell(String simpleSpell) {
                this.simpleSpell = simpleSpell;
            }

            public String getSortLetter() {
                return sortLetter;
            }

            public void setSortLetter(String sortLetter) {
                this.sortLetter = sortLetter;
            }
        }
    }
}
