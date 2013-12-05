<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:alr="urn:data.allure.qatools.yandex.ru">

    <xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match="alr:allure-test-run">
        <xsl:element name="alr:allure-test-run">
            <xsl:copy-of select="time"/>

            <xsl:for-each select="test-suites">
                <xsl:element name="test-suites">
                    <xsl:apply-templates select="@*|node()"/>
                </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="test-suites/test-cases/test-case">
                <xsl:element name="test-cases">
                    <xsl:copy-of select="child::*"/>
                </xsl:element>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>

    <xsl:template match="summary | failure | steps | attachments | labels"/>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>


</xsl:stylesheet>