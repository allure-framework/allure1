<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match="feature">
        <xsl:element name="feature">
            <xsl:call-template name="add-statistic-node"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="story">
        <xsl:element name="story">
            <xsl:call-template name="add-statistic-node"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="add-statistic-node">
        <xsl:element name="statistic">
            <xsl:attribute name="total">
                <xsl:value-of select="count(.//test-case)"/>
            </xsl:attribute>
            <xsl:attribute name="passed">
                <xsl:value-of select="count(.//test-case/status[text()='passed'])"/>
            </xsl:attribute>
            <xsl:attribute name="canceled">
                <xsl:value-of select="count(.//test-case/status[text()='canceled'])"/>
            </xsl:attribute>
            <xsl:attribute name="pending">
                <xsl:value-of select="count(.//test-case/status[text()='pending'])"/>
            </xsl:attribute>
            <xsl:attribute name="failed">
                <xsl:value-of select="count(.//test-case/status[text()='failed'])"/>
            </xsl:attribute>
            <xsl:attribute name="broken">
                <xsl:value-of select="count(.//test-case/status[text()='broken'])"/>
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