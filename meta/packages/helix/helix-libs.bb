DESCRIPTION = "Helix Client Libraries"
SECTION = "base"
DEPENDS = "ribosome gtk+ libxv"
HOMEPAGE = "http://helixcommunity.org"
LICENSE = "RPSL"

_SNAPSHOT = "atlas310_11212008"
_TARBALL_SERVER = "http://git.moblin.org/repos/users/rusty"

PV="r0"

SRC_URI = "${_TARBALL_SERVER}/helix-libs_${_SNAPSHOT}.tar.bz2 \
           file://helix-libs/helix-player.pc \
           file://helix-libs/add-clutter.patch;patch=1"
S = "${WORKDIR}/helix-libs_${_SNAPSHOT}"

export BUILD_ROOT=${STAGING_DIR_HOST}${libdir}/ribosome
export BUILDRC=${BUILD_ROOT}/buildrc
export SYSTEM_ID=linux-2.2-libc6-gcc32-i586
export BUILD=$BUILD_ROOT/bin/build.py

COMPATIBLE_HOST = '(i.86.*-linux)'

do_compile() {
	${BUILD} -k -trelease -mclutter -Phelix-client-all-defines clutter
}

do_install() {
	mkdir -p ${D}/opt/helix/lib
	mkdir -p ${D}/opt/helix/include
	mkdir -p ${D}${libdir}/pkgconfig

	install -m 0644 clientapps/clutter/player.h ${D}/opt/helix/include/
	install -m 0644 ../helix-libs/helix-player.pc ${D}${libdir}/pkgconfig

	install -m 0644 release/*.so ${D}/opt/helix/lib

	install -d ${D}${libdir}
	install -m 0644 release/libhelix-player.so ${D}${libdir}
}

sysroot_stage_all_append() {
	sysroot_stage_dir ${D}/opt/helix ${SYSROOT_DESTDIR}/${STAGING_DIR_TARGET}/helix
}

FILES_${PN} = "/usr/lib/libhelix-player.so"
FILES_${PN} += "/opt/helix/lib"
FILES_${PN}-dev = "/usr/lib/pkgconfig"
FILES_${PN}-dev += "/opt/helix/include"
