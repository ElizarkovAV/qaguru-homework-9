package model;

import java.util.List;

public class Menu {

    private String menuVersion;
    private List<Items> items;

    public String getMenuVersion() {
        return menuVersion;
    }

    public List<Items> getItems() {
        return items;
    }

    public static class Items {
        private Integer id;
        private String type;
        private String name;
        private Integer price;

        public Integer getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public Integer getPrice() {
            return price;
        }
    }

}
