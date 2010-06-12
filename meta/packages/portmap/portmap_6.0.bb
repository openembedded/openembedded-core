require portmap.inc

PR = "r6"

SRC_URI = "http://www.sourcefiles.org/Networking/Tools/Miscellanenous/portmap-6.0.tgz \
           file://destdir-no-strip.patch;apply=yes \
           file://no-tcpd-support.patch;apply=yes \
           file://portmap.init"

S = "${WORKDIR}/${PN}_${PV}/"

EXTRA_OEMAKE_append = " NO_TCP_WRAPPER=1 "

CPPFLAGS += "-DFACILITY=LOG_DAEMON -DENABLE_DNS"
CFLAGS += "-Wall -Wstrict-prototypes -fPIC"

fakeroot do_install() {
    install -d ${D}${mandir}/man8/ ${D}${base_sbindir} ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/portmap.init ${D}${sysconfdir}/init.d/portmap
    oe_runmake install DESTDIR=${D}
}
