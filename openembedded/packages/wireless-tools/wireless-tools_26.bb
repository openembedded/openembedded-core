DESCRIPTION = "Tools for the Linux Standard Wireless Extension Subsystem"
HOMEPAGE = "http://www.hpl.hp.com/personal/Jean_Tourrilhes/Linux/Tools.html"
SECTION = "base"
PRIORITY = "optional"
DEPENDS = "virtual/kernel"
MAINTAINER = "Michael 'Mickey' Lauer <mickey@Vanille.de>"
LICENSE = "GPL"
PR = "r6"

SRC_URI = "http://pcmcia-cs.sourceforge.net/ftp/contrib/wireless_tools.${PV}.tar.gz \
	   file://man.patch;patch=1 \
	   file://wireless-tools.if-pre-up"
S = "${WORKDIR}/wireless_tools.${PV}"

inherit module-base

do_configure() {
	rm -f wireless.h
}

do_compile() {
        unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
        oe_runmake KERNEL_SRC=${KERNEL_SOURCE} KERNEL_VERSION=${KERNEL_VERSION} BUILD_SHARED=y
}

do_install() {
	oe_runmake PREFIX=${D} install
	install -d ${D}${sysconfdir}/network/if-pre-up.d
	install ${WORKDIR}/wireless-tools.if-pre-up ${D}${sysconfdir}/network/if-pre-up.d/wireless-tools
}

FILES_${PN} = "${bindir} ${sbindir} ${libexecdir} ${libdir}/lib*.so.* \
               ${sysconfdir} ${sharedstatedir} ${localstatedir} \
               /bin /sbin /lib/*.so* ${datadir}/${PN} ${libdir}/${PN} \
	       /etc/network"
