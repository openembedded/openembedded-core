SUBDIRS = common

EXTRA_DIST = poky-docbook-to-pdf.in

bin_SCRIPTS = poky-docbook-to-pdf

edit = sed \
        -e 's,@datadir\@,$(pkgdatadir),g' \
        -e 's,@prefix\@,$(prefix),g'      \
        -e 's,@version\@,@VERSION@,g'

##
# These URI should be rewritten by your distribution's xml catalog to
# match your localy installed XSL stylesheets.
XSL_BASE_URI="http://docbook.sourceforge.net/release/xsl/current"
XSL_TEMPLATE_URI = $(XSL_BASE_URI)/template/titlepage.xsl

poky-docbook-to-pdf: poky-docbook-to-pdf.in
	rm -f poky-docbook-to-pdf
	$(edit) poky-docbook-to-pdf.in > poky-docbook-to-pdf

clean-local:
	rm -fr poky-docbook-to-pdf
	rm -fr poky-pr-docbook-to-pdf