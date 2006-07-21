DESCRIPTION = "Tools for the Linux Standard Wireless Extension Subsystem"
HOMEPAGE = "http://www.hpl.hp.com/personal/Jean_Tourrilhes/Linux/Tools.html"
SECTION = "base"
PRIORITY = "optional"
MAINTAINER = "Michael 'Mickey' Lauer <mickey@Vanille.de>"
LICENSE = "GPL"
PR = "r1"

SRC_URI = "http://www.hpl.hp.com/personal/Jean_Tourrilhes/Linux/wireless_tools.28.pre13.tar.gz \
           file://man.patch;patch=1 \
	   file://wireless-tools.if-pre-up \
	   file://zzz-wireless.if-pre-up"
S = "${WORKDIR}/wireless_tools.28"

CFLAGS =+ "-I${S}"
EXTRA_OEMAKE = "-e 'BUILD_SHARED=y' \
		'INSTALL_DIR=${D}${base_sbindir}' \
		'INSTALL_LIB=${D}${libdir}' \
		'INSTALL_INC=${D}${includedir}' \
		'INSTALL_MAN=${D}${mandir}'"

do_compile() {
	oe_runmake all libiw.a
}

do_stage() {
	install -m 0644 wireless.h ${STAGING_INCDIR}/
	install -m 0644 iwlib.h ${STAGING_INCDIR}/
	oe_libinstall -a -so libiw ${STAGING_LIBDIR}/
}

do_install() {
	oe_runmake PREFIX=${D} install-iwmulticall install-dynamic
	install -d ${D}${sysconfdir}/network/if-pre-up.d
	install ${WORKDIR}/wireless-tools.if-pre-up ${D}${sysconfdir}/network/if-pre-up.d/wireless-tools
	install ${WORKDIR}/zzz-wireless.if-pre-up ${D}${sysconfdir}/network/if-pre-up.d/zzz-wireless
}

PACKAGES = "libiw libiw-dev libiw-doc ${PN} ${PN}-doc"
FILES_libiw = "${libdir}/*.so.*"
FILES_libiw-dev = "${libdir}/*.a ${libdir}/*.so ${includedir}"
FILES_libiw-doc = "${mandir}/man7"
FILES_${PN} = "${bindir} ${sbindir} ${base_sbindir} ${base_bindir} ${sysconfdir}/network"
FILES_${PN}-doc = "${mandir}/man8"
