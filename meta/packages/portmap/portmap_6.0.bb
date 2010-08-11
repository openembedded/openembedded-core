require portmap.inc

DEPENDS += "tcp-wrappers"
PR = "r7"

SRC_URI = "http://www.sourcefiles.org/Networking/Tools/Miscellanenous/portmap-6.0.tgz \
           file://destdir-no-strip.patch \
           file://tcpd-config.patch \
           file://portmap.init"

S = "${WORKDIR}/${BPN}_${PV}/"

CPPFLAGS += "-DFACILITY=LOG_DAEMON -DENABLE_DNS -DHOSTS_ACCESS"
CFLAGS += "-Wall -Wstrict-prototypes -fPIC"

fakeroot do_install() {
    install -d ${D}${mandir}/man8/ ${D}${base_sbindir} ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/portmap.init ${D}${sysconfdir}/init.d/portmap
    oe_runmake install DESTDIR=${D}
}
