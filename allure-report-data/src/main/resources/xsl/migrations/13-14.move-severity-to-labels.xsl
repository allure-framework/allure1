<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>
    <xsl:strip-space elements="*"/>

    <!--remove @severity attribute-->
    <xsl:template match="@severity">
        <xsl:choose>
            <!--in case we labels node don't exists we need to create it and add new label-->
            <xsl:when test="not(../labels)">
                <xsl:element name="labels">
                    <xsl:call-template name="createSeverity">
                        <xsl:with-param name="severity" select="../@severity"/>
                    </xsl:call-template>
                </xsl:element>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <!--in case labels node exists we need to add new label-->
    <xsl:template match="*[@severity]/labels">
        <xsl:element name="labels">
            <xsl:apply-templates/>
            <xsl:call-template name="createSeverity">
                <xsl:with-param name="severity" select="../@severity"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>

    <xsl:template name="createSeverity">
        <xsl:param name="severity"/>
        <xsl:element name="label">
            <xsl:attribute name="name">
                <xsl:value-of select="'severity'"/>
            </xsl:attribute>
            <xsl:attribute name="value">
                <xsl:value-of select="$severity"/>
            </xsl:attribute>
        </xsl:element>
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