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

PR = "r0"

BASE_SRC_URI = "${SOURCEFORGE_MIRROR}/tcl/tcl${PV}-src.tar.gz \
                file://tcl-add-soname.patch"

SRC_URI = "${BASE_SRC_URI} \
	   file://fix_non_native_build_issue.patch \
	   file://fix_issue_with_old_distro_glibc.patch "

SRC_URI[md5sum] = "fa3a9bf9b2d6ed2431f1baa46f4058b8"
SRC_URI[sha256sum] = "9b868dd563e65671a26fcf518b6b86c1bb1b6756f48fdc90f04301d4f3a6596a"

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
	oe_libinstall -so libtcl8.5 ${D}${libdir}
	ln -sf ./tclsh8.5 ${D}${bindir}/tclsh
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
FILES_tcl-lib = "${libdir}/libtcl8.5.so*"
FILES_${PN} += "${prefix}/lib/tcl8.5 ${prefix}/lib/tcl8"
FILES_${PN}-dev += "${libdir}/tclConfig.sh"

# isn't getting picked up by shlibs code
RDEPENDS_${PN} += "tcl-lib"
RDEPENDS_${PN}_class-native = ""

BBCLASSEXTEND = "native"
