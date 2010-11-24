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
	xsltproc $(XSLTOPTS) -o yocto-project-kernal-manual.html yocto-project-kernal-manual-customization.xsl yocto-project-kernal-manual.xml

tarball: html
	tar -cvzf yocto-project-kernal-manual.tgz yocto-project-kernal-manual.html style.css figures/yocto-project-transp.png figures/kernel-big-picture.png figures/kernel-architecture-overview.png

validate:
	xmllint --postvalid --xinclude --noout yocto-project-kernal-manual.xml

OUTPUTS = yocto-project-kernal-manual.tgz yocto-project-kernal-manual.html
SOURCES = *.png *.xml *.css

publish:
	scp -r $(OUTPUTS) $(SOURCES) o-hand.com:/srv/www/pokylinux.org/doc/

clean:
	rm -f $(OUTPUTS)
