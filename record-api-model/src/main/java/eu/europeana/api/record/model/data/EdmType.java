package eu.europeana.api.record.model.data;

public enum EdmType {

    IMAGE("IMAGE"), SOUND("SOUND"), VIDEO("VIDEO"), _3D("3D"), TEXT("TEXT");

    private String type;

    EdmType(String type) { this.type = type; }

    @Override
    public String toString() { return type; }

    public static EdmType decode(String str) {
        for ( EdmType type : EdmType.values() ) {
            if ( type.toString().equals(str) ) { return type; }
        }
        return null;
    }
}

