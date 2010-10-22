all: html pdf tarball

pdf:
	../tools/poky-docbook-to-pdf poky-ref-manual.xml ../template
	../tools/poky-docbook-to-pdf bsp-guide.xml ../template

XSLTOPTS = --stringparam html.stylesheet style.css \
           --stringparam  chapter.autolabel 1 \
           --stringparam  appendix.autolabel A \
           --stringparam  section.autolabel 1 \
           --stringparam  section.label.includes.component.label 1 \
	   --xinclude

##
# These URI should be rewritten by your distribution's xml catalog to
# match your localy installed XSL stylesheets.
XSL_BASE_URI  = http://docbook.sourceforge.net/release/xsl/current
XSL_XHTML_URI = $(XSL_BASE_URI)/xhtml/docbook.xsl

html:
#       See http://www.sagehill.net/docbookxsl/HtmlOutput.html 
	xsltproc $(XSLTOPTS) -o poky-ref-manual.html $(XSL_XHTML_URI) poky-ref-manual.xml
	xsltproc $(XSLTOPTS) -o bsp-guide.html $(XSL_XHTML_URI) bsp-guide.xml

tarball: html
	tar -cvzf poky-ref-manual.tgz poky-ref-manual.html style.css figures/yocto-project-transp.png figures/poky-ref-manual.png

validate:
	xmllint --postvalid --xinclude --noout poky-ref-manual.xml

OUTPUTS = poky-ref-manual.tgz poky-ref-manual.html poky-ref-manual.pdf bsp-guide.pdf bsp-guide.html
SOURCES = *.png *.xml *.css *.svg

publish:
	scp -r $(OUTPUTS) $(SOURCES) o-hand.com:/srv/www/pokylinux.org/doc/

clean:
	rm -f $(OUTPUTS)
