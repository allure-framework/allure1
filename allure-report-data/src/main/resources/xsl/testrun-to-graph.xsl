<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:alr="urn:data.allure.qatools.yandex.ru">

    <xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match="alr:allure-test-run">
        <xsl:element name="alr:allure-graph">
            <xsl:element name="test-cases">
                <xsl:for-each select="test-suites//test-case">
                    <xsl:element name="test-case">
                        <xsl:copy-of select="./*"/>
                    </xsl:element>
                </xsl:for-each>
            </xsl:element>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>