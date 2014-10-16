<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        >

    <xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match="test-cases/test-case">
        <xsl:element name="test-case">
            <xsl:if test="count(labels) = 0">
                <xsl:element name="labels">
                    <xsl:call-template name="add-default-host-label"/>
                    <xsl:call-template name="add-default-thread-label"/>
                </xsl:element>
            </xsl:if>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="test-cases/test-case/labels">
        <xsl:element name="labels">
            <xsl:if test="count(label[@name='host']) = 0">
                <xsl:call-template name="add-default-host-label"/>
            </xsl:if>
            <xsl:if test="count(label[@name='thread']) = 0">
                <xsl:call-template name="add-default-thread-label"/>
            </xsl:if>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="add-default-host-label">
        <xsl:element name="label">
            <xsl:attribute name="name" select="'host'"/>
            <xsl:attribute name="value" select="'Default'"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="add-default-thread-label">
        <xsl:element name="label">
            <xsl:attribute name="name" select="'thread'"/>
            <xsl:attribute name="value" select="'Thread 1'"/>
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