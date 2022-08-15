package com.example.es.core;

public enum EnumDataType {

    AGGREGATEMETRICDOUBLE("aggregateMetricDouble"),
    BINARY("binary"),
    BOOLEAN("boolean"),
    COMPLETION("completion"),
    KEYWORD("keyword"),
    DATENANOS("dateNanos"),
    DATE("date"),
    DATERANGE("dateRange"),
    DENSEVECTOR("denseVector"),
    DOUBLE("double"),
    DOUBLERANGE("doubleRange"),
    ALIAS("alias"),
    FLATTENED("flattened"),
    FLOAT("float"),
    FLOATRANGE("floatRange"),
    GEOPONIT("geoPoint"),
    GEOSHAPE("getShape"),
    HALFFLOAT("halfFloat"),
    HISTOGRAM("histogram"),
    INTEGER("integer"),
    INTEGRERANGE("integerRange"),
    IP("ip"),
    IPRANGE("ipRange"),
    JOIN("join"),
    LONG("long"),
    MATCHONLYTEXT("matchOnlyText"),
    MURMUR3("murmur3"),
    NESTED("nested"),
    OBJECT("object"),
    PERCOLATOR("percolator"),
    POINT("point"),
    RANKFEATURE("rankFeature"),
    SCALEDFLOAT("scaledFloat"),
    SEARCHASYOUTYPE("searchAsYouType"),
    SHAPE("shape"),
    SHORT("short"),
    TOKENCOUNT("tokenCount"),
    UNSIGNEDLONG("unsignedLong"),
    VERSION("version"),
    TEXT("text"),
    WILDCARD("wildcard");




    private final String name;

    EnumDataType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
