all: html pdf tarball

pdf:

	./poky-doc-tools/poky-docbook-to-pdf poky-handbook.xml
	./poky-doc-tools/poky-docbook-to-pdf bsp-guide.xml
#       -- old way --
#	dblatex poky-handbook.xml

XSLTOPTS = --stringparam html.stylesheet style.css \
           --stringparam  chapter.autolabel 1 \
           --stringparam  appendix.autolabel 1 \
           --stringparam  section.autolabel 1
XSLTOPTS2 = --xinclude /usr/share/xml/docbook/stylesheet/nwalsh/html/docbook.xsl

html:
#       See http://www.sagehill.net/docbookxsl/HtmlOutput.html 
	xsltproc $(XSLTOPTS) -o poky-handbook.html $(XSLTOPTS2) poky-handbook.xml
	xsltproc $(XSLTOPTS) -o bsp-guide.html $(XSLTOPTS2) bsp-guide.xml
#       -- old way --
#	xmlto xhtml-nochunks poky-handbook.xml

tarball: html
	tar -cvzf poky-handbook.tgz poky-handbook.html style.css screenshots/ss-sato.png poky-beaver.png poky-handbook.png

validate:
	xmllint --postvalid --xinclude --noout poky-handbook.xml

OUTPUTS = poky-handbook.tgz poky-handbook.html poky-handbook.pdf bsp-guide.pdf
SOURCES = *.png *.xml *.css *.svg

publish:
	scp -r $(OUTPUTS) $(SOURCES) o-hand.com:/srv/www/pokylinux.org/doc/
