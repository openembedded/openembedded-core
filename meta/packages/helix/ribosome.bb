DESCRIPTION = "Ribosome: HelixCommunity Build System"
SECTION = "base"
HOMEPAGE = "http://helixcommunity.org"
LICENSE = "GPLv2"

_SNAPSHOT = "22102008"
_TARBALL_SERVER = "http://git.moblin.org/repos/users/rusty"

PV="r0"

SRC_URI = "${_TARBALL_SERVER}/ribosome-${_SNAPSHOT}.tar.bz2 \
           ${_TARBALL_SERVER}/client-bif-${_SNAPSHOT}.tar.bz2 \
           ${_TARBALL_SERVER}/common-bif-${_SNAPSHOT}.tar.bz2 \
           file://ribosome/clutter.bif \
           file://ribosome/buildrc"

S = "${WORKDIR}"

COMPATIBLE_HOST = '(i.86.*-linux)'

do_install() {
	# Install build system of doom
	install -d ${D}${libdir}
	cp -a ribosome-${_SNAPSHOT} ${D}${libdir}/ribosome
	install -m 0644 ribosome/buildrc ${D}${libdir}/ribosome/

	# Install client BIF's
	install -d ${D}${libdir}/ribosome/bif-cvs/helix/client/build/BIF
	install -m 0644 client-bif-${_SNAPSHOT}/*.bif \
           ${D}${libdir}/ribosome/bif-cvs/helix/client/build/BIF

	# Install common BIF's
	install -d ${D}${libdir}/ribosome/bif-cvs/helix/common/build/BIF
	install -m 0644 common-bif-${_SNAPSHOT}/*.bif \
           ${D}${libdir}/ribosome/bif-cvs/helix/common/build/BIF

	# Install our own custom BIF
	install -m 0644 ribosome/*.bif ${D}${libdir}/ribosome/bif-cvs/helix/client/build/BIF/
}
