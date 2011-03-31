XSLTOPTS = --stringparam html.stylesheet style.css \
           --stringparam  chapter.autolabel 1 \
           --stringparam  section.autolabel 1 \
           --stringparam  section.label.includes.component.label 1 \
	   --xinclude

##
# These URI should be rewritten by your distribution's xml catalog to
# match your localy installed XSL stylesheets.
XSL_BASE_URI  = http://docbook.sourceforge.net/release/xsl/current
XSL_XHTML_URI = $(XSL_BASE_URI)/xhtml/docbook.xsl

all: html pdf tarball

pdf:
	../tools/poky-docbook-to-pdf bsp-guide.xml ../template

html:
#       See http://www.sagehill.net/docbookxsl/HtmlOutput.html 
	xsltproc $(XSLTOPTS) -o bsp-guide.html bsp-guide-customization.xsl bsp-guide.xml

tarball: html
	tar -cvzf bsp-guide.tgz style.css bsp-guide.html bsp-guide.pdf figures/bsp-title.png

validate:
	xmllint --postvalid --xinclude --noout bsp-guide.xml

OUTPUTS = bsp-guide.pdf bsp-guide.html
SOURCES = *.png *.xml *.css *.svg

publish:
	scp -r $(OUTPUTS) $(SOURCES) o-hand.com:/srv/www/pokylinux.org/doc/

clean:
	rm -f $(OUTPUTS)
