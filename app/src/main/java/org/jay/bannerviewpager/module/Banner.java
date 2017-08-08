package org.jay.bannerviewpager.module;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jay on 2017/8/8.
 */

public class Banner implements Serializable{

    /**
     * code : 1
     * message : SUCCESS
     * data : {"timestamp":"2017-08-08","total":5,"banners":[{"id":8,"title":"Member Satisfaction Survey","landing_page":1,"type":1,"description":"","link":"","target_url":"http://tiny.cc/reworksurvey","image":"https://s3-ap-southeast-1.amazonaws.com/rework-bucket-live/communityBanners/7dc1aef3af7e790023f4906dae750443.jpg","sort":0,"deleted":0,"created_at":"2017-06-08 16:23:57","updated_at":"2017-06-09 10:33:27"},{"id":2,"title":"New Location Kuningan","landing_page":0,"type":1,"description":"New Location Kuningan","link":"3","target_url":"","image":"https://s3-ap-southeast-1.amazonaws.com/rework-bucket-live/communityBanners/47046298e457d5c304188a1d3d718acd.jpg","sort":1,"deleted":0,"created_at":"2017-04-20 14:58:02","updated_at":"2017-04-25 16:51:18"},{"id":3,"title":"Rework App","landing_page":0,"type":1,"description":"Rework App","link":"1","target_url":"","image":"https://s3-ap-southeast-1.amazonaws.com/rework-bucket-live/communityBanners/55cb8a7cf61c950ed5e8814c23ead0d9.jpg","sort":2,"deleted":0,"created_at":"2017-04-20 14:58:24","updated_at":"2017-04-21 11:26:07"},{"id":4,"title":"Rework Balance","landing_page":0,"type":1,"description":"Rework Balance","link":"1","target_url":"","image":"https://s3-ap-southeast-1.amazonaws.com/rework-bucket-live/communityBanners/4b8d75d5a2152543ec387826c9c2c330.jpg","sort":3,"deleted":0,"created_at":"2017-04-20 14:58:49","updated_at":"2017-04-21 11:27:17"},{"id":5,"title":"Become a Member","landing_page":0,"type":1,"description":"Become a member","link":"1","target_url":"","image":"https://s3-ap-southeast-1.amazonaws.com/rework-bucket-live/communityBanners/34acc8c5119227ce22202893723a4623.jpg","sort":4,"deleted":0,"created_at":"2017-04-20 14:59:28","updated_at":"2017-04-21 11:27:32"}]}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * timestamp : 2017-08-08
         * total : 5
         * banners : [{"id":8,"title":"Member Satisfaction Survey","landing_page":1,"type":1,"description":"","link":"","target_url":"http://tiny.cc/reworksurvey","image":"https://s3-ap-southeast-1.amazonaws.com/rework-bucket-live/communityBanners/7dc1aef3af7e790023f4906dae750443.jpg","sort":0,"deleted":0,"created_at":"2017-06-08 16:23:57","updated_at":"2017-06-09 10:33:27"},{"id":2,"title":"New Location Kuningan","landing_page":0,"type":1,"description":"New Location Kuningan","link":"3","target_url":"","image":"https://s3-ap-southeast-1.amazonaws.com/rework-bucket-live/communityBanners/47046298e457d5c304188a1d3d718acd.jpg","sort":1,"deleted":0,"created_at":"2017-04-20 14:58:02","updated_at":"2017-04-25 16:51:18"},{"id":3,"title":"Rework App","landing_page":0,"type":1,"description":"Rework App","link":"1","target_url":"","image":"https://s3-ap-southeast-1.amazonaws.com/rework-bucket-live/communityBanners/55cb8a7cf61c950ed5e8814c23ead0d9.jpg","sort":2,"deleted":0,"created_at":"2017-04-20 14:58:24","updated_at":"2017-04-21 11:26:07"},{"id":4,"title":"Rework Balance","landing_page":0,"type":1,"description":"Rework Balance","link":"1","target_url":"","image":"https://s3-ap-southeast-1.amazonaws.com/rework-bucket-live/communityBanners/4b8d75d5a2152543ec387826c9c2c330.jpg","sort":3,"deleted":0,"created_at":"2017-04-20 14:58:49","updated_at":"2017-04-21 11:27:17"},{"id":5,"title":"Become a Member","landing_page":0,"type":1,"description":"Become a member","link":"1","target_url":"","image":"https://s3-ap-southeast-1.amazonaws.com/rework-bucket-live/communityBanners/34acc8c5119227ce22202893723a4623.jpg","sort":4,"deleted":0,"created_at":"2017-04-20 14:59:28","updated_at":"2017-04-21 11:27:32"}]
         */

        private String timestamp;
        private int total;
        private List<BannersBean> banners;

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<BannersBean> getBanners() {
            return banners;
        }

        public void setBanners(List<BannersBean> banners) {
            this.banners = banners;
        }

        public static class BannersBean {
            /**
             * id : 8
             * title : Member Satisfaction Survey
             * landing_page : 1
             * type : 1
             * description :
             * link :
             * target_url : http://tiny.cc/reworksurvey
             * image : https://s3-ap-southeast-1.amazonaws.com/rework-bucket-live/communityBanners/7dc1aef3af7e790023f4906dae750443.jpg
             * sort : 0
             * deleted : 0
             * created_at : 2017-06-08 16:23:57
             * updated_at : 2017-06-09 10:33:27
             */

            private int id;
            private String title;
            private int landing_page;
            private int type;
            private String description;
            private String link;
            private String target_url;
            private String image;
            private int sort;
            private int deleted;
            private String created_at;
            private String updated_at;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getLanding_page() {
                return landing_page;
            }

            public void setLanding_page(int landing_page) {
                this.landing_page = landing_page;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getTarget_url() {
                return target_url;
            }

            public void setTarget_url(String target_url) {
                this.target_url = target_url;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getDeleted() {
                return deleted;
            }

            public void setDeleted(int deleted) {
                this.deleted = deleted;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            @Override
            public String toString() {
                return "BannersBean{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", landing_page=" + landing_page +
                        ", type=" + type +
                        ", description='" + description + '\'' +
                        ", link='" + link + '\'' +
                        ", target_url='" + target_url + '\'' +
                        ", image='" + image + '\'' +
                        ", sort=" + sort +
                        ", deleted=" + deleted +
                        ", created_at='" + created_at + '\'' +
                        ", updated_at='" + updated_at + '\'' +
                        '}';
            }
        }
    }
}
