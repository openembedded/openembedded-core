all: html pdf

pdf:

	./poky-doc-tools/poky-docbook-to-pdf poky-handbook.xml
#       -- old way --
#	dblatex poky-handbook.xml

html:
#       See http://www.sagehill.net/docbookxsl/HtmlOutput.html 
	xsltproc --stringparam html.stylesheet style.css \
	         --stringparam  chapter.autolabel 1 \
                 --stringparam  appendix.autolabel 1 \
	         --stringparam  section.autolabel 1 \
                 -o poky-handbook.html \
                 --xinclude /usr/share/xml/docbook/stylesheet/nwalsh/html/docbook.xsl                                 \
                  poky-handbook.xml
#       -- old way --
#	xmlto xhtml-nochunks poky-handbook.xml

tarball: html
	tar -cvzf poky-handbook.tgz poky-handbook.html style.css screenshots/ss-sato.png poky-beaver.png poky-handbook.png

validate:
	xmllint --postvalid --xinclude --noout poky-handbook.xml
