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

all: html pdf tarball

pdf:
	../tools/poky-docbook-to-pdf poky-ref-manual.xml ../template

html:
#       See http://www.sagehill.net/docbookxsl/HtmlOutput.html 
	xsltproc $(XSLTOPTS) -o poky-ref-manual.html poky-ref-manual-customization.xsl poky-ref-manual.xml

tarball: html
	tar -cvzf poky-ref-manual.tgz poky-ref-manual.html style.css figures/yocto-project-transp.png figures/poky-ref-manual.png screenshots/ss-sato.png

validate:
	xmllint --postvalid --xinclude --noout poky-ref-manual.xml

OUTPUTS = poky-ref-manual.tgz poky-ref-manual.html poky-ref-manual.pdf 
SOURCES = *.png *.xml *.css *.svg

publish:
	scp -r $(OUTPUTS) $(SOURCES) o-hand.com:/srv/www/pokylinux.org/doc/

clean:
	rm -f $(OUTPUTS)
