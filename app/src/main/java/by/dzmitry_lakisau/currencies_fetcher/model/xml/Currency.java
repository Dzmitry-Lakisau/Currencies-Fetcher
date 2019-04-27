package by.dzmitry_lakisau.currencies_fetcher.model.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import java.math.BigDecimal;

public class Currency {

    @Element(name="CharCode")
    private String charCode;

    @Element(name="Rate")
    private BigDecimal rate;

    @Element(name="Scale")
    private Integer scale;

    @Attribute(name="Id")
    private Integer id;

    @Element(name="NumCode")
    private String numCode;

    @Element(name="Name")
    private String name;

    public String getCharCode() {return this.charCode;}

    public BigDecimal getRate() {return this.rate;}

    public Integer getScale() {return this.scale;}

    public Integer getId() {return this.id;}

    public String getNumCode() {return this.numCode;}

    public String getName() {return this.name;}
    public void setName(String value) {this.name = value;}
}
