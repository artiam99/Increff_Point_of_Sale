<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:template match="/">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simple"
                                       page-height="8.5in" page-width="11in" margin-top=".5in"
                                       margin-bottom=".5in" margin-left=".5in" margin-right=".5in">
                    <fo:region-body margin-top="2cm" margin-bottom="2cm" />
                    <fo:region-before extent="2cm" overflow="hidden" />
                    <fo:region-after extent="1cm" overflow="hidden" />
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simple"
                              initial-page-number="1">
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block font-size="20.0pt" font-family="serif"
                              padding-after="3.0pt" space-before="5.0pt" text-align="center"
                              border-bottom-style="solid" border-bottom-width="1.0pt">
                        <xsl:text>Invoice</xsl:text>
                    </fo:block>
                    <fo:block>

                    </fo:block>
                </fo:static-content>
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block font-size="12.0pt" font-family="sans-serif"
                              padding-after="2.0pt" space-before="2.0pt" text-align="center"
                              border-top-style="solid" border-bottom-width="1.0pt">
                        <xsl:text>Page</xsl:text>
                        <fo:page-number />
                    </fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body">
                    <xsl:apply-templates select="bill" />
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:attribute-set name="tableAttributes">
        <xsl:attribute name="border">solid 0.1mm black</xsl:attribute>

    </xsl:attribute-set>
    <xsl:template match="bill">
        <fo:block text-align="center">
            <fo:table table-layout="fixed" width="100%">
                <fo:table-column column-width="50%" />
                <fo:table-column column-width="50%" />
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block font-size="12pt" font-family="sans-serif"
                                      color="black" text-align="left" padding-top="3pt">
                                Date :
                                <xsl:value-of select="date" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                            <fo:block font-size="12pt" font-family="sans-serif"
                                      color="black" text-align="right" padding-top="3pt">
                                Time :
                                <xsl:value-of select="time" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
            <fo:table table-layout="fixed" width="100%">
                <fo:table-column column-width="10%" />
                <fo:table-column column-width="15%" />
                <fo:table-column column-width="15%" />
                <fo:table-column column-width="15%" />
                <fo:table-column column-width="15%" />
                <fo:table-column column-width="15%" />
                <fo:table-column column-width="15%" />
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell padding-top="30pt">
                            <fo:block font-size="15pt" font-family="sans-serif"
                                      background-color="black" color="white" text-align="center"
                                      padding-top="3pt">
                                SL-No.
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding-top="30pt">
                            <fo:block font-size="15pt" font-family="sans-serif"
                                      background-color="black" color="white" text-align="center"
                                      padding-top="3pt">
                                Barcode
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding-top="30pt">
                            <fo:block font-size="15pt" font-family="sans-serif"
                                      background-color="black" color="white" text-align="center"
                                      padding-top="3pt">
                                Brand
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding-top="30pt">
                            <fo:block font-size="15pt" font-family="sans-serif"
                                      background-color="black" color="white" text-align="center"
                                      padding-top="3pt">
                                Name
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding-top="30pt">
                            <fo:block font-size="15pt" font-family="sans-serif"
                                      background-color="black" color="white" text-align="center"
                                      padding-top="3pt">
                                Quantity
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding-top="30pt">
                            <fo:block font-size="15pt" font-family="sans-serif"
                                      background-color="black" color="white" text-align="center"
                                      padding-top="3pt">
                                Selling Price
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding-top="30pt">
                            <fo:block font-size="15pt" font-family="sans-serif"
                                      background-color="black" color="white" text-align="center"
                                      padding-top="3pt">
                                Total
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
            <fo:table table-layout="fixed" width="100%">
                <fo:table-column column-width="10%" />
                <fo:table-column column-width="15%" />
                <fo:table-column column-width="15%" />
                <fo:table-column column-width="15%" />
                <fo:table-column column-width="15%" />
                <fo:table-column column-width="15%" />
                <fo:table-column column-width="15%" />
                <fo:table-body>
                    <fo:table-row>
                        <xsl:if test="position() mod 2">
                            <xsl:attribute name="background-color">
                                <xsl:text>#EEF0F2</xsl:text>
                            </xsl:attribute>
                        </xsl:if>
                        <fo:table-cell border-style="solid"
                                       border-width="1.0pt">
                            <xsl:apply-templates select="item/id" />
                        </fo:table-cell>
                        <fo:table-cell border-style="solid"
                                       border-width="1.0pt">
                            <xsl:apply-templates select="item/barcode" />
                        </fo:table-cell>
                        <fo:table-cell border-style="solid"
                                       border-width="1.0pt">
                            <xsl:apply-templates select="item/brand" />
                        </fo:table-cell>
                        <fo:table-cell border-style="solid"
                                       border-width="1.0pt">
                            <xsl:apply-templates select="item/name" />
                        </fo:table-cell>
                        <fo:table-cell border-style="solid"
                                       border-width="1.0pt">
                            <xsl:apply-templates select="item/quantity" />
                        </fo:table-cell>
                        <fo:table-cell border-style="solid"
                                       border-width="1.0pt">
                            <xsl:apply-templates select="item/sellingPrice" />
                        </fo:table-cell>
                        <fo:table-cell border-style="solid"
                                       border-width="1.0pt">
                            <xsl:apply-templates select="item/cost" />
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
            <fo:table table-layout="fixed" width="100%">
                <fo:table-column column-width="50%" />
                <fo:table-column column-width="50%" />
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell padding-top="50pt">
                            <fo:block font-size="20pt" font-family="sans-serif"
                                      color="black" text-align="center" >
                                Total
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding-top="50pt">
                            <fo:block font-size="20pt" font-family="sans-serif"
                                      color="blue" text-align="center">
                                <xsl:value-of select="total" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>
    <xsl:template match="id">
        <fo:block font-size="12pt" font-family="sans-serif"
                  space-after.optimum="3pt" text-align="center" padding="3pt">
            <xsl:value-of select="." />
        </fo:block>
    </xsl:template>
    <xsl:template match="barcode">
        <fo:block font-size="12pt" font-family="sans-serif"
                  space-after.optimum="3pt" text-align="center" padding="3pt">
            <xsl:value-of select="." />
        </fo:block>
    </xsl:template>
    <xsl:template match="brand">
        <fo:block font-size="12pt" font-family="sans-serif"
                  space-after.optimum="3pt" text-align="center" padding="3pt">
            <xsl:value-of select="." />
        </fo:block>
    </xsl:template>
    <xsl:template match="name">
        <fo:block font-size="12pt" font-family="sans-serif"
                  space-after.optimum="3pt" text-align="center" padding="3pt">
            <xsl:value-of select="." />
        </fo:block>
    </xsl:template>
    <xsl:template match="quantity">
        <fo:block font-size="12pt" font-family="sans-serif"
                  space-after.optimum="3pt" text-align="center" padding="3pt">
            <xsl:value-of select="." />
        </fo:block>
    </xsl:template>
    <xsl:template match="sellingPrice">
        <fo:block font-size="12pt" font-family="sans-serif"
                  space-after.optimum="3pt" text-align="center" padding="3pt">
            <xsl:value-of select="." />
        </fo:block>
    </xsl:template>
    <xsl:template match="cost">
        <fo:block font-size="12pt" font-family="sans-serif"
                  space-after.optimum="3pt" text-align="center" padding="3pt">
            <xsl:value-of select="." />
        </fo:block>
    </xsl:template>
</xsl:stylesheet>