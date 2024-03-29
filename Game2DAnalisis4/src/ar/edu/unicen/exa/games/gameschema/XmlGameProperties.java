//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.21 at 03:04:40 PM GFT 
//


package ar.edu.unicen.exa.games.gameschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for xmlGameProperties complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="xmlGameProperties">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="analizedProperties" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="filtering" type="{http://www.games.exa.unicen.edu.ar/GameSchema}xmlFiltering" minOccurs="0"/>
 *         &lt;element name="discretization" type="{http://www.games.exa.unicen.edu.ar/GameSchema}xmlDiscretization" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmlGameProperties", propOrder = {
    "analizedProperties",
    "filtering",
    "discretization"
})
public class XmlGameProperties {

    protected String analizedProperties;
    protected XmlFiltering filtering;
    protected XmlDiscretization discretization;

    /**
     * Gets the value of the analizedProperties property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnalizedProperties() {
        return analizedProperties;
    }

    /**
     * Sets the value of the analizedProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnalizedProperties(String value) {
        this.analizedProperties = value;
    }

    /**
     * Gets the value of the filtering property.
     * 
     * @return
     *     possible object is
     *     {@link XmlFiltering }
     *     
     */
    public XmlFiltering getFiltering() {
        return filtering;
    }

    /**
     * Sets the value of the filtering property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlFiltering }
     *     
     */
    public void setFiltering(XmlFiltering value) {
        this.filtering = value;
    }

    /**
     * Gets the value of the discretization property.
     * 
     * @return
     *     possible object is
     *     {@link XmlDiscretization }
     *     
     */
    public XmlDiscretization getDiscretization() {
        return discretization;
    }

    /**
     * Sets the value of the discretization property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlDiscretization }
     *     
     */
    public void setDiscretization(XmlDiscretization value) {
        this.discretization = value;
    }

}
