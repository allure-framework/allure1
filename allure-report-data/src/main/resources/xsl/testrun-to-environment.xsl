<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:utils="java:ru.yandex.qatools.allure.data.utils.TextUtils"
                xmlns:alr="urn:data.allure.qatools.yandex.ru"
                xmlns:qa="urn:model.commons.qatools.yandex.ru"
        >

    <xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match="alr:allure-test-run">
        <xsl:element name="qa:environment">
            <xsl:element name="id">
                <xsl:value-of select="utils:generateUid()"/>
            </xsl:element>
            <xsl:element name="name">
                <xsl:value-of select="'Allure Test Pack'"/>
            </xsl:element>

            <xsl:call-template name="add-parameter">
                <xsl:with-param name="name" select="'Report started at'"/>
                <xsl:with-param name="key" select="'report.time.start'"/>
                <xsl:with-param name="value" select="utils:dateToString(time/@start)"/>
            </xsl:call-template>

            <xsl:call-template name="add-parameter">
                <xsl:with-param name="name" select="'Report finished at'"/>
                <xsl:with-param name="key" select="'report.time.stop'"/>
                <xsl:with-param name="value" select="utils:dateToString(time/@stop)"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>

    <xsl:template name="add-parameter">
        <xsl:param name="name"/>
        <xsl:param name="key"/>
        <xsl:param name="value"/>

        <xsl:element name="parameter">
            <xsl:element name="name">
                <xsl:value-of select="$name"/>
            </xsl:element>
            <xsl:element name="key">
                <xsl:value-of select="$key"/>
            </xsl:element>
            <xsl:element name="value">
                <xsl:value-of select="$value"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>