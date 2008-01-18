# This recipe builds libowl and then stages the header and static lib;
# it intentionally does not stage the shared lib and create a package.
#
# Libowl is currently a 'cut and paste' library; this package makes our
# life a touch easier by not having to patch the source directly into
# applications; instead we add -lowl to the linker cmdline. Also, when
# we eventually make libowl into a normal shared library, this package
# will make that transition rather simple.

DESCRIPTION = "OpenedHand Widget Library"
HOMEPAGE = "http://www.o-hand.com"
LICENSE = "LGPL"
SECTION = "libs"
DEPENDS = "gtk+"
PV = "0.0+svnr${SRCREV}"
PR = "r3"

PACKAGES = ""

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=${PN};proto=http"

S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig

do_compile_prepend() {
	# have to unstage the library first so that the tests build
	rm -f ${STAGING_LIBDIR}/libowl*
	rm -f ${STAGING_INCDIR}/owl*
}

do_stage() {
	headers=`eval ls libowl/owl*.h`
	for header in $headers; do
		hdr_base=`eval basename $header`
		install -m 644 $header ${STAGING_INCDIR}/$hdr_base
	done
	install -m 644 libowl/.libs/libowl.a ${STAGING_LIBDIR}/
}
