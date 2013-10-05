DESCRIPTION = "Tool Command Language"
LICENSE = "BSD-3-Clause"
SECTION = "devel/tcltk"
HOMEPAGE = "http://tcl.sourceforge.net"
DEPENDS = "tcl-native zlib"
LIC_FILES_CHKSUM = "file://../license.terms;md5=3c6f62c07835353e36f0db550ccfb65a \
    file://../compat/license.terms;md5=3c6f62c07835353e36f0db550ccfb65a \
    file://../library/license.terms;md5=3c6f62c07835353e36f0db550ccfb65a \
    file://../macosx/license.terms;md5=3c6f62c07835353e36f0db550ccfb65a \
    file://../tests/license.terms;md5=3c6f62c07835353e36f0db550ccfb65a \
    file://../win/license.terms;md5=3c6f62c07835353e36f0db550ccfb65a \
    "

BASE_SRC_URI = "${SOURCEFORGE_MIRROR}/tcl/tcl${PV}-src.tar.gz \
                file://tcl-add-soname.patch"

SRC_URI = "${BASE_SRC_URI} \
	   file://fix_non_native_build_issue.patch \
	   file://fix_issue_with_old_distro_glibc.patch \
	   file://no_packages.patch \
	   file://tcl-remove-hardcoded-install-path.patch \
	   file://let-DST-include-year-2099.patch;patchdir=.. "

SRC_URI[md5sum] = "573aa5fe678e9185ef2b3c56b24658d3"
SRC_URI[sha256sum] = "354422b9c4791685499123b2dfe01faa98b555c08906c010cb4449ddc75dcade"

SRC_URI_class-native = "${BASE_SRC_URI}"

S = "${WORKDIR}/tcl${PV}/unix"

inherit autotools

DEPENDS_class-native = ""

EXTRA_OECONF = "--enable-threads --disable-rpath"

do_configure() {
	( cd ${S}; gnu-configize )
	oe_runconf
}

do_compile_prepend() {
	echo > ${S}/../compat/fixstrtod.c
}

do_install() {
	autotools_do_install
	oe_libinstall -so libtcl8.6 ${D}${libdir}
	ln -sf ./tclsh8.6 ${D}${bindir}/tclsh
	sed -i "s+${WORKDIR}+${STAGING_INCDIR}+g" tclConfig.sh
	sed -i "s,-L${libdir},-L=${libdir},g" tclConfig.sh
	sed -i "s,-I${includedir},-I=${includedir},g" tclConfig.sh 
	install -d ${D}${bindir_crossscripts}
	install -m 0755 tclConfig.sh ${D}${bindir_crossscripts}
	cd ..
	for dir in compat generic unix
	do
		install -d ${D}${includedir}/tcl${PV}/$dir
		install -m 0644 ${S}/../$dir/*.h ${D}${includedir}/tcl${PV}/$dir/
	done
}

SYSROOT_PREPROCESS_FUNCS += "tcl_sysroot_preprocess"
tcl_sysroot_preprocess () {
	sysroot_stage_dir ${D}${bindir_crossscripts} ${SYSROOT_DESTDIR}${bindir_crossscripts}
}

PACKAGES =+ "tcl-lib"
FILES_tcl-lib = "${libdir}/libtcl8.6.so*"
FILES_${PN} += "${libdir}/tcl8.6 ${libdir}/tcl8"
FILES_${PN}-dev += "${libdir}/tclConfig.sh ${libdir}/tclooConfig.sh"

# isn't getting picked up by shlibs code
RDEPENDS_${PN} += "tcl-lib"
RDEPENDS_${PN}_class-native = ""

BBCLASSEXTEND = "native"
