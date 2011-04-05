XSLTOPTS = --stringparam html.stylesheet style.css \
         --xinclude

XSL_BASE_URI  = http://docbook.sourceforge.net/release/xsl/current
XSL_XHTML_URI = $(XSL_BASE_URI)/xhtml/docbook.xsl

all: html tarball

##
# These URI should be rewritten by your distribution's xml catalog to
# match your localy installed XSL stylesheets.

html:
#       See http://www.sagehill.net/docbookxsl/HtmlOutput.html 

#	xsltproc $(XSLTOPTS) -o yocto-project-qs.html $(XSL_XHTML_URI) yocto-project-qs.xml
	xsltproc $(XSLTOPTS) -o yocto-project-qs.html yocto-project-qs-customization.xsl yocto-project-qs.xml

tarball: html
	tar -cvzf yocto-project-qs.tgz yocto-project-qs.html ypqs.pdf style.css figures/yocto-environment.png figures/building-an-image.png figures/using-a-pre-built-image.png figures/yocto-project-transp.png

validate:
	xmllint --postvalid --xinclude --noout yocto-project-qs.xml

OUTPUTS = yocto-project-qs.tgz yocto-project-qs.html ypqs.pdf
SOURCES = *.png *.xml *.css

publish:
	scp -r $(OUTPUTS) $(SOURCES) o-hand.com:/srv/www/pokylinux.org/doc/

clean:
	rm -f $(OUTPUTS)
