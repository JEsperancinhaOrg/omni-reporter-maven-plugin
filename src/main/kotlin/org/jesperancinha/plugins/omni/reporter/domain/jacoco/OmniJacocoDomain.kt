package org.jesperancinha.plugins.omni.reporter.domain.jacoco

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import org.jesperancinha.plugins.omni.reporter.parsers.readXmlValue
import java.io.InputStream

/**
 * Created by jofisaes on 04/01/2022
 */

data class Class(
    @JsonProperty("Method")
    val methods: List<Method> = emptyList(),
    @JsonProperty("Counter")
    val counters: List<Counter> = emptyList(),
    val name: String? = null,
    val sourcefilename: String? = null
)

data class Counter(
    val value: String? = null,
    val type: String? = null,
    val missed: Int,
    val covered: Int
)

data class Line(
    val value: String? = null,
    val nr: Int,
    val mi: Int = 0,
    val ci: Int,
    val mb: Int = 0,
    val cb: Int = 0
)

data class Method(
    @JsonProperty("Counter")
    val counters: List<Counter> = emptyList(),
    val name: String? = null,
    val desc: String? = null,
    val line: String? = null
)

data class Package(
    @JsonProperty("Class")
    val clazzs: List<Class> = emptyList(),
    @JsonProperty("Sourcefile")
    val sourcefiles: List<Sourcefile> = emptyList(),
    @JsonProperty("Counter")
    val counters: List<Counter> = emptyList(),
    val name: String? = null
)

data class Sessioninfo(
    val value: String? = null,
    val id: String? = null,
    val start: String? = null,
    val dump: String? = null
)

data class Sourcefile(
    @JsonProperty("Line")
    val lines: List<Line> = emptyList(),
    @JsonProperty("Counter")
    val counters: List<Counter> = emptyList(),
    val name: String
)

@JsonRootName("Stamp")
@JacksonXmlRootElement
data class Report(
    val sessioninfo: Sessioninfo? = null,
    @JsonProperty("Package")
    val packages: List<Package> = emptyList(),
    @JsonProperty("Counter")
    val counters: List<Counter> = emptyList(),
    val name: String? = null
)

val InputStream.readJacocoPackages
    get() = readXmlValue<Report>(this).packages
