DESCRIPTION = "GNU C Library"
HOMEPAGE = "http://www.gnu.org/software/libc/libc.html"
SECTION = "libs"
PRIORITY = "required"
LICENSE = "LGPL"
# nptl needs unwind support in gcc, which can't be built without glibc.
DEPENDS = "${@['virtual/${TARGET_PREFIX}gcc-initial', 'virtual/${TARGET_PREFIX}gcc']['nptl' in '${GLIBC_ADDONS}']} linux-libc-headers"
RDEPENDS_${PN}-dev = "linux-libc-headers-dev"
#this leads to circular deps, so lets not add it yet
#RDEPENDS_ldd += " bash"
PROVIDES = "virtual/libc ${@['virtual/${TARGET_PREFIX}libc-for-gcc', '']['nptl' in '${GLIBC_ADDONS}']}"
PROVIDES += "virtual/libintl virtual/libiconv"

inherit autotools

GLIBC_EXTRA_OECONF ?= ""
INHIBIT_DEFAULT_DEPS = "1"

do_rig_locales() {
	# If indicated, only build a limited selection of locales
	if [ "x${LIMIT_BUILT_LOCALES}" != "x" ]; then
		INFILE="${S}/localedata/SUPPORTED"
		OUTFILE="${S}/localedata/SUPPORTED.tmp"
		head -n 3 $INFILE > $OUTFILE
		for i in ${LIMIT_BUILT_LOCALES}; do
			echo 
			grep $i $INFILE >> $OUTFILE
		done
		head --lines=-1 $OUTFILE > $INFILE
		tail --lines=1 $OUTFILE | sed 's#\\##' >> $INFILE
	fi
}

addtask rig_locales before do_compile after do_configure
