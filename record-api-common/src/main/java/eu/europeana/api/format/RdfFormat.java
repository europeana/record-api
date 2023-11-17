package eu.europeana.api.format;

public enum RdfFormat {
    JSONLD("jsonld", "json", "application/ld+json", "application/json"),
    XML("rdf", "xml", "application/rdf+xml", "application/xml", "text/xml", "rdf/xml"),
    TURTLE("ttl", null, "text/turtle", "application/turtle", "application/x-turtle"),
    N3("n3", null, "text/n3", "text/rdf+n3", "application/n3"),
    NT("nt", null, "application/n-triples", "application/ntriples", "text/nt");

    private String extension;
    private String alternative;
    private String[] mediaTypes;

    RdfFormat(String extension, String alternative, String... mediaTypes) {
        this.extension = extension;
        this.alternative = alternative;
        this.mediaTypes = mediaTypes;
    }

    public static RdfFormat getFormatByExtension(String extension) {
        for (RdfFormat format : RdfFormat.values()) {
            if (format.acceptsExtension(extension)) {
                return format;
            }
        }
        return null;
    }

    public static RdfFormat getFormatByMediaType(String mediaType) {
        for (RdfFormat format : RdfFormat.values()) {
            if (format.acceptsMediaType(mediaType)) {
                return format;
            }
        }
        return null;
    }

    public String getExtension() {
        return extension;
    }

    public String getAlternative() {
        return alternative;
    }

    public String getMediaType() {
        return mediaTypes[0];
    }

    public boolean acceptsExtension(String extension) {
        return (this.extension.equals(extension)
                || (this.alternative != null && this.alternative.equals(extension)));
    }

    public boolean acceptsMediaType(String mediaType) {
        for (String mType : mediaTypes) {
            if (mType.equals(mediaType)) {
                return true;
            }
        }
        return false;
    }
}