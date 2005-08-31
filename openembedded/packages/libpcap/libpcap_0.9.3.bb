DESCRIPTION = "Network Packet Capture Library"
HOMEPAGE = "http://www.tcpdump.org/"
LICENSE = "BSD"
SECTION = "libs/network"
PR = "r1"

SRC_URI = "http://www.tcpdump.org/release/libpcap-${PV}.tar.gz \
           file://shared.patch;patch=1"

inherit autotools

EXTRA_OECONF = "--with-pcap=linux"

CPPFLAGS_prepend = "-I${S} "
CFLAGS_prepend = "-I${S} "
CXXFLAGS_prepend = "-I${S} "

do_configure_prepend () {
	if [ ! -e acinclude.m4 ]; then
		cat aclocal.m4 > acinclude.m4
	fi
}

do_stage () {
	install -m 0644 pcap.h ${STAGING_INCDIR}/pcap.h
	install -m 0644 pcap-namedb.h ${STAGING_INCDIR}/pcap-namedb.h
	install -m 0644 pcap-bpf.h ${STAGING_INCDIR}/pcap-bpf.h
	oe_libinstall -a -so libpcap ${STAGING_LIBDIR}
	install -d ${STAGING_INCDIR}/net
	ln -sf ${STAGING_INCDIR}/pcap-bpf.h ${STAGING_INCDIR}/net/bpf.h
	install -m 0644 acinclude.m4 ${STAGING_DATADIR}/aclocal/libpcap.m4
}
