<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:alr="urn:data.allure.qatools.yandex.ru"
                xmlns:utils="java:ru.yandex.qatools.allure.data.utils.TextUtils"
                exclude-result-prefixes="utils">

    <xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match="alr:allure-test-run">
        <xsl:element name="alr:allure-test-run">
            <xsl:call-template name="add-time-node-for-test-run"/>

            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="test-suite">
        <xsl:element name="test-suite">
            <xsl:call-template name="add-uid-node">
                <xsl:with-param name="name" select="name/text()"/>
            </xsl:call-template>
            <xsl:call-template name="add-title-node"/>
            <xsl:call-template name="add-time-node"/>
            <xsl:call-template name="add-statistic-node"/>

            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="test-case">
        <xsl:element name="test-case">
            <xsl:call-template name="add-uid-node">
                <xsl:with-param name="name" select="concat(name/text(), ../../name/text())"/>
            </xsl:call-template>
            <xsl:call-template name="add-title-node"/>
            <xsl:call-template name="add-time-node"/>
            <xsl:call-template name="add-summary-node"/>
            <xsl:call-template name="add-severity-node"/>
            <xsl:call-template name="add-status-node"/>

            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="step">
        <xsl:element name="step">
            <xsl:call-template name="add-title-node"/>
            <xsl:call-template name="add-time-node"/>
            <xsl:call-template name="add-summary-node"/>
            <xsl:call-template name="add-status-node"/>

            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="test-case/labels">
        <xsl:element name="labels">
            <xsl:copy-of select="child::*"/>
            <xsl:call-template name="copy-suite-labels-to-test-case"/>

            <xsl:call-template name="add-without-feature-label-if-needed"/>
            <xsl:call-template name="add-without-story-label-if-needed"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="attachment">
        <xsl:element name="attachment">
            <xsl:attribute name="uid">
                <xsl:value-of select="utils:generateUid()"/>
            </xsl:attribute>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="add-time-node-for-test-run">
        <xsl:choose>
            <xsl:when test="count(//test-suite)=0">
                <xsl:call-template name="add-time-node-params">
                    <xsl:with-param name="start" select="0"/>
                    <xsl:with-param name="stop" select="0"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="count(//test-suite)>0">
                <xsl:call-template name="add-time-node-params">
                    <xsl:with-param name="start" select="min(//test-suite/@start)"/>
                    <xsl:with-param name="stop" select="max(//test-suite/@stop)"/>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="add-time-node">
        <xsl:call-template name="add-time-node-params">
            <xsl:with-param name="start" select="@start"/>
            <xsl:with-param name="stop" select="@stop"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="add-time-node-params">
        <xsl:param name="start"/>
        <xsl:param name="stop"/>

        <xsl:element name="time">
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

    <xsl:template name="add-statistic-node">
        <xsl:element name="statistic">
            <xsl:attribute name="total">
                <xsl:value-of select="count(test-cases/test-case)"/>
            </xsl:attribute>
            <xsl:attribute name="passed">
                <xsl:value-of select="count(test-cases/test-case[@status='passed'])"/>
            </xsl:attribute>
            <xsl:attribute name="canceled">
                <xsl:value-of select="count(test-cases/test-case[@status='canceled'])"/>
            </xsl:attribute>
            <xsl:attribute name="pending">
                <xsl:value-of select="count(test-cases/test-case[@status='pending'])"/>
            </xsl:attribute>
            <xsl:attribute name="failed">
                <xsl:value-of select="count(test-cases/test-case[@status='failed'])"/>
            </xsl:attribute>
            <xsl:attribute name="broken">
                <xsl:value-of select="count(test-cases/test-case[@status='broken'])"/>
            </xsl:attribute>
        </xsl:element>
    </xsl:template>

    <xsl:template name="add-severity-node">
        <xsl:choose>
            <xsl:when test="count(labels/label[@name='severity']) = 0">
                <xsl:element name="severity">normal</xsl:element>
            </xsl:when>
            <xsl:when test="count(labels/label[@name='severity']) > 0">
                <xsl:element name="severity">
                    <xsl:value-of select="labels/label[@name='severity']/@value"/>
                </xsl:element>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="add-status-node">
        <xsl:element name="status">
            <xsl:value-of select="@status"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="add-uid-node">
        <xsl:param name="name"/>
        <xsl:element name="uid">
            <xsl:value-of select="utils:generateUid()"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="add-summary-node">
        <xsl:element name="summary" inherit-namespaces="yes">
            <xsl:attribute name="attachments">
                <xsl:value-of select="count(.//attachment)"/>
            </xsl:attribute>
            <xsl:attribute name="steps">
                <xsl:value-of select="count(.//step)"/>
            </xsl:attribute>
        </xsl:element>
    </xsl:template>

    <xsl:template name="copy-suite-labels-to-test-case">
        <xsl:copy-of select="../../../labels/*"/>
    </xsl:template>

    <xsl:template name="add-without-feature-label-if-needed">
        <xsl:if test="(count(label[@name='feature']) = 0) and (count(../../../labels/label[@name='feature']) = 0)">
            <xsl:element name="label">
                <xsl:attribute name="name" select="'feature'"/>
                <xsl:attribute name="value" select="'Without feature'"/>
            </xsl:element>
        </xsl:if>
    </xsl:template>

    <xsl:template name="add-without-story-label-if-needed">
        <xsl:if test="(count(label[@name='story']) = 0) and (count(../../../labels/label[@name='story']) = 0)">
            <xsl:element name="label">
                <xsl:attribute name="name" select="'story'"/>
                <xsl:attribute name="value" select="'Without story'"/>
            </xsl:element>
        </xsl:if>
    </xsl:template>

    <xsl:template name="add-title-node">
        <xsl:if test="count(title) = 0">
            <xsl:element name="title">
                <xsl:value-of select="utils:humanize(name/text())"/>
            </xsl:element>
        </xsl:if>
    </xsl:template>

    <xsl:template match="@start | @stop | @status">
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