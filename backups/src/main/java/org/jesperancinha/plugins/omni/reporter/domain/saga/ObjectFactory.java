//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.30 at 08:16:47 AM CET 
//


package org.jesperancinha.plugins.omni.reporter.domain.saga;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.jesperancinha.plugins.omni.reporter.domain.saga package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Coverage_QNAME = new QName("", "coverage");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.jesperancinha.plugins.omni.reporter.domain.saga
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CoverageType }
     * 
     */
    public CoverageType createCoverageType() {
        return new CoverageType();
    }

    /**
     * Create an instance of {@link SourcesType }
     * 
     */
    public SourcesType createSourcesType() {
        return new SourcesType();
    }

    /**
     * Create an instance of {@link LineType }
     * 
     */
    public LineType createLineType() {
        return new LineType();
    }

    /**
     * Create an instance of {@link LinesType }
     * 
     */
    public LinesType createLinesType() {
        return new LinesType();
    }

    /**
     * Create an instance of {@link ClassType }
     * 
     */
    public ClassType createClassType() {
        return new ClassType();
    }

    /**
     * Create an instance of {@link ClassesType }
     * 
     */
    public ClassesType createClassesType() {
        return new ClassesType();
    }

    /**
     * Create an instance of {@link PackageType }
     * 
     */
    public PackageType createPackageType() {
        return new PackageType();
    }

    /**
     * Create an instance of {@link PackagesType }
     * 
     */
    public PackagesType createPackagesType() {
        return new PackagesType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CoverageType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CoverageType }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "coverage")
    public JAXBElement<CoverageType> createCoverage(CoverageType value) {
        return new JAXBElement<CoverageType>(_Coverage_QNAME, CoverageType.class, null, value);
    }

}
