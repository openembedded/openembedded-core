SUMMARY = "Interface for user-level network packet capture"
DESCRIPTION = "Libpcap provides a portable framework for low-level network \
monitoring.  Libpcap can provide network statistics collection, \
security monitoring and network debugging."
HOMEPAGE = "http://www.tcpdump.org/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=53067&atid=469577"
SECTION = "libs/network"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1d4b0366557951c84a94fabe3529f867 \
                    file://pcap.h;beginline=1;endline=32;md5=39af3510e011f34b8872f120b1dc31d2"
DEPENDS = "flex-native bison-native"

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluetooth', '', d)}"
PACKAGECONFIG[bluetooth] = "--enable-bluetooth,--disable-bluetooth,bluez4"
PACKAGECONFIG[canusb] = "--enable-canusb,--enable-canusb=no,libusb"
PACKAGECONFIG[libnl] = "--with-libnl,--without-libnl,libnl"

INC_PR = "r5"

SRC_URI = "http://www.tcpdump.org/release/${BP}.tar.gz"

BINCONFIG = "${bindir}/pcap-config"

inherit autotools binconfig-disabled

EXTRA_OECONF = "--with-pcap=linux"

PACKAGECONFIG ??= ""
PACKAGECONFIG[libnl1] = "--with-libnl,--without-libnl,libnl1,libnl1"
PACKAGECONFIG[dbus] = "--enable-dbus,--disable-dbus,dbus"

CPPFLAGS_prepend = "-I${S} "
CFLAGS_prepend = "-I${S} "
CXXFLAGS_prepend = "-I${S} "

do_configure_prepend () {
    if [ ! -e ${S}/acinclude.m4 ]; then
        cat ${S}/aclocal.m4 > ${S}/acinclude.m4
    fi
    sed -i -e's,^V_RPATH_OPT=.*$,V_RPATH_OPT=,' ${S}/pcap-config.in
}
