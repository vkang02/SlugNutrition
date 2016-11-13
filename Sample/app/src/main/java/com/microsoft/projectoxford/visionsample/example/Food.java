
package com.microsoft.projectoxford.visionsample.example;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Food{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ndbno")
    @Expose
    private String ndbno;

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The ndbno
     */
    public String getNdbno() {
        return ndbno;
    }

    /**
     * 
     * @param ndbno
     *     The ndbno
     */
    public void setNdbno(String ndbno) {
        this.ndbno = ndbno;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", ndbno='" + ndbno + '\'' +
                '}';
    }
}
