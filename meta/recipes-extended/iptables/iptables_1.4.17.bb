SUMMARY = "Tools for managing kernel packet filtering capabilities"
DESCRIPTION = "iptables is the userspace command line program used to configure and control network packet \
filtering code in Linux."
HOMEPAGE = "http://www.netfilter.org/"
BUGTRACKER = "http://bugzilla.netfilter.org/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263\
                    file://iptables/iptables.c;beginline=13;endline=25;md5=c5cffd09974558cf27d0f763df2a12dc"

PR = "r1"

RRECOMMENDS_${PN} = "kernel-module-x-tables \
                     kernel-module-ip-tables \
                     kernel-module-iptable-filter \
                     kernel-module-iptable-nat \
                     kernel-module-nf-defrag-ipv4 \
                     kernel-module-nf-conntrack \
                     kernel-module-nf-conntrack-ipv4 \
                     kernel-module-nf-nat \
                     kernel-module-ipt-masquerade"
FILES_${PN} =+ "${libdir}/xtables/ ${datadir}/xtables"
FILES_${PN}-dbg =+ "${libdir}/xtables/.debug"

SRC_URI = "http://netfilter.org/projects/iptables/files/iptables-${PV}.tar.bz2 \
           file://types.h-add-defines-that-are-required-for-if_packet.patch \
           file://fix-link-failure-ip6t-NETMAP.patch \
           file://fix-iptables-extensions-build-error.patch \
           file://0001-configure-Add-option-to-enable-disable-libnfnetlink.patch \
          "
SRC_URI[md5sum] = "c3fb2ffd5b39d0d54b06ccc4c8660116"
SRC_URI[sha256sum] = "51e7a769469383b6ad308a6a19cdd2bd813cf4593e21a156a543a1cd70554925"

inherit autotools

EXTRA_OECONF = "--with-kernel=${STAGING_INCDIR} \
               "
PACKAGECONFIG ?= "${@base_contains('DISTRO_FEATURES', 'ipv6', 'ipv6', '', d)} \
                 "

PACKAGECONFIG[ipv6] = "--enable-ipv6,--disable-ipv6,"

# libnfnetlink recipe is in meta-networking layer
PACKAGECONFIG[libnfnetlink] = "--enable-libnfnetlink,--disable-libnfnetlink,libnfnetlink"

do_configure_prepend() {
	# Remove some libtool m4 files
	# Keep ax_check_linker_flags.m4 which belongs to autoconf-archive.
	rm -f libtool.m4 lt~obsolete.m4 ltoptions.m4 ltsugar.m4 ltversion.m4
}
