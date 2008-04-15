DESCRIPTION = "The GNU internationalization library."
HOMEPAGE = "http://www.gnu.org/software/gettext/gettext.html"
SECTION = "libs"
LICENSE = "GPL"
PR = "r10"
PROVIDES = "virtual/libintl"

SRC_URI = "${GNU_MIRROR}/gettext/gettext-${PV}.tar.gz \
	   file://gettext-vpath.patch;patch=1;pnum=1 \
	   file://fixchicken.patch;patch=1;pnum=1 \
           file://linklib_from_0.17.patch;patch=1 \
           file://getline.m4.patch;patch=1 \
           file://disable_java.patch;patch=1"

SRC_URI_append_linux-uclibc = " file://gettext-error_print_progname.patch;patch=1"
SRC_URI_append_linux-uclibcgnueabi = " file://gettext-error_print_progname.patch;patch=1"

PARALLEL_MAKE = ""

inherit autotools

EXTRA_OECONF += "--without-lisp"
acpaths = '-I ${S}/autoconf-lib-link/m4/ \
	   -I ${S}/gettext-runtime/m4 \
	   -I ${S}/gettext-tools/m4'

do_configure_prepend() {
	rm -f ${S}/config/m4/libtool.m4
	install -m 0644 ${STAGING_DATADIR}/aclocal/libtool.m4 ${S}/config/m4/
}

do_stage () {
	autotools_stage_all

	# config.rpath is needed by some configure macros and needs to be autoinstalled.
	# automake will do this but config.rpath needs to be visible to automake
	for i in `ls -d ${STAGING_DATADIR}/automake*`
	do
		cp ${STAGING_DATADIR}/gettext/config.rpath $i
	done
}
