SUBDIRS = common

EXTRA_DIST = poky-docbook-to-pdf.in

bin_SCRIPTS = poky-docbook-to-pdf

edit = sed \
        -e 's,@datadir\@,$(pkgdatadir),g' \
        -e 's,@prefix\@,$(prefix),g'      \
        -e 's,@version\@,@VERSION@,g'      

poky-docbook-to-pdf: poky-docbook-to-pdf.in
	rm -f poky-docbook-to-pdf
	$(edit) poky-docbook-to-pdf.in > poky-docbook-to-pdf

clean-local:
	rm -fr poky-docbook-to-pdf
	rm -fr poky-pr-docbook-to-pdf