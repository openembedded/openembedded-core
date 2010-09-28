SUMMARY = "TCP / IP networking and traffic control utilities"
DESCRIPTION = "Iproute2 is a collection of utilities for controlling \
TCP / IP networking and traffic control in Linux.  Of the utilities ip \
and tc are the most important.  ip controls IPv4 and IPv6 \
configuration and tc stands for traffic control."
HOMEPAGE = "http://www.linuxfoundation.org/collaborate/workgroups/networking/iproute2"
SECTION = "base"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a \
                    file://ip/ip.c;md5=186731afa4b42e30b9b37efa4ec90b93"
DEPENDS = "flex-native bison-native"

inherit update-alternatives

EXTRA_OEMAKE = "CC='${CC}' KERNEL_INCLUDE=${STAGING_KERNEL_DIR}/include DOCDIR=${docdir}/iproute2 SUBDIRS='lib tc ip' SBINDIR=/sbin"

do_install () {
    oe_runmake DESTDIR=${D} install
    mv ${D}${base_sbindir}/ip ${D}${base_sbindir}/ip.iproute2
    install -d ${D}${datadir}
    mv ${D}/share/* ${D}${datadir}/ || true
    rm ${D}/share -rf || true
}

FILES_${PN} += "/usr/lib/tc/*"
FILES_${PN}-dbg += "/usr/lib/tc/.debug"

ALTERNATIVE_NAME = "ip"
ALTERNATIVE_PATH = "${base_sbindir}/ip.iproute2"
ALTERNATIVE_LINK = "${base_bindir}/ip"
ALTERNATIVE_PRIORITY = "100"

PARALLEL_MAKE = ""
