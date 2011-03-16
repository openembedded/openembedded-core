DESCRIPTION  = "Sat Solver"
HOMEPAGE = "http://en.opensue.org/Portal:Libzypp"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE.BSD;md5=62272bd11c97396d4aaf1c41bc11f7d8"

DEPENDS = "libcheck rpm zlib expat db"

PV = "0.0-git${SRCPV}"
PR = "r6"

SRC_URI = "git://gitorious.org/opensuse/sat-solver.git;protocol=git \
           file://sat-solver_rpm5.patch \
           file://cmake.patch \
           file://db5.patch \
           file://builtin-arch.patch;apply=no \
           file://no-builtin-arch.patch;apply=no \
          "

S = "${WORKDIR}/git"

EXTRA_OECMAKE += "-DLIB=lib -DRPM5=RPM5"

inherit cmake pkgconfig

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_archpatch () {
	PKG_ARCH_TAIL=`sed -n ${S}/src/poolarch.c -e "s|^  \"\(${BASE_PACKAGE_ARCH}\)\",.*\"\(.*\)\",.*$|\2|p"`
	if [ "x${PKG_ARCH_TAIL}" == x ]; then
		PATCHFILE=${WORKDIR}/no-builtin-arch.patch
	else
		PATCHFILE=${WORKDIR}/builtin-arch.patch
	fi

	sed -i "${PATCHFILE}" \
		-e "s|@MACHINE_ARCH@|${MACHINE_ARCH}|g" \
		-e "s|@PKG_ARCH@|${BASE_PACKAGE_ARCH}|g" \
		-e "s|@PKG_ARCH_TAIL@|${PKG_ARCH_TAIL}|g"

	patch -p1 -i "${PATCHFILE}"
}

addtask archpatch before do_patch after do_unpack
