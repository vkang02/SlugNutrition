
package com.microsoft.projectoxford.visionsample.example;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyList {

    @SerializedName("myList")
    @Expose
    private List<MyList> myList = new ArrayList<MyList>();

    /**
     * 
     * @return
     *     The myList
     */
    public List<MyList> getMyList() {
        return myList;
    }

    /**
     * 
     * @param myList
     *     The myList
     */
    public void setMyList(List<MyList> myList) {
        this.myList = myList;
    }

}
