
package com.microsoft.projectoxford.visionsample.example;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyList {

    @SerializedName("mylist")
    @Expose
    private List<Food> myList = new ArrayList<Food>();

    /**
     * 
     * @return
     *     The myList
     */
    public List<Food> getMyList() {
        return myList;
    }

    /**
     * 
     * @param myList
     *     The myList
     */
    public void setMyList(List<Food> myList) {
        this.myList = myList;
    }

}
