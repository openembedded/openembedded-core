DESCRIPTION="EXT2 Filesystem Utilities"
SECTION="base"
PRIORITY="optional"
MAINTAINER="Greg Gilbert <greg@treke.net>"
RDEPENDS="libc6"
DEPENDS=virtual/libc 

SRC_URI=${SOURCEFORGE_MIRROR}/e2fsprogs/e2fsprogs-${PV}.tar.gz \
	file://${FILESDIR}/ln.patch;patch=1 \
	file://${FILESDIR}/configure.patch;patch=1 \
	file://${FILESDIR}/compile-subst.patch;patch=1 \
	file://${FILESDIR}/m4.patch;patch=1

inherit autotools

do_compile_prepend () {
	find ./ -print|xargs chmod u=rwX
}
