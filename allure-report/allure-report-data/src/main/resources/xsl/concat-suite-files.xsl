<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns2="urn:model.allure.qatools.yandex.ru"
                xmlns:alr="urn:data.allure.qatools.yandex.ru"
                xmlns:uuid="java:java.util.UUID"
                exclude-result-prefixes="uuid">

    <xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>
    <xsl:strip-space elements="*"/>

    <!--this variable used to search for all documents-->
    <xsl:variable name="root" select="alr:list-files"/>

    <!--Join point for list-files xml-->
    <!--add root <alr:test-suites-pack> and apply templates for sub-nodes-->
    <!--<xsd:complexType name="list-files">-->
    <!--<xsd:sequence>-->
    <!--<xsd:element name="files" type="xsd:string" minOccurs="0" maxOccurs="unbounded"/>-->
    <!--</xsd:sequence>-->
    <!--</xsd:complexType>-->
    <xsl:template match="alr:list-files">
        <xsl:element name="alr:allure-test-run">
            <xsl:call-template name="add-time-node-for-allure-test-run"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <!--apply templates for document by path from files node-->
    <xsl:template match="files">
        <xsl:apply-templates select="document(.)"/>
    </xsl:template>

    <!--Join point for test-suite xml-->
    <xsl:template match="ns2:test-suite">
        <xsl:element name="test-suites" inherit-namespaces="yes">
           <xsl:element name="uid">
               <xsl:value-of select="uuid:randomUUID()"/>
           </xsl:element>

            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>

    <!--After test-suites title node add time and statistic nodes-->
    <xsl:template match="ns2:test-suite/title">

        <xsl:copy copy-namespaces="no">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>

        <xsl:call-template name="add-time-node"/>
        <xsl:call-template name="add-statistic-node"/>
    </xsl:template>

    <!--Before test-cases title node add suite uid-->
    <!--After test-cases title node add time and summary nodes-->
    <xsl:template match="test-cases/title">

        <xsl:element name="uid">
            <xsl:value-of select="uuid:randomUUID()"/>
        </xsl:element>

        <xsl:call-template name="copy-all-without-namespace"/>
        <xsl:call-template name="add-time-node"/>
        <xsl:call-template name="add-summary-node"/>
    </xsl:template>

    <!--After steps title node add time, status and statistic nodes-->
    <xsl:template match="steps/title">
        <xsl:call-template name="copy-all-without-namespace"/>
        <xsl:call-template name="add-time-node"/>
        <xsl:call-template name="add-summary-node"/>

        <xsl:element name="status">
            <xsl:value-of select="../@status"/>
        </xsl:element>
    </xsl:template>

    <!--Match test-cases node-->
    <xsl:template match="test-cases">
        <xsl:element name="test-cases">
            <!--Modify content inside test-cases node-->
            <xsl:apply-templates select="@*|node()"/>

            <!--Add severity and status-->
            <xsl:call-template name="add-severity-node"/>
            <xsl:call-template name="add-status-node"/>
        </xsl:element>
    </xsl:template>

    <!--Skipp this attribute nodes-->
    <xsl:template match="@start | @stop | @status | @severity">
    </xsl:template>

    <!--Other nodes just copy without namespace-->
    <xsl:template match="@*|node()">
        <xsl:call-template name="copy-all-without-namespace"/>
    </xsl:template>

    <xsl:template name="copy-all-without-namespace">
        <xsl:copy copy-namespaces="no">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <!--Add Time node-->
    <!--<xsd:element name="time" type="alr:time"/>-->

    <!--<xsd:complexType name="time">-->
    <!--<xsd:attribute name="start" type="xsd:long" default="0"/>-->
    <!--<xsd:attribute name="stop" type="xsd:long" default="0"/>-->
    <!--<xsd:attribute name="duration" type="xsd:long" default="0"/>-->
    <!--</xsd:complexType>-->
    <xsl:template name="add-time-node">
        <xsl:element name="time">
            <xsl:variable name="start">
                <xsl:value-of select="../@start"/>
            </xsl:variable>
            <xsl:variable name="stop">
                <xsl:value-of select="../@stop"/>
            </xsl:variable>

            <xsl:attribute name="start">
                <xsl:value-of select="$start"/>
            </xsl:attribute>
            <xsl:attribute name="stop">
                <xsl:value-of select="$stop"/>
            </xsl:attribute>
            <xsl:attribute name="duration">
                <xsl:value-of select="format-number($stop - $start, '0')"/>
            </xsl:attribute>
        </xsl:element>
    </xsl:template>

    <!--Add Statistic node-->
    <!--<xsd:element name="statistic" type="alr:statistic"/>-->

    <!--<xsd:complexType name="statistic">-->
    <!--<xsd:attribute name="total" type="xsd:long" default="0"/>-->
    <!--<xsd:attribute name="passed" type="xsd:long" default="0"/>-->
    <!--<xsd:attribute name="skipped" type="xsd:long" default="0"/>-->
    <!--<xsd:attribute name="failed" type="xsd:long" default="0"/>-->
    <!--<xsd:attribute name="broken" type="xsd:long" default="0"/>-->
    <!--</xsd:complexType>-->
    <xsl:template name="add-statistic-node">
        <xsl:element name="statistic">
            <xsl:attribute name="total">
                <xsl:value-of select="count(../test-cases)"/>
            </xsl:attribute>
            <xsl:attribute name="passed">
                <xsl:value-of select="count(../test-cases[@status='passed'])"/>
            </xsl:attribute>
            <xsl:attribute name="skipped">
                <xsl:value-of select="count(../test-cases[@status='skipped'])"/>
            </xsl:attribute>
            <xsl:attribute name="failed">
                <xsl:value-of select="count(../test-cases[@status='failed'])"/>
            </xsl:attribute>
            <xsl:attribute name="broken">
                <xsl:value-of select="count(../test-cases[@status='broken'])"/>
            </xsl:attribute>
        </xsl:element>
    </xsl:template>

    <!--Add Severity node-->
    <!--<xsd:element name="severity" type="alr:severity-level"/>-->

    <!--<xsd:simpleType name="severity-level">-->
    <!--<xsd:restriction base="xsd:string">-->
    <!--<xsd:enumeration value="blocker"/>-->
    <!--<xsd:enumeration value="critical"/>-->
    <!--<xsd:enumeration value="normal"/>-->
    <!--<xsd:enumeration value="minor"/>-->
    <!--<xsd:enumeration value="trivial"/>-->
    <!--</xsd:restriction>-->
    <!--</xsd:simpleType>-->
    <xsl:template name="add-severity-node">
        <xsl:element name="severity">
            <xsl:value-of select="@severity"/>
        </xsl:element>
    </xsl:template>

    <!--Add status node-->
    <!--<xsd:element name="status" type="alr:status"/>-->

    <!--<xsd:simpleType name="status">-->
    <!--<xsd:restriction base="xsd:string">-->
    <!--<xsd:enumeration value="failed"/>-->
    <!--<xsd:enumeration value="broken"/>-->
    <!--<xsd:enumeration value="passed"/>-->
    <!--<xsd:enumeration value="skipped"/>-->
    <!--</xsd:restriction>-->
    <!--</xsd:simpleType>-->
    <xsl:template name="add-status-node">
        <xsl:element name="status">
            <xsl:value-of select="@status"/>
        </xsl:element>
    </xsl:template>

    <!--Add summary node-->
    <!--<xsd:element name="summary" type="alr:summary"/>-->

    <!--<xsd:complexType name="summary">-->
    <!--<xsd:attribute name="attachments" type="xsd:long" default="0"/>-->
    <!--<xsd:attribute name="steps" type="xsd:long" default="0"/>-->
    <!--</xsd:complexType>-->
    <xsl:template name="add-summary-node">
        <xsl:element name="summary" inherit-namespaces="yes">
            <xsl:attribute name="attachments">
                <xsl:value-of select="count(..//attachments)"/>
            </xsl:attribute>
            <xsl:attribute name="steps">
                <xsl:value-of select="count(..//steps)"/>
            </xsl:attribute>
        </xsl:element>
    </xsl:template>

    <xsl:template name="add-time-node-for-allure-test-run">
        <xsl:element name="time">
            <xsl:variable name="start">
                <xsl:choose>
                    <xsl:when test="count($root/files) > 0">
                        <xsl:value-of select="min(document($root/files)//ns2:test-suite/@start)"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="0"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            <xsl:variable name="stop">
                <xsl:choose>
                    <xsl:when test="count($root/files) > 0">
                        <xsl:value-of select="max(document($root/files)//ns2:test-suite/@stop)"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="0"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:variable>

            <xsl:attribute name="start">
                <xsl:value-of select="format-number($start, '0')"/>
            </xsl:attribute>
            <xsl:attribute name="stop">
                <xsl:value-of select="format-number($stop, '0')"/>
            </xsl:attribute>
            <xsl:attribute name="duration">
                <xsl:value-of select="format-number($stop - $start, '0')"/>
            </xsl:attribute>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>