package org.redcraft.extensions.material;

public class ExtensionMaterialData {

    public static ExtensionMaterialData EMPTY = new ExtensionMaterialData(null,null);

    ExtensionMaterial material;
    ExtensionData data;

    public ExtensionMaterialData(ExtensionMaterial material, ExtensionData data) {
        this.material = material;
        this.data = data;
    }


    public ExtensionData data() {
        return data;
    }
    public ExtensionMaterial material() {
        return material;
    }
    public void data(ExtensionData data) {
        this.data = data;
    }
    public void material(ExtensionMaterial material) {
        this.material = material;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
