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
	../tools/poky-docbook-to-pdf kernel-manual.xml ../template

##
# These URI should be rewritten by your distribution's xml catalog to
# match your localy installed XSL stylesheets.

html:
#       See http://www.sagehill.net/docbookxsl/HtmlOutput.html 

#	xsltproc $(XSLTOPTS) -o yocto-project-qs.html $(XSL_XHTML_URI) yocto-project-qs.xml
	xsltproc $(XSLTOPTS) -o kernel-manual.html yocto-project-kernel-manual-customization.xsl kernel-manual.xml

tarball: html
	tar -cvzf yocto-project-kernel-manual.tgz yocto-project-kernel-manual.html style.css figures/kernel-title.png figures/kernel-big-picture.png figures/kernel-architecture-overview.png

validate:
	xmllint --postvalid --xinclude --noout yocto-project-kernel-manual.xml

OUTPUTS = yocto-project-kernel-manual.tgz yocto-project-kernel-manual.html kernel-manual.pdf
SOURCES = *.png *.xml *.css

publish:
	scp -r $(OUTPUTS) $(SOURCES) o-hand.com:/srv/www/pokylinux.org/doc/

clean:
	rm -f $(OUTPUTS)
