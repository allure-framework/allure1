<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:alr="urn:data.allure.qatools.yandex.ru"
                xmlns:utils="java:ru.yandex.qatools.allure.data.utils.TextUtils"
                exclude-result-prefixes="utils">

    <xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match="alr:allure-test-run">
        <xsl:element name="alr:allure-defects">
            <xsl:element name="defects-list">
                <xsl:for-each-group select="test-cases//test-case[./status='failed' or ./status='broken']"
                                    group-by="status">
                    <xsl:element name="defect">

                        <xsl:call-template name="add-title-node"/>
                        <xsl:call-template name="add-status-node"/>

                        <xsl:element name="defects">
                            <xsl:call-template name="add-test-cases-grouped-by-message"/>
                        </xsl:element>
                    </xsl:element>
                </xsl:for-each-group>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template name="add-title-node">
        <xsl:choose>
            <xsl:when test="current-grouping-key()='failed'">
                <xsl:element name="title">
                    <xsl:text>Product defects</xsl:text>
                </xsl:element>
            </xsl:when>
            <xsl:when test="current-grouping-key()='broken'">
                <xsl:element name="title">
                    <xsl:text>Test defects</xsl:text>
                </xsl:element>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="add-status-node">
        <xsl:element name="status">
            <xsl:value-of select="current-grouping-key()"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="add-test-cases-grouped-by-message">
        <xsl:for-each-group select="current-group()"
                            group-by="utils:getMessageMask(failure/message)">
            <xsl:element name="defect-item">
                <xsl:call-template name="add-uid-node"/>
                <xsl:element name="failure">
                    <xsl:element name="message">
                        <xsl:choose>
                            <xsl:when test="count(current-group())=1">
                                <xsl:value-of select="current()/failure/message/text()"/>
                            </xsl:when>
                            <xsl:when test="count(current-group())!=1">
                                <xsl:value-of select="current-grouping-key()"/>
                            </xsl:when>
                        </xsl:choose>
                    </xsl:element>
                </xsl:element>
                <xsl:element name="test-cases">

                    <xsl:for-each select="current-group()">
                        <xsl:element name="test-case">
                            <xsl:copy-of select="current()/*"/>
                        </xsl:element>
                    </xsl:for-each>
                </xsl:element>
            </xsl:element>
        </xsl:for-each-group>
    </xsl:template>

    <xsl:template name="add-uid-node">
        <xsl:element name="uid">
            <xsl:value-of select="utils:generateUid()"/>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>