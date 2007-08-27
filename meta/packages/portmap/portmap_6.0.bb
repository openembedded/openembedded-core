require portmap.inc

SRC_URI = "http://neil.brown.name/portmap/portmap-6.0.tgz \
           file://destdir-no-strip.patch;patch=1 \
	   file://no-tcpd-support.patch;patch=1 \
           file://no-libwrap.patch;patch=1;pnum=0 \
           file://portmap.init "

S = "${WORKDIR}/${PN}_${PV}/"

CPPFLAGS += "-DFACILITY=LOG_DAEMON -DENABLE_DNS"

fakeroot do_install() {
    install -d ${D}${mandir}/man8/ ${D}${base_sbindir} ${D}${sysconfdir}/init.d
    install -m 0757 ${WORKDIR}/portmap.init ${D}${sysconfdir}/init.d/portmap
    oe_runmake install DESTDIR=${D}
}
