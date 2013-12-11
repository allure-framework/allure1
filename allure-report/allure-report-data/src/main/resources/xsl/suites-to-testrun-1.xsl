<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:model="urn:model.allure.qatools.yandex.ru"
                xmlns:alr="urn:data.allure.qatools.yandex.ru">

    <xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match="alr:list-files">
        <xsl:element name="alr:allure-test-run">
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="files">
        <xsl:element name="test-suites">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="file">
        <xsl:element name="test-suite" inherit-namespaces="yes">
            <xsl:apply-templates select="document(.)"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="model:test-suite">
        <xsl:apply-templates select="@*|node()"/>
    </xsl:template>

    <xsl:template match="@*|node()">
        <xsl:call-template name="copy-all-without-namespace"/>
    </xsl:template>

    <xsl:template name="copy-all-without-namespace">
        <xsl:copy copy-namespaces="no">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>