//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.30 at 08:16:47 AM CET 
//


package org.jesperancinha.plugins.omni.reporter.domain.saga;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for classType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="classType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="methods" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="lines" type="{}linesType"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="filename" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="line-rate" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="branch-rate" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="complexity" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "classType", propOrder = {
    "methods",
    "lines"
})
public class ClassType {

    @XmlElement(required = true)
    protected String methods;
    @XmlElement(required = true)
    protected LinesType lines;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "filename")
    protected String filename;
    @XmlAttribute(name = "line-rate")
    protected String lineRate;
    @XmlAttribute(name = "branch-rate")
    protected String branchRate;
    @XmlAttribute(name = "complexity")
    protected String complexity;

    /**
     * Gets the value of the methods property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethods() {
        return methods;
    }

    /**
     * Sets the value of the methods property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethods(String value) {
        this.methods = value;
    }

    /**
     * Gets the value of the lines property.
     * 
     * @return
     *     possible object is
     *     {@link LinesType }
     *     
     */
    public LinesType getLines() {
        return lines;
    }

    /**
     * Sets the value of the lines property.
     * 
     * @param value
     *     allowed object is
     *     {@link LinesType }
     *     
     */
    public void setLines(LinesType value) {
        this.lines = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the filename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the value of the filename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilename(String value) {
        this.filename = value;
    }

    /**
     * Gets the value of the lineRate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLineRate() {
        return lineRate;
    }

    /**
     * Sets the value of the lineRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLineRate(String value) {
        this.lineRate = value;
    }

    /**
     * Gets the value of the branchRate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBranchRate() {
        return branchRate;
    }

    /**
     * Sets the value of the branchRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBranchRate(String value) {
        this.branchRate = value;
    }

    /**
     * Gets the value of the complexity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComplexity() {
        return complexity;
    }

    /**
     * Sets the value of the complexity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComplexity(String value) {
        this.complexity = value;
    }

}
