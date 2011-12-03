DESCRIPTION = "Tool Command Language"
LICENSE = "BSD-3-Clause"
SECTION = "devel/tcltk"
HOMEPAGE = "http://tcl.sourceforge.net"
DEPENDS = "tcl-native"
LIC_FILES_CHKSUM = "file://../license.terms;md5=a47a9be26d03f925fc1fbd2784f27e11 \
    file://../compat/license.terms;md5=a47a9be26d03f925fc1fbd2784f27e11 \
    file://../library/license.terms;md5=a47a9be26d03f925fc1fbd2784f27e11 \
    file://../macosx/license.terms;md5=a47a9be26d03f925fc1fbd2784f27e11 \
    file://../tests/license.terms;md5=a47a9be26d03f925fc1fbd2784f27e11 \
    file://../win/license.terms;md5=a47a9be26d03f925fc1fbd2784f27e11 \
    "

PR = "r2"

BASE_SRC_URI = "${SOURCEFORGE_MIRROR}/tcl/tcl${PV}-src.tar.gz \
                file://tcl-add-soname.patch"

SRC_URI = "${BASE_SRC_URI} \
	   file://fix_non_native_build_issue.patch "

SRC_URI[md5sum] = "b01a9691c83990b3db0ce62d1012ca67"
SRC_URI[sha256sum] = "8addc385fa6b5be4605e6d68fbdc4c0e674c5af1dc1c95ec5420390c4b08042a"

SRC_URI_virtclass-native = "${BASE_SRC_URI}"

S = "${WORKDIR}/tcl${PV}/unix"

inherit autotools

DEPENDS_virtclass-native = ""

EXTRA_OECONF = "--enable-threads"

do_configure() {
	gnu-configize
	oe_runconf
}

do_compile_prepend() {
	echo > ../compat/fixstrtod.c
}

do_install() {
	autotools_do_install
	oe_libinstall -so libtcl8.5 ${STAGING_LIBDIR}
	ln -sf ./tclsh8.5 ${D}${bindir}/tclsh
	sed -i "s+${WORKDIR}+${STAGING_INCDIR}+g" tclConfig.sh
	sed -i "s,-L${libdir},," tclConfig.sh
	install -d ${STAGING_BINDIR_CROSS}/
	install -m 0755 tclConfig.sh ${STAGING_BINDIR_CROSS}
	cd ..
	for dir in compat generic unix
	do
		install -d ${STAGING_INCDIR}/tcl${PV}/$dir
		install -m 0644 $dir/*.h ${STAGING_INCDIR}/tcl${PV}/$dir/
	done
}

PACKAGES =+ "${PN}-lib"
FILES_${PN}-lib = "${libdir}/libtcl8.5.so*"
FILES_${PN} += "${prefix}/lib/tcl8.5"
FILES_${PN}-dev += "${libdir}/tclConfig.sh"

BBCLASSEXTEND = "native"
