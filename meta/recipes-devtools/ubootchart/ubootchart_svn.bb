SUMMARY = "A boot profiling tool"
HOMEPAGE = "http://code.google.com/p/ubootchart/"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=8f0e2cd40e05189ec81232da84bd6e1a"
SRCREV = "12"
PV = "0.0+r${SRCPV}"
PR = "r6"

#RRECOMMENDS_${PN} = "acct"

SRC_URI = "svn://ubootchart.googlecode.com/svn/;protocol=http;module=trunk \
        file://sysvinit.patch;striplevel=0 \
        file://ubootchart-stop \
        file://ubootchart.desktop"

S = "${WORKDIR}/trunk"

inherit update-alternatives

ALTERNATIVE_${PN} = "init"
ALTERNATIVE_TARGET[init] = "${base_sbindir}/ubootchartd"
ALTERNATIVE_LINK_NAME[init] = "${base_sbindir}/init"
ALTERNATIVE_PRIORITY = "70"

do_compile() {
        ${CC} ${CFLAGS} ${LDFLAGS} ${LIBS} ${INCLUDES} ${S}/ubootchartd_bin.c -o ubootchartd_bin
}

do_install() {
        install -m 0755 -d ${D}${base_sbindir} ${D}${sysconfdir}/ubootchart ${D}${datadir}/applications
        install -m 0755 ${S}/ubootchartd_bin ${D}${base_sbindir}
        install -m 0755 ${S}/ubootchartd ${D}${base_sbindir}
        install -m 0644 ${S}/ubootchart.conf ${D}${sysconfdir}/ubootchart
        install -m 0755 ${S}/start.sh ${D}${sysconfdir}/ubootchart
        install -m 0755 ${S}/finish.sh ${D}${sysconfdir}/ubootchart
        
        install -m 0755 ${WORKDIR}/ubootchart-stop ${D}${base_sbindir}
        install -m 0644 ${WORKDIR}/ubootchart.desktop ${D}${datadir}/applications
}
