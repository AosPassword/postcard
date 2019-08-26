package org.wuneng.web.postcard.beans;

public class Direction {
    private Integer id;
    private String direction_name;

    public Direction(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDirection_name() {
        return direction_name;
    }

    public void setDirection_name(String direction_name) {
        this.direction_name = direction_name;
    }

    @Override
    public String toString() {
        return "Direction{" +
                "id=" + id +
                ", direction_name='" + direction_name + '\'' +
                '}';
    }
}
