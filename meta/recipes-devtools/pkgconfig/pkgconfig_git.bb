require pkgconfig.inc

SRC_URI = "git://anongit.freedesktop.org/pkg-config;protocol=git \
           file://autofoo.patch \
           file://glibconfig-sysdefs.h"

S = "${WORKDIR}/git/"

SRCREV = "66d49f1375fec838bcd301bb4ca2ef76cee0e47c"
PV = "0.23+git${SRCPV}"

DEFAULT_PREFERENCE = "-1"

# Can't do native version with git since git-native depends on pkgconfig-native
BBCLASSEXTEND = "nativesdk"

do_fixsource() {
	# Adapted from autogen.sh
	cd ${S}
	tar -xvzf glib-1.2.10.tar.gz
	
	chmod +w `find glib-1.2.10 -name Makefile.am`
	perl -p -i.bak -e "s/lib_LTLIBRARIES/noinst_LTLIBRARIES/g" `find glib-1.2.10 -name Makefile.am`
	perl -p -i.bak -e "s/bin_SCRIPTS/noinst_SCRIPTS/g" `find glib-1.2.10 -name Makefile.am`
	perl -p -i.bak -e "s/include_HEADERS/noinst_HEADERS/g" `find glib-1.2.10 -name Makefile.am`
	perl -p -i.bak -e "s/glibnoinst_HEADERS/noinst_HEADERS/g" `find glib-1.2.10 -name Makefile.am`
	perl -p -i.bak -e 's/([a-zA-Z0-9]+)_DATA/noinst_DATA/g' `find glib-1.2.10 -name Makefile.am`
	perl -p -i.bak -e "s/info_TEXINFOS/noinst_TEXINFOS/g" `find glib-1.2.10 -name Makefile.am`
	perl -p -i.bak -e "s/man_MANS/noinst_MANS/g" `find glib-1.2.10 -name Makefile.am`
	
	## patch gslist.c to have stable sort
	perl -p -w -i.bak -e 's/if \(compare_func\(l1->data,l2->data\) < 0\)/if \(compare_func\(l1->data,l2->data\) <= 0\)/g' glib-1.2.10/gslist.c
	
	# Update random auto* files to actually have something which have a snowball's
	# chance in a hot place of working with modern auto* tools.
	
	(cd glib-1.2.10 && for p in ../glib-patches/*.diff; do echo $p; patch -p1 < $p || exit 1; done ) || exit 1
}

addtask fixsource before do_patch after do_unpack
