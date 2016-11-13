
package com.microsoft.projectoxford.visionsample.Nutrients;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Nutrients {

    @SerializedName("myNutrients")
    @Expose
    private MyNutrients myNutrients;

    /**
     * 
     * @return
     *     The myNutrients
     */
    public MyNutrients getMyNutrients() {
        return myNutrients;
    }

    /**
     * 
     * @param myNutrients
     *     The myNutrients
     */
    public void setMyNutrients(MyNutrients myNutrients) {
        this.myNutrients = myNutrients;
    }

}
