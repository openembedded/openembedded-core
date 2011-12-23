SUMMARY = "Interface for user-level network packet capture"
DESCRIPT = "Libpcap provides a portable framework for low-level network \
monitoring.  Libpcap can provide network statistics collection, \
security monitoring and network debugging."
HOMEPAGE = "http://www.tcpdump.org/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=53067&atid=469577"
SECTION = "libs/network"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1d4b0366557951c84a94fabe3529f867 \
                    file://pcap.h;beginline=1;endline=34;md5=8d6cf7e17d5745010d633e30bc529ea9"
DEPENDS = "flex-native bison-native"

INC_PR = "r0"

SRC_URI = "http://www.tcpdump.org/release/libpcap-${PV}.tar.gz"

inherit autotools binconfig

EXTRA_OECONF = "--with-pcap=linux"

CPPFLAGS_prepend = "-I${S} "
CFLAGS_prepend = "-I${S} "
CXXFLAGS_prepend = "-I${S} "

do_configure_prepend () {
    if [ ! -e acinclude.m4 ]; then
        cat aclocal.m4 > acinclude.m4
    fi
}
