
package com.microsoft.projectoxford.visionsample.Nutrients;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MyNutrients {

    @SerializedName("Total Fat")
    @Expose
    private TotalFat totalFat;
    @SerializedName("Protein")
    @Expose
    private Protein protein;
    @SerializedName("Cholesterol")
    @Expose
    private Cholesterol cholesterol;
    @SerializedName("Total Carbohydrate")
    @Expose
    private TotalCarbohydrate totalCarbohydrate;
    @SerializedName("Dietary Fiber")
    @Expose
    private DietaryFiber dietaryFiber;
    @SerializedName("Sugars")
    @Expose
    private Sugars sugars;
    @SerializedName("Sodium")
    @Expose
    private Sodium sodium;
    @SerializedName("kcal")
    @Expose
    private Kcal kcal;

    /**
     * 
     * @return
     *     The totalFat
     */
    public TotalFat getTotalFat() {
        return totalFat;
    }

    /**
     * 
     * @param totalFat
     *     The Total Fat
     */
    public void setTotalFat(TotalFat totalFat) {
        this.totalFat = totalFat;
    }

    /**
     * 
     * @return
     *     The protein
     */
    public Protein getProtein() {
        return protein;
    }

    /**
     * 
     * @param protein
     *     The Protein
     */
    public void setProtein(Protein protein) {
        this.protein = protein;
    }

    /**
     * 
     * @return
     *     The cholesterol
     */
    public Cholesterol getCholesterol() {
        return cholesterol;
    }

    /**
     * 
     * @param cholesterol
     *     The Cholesterol
     */
    public void setCholesterol(Cholesterol cholesterol) {
        this.cholesterol = cholesterol;
    }

    /**
     * 
     * @return
     *     The totalCarbohydrate
     */
    public TotalCarbohydrate getTotalCarbohydrate() {
        return totalCarbohydrate;
    }

    /**
     * 
     * @param totalCarbohydrate
     *     The Total Carbohydrate
     */
    public void setTotalCarbohydrate(TotalCarbohydrate totalCarbohydrate) {
        this.totalCarbohydrate = totalCarbohydrate;
    }

    /**
     * 
     * @return
     *     The dietaryFiber
     */
    public DietaryFiber getDietaryFiber() {
        return dietaryFiber;
    }

    /**
     * 
     * @param dietaryFiber
     *     The Dietary Fiber
     */
    public void setDietaryFiber(DietaryFiber dietaryFiber) {
        this.dietaryFiber = dietaryFiber;
    }

    /**
     * 
     * @return
     *     The sugars
     */
    public Sugars getSugars() {
        return sugars;
    }

    /**
     * 
     * @param sugars
     *     The Sugars
     */
    public void setSugars(Sugars sugars) {
        this.sugars = sugars;
    }

    /**
     * 
     * @return
     *     The sodium
     */
    public Sodium getSodium() {
        return sodium;
    }

    /**
     * 
     * @param sodium
     *     The Sodium
     */
    public void setSodium(Sodium sodium) {
        this.sodium = sodium;
    }

    /**
     * 
     * @return
     *     The kcal
     */
    public Kcal getKcal() {
        return kcal;
    }

    /**
     * 
     * @param kcal
     *     The kcal
     */
    public void setKcal(Kcal kcal) {
        this.kcal = kcal;
    }

}
