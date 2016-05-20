package com.wos.dernv.evilbanefiends.objects;

/**
 * Created by der_w on 5/18/2016.
 */
public class SpinnerWikiEq {

    private  String itemName ="";
    private  String imageURL ="";


    /*********** Set Methods ******************/
    public void setItemName(String CompanyName)
    {
        this.itemName = CompanyName;
    }

    public void setImageURL(String Image)
    {
        this.imageURL = Image;
    }



    /*********** Get Methods ****************/
    public String getItemName()
    {
        return this.itemName;
    }

    public String getImageURL()
    {
        return this.imageURL;
    }

}
