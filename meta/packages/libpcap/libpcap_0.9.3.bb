DESCRIPTION = "Network Packet Capture Library"
HOMEPAGE = "http://www.tcpdump.org/"
LICENSE = "BSD"
SECTION = "libs/network"
PR = "r2"

SRC_URI = "http://www.at.tcpdump.org/release/libpcap-${PV}.tar.gz \
           file://shared.patch;patch=1 \
           file://config-fixes.patch;patch=1"

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

# Does anything really need these things?
#do_stage () {
#	autotools_stage_all
#
#	install -d ${STAGING_INCDIR}/net
#	ln -sf ${STAGING_INCDIR}/pcap-bpf.h ${STAGING_INCDIR}/net/bpf.h
#	
#	install -m 0644 acinclude.m4 ${STAGING_DATADIR}/aclocal/libpcap.m4
#}
