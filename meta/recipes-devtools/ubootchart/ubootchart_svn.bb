DESCRIPTION = "A boot profiling tool"
HOMEPAGE = "http://code.google.com/p/ubootchart/"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=8f0e2cd40e05189ec81232da84bd6e1a"
SRCREV = "10"
PV = "0.0+r${SRCPV}"
PR = "r5"

#RRECOMMENDS_${PN} = "acct"

SRC_URI = "svn://ubootchart.googlecode.com/svn/;proto=http;module=trunk \
        file://sysvinit.patch;striplevel=0 \
        file://ubootchart-stop \
        file://ubootchart.desktop"

S = "${WORKDIR}/trunk"

inherit update-alternatives

ALTERNATIVE_NAME = "init"
ALTERNATIVE_LINK = "${base_sbindir}/init"
ALTERNATIVE_PATH = "${base_sbindir}/ubootchartd"
ALTERNATIVE_PRIORITY = "70"

do_compile() {
        ${CC} ${CFLAGS} ${LDFLAGS} ${LIBS} ${INCLUDES} ${S}/ubootchartd_bin.c -o ubootchartd_bin
}

do_install() {
        install -m 0755 -d ${D}/sbin ${D}/etc/ubootchart ${D}/usr/share/applications
        install -m 0755 ${S}/ubootchartd_bin ${D}/sbin
        install -m 0755 ${S}/ubootchartd ${D}/sbin
        install -m 0644 ${S}/ubootchart.conf ${D}/etc/ubootchart
        install -m 0755 ${S}/start.sh ${D}/etc/ubootchart
        install -m 0755 ${S}/finish.sh ${D}/etc/ubootchart
        
        install -m 0755 ${WORKDIR}/ubootchart-stop ${D}/sbin
        install -m 0644 ${WORKDIR}/ubootchart.desktop ${D}/usr/share/applications
}
