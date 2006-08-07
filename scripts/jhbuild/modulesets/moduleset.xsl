<?xml version='1.0'?> <!--*- mode: nxml -*-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:output method="html" encoding="ISO-8859-1" indent="yes" />
  <xsl:key name="module-id" match="moduleset/*" use="@id" />

  <xsl:template match="/">
    <html>
      <head>
        <title>Module Set</title>
        <style type="text/css">
          <xsl:text>
            div.cvsmodule, div.mozillamodule {
              padding: 0.5em;
              margin: 0.5em;
              background: #87CEFA;
            }
            div.svnmodule {
              padding: 0.5em;
              margin: 0.5em;
              background: #67AEDA;
            }
            div.metamodule {
              padding: 0.5em;
              margin: 0.5em;
              background: #F08080;
            }
            div.tarball {
              padding: 0.5em;
              margin: 0.5em;
              background: #EEDD82;
            }
          </xsl:text>
        </style>
      </head>
      <body>
        <xsl:apply-templates />
      </body>
    </html>
  </xsl:template>

  <xsl:template match="moduleset">
    <h1>Module Set</h1>
    <xsl:apply-templates />
  </xsl:template>

  <xsl:template match="dependencies">
    <xsl:variable name="deps" select="dep/@package" />
    <xsl:for-each select="$deps">
      <a href="#{generate-id(key('module-id', .))}">
        <xsl:value-of select="." />
      </a>
      <xsl:if test="not($deps[last()] = .)">
        <xsl:text>, </xsl:text>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>

  <xsl:template match="cvsmodule">
    <div class="{name(.)}">
      <h2>
        <xsl:value-of select="@id" />
        <a name="{generate-id(.)}" />
      </h2>
      <table>
        <tr>
          <th align="left">Module:</th>
          <td>
            <xsl:choose>
              <xsl:when test="@module">
                <xsl:value-of select="@module" />
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="@id" />
              </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="@revision">
              <xsl:text> rv:</xsl:text>
              <xsl:value-of select="@revision" />
            </xsl:if>
          </td>
        </tr>
        <xsl:if test="@checkoutdir">
          <tr>
            <th align="left">Checkout directory:</th>
            <td><xsl:value-of select="@checkoutdir" /></td>
          </tr>
        </xsl:if>
        <xsl:if test="@autogenargs">
          <tr>
            <th align="left">Autogen args:</th>
            <td><xsl:value-of select="@autogenargs" /></td>
          </tr>
        </xsl:if>
        <xsl:if test="@cvsroot">
          <tr>
            <th align="left">CVS Root:</th>
            <td><xsl:value-of select="@cvsroot" /></td>
          </tr>
        </xsl:if>
        <xsl:if test="dependencies">
          <tr>
            <th align="left" valign="top">Dependencies:</th>
            <td><xsl:apply-templates select="dependencies" /></td>
          </tr>
        </xsl:if>
      </table>
    </div>
  </xsl:template>

  <xsl:template match="svnmodule">
    <div class="{name(.)}">
      <h2>
        <xsl:value-of select="@id" />
        <a name="{generate-id(.)}" />
      </h2>
      <table>
        <tr>
          <th align="left">Module:</th>
          <td>
            <xsl:choose>
              <xsl:when test="@module">
                <xsl:value-of select="@module" />
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="@id" />
              </xsl:otherwise>
            </xsl:choose>
          </td>
        </tr>
        <xsl:if test="@checkoutdir">
          <tr>
            <th align="left">Checkout directory:</th>
            <td><xsl:value-of select="@checkoutdir" /></td>
          </tr>
        </xsl:if>
        <xsl:if test="@autogenargs">
          <tr>
            <th align="left">Autogen args:</th>
            <td><xsl:value-of select="@autogenargs" /></td>
          </tr>
        </xsl:if>
        <xsl:if test="@svnroot">
          <tr>
            <th align="left">SVN Repository:</th>
            <td><xsl:value-of select="@svnroot" /><xsl:if test="@path"><xsl:value-of select="@path" /></xsl:if></td>
          </tr>
        </xsl:if>
        <xsl:if test="dependencies">
          <tr>
            <th align="left" valign="top">Dependencies:</th>
            <td><xsl:apply-templates select="dependencies" /></td>
          </tr>
        </xsl:if>
      </table>
    </div>
  </xsl:template>

  <xsl:template match="metamodule">
    <div class="{name(.)}">
      <h2>
        <xsl:value-of select="@id" />
        <a name="{generate-id(.)}" />
      </h2>
      <table>
        <xsl:if test="dependencies">
          <tr>
            <th align="left" valign="top">Dependencies:</th>
            <td><xsl:apply-templates select="dependencies" /></td>
          </tr>
        </xsl:if>
      </table>
    </div>
  </xsl:template>

  <xsl:template match="patches">
    <ul>
      <xsl:for-each select="patch">
        <li><xsl:value-of select="." /></li>
      </xsl:for-each>
    </ul>
  </xsl:template>

  <xsl:template match="tarball">
    <div class="{name(.)}">
      <h2>
        <xsl:value-of select="@id" />
        <a name="{generate-id(.)}" />
      </h2>
      <table>
        <tr>
          <th align="left">Version:</th>
          <td><xsl:value-of select="@version" /></td>
        </tr>
        <xsl:if test="@versioncheck">
          <tr>
            <th align="left">Version check:</th>
            <td><xsl:value-of select="@versioncheck" /></td>
          </tr>
        </xsl:if>
        <tr>
          <th align="left">Source:</th>
          <td>
            <a href="{source/@href}">
              <xsl:value-of select="source/@href" />
            </a>
            <xsl:if test="source/@size">
              <xsl:text> (</xsl:text>
              <xsl:value-of select="source/@size" />
              <xsl:text> bytes)</xsl:text>
            </xsl:if>
          </td>
        </tr>
        <xsl:if test="patches">
          <tr>
            <th align="left" valign="top">Patches:</th>
            <td><xsl:apply-templates select="patches" /></td>
          </tr>
        </xsl:if>
        <xsl:if test="dependencies">
          <tr>
            <th align="left" valign="top">Dependencies:</th>
            <td><xsl:apply-templates select="dependencies" /></td>
          </tr>
        </xsl:if>
      </table>
    </div>
  </xsl:template>

  <xsl:template match="mozillamodule">
    <div class="{name(.)}">
      <h2>
        <xsl:value-of select="@id" />
        <a name="{generate-id(.)}" />
      </h2>
      <table>
	<tr>
          <th align="left">Module:</th>
          <td>
            <xsl:choose>
              <xsl:when test="@module">
                <xsl:value-of select="@module" />
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="@id" />
              </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="@revision">
              <xsl:text> rv:</xsl:text>
              <xsl:value-of select="@revision" />
            </xsl:if>
          </td>
        </tr>
        <xsl:if test="@checkoutdir">
          <tr>
            <th align="left">Checkout directory:</th>
            <td><xsl:value-of select="@checkoutdir" /></td>
          </tr>
        </xsl:if>
        <xsl:if test="@autogenargs">
          <tr>
            <th align="left">Autogen args:</th>
            <td><xsl:value-of select="@autogenargs" /></td>
          </tr>
        </xsl:if>
        <xsl:if test="@cvsroot">
          <tr>
            <th align="left">CVS Root:</th>
            <td><xsl:value-of select="@cvsroot" /></td>
          </tr>
        </xsl:if>
        <xsl:if test="dependencies">
          <tr>
            <th align="left" valign="top">Dependencies:</th>
            <td><xsl:apply-templates select="dependencies" /></td>
          </tr>
        </xsl:if>
      </table>
    </div>
  </xsl:template>

</xsl:stylesheet>
