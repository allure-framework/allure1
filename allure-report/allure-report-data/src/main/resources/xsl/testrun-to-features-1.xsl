<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:alr="urn:data.allure.qatools.yandex.ru"
                xmlns:uuid="java:java.util.UUID"
                exclude-result-prefixes="uuid">

    <xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match="alr:allure-test-run">
        <xsl:element name="alr:allure-features">
            <xsl:for-each-group select="test-cases/labels/label[@name='Feature']" group-by="@value">
                <xsl:element name="feature">

                    <xsl:call-template name="add-title-node"/>

                    <xsl:element name="stories">
                        <xsl:for-each-group select="current-group()/../label[@name='Story']" group-by="@value">
                            <xsl:element name="story">
                                <xsl:call-template name="add-title-node"/>


                                <xsl:element name="test-cases">
                                    <xsl:for-each select="current-group()">
                                        <xsl:element name="test-case">
                                            <xsl:apply-templates select="../.."/>
                                        </xsl:element>
                                    </xsl:for-each>
                                </xsl:element>

                                <xsl:call-template name="add-uid-node"/>
                            </xsl:element>
                        </xsl:for-each-group>
                    </xsl:element>
                </xsl:element>
            </xsl:for-each-group>
        </xsl:element>
    </xsl:template>

    <xsl:template match="test-cases">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="summary | failure | steps | attachments | labels"/>

    <xsl:template match="@*|node()">
        <xsl:call-template name="copy-all-without-namespace"/>
    </xsl:template>

    <xsl:template name="copy-all-without-namespace">
        <xsl:copy copy-namespaces="no">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template name="add-title-node">
        <xsl:element name="title">
            <xsl:value-of select="current-grouping-key()"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="add-uid-node">
        <xsl:element name="uid">
            <xsl:value-of select="uuid:randomUUID()"/>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>