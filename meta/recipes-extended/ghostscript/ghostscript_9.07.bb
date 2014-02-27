SUMMARY = "The GPL Ghostscript PostScript/PDF interpreter"
DESCRIPTION = "Ghostscript is used for PostScript/PDF preview and printing.  Usually as \
a back-end to a program such as ghostview, it can display PostScript and PDF \
documents in an X11 environment. \
\
Furthermore, it can render PostScript and PDF files as graphics to be printed \
on non-PostScript printers. Supported printers include common \
dot-matrix, inkjet and laser models. \
\
Package gsfonts contains a set of standard fonts for Ghostscript. \
"
HOMEPAGE = "http://www.ghostscript.com"
SECTION = "console/utils"

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a5146dadaa8cd9f5f913b7577d49bf19"

DEPENDS = "ghostscript-native tiff jpeg fontconfig cups"
DEPENDS_class-native = ""

SRC_URI_BASE = "http://downloads.ghostscript.com/public/ghostscript-${PV}.tar.gz"

SRC_URI = "${SRC_URI_BASE} \
           file://ghostscript-9.02-prevent_recompiling.patch \
           file://ghostscript-9.02-genarch.patch \
           file://objarch.h \
           file://ghostscript-9.02-parallel-make.patch \
           file://ghostscript-9.05-NOT-check-endian.patch \
           "

SRC_URI_class-native = "${SRC_URI_BASE} \
                        file://ghostscript-native-fix-disable-system-libtiff.patch \
                        "

SRC_URI[md5sum] = "57ebf17c5abcf0fc95a386bfff08c1a4"
SRC_URI[sha256sum] = "44800d004c53f13192d1b5db413119198ddfc8a11c4d2a030aac2f2fda822ebf"

EXTRA_OECONF = "--without-x --with-system-libtiff --without-jbig2dec \
                --with-fontpath=${datadir}/fonts --with-install-cups \
                --without-libidn \
                "

# Explicity disable libtiff, fontconfig,
# freetype, cups for ghostscript-native
EXTRA_OECONF_class-native = "--without-x --with-system-libtiff=no \
                             --without-jbig2dec \
                             --with-fontpath=${datadir}/fonts \
                             --without-libidn --disable-fontconfig \
                             --disable-freetype --disable-cups"

# This has been fixed upstream but for now we need to subvert the check for time.h
# http://bugs.ghostscript.com/show_bug.cgi?id=692443
# http://bugs.ghostscript.com/show_bug.cgi?id=692426
CFLAGS += "-DHAVE_SYS_TIME_H=1"
BUILD_CFLAGS += "-DHAVE_SYS_TIME_H=1"

inherit autotools-brokensep

do_configure_prepend () {
	mkdir -p obj
	mkdir -p soobj
	if [ -e ${WORKDIR}/objarch.h ]; then
		cp ${WORKDIR}/objarch.h obj/arch.h
	fi
	if [ ${SITEINFO_ENDIANNESS} = "le" ]; then
		export BIGENDIAN="0"
		export BIGENDIAN="0"
	else
		export BIGENDIAN="1"
		export BIGENDIAN="1"
	fi
}

do_configure_append () {
	# copy tools from the native ghostscript build
	if [ "${PN}" != "ghostscript-native" ]; then
		mkdir -p obj/aux soobj
		for i in genarch genconf mkromfs echogs gendev genht; do
			cp ${STAGING_BINDIR_NATIVE}/ghostscript-${PV}/$i obj/aux/$i
		done
	fi

	# replace cups paths from sysroots/.../usr/bin/crossscripts/cups-config with target paths
	# CUPSDATA is compiled into a utility, and CUPSSERVERBIN is used as an install path
	CUPSSERVERBIN=${exec_prefix}/lib/cups          # /usr/lib NOT libdir
	CUPSDATA=${datadir}/cups

	sed -e "s#^CUPSSERVERBIN=.*#CUPSSERVERBIN=${CUPSSERVERBIN}#" -i Makefile
	sed -e "s#^CUPSDATA=.*#CUPSDATA=${CUPSDATA}#" -i Makefile
}

do_install_append () {
    mkdir -p ${D}${datadir}/ghostscript/${PV}/
    cp -r Resource ${D}${datadir}/ghostscript/${PV}/
    cp -r iccprofiles ${D}${datadir}/ghostscript/${PV}/

    chown -R root:lp ${D}${sysconfdir}/cups
}

do_compile_class-native () {
    mkdir -p obj
    for i in genarch genconf mkromfs echogs gendev genht; do
        oe_runmake obj/aux/$i
    done
}

do_install_class-native () {
    install -d ${D}${bindir}/ghostscript-${PV}
    for i in genarch genconf mkromfs echogs gendev genht; do
        install -m 755 obj/aux/$i ${D}${bindir}/ghostscript-${PV}/$i
    done
}

BBCLASSEXTEND = "native"

# Ghostscript install tool 'instcopy' tries to remove already created
# directories during install and parallel make causes problems.
PARALLEL_MAKEINST=""

PACKAGES =+ "${PN}-cups"

FILES_${PN}-dbg += "${exec_prefix}/lib/cups/filter/.debug"

FILES_${PN}-cups += "${exec_prefix}/lib/cups/filter/gstoraster \
                     ${exec_prefix}/lib/cups/filter/gstopxl \
                     ${datadir}/cups \
                     ${sysconfdir}/cups \
                     "

RDEPENDS_${PN}-cups = "${PN}"
