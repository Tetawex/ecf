package org.tetawex.ecf.model;

/**
 * Created by tetawex on 16.08.17.
 */
@Deprecated
public class Table {
    int width;
    int height;
    int nozhkaCount;
    String materialName;

    public Table(int width, int height,
                 int nozhkaCount,
                 String materialName) {
        this.width = width;
        this.height = height;
        this.nozhkaCount = nozhkaCount;
        this.materialName = materialName;
    }

    Table weirdTable=new Table(1,1,
            1, "SophisticatedWood");


}