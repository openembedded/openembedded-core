DESCRIPTION = "diffstat reads the output of diff and displays a histogram of \
the insertions, deletions, and modifications per-file. It is useful for \
reviewing large, complex patch files."
HOMEPAGE = "http://invisible-island.net/diffstat/"
PRIORITY = "optional"
SECTION = "devel"

# NOTE: The upstream maintainer has a single 'diffstat.tar.gz' for the
# latest version of the package.  It could easily change out from under us.
# I'd rather rely on debian, and possible have the sources vanish, than risk
# the sources _changing_ underneith us. -CL
SRC_URI = "${DEBIAN_MIRROR}/main/d/diffstat/diffstat_${PV}.orig.tar.gz \
	   ${DEBIAN_MIRROR}/main/d/diffstat/diffstat_${PV}-1.diff.gz;patch=1"
S = "${WORKDIR}/diffstat-${PV}"

inherit autotools

do_configure () {
	if [ ! -e acinclude.m4 ]; then
		mv aclocal.m4 acinclude.m4
	fi
	autotools_do_configure
}
